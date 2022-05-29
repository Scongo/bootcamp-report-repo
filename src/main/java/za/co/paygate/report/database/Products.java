package za.co.paygate.report.database;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.List;

/**
 * Created by AlecE on 6/15/2017.
 */
@Entity
@Table(name = "ZZZZ_Product")
public class Products {

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "ID", columnDefinition = "int")
	private Integer id;

	@Column(name = "NAME", length = 225)
	@NotNull
	private String name;


	@Column(name = "PRICE")
	@NotNull
	private Integer price;

	@Column(name = "DESC", columnDefinition = "text")
	@NotNull
	private Integer desc;

	public Integer getId() {
		return id;
	}

	public Products setId(Integer id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public Products setName(String name) {
		this.name = name;
		return this;
	}

	public Integer getPrice() {
		return price;
	}

	public Products setPrice(Integer price) {
		this.price = price;
		return this;
	}

	public Integer getDesc() {
		return desc;
	}

	public Products setDesc(Integer desc) {
		this.desc = desc;
		return this;
	}

	public static List<Products> findProducts(EntityManager em) {
		List<Products> results = em.createQuery("SELECT o FROM Products AS o", Products.class)
				.getResultList();
		return results;
	}
}
