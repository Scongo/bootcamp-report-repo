import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.paygate.report.config.Config;
import za.co.paygate.report.config.Op;
import za.co.paygate.report.database.Order;
import za.co.paygate.report.database.OrderItems;
import za.co.paygate.report.database.OrderPayment;
import za.co.paygate.report.database.Products;
import za.co.paygate.report.pojos.ResponseItem;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by AlecE on 6/15/2017.
 *
 * See data.json for you data
 * See ER.png for database design
 */
public class Report {

	private static Logger log = LoggerFactory.getLogger(Report.class);
	private static EntityManagerFactory emf = null;
	private static EntityManager em = null;
	public static Config config;

	public static void main(String[] args) throws IOException {
		System.out.println("Hello this is where you start. See C:\\Users\\AlecE\\bitbucket\\paygate-bootcamp\\bootcamp-report\\src\\main\\resources\\config\\dev\\persistence.properties for database config");
		loadConfig(args);
		ObjectMapper objectMapper = new ObjectMapper();
		List<ResponseItem> responseItem = objectMapper.readValue(new File("data.json"), new TypeReference<List<ResponseItem>>(){});
		System.out.println(responseItem);
		emf = Persistence.createEntityManagerFactory("paymentReport");
		em = emf.createEntityManager();

		ArrayList<Order> orders = new ArrayList<>();
		ArrayList<OrderPayment> orderPaymentList = new ArrayList<>();
		ArrayList<Products> productList = new ArrayList<>();
		ArrayList<OrderItems> orderItemsList = new ArrayList<>();

		Order order = new Order();
		OrderItems orderItems = new OrderItems();
		Products products = new Products();
		OrderPayment orderPayment = new OrderPayment();

		if(!(Order.getOrder(em).size() > 0)) {
			responseItem.forEach(responseItems -> {

				order.setOrderNumber(responseItems.getOrderId());
				order.setAmount(responseItems.getAmount());
				order.setDesc(responseItems.getDesc());
				order.setCreatedAt(new Date(responseItems.getCreated() * 1000));

				if (responseItems.getItems() != null || responseItems.getItems().size() > 0) {
					responseItems.getItems().forEach(item -> {
						products.setName(item.getProduct().getName());
						products.setPrice(item.getProduct().getPrice());
						products.setDesc(item.getProduct().getDesc());
						orderItems.setProducts(products);
						orderItemsList.add(orderItems);
						Products.saveProduct(em, products);
					});
					order.setOrderItems(new HashSet<OrderItems>(orderItemsList));
				}
				int[] runCount = {0};

				if (responseItems.getPayments() != null) {
					responseItems.getPayments().forEach(payment -> {
						orderPayment.setStatus(Integer.parseInt(payment.getStatus()));
						orderPayment.setStatusDesc(payment.getStatusDesc());
						orderPayment.setAmount(payment.getAmount());
						orderPayment.setOrders(order);
						orderPaymentList.add(orderPayment);
						runCount[0]++;
						//OrderPayment.savePayment(em, orderPayment);
					});
					order.setStatus(Integer.parseInt(responseItems.getPayments().get(runCount[0] - 1).getStatus()));
					order.setOrderPayments(new HashSet<OrderPayment>(orderPaymentList));
				}
				Order.saveOrder(em, order);
			});
		}
		em.detach(order);
		//List<Products> products =  Products.findProducts(em);

	}


	public static void loadConfig(String[] args) {
		String env = Op.of(Config.getEnv(args)).get("dev");

		config = new Config("application.properties", env)
				.load("persistence.properties", env);

		// check that configs loaded
		if (!config.isLoaded()) {
			log.error("Config Not Loaded");
			System.exit(1);
		} else {
			config.load(args);
			log.info("env:" + env);
		}
	}
}
