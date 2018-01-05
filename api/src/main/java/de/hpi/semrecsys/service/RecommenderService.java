package de.hpi.semrecsys.service;

import de.hpi.semrecsys.Recommendation;
import de.hpi.semrecsys.main.Recommender;
import de.hpi.semrecsys.model.Product;
import de.hpi.semrecsys.output.RecommendationResult;
import de.hpi.semrecsys.output.RecommendationResultsHolder;
import de.hpi.semrecsys.populator.Populator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommenderService {

    private final Recommender recommender;
    private PersistenceService persistenceService;
    private Populator populator;

    public RecommenderService(PersistenceService persistenceService){
        this.persistenceService = persistenceService;
        this.populator = Populator.getDefault(persistenceService);
        this.recommender = Recommender.getDefault(persistenceService);
    }

    public List<Recommendation> recommendProduct(String productId) {
        Product product = persistenceService.getProduct(productId);
        RecommendationResultsHolder recommendationResultsHolder = recommender.recommendGenerated(product);
        return recommendationResultsHolder.getRecommendationResults().stream().map(RecommendationResult::toRecommendation).collect(Collectors.toList());
    }

}
