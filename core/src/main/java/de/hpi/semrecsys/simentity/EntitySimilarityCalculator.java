package de.hpi.semrecsys.simentity;

import de.hpi.semrecsys.model.Entity;


/**
 * basic functionality for calculation of entity similarity
 */
public interface EntitySimilarityCalculator {
	public Double calculateSimilarity(Entity entity1, Entity entity2);
}
