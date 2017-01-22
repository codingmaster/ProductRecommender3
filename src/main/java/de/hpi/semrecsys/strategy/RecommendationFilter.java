package de.hpi.semrecsys.strategy;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import de.hpi.semrecsys.config.RecommenderProperties;
import de.hpi.semrecsys.config.SemRecSysConfigurator;
import de.hpi.semrecsys.model.AttributeEntity;
import de.hpi.semrecsys.model.Product;
import de.hpi.semrecsys.output.RecommendationResult;
import de.hpi.semrecsys.output.RecommendationResultsHolder;
import de.hpi.semrecsys.similarity.AttributeEntityMapping;
import de.hpi.semrecsys.similarity.category.ProductSimilarityCalculator;

public class RecommendationFilter {

	Logger log = Logger.getLogger(getClass());
	ProductSimilarityCalculator similarityCalculator;
	boolean calculateIntraListSimilarity = true;

	public RecommendationFilter(SemRecSysConfigurator configurator) {
		this.similarityCalculator = new ProductSimilarityCalculator(configurator);
	}

	public void setLogLevel(Level level) {
		log.setLevel(level);

	}

	public RecommendationResultsHolder filterPreselectedRecommendations(
			RecommendationResultsHolder preselectedRecommendations) {
		RecommendationResultsHolder filteredResults = new RecommendationResultsHolder(
				preselectedRecommendations.getBaseProduct());
		Product lastProduct = null;
		for (RecommendationResult recommendationResult : preselectedRecommendations.getRecommendationResults()) {
			Product recommendationProduct = recommendationResult.recommendedProduct();
			if (isRelevantRecommendation(lastProduct, recommendationResult)) {
				filteredResults.add(recommendationResult);
				lastProduct = recommendationProduct;
			} else {
				log.warn("Product " + recommendationProduct + " wasn't added to the result list");
			}
		}
		return filteredResults;
	}

	private boolean isRelevantRecommendation(Product lastProduct, RecommendationResult recommendationResult) {

		Product recommendationProduct = recommendationResult.recommendedProduct();
		Product baseProduct = recommendationResult.getBaseProduct();
		return recommendationResult != null && recommendationProduct != null
				&& RecommendationValidator.isValidRecommendation(recommendationProduct)
				&& recommendationProduct.getTitle() != null && !recommendationProduct.getImgPathes().isEmpty()
				// && recommendationResult.getRelativeScore() != null
				// && recommendationResult.getRelativeScore() >=
				// RecommenderProperties.MIN_PRODUCT_SIMILARITY
				// && recommendationResult.getRelativeScore() <=
				// RecommenderProperties.MAX_PRODUCT_SIMILARITY
				&& !isSimilar(recommendationProduct, lastProduct) && !isSimilar(recommendationProduct, baseProduct);

	}

	public boolean isSimilar(Product product1, Product product2) {
		if (calculateIntraListSimilarity) {
			log.info("Check intra similarity " + product1 + "; " + product2);
			if (product2 == null || product2.getTitle() == null) {
				return false;
			}
			Double value = calculateQuickSimilarity(product1, product2);
			log.info("Dice value: = " + value);

			if (value >= RecommenderProperties.MAX_PRODUCT_FILTER_SIMILARITY) {
				return true;
			}
		}
		return false;
	}

	public Double calculateQuickSimilarity(Product product1, Product product2) {
		similarityCalculator.fillProductWithAttributeEntityMapping(product1);
		AttributeEntityMapping attributeEntityHolder1 = product1.getAttributeEntityMapping();
		AttributeEntityMapping attributeEntityHolder2 = product2.getAttributeEntityMapping();

		List<AttributeEntity> commonAttributeEntities = new ArrayList<AttributeEntity>();
		List<AttributeEntity> attributeEntities1 = attributeEntityHolder1.getAttributeEntities();
		List<AttributeEntity> attributeEntities2 = attributeEntityHolder2.getAttributeEntities();

		for (AttributeEntity attributeEntity1 : attributeEntities1) {
			if (attributeEntities2.contains(attributeEntity1)) {
				commonAttributeEntities.add(attributeEntity1);
			}
		}

		log.debug("attributeEntities1: " + attributeEntities1);
		log.debug("attributeEntities2: " + attributeEntities2);
		log.debug("commonAttributeEntities: " + commonAttributeEntities);

		int attributeEntities1Size = attributeEntities1.size();
		int attributeEntities2Size = attributeEntities2.size();
		int commonEntitiesSize = commonAttributeEntities.size();

		log.debug("ae1: " + attributeEntities1Size);
		log.debug("ae2: " + attributeEntities2Size);
		log.debug("common: " + commonEntitiesSize);
		Double dice = (double) (2 * (double) commonEntitiesSize / (double) (attributeEntities1Size + attributeEntities2Size));
		return dice;
	}

}
