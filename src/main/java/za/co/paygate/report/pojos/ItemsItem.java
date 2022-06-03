package za.co.paygate.report.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ItemsItem{

	@JsonProperty("product")
	private Product product;

	public Product getProduct(){
		return product;
	}
}