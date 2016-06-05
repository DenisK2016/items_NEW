package by.dk.training.items.dataaccess.filters;

import java.io.Serializable;

import javax.persistence.metamodel.SingularAttribute;

import by.dk.training.items.datamodel.Type;
import by.dk.training.items.datamodel.UserProfile;

public class TypeFilter implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String typeName;
	private Type parentType;
	private UserProfile user;
	private String nullParent;
	private Type chilldType;

	private SingularAttribute sortProperty;
	private boolean sortOrder;
	private Integer offset;
	private Integer limit;

	private boolean isFetchParentType;
	private boolean isFetchUser;
	private boolean isFetchChildTypes;

	public boolean isFetchChildTypes() {
		return isFetchChildTypes;
	}

	public void setFetchChildTypes(boolean isChildTypes) {
		this.isFetchChildTypes = isChildTypes;
	}

	public Type getChilldType() {
		return chilldType;
	}

	public void setChilldType(Type chilldType) {
		this.chilldType = chilldType;
	}

	public String getNullParent() {
		return nullParent;
	}

	public void setNullParent(String nullParent) {
		this.nullParent = nullParent;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserProfile getUser() {
		return user;
	}

	public void setUser(UserProfile user) {
		this.user = user;
	}

	public boolean isFetchUser() {
		return isFetchUser;
	}

	public void setFetchUser(boolean isFetchUser) {
		this.isFetchUser = isFetchUser;
	}

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
