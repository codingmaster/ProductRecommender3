package de.hpi.semrecsys.simentity;

import de.hpi.semrecsys.config.SemRecSysConfigurator;
import de.hpi.semrecsys.model.Entity;

/**
 * calculate entity similarity using taxonomical structure
 */
public class CategoryEntitySimilarityCalculator extends WikipageLinksEntitySimilarityCalculator implements
		EntitySimilarityCalculator {

	public CategoryEntitySimilarityCalculator(SemRecSysConfigurator configurator) {
		super(configurator);
	}

	public CategoryEntitySimilarityCalculator(SemRecSysConfigurator configurator, int depth) {
		super(configurator, depth);
	}

	@Override
	public Double calculateSimilarity(Entity entity1, Entity entity2) {
		return super.calculateSimilarity(entity1, entity2);
	}

	@Override
	public String getQuery(int depth, String uri, String... vars) {
		String sparqlQueryString1 = null;
		if (depth == 0) {
			sparqlQueryString1 = "SELECT DISTINCT ?" + vars[0] + " WHERE { " + "<" + uri
					+ "> <http://purl.org/dc/terms/subject> ?" + vars[0] + " . " + "} ";
		} else if (depth == 1) {
			sparqlQueryString1 = "SELECT DISTINCT ?" + vars[0] + " ?" + vars[1] + " WHERE { " + "<" + uri
					+ "> <http://purl.org/dc/terms/subject> ?" + vars[1] + ". " + "?" + vars[1] + " ?p ?" + vars[0]
					+ " . " + "FILTER isURI(?" + vars[0] + " )" + "} ";
		} else if (depth == 2) {
			sparqlQueryString1 = "SELECT DISTINCT ?" + vars[0] + " WHERE " + "{ " + "<" + uri
					+ "> <http://purl.org/dc/terms/subject> ?" + vars[1] + ". " + "?o ?p ?o2 . " + "?o2 ?p ?" + vars[0]
					+ " . " + "FILTER isURI(?" + vars[0] + " )" + "} ";
		} else if (depth == 3) {
			sparqlQueryString1 = "SELECT DISTINCT ?" + vars[0] + " WHERE " + "{ " + "<" + uri
					+ "> <http://purl.org/dc/terms/subject> ?" + vars[1] + ". " + "?o ?p ?o2 . " + "?o2 ?p ?o3 . "
					+ "?o3 ?p ?" + vars[0] + " . " + "FILTER isURI(?" + vars[0] + " )" + "} ";
		}
		return sparqlQueryString1;
	}

}
