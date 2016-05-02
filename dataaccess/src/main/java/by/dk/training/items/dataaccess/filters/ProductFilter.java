package by.dk.training.items.dataaccess.filters;

import java.math.BigDecimal;

import javax.persistence.metamodel.SingularAttribute;

import by.dk.training.items.datamodel.Type;

public class ProductFilter {

	private String nameProduct;
	private String limitProduct;
	private BigDecimal priceProduct;
	private Boolean status;
	private Type types;

	private SingularAttribute sortProperty;
	private boolean sortOrder;
	private Integer offset;
	private Integer limit;

	private boolean isFetchType;

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

	public String getLimitProduct() {
		return limitProduct;
	}

	public void setLimitProduct(String limitProduct) {
		this.limitProduct = limitProduct;
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
