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

import by.dk.training.items.dataaccess.TypeDao;
import by.dk.training.items.dataaccess.filters.TypeFilter;
import by.dk.training.items.datamodel.Type;
import by.dk.training.items.datamodel.Type_;

@Repository
public class TypeDaoImpl extends AbstractDaoImpl<Type, Long> implements TypeDao {

	protected TypeDaoImpl() {
		super(Type.class);

	}

	@Override
	public List<Type> find(TypeFilter filter) {
		EntityManager em = getEntityManager();

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Type> cq = cb.createQuery(Type.class);

		Root<Type> from = cq.from(Type.class); // SELECT .. FROM ...

		cq.select(from); // Указывает что селектать SELECT *. from - это
							// таблица,
							// а from.get... - это конкретная колонка

		if (filter.getTypeName() != null || filter.getParentType() != null) {
			Predicate tNameEqualCondition = cb.equal(from.get(Type_.typeName), filter.getTypeName());
			Predicate pTypeEqualCondition = cb.equal(from.get(Type_.parentType), filter.getParentType());
			cq.where(cb.or(tNameEqualCondition, pTypeEqualCondition));
		}

		// set fetching
		if (filter.isFetchParentType()) {
			from.fetch(Type_.parentType, JoinType.LEFT);
		}

		// set sort params
		if (filter.getSortProperty() != null) {
			cq.orderBy(new OrderImpl(from.get(filter.getSortProperty()), filter.isSortOrder()));
		}

		TypedQuery<Type> q = em.createQuery(cq);

		// set paging
		if (filter.getOffset() != null && filter.getLimit() != null) {
			q.setFirstResult(filter.getOffset());
			q.setMaxResults(filter.getLimit());
		}

		// set execute query
		List<Type> allitems = q.getResultList();

		return allitems;
	}
	
	@Override
	public Long count(TypeFilter filter) {
		EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Type> from = cq.from(Type.class);
        cq.select(cb.count(from));
        TypedQuery<Long> q = em.createQuery(cq);
        return q.getSingleResult();
	}
	
	protected void setPaging(TypeFilter filter, TypedQuery<Type> q) {
        if (filter.getOffset() != null && filter.getLimit() != null) {
            q.setFirstResult(filter.getOffset());
            q.setMaxResults(filter.getLimit());
        }
    }

}
