package by.dk.training.items.datamodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "package")
public class Package implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", nullable = false, unique = true)
	private Long id;

	@ManyToOne(targetEntity = Recipient.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "id_recipient", nullable = false)
	private Recipient idRecipient;

	@Column(nullable = false)
	private BigDecimal price;

	@Column(nullable = false)
	private Double weight;

	@ManyToOne(targetEntity = UserProfile.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "id_user", nullable = false)
	private UserProfile idUser;

	@Column(nullable = false)
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date date;

	@Column(length = 1000)
	private String description;

	@Column(name = "country_sender", nullable = false, length = 100)
	private String countrySender;

	@Column(name = "payment_deadline", nullable = false)
	private String paymentDeadline;

	@Column(nullable = false)
	private BigDecimal fine;

	@Column(nullable = false)
	private Boolean paid;

	@JoinTable(name = "package_2_product", joinColumns = { @JoinColumn(name = "package_id") }, inverseJoinColumns = {
			@JoinColumn(name = "product_id") })
	@ManyToMany(targetEntity = Product.class, fetch = FetchType.LAZY)
	private List<Product> products;

	public Boolean getPaid() {
		return paid;
	}

	public void setPaid(Boolean paid) {
		this.paid = paid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Recipient getIdRecipient() {
		return idRecipient;
	}

	public void setIdRecipient(Recipient idRecipient) {
		this.idRecipient = idRecipient;
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

	public UserProfile getIdUser() {
		return idUser;
	}

	public void setIdUser(UserProfile idUser) {
		this.idUser = idUser;
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

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	@Override
	public String toString() {
		return "Packages [trackingCode=" + id + ", price=" + price + ", weight=" + weight + ", date=" + date
				+ ", description=" + description + ", countrySender=" + countrySender + ", paymentDeadline="
				+ paymentDeadline + ", fine=" + fine + ", paid=" + paid + "]";
	}

}
