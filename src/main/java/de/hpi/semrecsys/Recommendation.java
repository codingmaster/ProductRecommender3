package de.hpi.semrecsys;

/**
 * Interface for Recommendation objects
 * @author Michael Wolowyk
 *
 */
public interface Recommendation extends DBObject {

	public RecommendationId getId();

	public int getLinkedProductId();

	public String recommendationScoreToString();

	public boolean empty();

	public String recommendationType();

}
