package za.co.paygate.report.pojos;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Response{

	@JsonProperty("Response")
	private List<ResponseItem> response;

	public List<ResponseItem> getResponse(){
		return response;
	}
}