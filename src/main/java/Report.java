import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.paygate.report.config.Config;
import za.co.paygate.report.config.Op;
import za.co.paygate.report.database.Products;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

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

	public static void main(String[] args) {
		System.out.println("Hello this is where you start. See C:\\Users\\AlecE\\bitbucket\\paygate-bootcamp\\bootcamp-report\\src\\main\\resources\\config\\dev\\persistence.properties for database config");
		emf = Persistence.createEntityManagerFactory("default", config.getMap());
		em = emf.createEntityManager();

		List<Products> products =  Products.findProducts(em);

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
