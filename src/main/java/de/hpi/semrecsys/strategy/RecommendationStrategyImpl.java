package de.hpi.semrecsys.strategy;

import java.util.Collections;

import org.apache.log4j.Logger;

import de.hpi.semrecsys.GeneratedRecommendation;
import de.hpi.semrecsys.config.RecommenderProperties;
import de.hpi.semrecsys.config.SemRecSysConfigurator;
import de.hpi.semrecsys.model.Product;
import de.hpi.semrecsys.output.RecommendationResult;
import de.hpi.semrecsys.output.RecommendationResultsHolder;
import de.hpi.semrecsys.similarity.category.ProductSimilarityCalculator;
import de.hpi.semrecsys.virtuoso.SparqlQueryManager.QueryType;

/***
 * 
 * @author Michael Wolowyk
 *
 */
public class RecommendationStrategyImpl implements RecommendationStrategy {

	RecommendationPreselector preselector;
	RecommendationFilter filter;
	ProductSimilarityCalculator similarityCalculator;

	Logger log = Logger.getLogger(getClass());
	public RecommendationStrategyImpl(SemRecSysConfigurator configurator) {
		init(configurator);
	}

	public void init(SemRecSysConfigurator configurator) {
		this.preselector = new RecommendationPreselector(configurator);
		this.filter = new RecommendationFilter(configurator);
		this.similarityCalculator = new ProductSimilarityCalculator(configurator);
	}

	@Override
	public RecommendationResultsHolder getRecommendationResults(Product product, String type) {
		log.info("\n" + product + " is similar to: ");

		// 1. Preselect products
		log.info("\nStep1: Preselect Recommendations");
		RecommendationResultsHolder recommendationResults = preselector.getPreselectedSimilarProducts(product,
				QueryType.PRODUCTS_WITH_SIMILAR_ENTITIES, RecommenderProperties.NUMBER_OF_RESULTS * 3);

		similarityCalculator.fillProductWithAttributeEntityMapping(product);

		log.info("\nRecommended products: \n" + recommendationResults);

		// 2. filter too similar products
		log.info("\nStep2: Filter too similar recommendations");
		recommendationResults = filter.filterPreselectedRecommendations(recommendationResults);

		// 3. find similar products more precisely from preselection
		log.info("\nStep3: Reorder recommendations based on precise calculation");
		for (RecommendationResult recommendation : recommendationResults.getRecommendationResults()) {
			GeneratedRecommendation.type = type;
			recommendation = similarityCalculator.fillWithSimilarEntities(QueryType.SIMILAR_ENTITIES_BETWEEN_PRODUCTS,
					recommendation);

		}

		// 4. filter again
		log.info("\nStep4: Filter again");
		recommendationResults = filter.filterPreselectedRecommendations(recommendationResults);

		Collections.sort(recommendationResults.getRecommendationResults());
		return recommendationResults;
	}
}
