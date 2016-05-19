package by.dk.training.items.datamodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class Product implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	@Column(name = "name_product", nullable = false, unique = true)
	private String nameProduct;

	@Column(name = "\"limit\"", nullable = false)
	private String limit;

	@Column(name = "price_product", nullable = false)
	private BigDecimal priceProduct;

	@Column(nullable = false)
	private Boolean status;

	@JoinTable(name = "type_2_product", joinColumns = { @JoinColumn(name = "product_id") }, inverseJoinColumns = {
			@JoinColumn(name = "type_id") })
	@ManyToMany(targetEntity = Type.class, fetch = FetchType.LAZY)
	private List<Type> types = new ArrayList<>();

	public Product() {
		super();
	}

	public  List<Type> getTypes() {
		return types;
	}

	public void setTypes(Type type) {
		if(!types.contains(type)){
			types.add(type);
		}
		Type pType = type.getParentType();
		while (pType != null) {
			if (!types.contains(pType)) {
				types.add(pType);
			}
			pType = pType.getParentType();
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNameProduct() {
		return nameProduct;
	}

	public void setNameProduct(String nameProduct) {
		this.nameProduct = nameProduct;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
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

	@Override
	public String toString() {
		return "Products [id=" + id + ", nameProduct=" + nameProduct + ", limit=" + limit + ", priceProduct="
				+ priceProduct + ", status=" + status + ", Types product=" + types + "]";
	}

}
