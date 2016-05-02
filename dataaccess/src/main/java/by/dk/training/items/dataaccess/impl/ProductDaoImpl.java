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

import by.dk.training.items.dataaccess.ProductDao;
import by.dk.training.items.dataaccess.filters.ProductFilter;
import by.dk.training.items.datamodel.Product;
import by.dk.training.items.datamodel.Product_;

@Repository
public class ProductDaoImpl extends AbstractDaoImpl<Product, Long> implements ProductDao {

	protected ProductDaoImpl() {
		super(Product.class);
	}

	@Override
	public List<Product> find(ProductFilter filter) {
		EntityManager em = getEntityManager();

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Product> cq = cb.createQuery(Product.class);

		Root<Product> from = cq.from(Product.class);

		cq.select(from);

		boolean nameProduct = (filter.getNameProduct() != null);
		boolean limit = (filter.getLimitProduct() != null);
		boolean price = (filter.getPriceProduct() != null);
		boolean status = (filter.getStatus() != null);
		boolean types = (filter.getTypes() != null);
		boolean filt = (nameProduct || limit || price || status || types);

		if (filt) {
			Predicate nProductEqualCondition = cb.equal(from.get(Product_.nameProduct), filter.getNameProduct());
			Predicate lProductEqualCondition = cb.equal(from.get(Product_.limit), filter.getLimitProduct());
			Predicate pProductEqualCondition = cb.equal(from.get(Product_.priceProduct), filter.getPriceProduct());
			Predicate statustEqualCondition = cb.equal(from.get(Product_.status), filter.getStatus());
			Predicate typesEqualCondition = cb.isMember(filter.getTypes(), from.get(Product_.types));
			cq.where(cb.or(nProductEqualCondition, lProductEqualCondition, pProductEqualCondition,
					statustEqualCondition, typesEqualCondition));
		}

		// set fetching
		if (filter.isFetchType()) {
			from.fetch(Product_.types, JoinType.LEFT);
		}

		// set sort params
		if (filter.getSortProperty() != null) {
			cq.orderBy(new OrderImpl(from.get(filter.getSortProperty()), filter.isSortOrder()));
		}

		TypedQuery<Product> q = em.createQuery(cq);

		// set paging
		if (filter.getOffset() != null && filter.getLimit() != null) {
			q.setFirstResult(filter.getOffset());
			q.setMaxResults(filter.getLimit());
		}

		// set execute query
		List<Product> allitems = q.getResultList();
		List<Product> all = new ArrayList<>();

		for (Product pr : allitems) {
			if (!all.contains(pr)) {
				all.add(pr);
			}
		}
		allitems = all;

		return allitems;
	}

}
