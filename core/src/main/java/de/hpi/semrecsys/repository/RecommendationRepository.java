package de.hpi.semrecsys.repository;

import de.hpi.semrecsys.RecommendationImpl;

import java.util.List;

public interface RecommendationRepository extends BaseRepository<RecommendationImpl>{
    List<RecommendationImpl> findByIdProductId(int productId);
}
