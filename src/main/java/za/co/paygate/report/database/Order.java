package za.co.paygate.report.database;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by AlecE on 6/15/2017.
 */
@Entity
@Table(name = "ZZZZ_Orders")
public class Order {

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "ID", columnDefinition = "int")
	private Integer id;

	@Column(name = "order_no")
	private String orderNumber;


	@Column(name = "AMOUNT")
	private Integer amount;

	@Column(name = "order_desc")
	private String desc;

	@Column(name = "order_status")
	private Integer status;

	@CreationTimestamp
	@Column(name = "date_created", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	Set<OrderPayment> orderPayments = new HashSet<>();

	@ManyToMany
	@JoinTable(name = "zzzz_order_items",
			joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"))
	Set<Products> product = new HashSet<>();

	public Set<OrderPayment> getOrderPayments() {
		return orderPayments;
	}

	public void setOrderPayments(Set<OrderPayment> orderPayments) {
		this.orderPayments = orderPayments;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getDesc() {
		return desc;
	}

	public Set<Products> getProduct() {
		return product;
	}

	public void setProduct(Set<Products> product) {
		this.product = product;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public static void saveOrder(EntityManager em, Order order) {
			em.getTransaction().begin();
			em.merge(order);
			em.getTransaction().commit();
	}
	public static List<Order> getOrder(EntityManager em){
		List<Order> orderQuery = em.createQuery("from Order", Order.class)
				.getResultList();
		return orderQuery;
	}
}
