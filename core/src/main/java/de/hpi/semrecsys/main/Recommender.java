package de.hpi.semrecsys.main;

import com.hp.gagawa.java.elements.Html;
import de.hpi.semrecsys.config.SemRecSysConfigurator;
import de.hpi.semrecsys.model.Product;
import de.hpi.semrecsys.output.HTMLOutputCreator;
import de.hpi.semrecsys.output.RecommendationResult;
import de.hpi.semrecsys.output.RecommendationResultsHolder;
import de.hpi.semrecsys.service.PersistenceService;
import de.hpi.semrecsys.strategy.RecommendationStrategy;
import de.hpi.semrecsys.strategy.RecommendationStrategyImpl;
import de.hpi.semrecsys.utils.FileUtils;

import java.io.File;
import java.util.Map;

/**
 * generates recommendations for the given product
 * @author Michael Wolowyk
 *
 */
public class Recommender {

	private PersistenceService persistenceService;
	SemRecSysConfigurator configurator;

	RecommendationStrategy recommendationStrategy;

	public Recommender(SemRecSysConfigurator configurator) {
		this.configurator = configurator;
		this.recommendationStrategy = new RecommendationStrategyImpl(configurator);
	}


    public static Recommender getDefault(PersistenceService persistenceService) {
        return new Recommender(SemRecSysConfigurator.getDefaultConfigurator(), persistenceService);
    }

    public Recommender(SemRecSysConfigurator configurator, PersistenceService persistenceService) {
        this.configurator = configurator;
        this.persistenceService = persistenceService;
        this.recommendationStrategy = new RecommendationStrategyImpl(configurator, persistenceService);
    }

	/**
	 * generates recommendations for product1 of type with special attribute weights 
	 * @param product1
	 * @param attributeWeights custom attributeWeights
	 * @param type one of generated, existing, random
	 */
	public void recommendGenerated(Product product1, Map<String, Double> attributeWeights, String type) {
		configurator.getJsonProperties().setAttributesByType(attributeWeights);
		recommendGenerated(product1);
	}

	/**
	 * generates recommendations for product of type and saves them to database and to HTML output file
	 * @param product
	 *
	 */
	public RecommendationResultsHolder recommendGenerated(Product product) {
		String type = "generated";
		RecommendationResultsHolder recommendationResults = new RecommendationResultsHolder(product);
		if (product != null && product.getTitle() != null) {

			long start = System.currentTimeMillis();
			recommendationResults = recommendationStrategy.getRecommendationResults(
					product, type);
			saveRecommendationsToDatabase(recommendationResults);
			File outFile = printRecommendationsToHTML(recommendationResults, type);
			long fin = System.currentTimeMillis();

			System.out.println("Execution time: " + String.valueOf(fin - start) + " ms");
			System.out.println("Output is written to " + outFile.getAbsolutePath());
		} else {
			System.out.println("Product " + product + " is empty");
		}
		return recommendationResults;
	}

//	/**
//	 * creates random recommendations for product1 and saves them to the database
//	 * @param product1
//	 */
//	public void recommendRandom(Product product1) {
//		System.out.println("\nRandom Recommendations: ");
//		for (int j = 0; j < RecommenderProperties.NUMBER_OF_RESULTS; j++) {
//			Product random = productManager.getRandom();
//			RecommendationId recommendationId = new RecommendationId(product1.getProductId(), j);
//			RandomRecommendation randomRecommendation = new RandomRecommendation(recommendationId,
//					random.getProductId());
//			RandomRecommendationDAO.getDefault().attachDirty(randomRecommendation);
//			System.out.println(random);
//		}
//	}

	private File printRecommendationsToHTML(RecommendationResultsHolder recommendationResults, String type) {
		Html html = createRecommendationsHTML(recommendationResults);

		Product product1 = recommendationResults.getBaseProduct();
		String htmlToString = html.write();
		String customer = configurator.getJsonProperties().getCustomer();
		String outPath = new File(SemRecSysConfigurator.getPropertiesDirPath()).getParentFile().getAbsolutePath()
				+ "/../out_" + customer + "/" + product1.getProductId() + "_" + type + ".html";

		try {
			FileUtils.writeTextToFile(htmlToString, outPath, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new File(outPath);
	}

	private void saveRecommendationsToDatabase(RecommendationResultsHolder recommendationResults) {
		persistenceService.saveRecommendations(recommendationResults.getRecommendationResults());
	}

	private Html createRecommendationsHTML(RecommendationResultsHolder recommendationResults) {
		HTMLOutputCreator htmlCreator = new HTMLOutputCreator(configurator,
				recommendationResults.getBaseProduct());
		for (RecommendationResult recommendationResult : recommendationResults.getRecommendationResults()) {
			htmlCreator.addRecommendationEntry(recommendationResult);
		}
		Html html = htmlCreator.getHtml();
		return html;
	}
}
