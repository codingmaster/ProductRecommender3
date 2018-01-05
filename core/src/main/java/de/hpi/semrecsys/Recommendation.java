package de.hpi.semrecsys;

/**
 * Interface for Recommendation objects
 * @author Michael Wolowyk
 *
 */
public interface Recommendation extends DBObject {

	public RecommendationId getId();

	public String getLinkedProductId();

	public String recommendationScoreToString();

	Double getScore();

	Double getRelativeScore();

	public boolean empty();

	public String recommendationType();

}
