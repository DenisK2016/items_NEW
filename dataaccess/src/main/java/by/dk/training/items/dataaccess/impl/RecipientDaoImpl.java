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

import by.dk.training.items.dataaccess.RecipientDao;
import by.dk.training.items.dataaccess.filters.RecipientFilter;
import by.dk.training.items.datamodel.Recipient;
import by.dk.training.items.datamodel.Recipient_;

@Repository
public class RecipientDaoImpl extends AbstractDaoImpl<Recipient, Long> implements RecipientDao {

	protected RecipientDaoImpl() {
		super(Recipient.class);
	}

	@Override
	public List<Recipient> find(RecipientFilter filter) {
		EntityManager em = getEntityManager();

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Recipient> cq = cb.createQuery(Recipient.class);

		Root<Recipient> from = cq.from(Recipient.class);

		cq.select(from);

		boolean name = (filter.getName() != null);
		boolean city = (filter.getCity() != null);
		boolean address = (filter.getAddress() != null);
		boolean user = (filter.getUser() != null);
		boolean id = (filter.getId() != null);
		boolean filt = name || city || address || user || id;
		if (filt) {
			Predicate idEqualCondition = cb.equal(from.get(Recipient_.id), filter.getId());
			Predicate nameEqualCondition = cb.equal(from.get(Recipient_.name), filter.getName());
			Predicate cityEqualCondition = cb.equal(from.get(Recipient_.city), filter.getCity());
			Predicate addressEqualCondition = cb.equal(from.get(Recipient_.address), filter.getAddress());
			Predicate userEqualCondition = cb.equal(from.get(Recipient_.idUser), filter.getUser());
			cq.where(cb.or(idEqualCondition, nameEqualCondition, cityEqualCondition, addressEqualCondition,
					userEqualCondition));
		}

		// set fetching
		if (filter.isFetchPackages()) {
			from.fetch(Recipient_.packages, JoinType.LEFT);
		}

		if (filter.isFetchUser()) {
			from.fetch(Recipient_.idUser, JoinType.LEFT);
		}

		// set sort params
		if (filter.getSortProperty() != null) {
			cq.orderBy(new OrderImpl(from.get(filter.getSortProperty()), filter.isSortOrder()));
		}

		TypedQuery<Recipient> q = em.createQuery(cq);

		// set paging
		if (filter.getOffset() != null && filter.getLimit() != null) {
			q.setFirstResult(filter.getOffset());
			q.setMaxResults(filter.getLimit());
		}

		// set execute query
		List<Recipient> allitems = q.getResultList();

		return allitems;
	}

	@Override
	public Long count(RecipientFilter filter) {
		EntityManager em = getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Recipient> from = cq.from(Recipient.class);
		cq.select(cb.count(from));
		TypedQuery<Long> q = em.createQuery(cq);
		return q.getSingleResult();
	}

	protected void setPaging(RecipientFilter filter, TypedQuery<Recipient> q) {
		if (filter.getOffset() != null && filter.getLimit() != null) {
			q.setFirstResult(filter.getOffset());
			q.setMaxResults(filter.getLimit());
		}
	}

}
