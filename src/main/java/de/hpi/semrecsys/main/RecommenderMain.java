package de.hpi.semrecsys.main;

import de.hpi.semrecsys.config.SemRecSysConfigurator;
import de.hpi.semrecsys.config.SemRecSysConfigurator.Customer;
import de.hpi.semrecsys.model.Attribute.AttributeType;
import de.hpi.semrecsys.model.Product;
import de.hpi.semrecsys.persistence.ProductDAO;
import de.hpi.semrecsys.populator.Populator;
import de.hpi.semrecsys.populator.Populator.PopulationOption;
import de.hpi.semrecsys.strategy.RecommendationStrategy;
import de.hpi.semrecsys.strategy.RecommendationStrategyImpl;

/**
 * Main class for recommendation generation
 * @author Michael Wolowyk
 *
 */
public class RecommenderMain {

	static final double PRODUCT_NAME_THRESHOLD = 20;
	static Customer customer = Customer.dobos;
	static SemRecSysConfigurator configurator = SemRecSysConfigurator.getDefaultConfigurator(customer);
	static RecommendationStrategy recommendationStrategy = new RecommendationStrategyImpl(configurator);

	static ProductDAO productManager = ProductDAO.getDefault();
	static Recommender recommender = new Recommender(configurator);
	private static Populator populator = new Populator(configurator);

	static AttributeType[] attributeTypes = { AttributeType.unstruct, AttributeType.img };
	static String type = "generated";

	static int[] naturideenSelectedProducts = { 110, 36, 78, 3232, 1868, 1358, 1563, 1797, 2512, 1117 };
	static int[] melovelySelectedProducts = { 1815, 920, 3132, 3516, 3031, 3122, 3446 };
	static int[] dobosSelectedProducts = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };

	public static void main(String[] args) {
		int[] selectedProducts;
//		if (customer.equals(Customer.naturideen) || customer.equals(Customer.naturideen2)) {
//			selectedProducts = naturideenSelectedProducts;
//		} else {
//			selectedProducts = melovelySelectedProducts;
//		}
		// execute(55);
		for (int productId : dobosSelectedProducts) {
			execute(productId);
		}


	}

	private static void executeComplete(int productId) {
		AttributeType[] structAttributeTypes = { AttributeType.unstruct, AttributeType.struct, AttributeType.cat,
				AttributeType.split, AttributeType.img };
		attributeTypes = structAttributeTypes;
		// type = "generated";
		populator.populateMeta(true, configurator.getMetaGraphName(), attributeTypes);
		execute(productId);
	}

	/**
	 * Generates recommendations for structured attributes for productId
	 * @param productId
	 */
	public static void executeStruct(int productId) {
		AttributeType[] structAttributeTypes = { AttributeType.struct, AttributeType.cat, AttributeType.split,
				AttributeType.img };
		attributeTypes = structAttributeTypes;
		type = "struct";
		populator.populateMeta(true, configurator.getMetaGraphName(), attributeTypes);
		execute(productId);
	}
	/**
	 * Generates recommendations for unstructured attributes for productId
	 * @param productId
	 */
	public static void executeUnstruct(int productId) {
		type = "unstruct";
		AttributeType[] unstructAttributeTypes = { AttributeType.unstruct, AttributeType.img };
		attributeTypes = unstructAttributeTypes;
		populator.populateMeta(true, configurator.getMetaGraphName(), attributeTypes);
		execute(productId);
	}

	/**
	 * generate random recommendations for productId
	 * @param productId
	 */
	public static void executeRand() {
		while (true) {
			recommender.recommendGenerated(productManager.getRandom(), type);
		}

	}

	/**
	 * generates recommendations for all products in the database
	 */
	public static void execute() {
		PopulationOption[] options = { PopulationOption.meta };
		// ,PopulationOption.attribute_sim, PopulationOption.entity_sim };
		PopulatorMain.execute(options);
		int maxId = configurator.getJsonProperties().getMaxProdId();
		int minId = configurator.getJsonProperties().getMinProdId();
		for (int i = minId; i < maxId; i++) {
			Product product1 = productManager.findById(i);
			recommender.recommendGenerated(product1, type);
			// recommender.recommendRandom(product1);
		}
	}

	/**
	 * generates recommendations for the given productId
	 * 
	 * @param productId
	 */
	public static void execute(Integer productId) {
		Product product1 = productManager.findById(productId);
		recommender.recommendGenerated(product1, type);

	}

}
