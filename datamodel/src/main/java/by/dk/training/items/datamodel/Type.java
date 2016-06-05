package by.dk.training.items.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "type")
public class Type implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	@Column(name = "type_name", nullable = false, unique = true)
	private String typeName;

	@ManyToOne(targetEntity = Type.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "parent_type", referencedColumnName = "id")
	private Type parentType;

	@OneToMany(mappedBy = "parentType", fetch = FetchType.LAZY)
	private List<Type> childTypes = new ArrayList<>();

	@ManyToOne(targetEntity = UserProfile.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "id_user")
	private UserProfile idUser;

	public List<Type> getChildTypes() {
		return childTypes;
	}

	public void setChildTypes(Type childTypes) {
		this.childTypes.add(childTypes);
	}

	public Type() {
		super();
	}

	public UserProfile getIdUser() {
		return idUser;
	}

	public void setIdUser(UserProfile idUser) {
		this.idUser = idUser;
	}

	public Type getParentType() {
		return parentType;
	}

	public void setParentType(Type parentType) {
		this.parentType = parentType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public String toString() {
		return "Type [id=" + id + ", typeName=" + typeName + ", parentType=" + parentType + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((parentType == null) ? 0 : parentType.hashCode());
		result = prime * result + ((typeName == null) ? 0 : typeName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Type other = (Type) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (parentType == null) {
			if (other.parentType != null) {
				return false;
			}
		} else if (!parentType.equals(other.parentType)) {
			return false;
		}
		if (typeName == null) {
			if (other.typeName != null) {
				return false;
			}
		} else if (!typeName.equals(other.typeName)) {
			return false;
		}
		return true;
	}

}
