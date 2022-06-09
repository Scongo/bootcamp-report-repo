import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.paygate.report.config.Config;
import za.co.paygate.report.config.Op;
import za.co.paygate.report.database.Order;
import za.co.paygate.report.database.OrderItems;
import za.co.paygate.report.database.OrderPayment;
import za.co.paygate.report.database.Products;
import za.co.paygate.report.pojos.ResponseItem;
import za.co.paygate.report.repository.OrderItemsRepository;
import za.co.paygate.report.repository.OrderPaymentRepository;
import za.co.paygate.report.repository.OrderRepository;
import za.co.paygate.report.repository.ProductsRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
	private static ProductsRepository productsRepository;
	private static OrderRepository orderRepository;
	private static Products products;
	private static ArrayList<String> productNameList;

	public static void main(String[] args) throws IOException {

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
		productNameList = new ArrayList<>();

		Order order = new Order();
		OrderItems orderItems = new OrderItems();
		products = new Products();
		OrderPayment orderPayment = new OrderPayment();

		orderRepository = new OrderRepository(em);
		productsRepository = new ProductsRepository(em);


		// Persist Order information to PaymentDB

		if(!(Order.getOrder(em).size() > 0)) {
			//em.detach(order);

			saveProducts(responseItem);

			responseItem.forEach(responseItems -> {

				order.setOrderNumber(responseItems.getOrderId());
				order.setAmount(responseItems.getAmount());
				order.setDesc(responseItems.getDesc());
				order.setCreatedAt(new Date(responseItems.getCreated() * 1000));


				final Set<Products> products1 = CollectionUtils.isEmpty(productNameList)
									? null
									: productsRepository.findAll().stream()
									.filter(Objects::nonNull)
									.filter(products2 -> productNameList.contains(products2.getName()))
									.collect(Collectors.toSet());



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

				order.setProduct(products1);
				orderRepository.save(order);

			});
		}

		// ORDER_ID, DESC, AMOUNT, NUMBER_OF_ITEMS, NUMBER_OF_PAYMENTS, PAYMENT_STATUS, AMOUNT
		List<Order> orderList = orderRepository.findAll();
		System.out.println("Orders payed for :");
		orderList.forEach(order1 -> {
			if(order1.getStatus() != null){
				if(order1.getStatus().equals(1)){
					order1.getOrderPayments().forEach(pay ->{
						pay.getStatusDesc();
						System.out.println(order1.getId() +" , "+ order1.getDesc()+" , "+
								order1.getAmount()+" , "+ pay.getStatusDesc());
					});
				}
			}
		});

		System.out.println("===============================");
		System.out.println("Orders has no payment attempted:");
		orderList.forEach(noOrder -> {
			if(noOrder.getStatus() == null){
			System.out.println(noOrder.getId() +" , "+ noOrder.getDesc()+" , "+
					noOrder.getAmount()+" , "+ noOrder.getStatus());

			}
		});

	}

	public static void saveProducts(List<ResponseItem> responseItem ){
		responseItem.forEach(responseItems -> {
			responseItems.getItems().forEach(item -> {
				if (item.getProduct() != null) {
					//Products.saveProduct(em, products);
					if (productsRepository.findByName(item.getProduct().getName()).size() <= 0) {
						products.setName(item.getProduct().getName());
						products.setPrice(item.getProduct().getPrice());
						products.setDesc(item.getProduct().getDesc());
						productsRepository.save(products);
					}

				}
				productNameList.add(item.getProduct().getName());
			});
		});
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
