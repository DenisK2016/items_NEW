package by.dk.training.items.dataaccess.filters;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.metamodel.SingularAttribute;

import by.dk.training.items.datamodel.Type;
import by.dk.training.items.datamodel.UserProfile;

public class ProductFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String nameProduct;
	private Double weightProduct;
	private BigDecimal priceProduct;
	private Boolean status;
	private Type types;
	private UserProfile user;

	private SingularAttribute sortProperty;
	private boolean sortOrder;
	private Integer offset;
	private Integer limit;

	private boolean isFetchType;
	private boolean isFetchUser;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isFetchUser() {
		return isFetchUser;
	}

	public void setFetchUser(boolean isFetchUser) {
		this.isFetchUser = isFetchUser;
	}

	public UserProfile getUser() {
		return user;
	}

	public void setUser(UserProfile user) {
		this.user = user;
	}

	public Type getTypes() {
		return types;
	}

	public void setTypes(Type types) {
		this.types = types;
	}

	public String getNameProduct() {
		return nameProduct;
	}

	public void setNameProduct(String nameProduct) {
		this.nameProduct = nameProduct;
	}

	public Double getWeightProduct() {
		return weightProduct;
	}

	public void setWeightProduct(Double weightProduct) {
		this.weightProduct = weightProduct;
	}

	public BigDecimal getPriceProduct() {
		return priceProduct;
	}

	public void setPriceProduct(BigDecimal priceProduct) {
		this.priceProduct = priceProduct;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
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

	public boolean isFetchType() {
		return isFetchType;
	}

	public void setFetchType(boolean isFetchType) {
		this.isFetchType = isFetchType;
	}

}
