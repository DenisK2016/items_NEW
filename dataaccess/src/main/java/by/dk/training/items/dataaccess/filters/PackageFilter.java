package by.dk.training.items.dataaccess.filters;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;

import by.dk.training.items.datamodel.Product;
import by.dk.training.items.datamodel.Recipient;
import by.dk.training.items.datamodel.UserProfile;

public class PackageFilter implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private BigDecimal price;
	private Double weight;
	private Date date;
	private String description;
	private String countrySender;
	private String paymentDeadline;
	private BigDecimal fine;
	private Boolean paid;
	private UserProfile user;
	private Recipient recipint;
	private Product product;

	private SingularAttribute sortProperty;
	private boolean sortOrder;
	private Integer offset;
	private Integer limit;

	private boolean isFetchUser;
	private boolean isFetchRecipient;
	private boolean isFetchProduct;

	public UserProfile getUser() {
		return user;
	}

	public void setUser(UserProfile user) {
		this.user = user;
	}

	public Recipient getRecipint() {
		return recipint;
	}

	public void setRecipint(Recipient recipint) {
		this.recipint = recipint;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public boolean isFetchProduct() {
		return isFetchProduct;
	}

	public void setFetchProduct(boolean isFetchProduct) {
		this.isFetchProduct = isFetchProduct;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCountrySender() {
		return countrySender;
	}

	public void setCountrySender(String countrySender) {
		this.countrySender = countrySender;
	}

	public String getPaymentDeadline() {
		return paymentDeadline;
	}

	public void setPaymentDeadline(String paymentDeadline) {
		this.paymentDeadline = paymentDeadline;
	}

	public BigDecimal getFine() {
		return fine;
	}

	public void setFine(BigDecimal fine) {
		this.fine = fine;
	}

	public Boolean getPaid() {
		return paid;
	}

	public void setPaid(Boolean paid) {
		this.paid = paid;
	}

	public SingularAttribute getSortProperty() {
		return sortProperty;
	}

	public void setSortProperty(SingularAttribute sortProperty) {
		this.sortProperty = sortProperty;
	}

	public boolean isSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(boolean sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public boolean isFetchUser() {
		return isFetchUser;
	}

	public void setFetchUser(boolean isFetchUser) {
		this.isFetchUser = isFetchUser;
	}

	public boolean isFetchRecipient() {
		return isFetchRecipient;
	}

	public void setFetchRecipient(boolean isFetchRecipient) {
		this.isFetchRecipient = isFetchRecipient;
	}

}
