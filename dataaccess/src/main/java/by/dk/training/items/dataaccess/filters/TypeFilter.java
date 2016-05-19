package by.dk.training.items.dataaccess.filters;

import java.io.Serializable;

import javax.persistence.metamodel.SingularAttribute;

import by.dk.training.items.datamodel.Type;

public class TypeFilter implements Serializable{

	private static final long serialVersionUID = 1L;
	private String typeName;
	private Type parentType;

	private SingularAttribute sortProperty;
	private boolean sortOrder;
	private Integer offset;
	private Integer limit;

	private boolean isFetchParentType;

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Type getParentType() {
		return parentType;
	}

	public void setParentType(Type parentType) {
		this.parentType = parentType;
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

	public boolean isFetchParentType() {
		return isFetchParentType;
	}

	public void setFetchParentType(boolean isFetchParentType) {
		this.isFetchParentType = isFetchParentType;
	}

}
