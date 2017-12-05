package de.hpi.semrecsys.dto;

import de.hpi.semrecsys.Recommendation;
import de.hpi.semrecsys.RecommendationId;

public class RecommendationDto extends BaseDto{

    private final int linkedProductId;
    private final int position;
    private final Double score;
    private final Double relativeScore;

    public RecommendationDto(Recommendation recommendation) {
        this.linkedProductId = recommendation.getLinkedProductId();
        RecommendationId recommendationId = recommendation.getId();
        this.position = recommendationId.getPosition();
        this.score = recommendation.getScore();
        this.relativeScore = recommendation.getRelativeScore();
    }

    public int getLinkedProductId() {
        return linkedProductId;
    }

    public int getPosition() {
        return position;
    }

    public Double getScore() {
        return score;
    }

    public Double getRelativeScore() {
        return relativeScore;
    }
}
