package by.dk.training.items.dataaccess.impl;

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

import by.dk.training.items.dataaccess.UserProfileDao;
import by.dk.training.items.dataaccess.filters.UserFilter;
import by.dk.training.items.datamodel.UserCredentials_;
import by.dk.training.items.datamodel.UserProfile;
import by.dk.training.items.datamodel.UserProfile_;

@Repository
public class UserProfileDaoImpl extends AbstractDaoImpl<UserProfile, Long> implements UserProfileDao {

	protected UserProfileDaoImpl() {
		super(UserProfile.class);

	}

	@Override
	public List<UserProfile> find(UserFilter filter) {
		EntityManager em = getEntityManager();

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<UserProfile> cq = cb.createQuery(UserProfile.class);

		Root<UserProfile> from = cq.from(UserProfile.class); // SELECT .. FROM ...

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
			Predicate loginEqualCondition = cb.equal(from.get(UserProfile_.login), filter.getLogin());
			Predicate fNameEqualCondition = cb.equal(from.get(UserProfile_.userCredentials).get(UserCredentials_.firstName),
					filter.getFirstName());
			Predicate lNameEqualCondition = cb.equal(from.get(UserProfile_.userCredentials).get(UserCredentials_.lastName),
					filter.getLastName());
			Predicate createdEqualCondition = cb.equal(from.get(UserProfile_.userCredentials).get(UserCredentials_.created),
					filter.getCreated());
			Predicate statusEqualCondition = cb.equal(from.get(UserProfile_.userCredentials).get(UserCredentials_.status),
					filter.getStatus());
			Predicate postEqualCondition = cb.equal(from.get(UserProfile_.userCredentials).get(UserCredentials_.post),
					filter.getPost());
			Predicate rankEqualCondition = cb.equal(from.get(UserProfile_.userCredentials).get(UserCredentials_.rank),
					filter.getRank());
			Predicate emailEqualCondition = cb.equal(from.get(UserProfile_.userCredentials).get(UserCredentials_.email),
					filter.getEmail());
			cq.where(cb.or(loginEqualCondition, fNameEqualCondition, lNameEqualCondition, createdEqualCondition,
					statusEqualCondition, postEqualCondition, rankEqualCondition, emailEqualCondition));
		}

		// set fetching
		if (filter.isFetchCredentials()) {
			from.fetch(UserProfile_.userCredentials, JoinType.LEFT);
		}

		if (filter.isFetchPackages()) {
			from.fetch(UserProfile_.packages, JoinType.LEFT);
		}

		// set sort params
		if (filter.getSortProperty() != null) {
			cq.orderBy(new OrderImpl(from.get(filter.getSortProperty()), filter.isSortOrder()));
		}

		TypedQuery<UserProfile> q = em.createQuery(cq);

		// set paging
		if (filter.getOffset() != null && filter.getLimit() != null) {
			q.setFirstResult(filter.getOffset());
			q.setMaxResults(filter.getLimit());
		}

		// set execute query
		List<UserProfile> allitems = q.getResultList();

		return allitems;
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
