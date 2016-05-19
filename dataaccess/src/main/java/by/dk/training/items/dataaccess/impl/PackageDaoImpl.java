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

import by.dk.training.items.dataaccess.PackageDao;
import by.dk.training.items.dataaccess.filters.PackageFilter;
import by.dk.training.items.datamodel.Package;
import by.dk.training.items.datamodel.Package_;

@Repository
public class PackageDaoImpl extends AbstractDaoImpl<Package, Long> implements PackageDao {

	protected PackageDaoImpl() {
		super(Package.class);
	}

	@Override
	public List<Package> find(PackageFilter filter) {
		EntityManager em = getEntityManager();

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Package> cq = cb.createQuery(Package.class);

		Root<Package> from = cq.from(Package.class);

		cq.select(from);

		boolean price = (filter.getPrice() != null);
		boolean weight = (filter.getWeight() != null);
		boolean date = (filter.getDate() != null);
		boolean description = (filter.getDescription() != null);
		boolean cSender = (filter.getCountrySender() != null);
		boolean paymentDead = (filter.getPaymentDeadline() != null);
		boolean fine = (filter.getFine() != null);
		boolean paid = (filter.getPaid() != null);
		boolean user = (filter.getUser() != null);
		boolean recipient = (filter.getRecipint() != null);
		boolean product = (filter.getProduct() != null);
		boolean filt = (user || recipient || product || price || weight || date || description || cSender || paymentDead
				|| fine || paid);
		if (filt) {
			Predicate priceEqualCondition = cb.equal(from.get(Package_.price), filter.getPrice());
			Predicate weightEqualCondition = cb.equal(from.get(Package_.weight), filter.getWeight());
			Predicate dateEqualCondition = cb.equal(from.get(Package_.date), filter.getDate());
			Predicate descrEqualCondition = cb.equal(from.get(Package_.description), filter.getDescription());
			Predicate countryEqualCondition = cb.equal(from.get(Package_.countrySender), filter.getCountrySender());
			Predicate paymentEqualCondition = cb.equal(from.get(Package_.paymentDeadline), filter.getPaymentDeadline());
			Predicate fineEqualCondition = cb.equal(from.get(Package_.fine), filter.getFine());
			Predicate paidEqualCondition = cb.equal(from.get(Package_.paid), filter.getPaid());
			Predicate userEqualCondition = cb.equal(from.get(Package_.idUser), filter.getUser());
			Predicate recipientEqualCondition = cb.equal(from.get(Package_.idRecipient), filter.getRecipint());
			if(product == true){
				Predicate productEqualCondition = cb.isMember(filter.getProduct(), from.get(Package_.products));
				cq.where(cb.or(priceEqualCondition, weightEqualCondition, dateEqualCondition, descrEqualCondition,
						countryEqualCondition, paymentEqualCondition, fineEqualCondition, paidEqualCondition,
						userEqualCondition, recipientEqualCondition, productEqualCondition)).distinct(true);
			} else{
				cq.where(cb.or(priceEqualCondition, weightEqualCondition, dateEqualCondition, descrEqualCondition,
						countryEqualCondition, paymentEqualCondition, fineEqualCondition, paidEqualCondition,
						userEqualCondition, recipientEqualCondition)).distinct(true);
			}
		}

		// set fetching
		if (filter.isFetchUser()) {
			from.fetch(Package_.idUser, JoinType.LEFT);
		}

		if (filter.isFetchRecipient()) {
			from.fetch(Package_.idRecipient, JoinType.LEFT);
		}

		if (filter.isFetchProduct()) {
			from.fetch(Package_.products, JoinType.LEFT);
		}

		// set sort params
		if (filter.getSortProperty() != null) {
			cq.orderBy(new OrderImpl(from.get(filter.getSortProperty()), filter.isSortOrder()));
		}

		TypedQuery<Package> q = em.createQuery(cq);

		// set paging
		if (filter.getOffset() != null && filter.getLimit() != null) {
			q.setFirstResult(filter.getOffset());
			q.setMaxResults(filter.getLimit());
		}

		// set execute query
		List<Package> allitems = q.getResultList();

		return allitems;
	}

	@Override
	public Long count(PackageFilter filter) {
		EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Package> from = cq.from(Package.class);
        cq.select(cb.count(from));
        TypedQuery<Long> q = em.createQuery(cq);
        return q.getSingleResult();
	}
	
	protected void setPaging(PackageFilter filter, TypedQuery<Package> q) {
        if (filter.getOffset() != null && filter.getLimit() != null) {
            q.setFirstResult(filter.getOffset());
            q.setMaxResults(filter.getLimit());
        }
    }

}
