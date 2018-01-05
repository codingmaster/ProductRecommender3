package de.hpi.semrecsys.populator;

import de.hpi.semrecsys.config.SemRecSysConfigurator;
import de.hpi.semrecsys.model.Attribute;
import de.hpi.semrecsys.model.Attribute.AttributeType;
import de.hpi.semrecsys.model.AttributeEntity;
import de.hpi.semrecsys.model.CustomEntity;
import de.hpi.semrecsys.model.Entity;
import de.hpi.semrecsys.model.Product;
import de.hpi.semrecsys.similarity.AttributeEntityMapping;
import de.hpi.semrecsys.spotlight.SpotlightConnector;
import de.hpi.semrecsys.spotlight.SpotlightConnector.EndpointType;
import de.hpi.semrecsys.spotlight.SpotlightRequest;
import de.hpi.semrecsys.spotlight.SpotlightResponse;
import de.hpi.semrecsys.spotlight.SpotlightResponse.ResponseResource;
import de.hpi.semrecsys.utils.Namespacer;
import de.hpi.semrecsys.utils.TextExtractor;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * finds entities from the given text using Spotlight Webservice
 * @see <a href="https://github.com/dbpedia-spotlight/dbpedia-spotlight/wiki">Spotlight Webservice</a>
 *
 */
public class EntityFinder {

	private TextExtractor textExtractor;
	private SpotlightConnector spotlightConnector;
	private Namespacer namespacer;
//	private double simThreshold;

	private EndpointType endpointType;
	private Logger log = Logger.getLogger(getClass());
//	private double confidence;
	private AttributeEntityMapping attributeEntitiesMapping;
	private SemRecSysConfigurator configurator;

	public EntityFinder(SemRecSysConfigurator configurator) {
		init(configurator);
	}

	private void init(SemRecSysConfigurator configurator) {
		this.configurator = configurator;
		this.textExtractor = configurator.getTextExtractor();
		this.spotlightConnector = configurator.getSpotlightConnector();
		this.namespacer = configurator.getNamespacer();
		this.endpointType = EndpointType.ANNOTATE;
		this.attributeEntitiesMapping = new AttributeEntityMapping(configurator);

	}

    /**
     * returns entities found in text with confidence
     * @param text
     * @param confidence
     * @return map from entity to list of response resources
     */
	public Map<Entity, List<ResponseResource>> getEntitiesFromText(String text, Double confidence) {
		List<ResponseResource> resources = getResourceEntities(text, confidence);
		return getEntityResponseResourceMap(resources);
	}

    /**
     * returns mapping from attribute to entities for the given product
     * @param product
     * @return attribute entity mapping
     */
	public AttributeEntityMapping getAttributeEntityMapping(Product product) {
		attributeEntitiesMapping.setProduct(product);
		// for each string attribute in product
		for (List<Attribute> attributeList : product.getAttributes().values()) {
			for (Attribute attribute : attributeList) {
				log.debug("Processing attribute: " + attribute.getValue());
				if (attribute.getType().equals(AttributeType.unstruct)) {
					List<AttributeEntity> attributeEntities = processUnstructAttribute(attribute);
					log.debug("Found: " + attributeEntities.size() + " attributeEntities: " + attributeEntities);

					attributeEntitiesMapping.addAllAttributeEntities(attributeEntities);
				} else {
					attributeEntitiesMapping.addAttributeEntity(processStructAttribute(attribute));
				}
			}
		}
		return attributeEntitiesMapping;
	}

	private AttributeEntity processStructAttribute(Attribute attribute) {
		Entity entity = new Entity(attribute.getValue(), "meta");
		entity.setMeta(true);
		return new AttributeEntity(attribute, entity);
	}

