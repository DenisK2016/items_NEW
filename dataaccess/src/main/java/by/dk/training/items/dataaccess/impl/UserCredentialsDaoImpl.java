package by.dk.training.items.dataaccess.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.jpa.criteria.OrderImpl;
import org.springframework.stereotype.Repository;

import by.dk.training.items.dataaccess.UserCredentialsDao;
import by.dk.training.items.dataaccess.filters.UserFilter;
import by.dk.training.items.datamodel.UserCredentials;
import by.dk.training.items.datamodel.UserCredentials_;
import by.dk.training.items.datamodel.UserProfile;
import by.dk.training.items.datamodel.UserProfile_;

@Repository
public class UserCredentialsDaoImpl extends AbstractDaoImpl<UserCredentials, Long> implements UserCredentialsDao {

	protected UserCredentialsDaoImpl() {
		super(UserCredentials.class);
	}

	@Override
	public List<UserProfile> find(UserFilter filter) {

		EntityManager em = getEntityManager();

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<UserCredentials> cq = cb.createQuery(UserCredentials.class);

		Root<UserCredentials> from = cq.from(UserCredentials.class); // SELECT
																		// ..
																		// FROM
																		// ...

		cq.select(from); // Указывает что селектать SELECT *. from - это
							// таблица,
							// а from.get... - это конкретная колонка

		boolean fName = (filter.getFirstName() != null);
		boolean lName = (filter.getLastName() != null);
		boolean login = (filter.getLogin() != null);
		boolean create = (filter.getCreated() != null);
		boolean stat = (filter.getStatus() != null);
		boolean post = (filter.getPost() != null);
		boolean rank = (filter.getRank() != null);
		boolean email = (filter.getEmail() != null);
		boolean filt = (fName || lName || login || create || stat || post || rank || email);

		if (filt) {
			Predicate loginEqualCondition = cb.equal(from.get(UserCredentials_.user).get(UserProfile_.login),
					filter.getLogin());
			Predicate fNameEqualCondition = cb.equal(from.get(UserCredentials_.firstName), filter.getFirstName());
			Predicate lNameEqualCondition = cb.equal(from.get(UserCredentials_.lastName), filter.getLastName());
			Predicate createdEqualCondition = cb.equal(from.get(UserCredentials_.created), filter.getCreated());
			Predicate statusEqualCondition = cb.equal(from.get(UserCredentials_.status), filter.getStatus());
			Predicate postEqualCondition = cb.equal(from.get(UserCredentials_.post), filter.getPost());
			Predicate rankEqualCondition = cb.equal(from.get(UserCredentials_.rank), filter.getRank());
			Predicate emailEqualCondition = cb.equal(from.get(UserCredentials_.email), filter.getEmail());
			cq.where(cb.or(loginEqualCondition, fNameEqualCondition, lNameEqualCondition, createdEqualCondition,
					statusEqualCondition, postEqualCondition, rankEqualCondition, emailEqualCondition));
		}

		// set fetching
		if (filter.isFetchCredentials()) {
			from.fetch(UserCredentials_.user, JoinType.LEFT);
		}

		// set sort params
		if (filter.getSortProperty() != null) {
			cq.orderBy(new OrderImpl(from.get(filter.getSortProperty()), filter.isSortOrder()));
		}

		TypedQuery<UserCredentials> q = em.createQuery(cq);

		// set paging
		if (filter.getOffset() != null && filter.getLimit() != null) {
			q.setFirstResult(filter.getOffset());
			q.setMaxResults(filter.getLimit());
		}

		// set execute query
		List<UserCredentials> allitems = q.getResultList();

		List<UserProfile> allitems2 = new ArrayList<UserProfile>();
		for (int i = 0; i < allitems.size(); i++) {
			allitems2.add(i, allitems.get(i).getUser());
		}

		return allitems2;
	}

	@Override
	public Long count(UserFilter filter) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<UserProfile> from = cq.from(UserProfile.class);
		cq.select(cb.count(from));
		TypedQuery<Long> q = em.createQuery(cq);
		return q.getSingleResult();
	}

	protected void setPaging(UserFilter filter, TypedQuery<UserFilter> q) {
		if (filter.getOffset() != null && filter.getLimit() != null) {
			q.setFirstResult(filter.getOffset());
			q.setMaxResults(filter.getLimit());
		}
	}

}
