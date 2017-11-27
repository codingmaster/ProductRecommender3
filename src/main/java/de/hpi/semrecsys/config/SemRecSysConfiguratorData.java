package de.hpi.semrecsys.config;

import de.hpi.semrecsys.model.Entity;
import de.hpi.semrecsys.spotlight.SpotlightConnector;
import de.hpi.semrecsys.utils.Namespacer;
import de.hpi.semrecsys.utils.TextExtractor;
import org.apache.log4j.Logger;
import virtuoso.jdbc3.VirtuosoDataSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Helper class for configuration objects
 * @author Michael Wolowyk
 *
 */
public class SemRecSysConfiguratorData {
	public Namespacer namespacer;
	public String spotlightUrl;
	public TextExtractor textExtractor;
	public SpotlightConnector spotlightConnector;
	public VirtuosoDataSource virtuosoDatasource;
	public RecommenderProperties recommenderProperties;
	public String sparqlPropertiesPath;
	public String colorsPath;
	public String customProperties;
	public Properties sparqlProperties;
	public Logger logger;
	public JSONProperties jsonProperties;
	public String dbpediaSparqlEndpoint;
	public Map<String, ColorEntity> colors;
	public Map<String, Map<Entity, List<Entity>>> customPropertiesSimilarityMaps;
	public String propertySizesFileName;
	public CustomAttributeSimilarityCreator customAttributeSimilarityCreator;
	public int numberOfDbpediaTiples;

	public SemRecSysConfiguratorData(String sparqlPropertiesPath, String colorsPath, String customProperties) {
		this.sparqlPropertiesPath = sparqlPropertiesPath;
		this.colorsPath = colorsPath;
		this.customProperties = customProperties;
		this.colors = new HashMap<String, ColorEntity>();
		this.logger = Logger.getLogger(getClass());
		this.customPropertiesSimilarityMaps = new HashMap<String, Map<Entity, List<Entity>>>();
	}

	public enum LanguageCode {
		EN, DE;
	}

}