	private List<AttributeEntity> processUnstructAttribute(Attribute attribute) {
		List<ResponseResource> resources = getResources(attribute);
		List<AttributeEntity> attributeEntities = new ArrayList<>();
		if (!resources.isEmpty()) {
			Map<Entity, List<ResponseResource>> entityResponseResourceMap = getEntityResponseResourceMap(resources);

			for (Entry<Entity, List<ResponseResource>> mapEntry : entityResponseResourceMap.entrySet()) {
				Entity entity = mapEntry.getKey();
				List<ResponseResource> responseList = mapEntry.getValue();
				AttributeEntity attributeEntity = new AttributeEntity(attribute, entity);
				attributeEntity.addResponseResources(responseList);
				attributeEntities.add(attributeEntity);
			}
		}
		return attributeEntities;
	}

	private List<ResponseResource> getResources(Attribute attribute) {
		String text = attribute.getValue();

		List<ResponseResource> resourceEntities = new ArrayList<>();
		configurator.getJsonProperties().getAttributes()
				.stream()
				.filter(jsonAttribute -> jsonAttribute.getName().equals(attribute.getAttributeCode()))
				.findFirst()
				.ifPresent(jsonAttribute -> {
					Double attributeConfidence = jsonAttribute.getConfidence();
					resourceEntities.addAll(getResourceEntities(text, attributeConfidence));
				});

		if (resourceEntities.isEmpty()) {
			resourceEntities.addAll(getResourceEntities(text, SemRecSysConfigurator.MINIMAL_ATTRIBUTE_CONFIDENCE));
		}

		return resourceEntities;
	}
//
//	private void updateConfidenceAndThreshold(Attribute attribute) {
//		confidence = SemRecSysConfigurator.MINIMAL_ATTRIBUTE_CONFIDENCE;
//		simThreshold = SemRecSysConfigurator.MINIMAL_SIM_THRESHOLD;
//		if (attribute.getType().equals(AttributeType.unstruct)) {
//			confidence = SemRecSysConfigurator.PLAIN_TEXT_CONFIDENCE;
//			simThreshold = SemRecSysConfigurator.PLAIN_TEXT_SIM_THRESHOLD;
//		}
//	}

	/**
	 * Executes request to Spotlight Endpoint and returns entity resources from
	 * it
	 * 
	 * @param text
	 *            input text
	 * @return list of response entity resources
	 */
	private List<ResponseResource> getResourceEntities(String text, Double confidence) {
		SpotlightRequest spotlightRequest = new SpotlightRequest(text, confidence);
		return getResourceEntities(spotlightRequest);

	}

	private List<ResponseResource> getResourceEntities(SpotlightRequest request) {
		SpotlightResponse spotlightResponse = spotlightConnector.getSpotlightResponse(request, endpointType);

		List<ResponseResource> resources = spotlightResponse.getResources();
		String text = request.getText();

		// get resources from splitted text
		text = textExtractor.getSplittedText(text).replace("\n", "");
		if (!text.isEmpty()) {
			request.setText(text);
			spotlightResponse = spotlightConnector.getSpotlightResponse(request, endpointType);
			log.debug("Spotlight Response: " + spotlightResponse);
		}
		return resources;
	}

	protected Map<Entity, List<ResponseResource>> getEntityResponseResourceMap(List<ResponseResource> resources) {
		Map<Entity, List<ResponseResource>> entityResponseResourceMap = new HashMap<Entity, List<ResponseResource>>();

		for (ResponseResource resource : resources) {
			String resourceUri = resource.getURI();

			String namespacedUri = namespacer.process(resourceUri);
			Entity entity = new Entity(namespacedUri);
			if (resource.isMeta()) {
				String uri = namespacedUri.replace("meta:", "");
				entity = new CustomEntity(uri);
			}

			List<ResponseResource> entityResources = entityResponseResourceMap.get(entity);
			if (entityResources == null) {
				entityResources = new ArrayList<>();
			}
			entityResources.add(resource);
			entityResponseResourceMap.put(entity, entityResources);

		}
		return entityResponseResourceMap;
	}

}
