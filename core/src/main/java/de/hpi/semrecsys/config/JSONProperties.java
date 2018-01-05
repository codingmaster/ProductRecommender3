package de.hpi.semrecsys.config;

import de.hpi.semrecsys.model.Attribute.AttributeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Container for properties from properties/customer/properties.json file
 * @author Michael Wolowyk
 *
 */
public class JSONProperties {

	private List<String> filterAttributes = new ArrayList<String>();
	private List<JSONAttribute> attributes = new ArrayList<JSONAttribute>();
	private String customer;
	private String customerWebsite;
	private String customerLogo;
	private Integer minProdId = -1;
	private Integer maxProdId = -1;
	private Map<String, Double> attributesByType;

	public List<JSONAttribute> getAttributes() {
		return attributes;
	}

	public Map<String, Double> getAttributesByType() {
		return getAttributesByType(AttributeType.struct, AttributeType.unstruct, AttributeType.split,
				AttributeType.cat, AttributeType.img);
	}

	public Map<String, Double> getAttributesByType(AttributeType... types) {
		attributesByType = new HashMap<>();
		List<String> typeList = new ArrayList<String>();
		for (AttributeType attributeType : types) {
			typeList.add(attributeType.name());
		}
		for (JSONAttribute attribute : getAttributes()) {
			String type = attribute.getType();
			if (typeList.contains(type)) {
				attributesByType.put(attribute.getName(), attribute.getWeight());
			}
		}
		return attributesByType;
	}

	public void updateAttributeWeight(String attribute, Double newWeight) {
		attributesByType.put(attribute, newWeight);
	}

	public void setAttributesByType(Map<String, Double> attributesByType) {
		this.attributesByType = attributesByType;
	}

	public String getCustomer() {
		return customer;
	}

	public String getCustomerWebsite() {
		return customerWebsite;
	}

	public Integer getMaxProdId() {
		return maxProdId;
	}

	public Integer getMinProdId() {
		return minProdId;
	}

	public String getCustomerLogo() {
		return customerLogo;
	}

	public List<String> getFilterAttributes() {
		return filterAttributes;
	}

	public static class JSONAttribute {
		String name;
		Double weight;
		String type;
		Double confidence;

		public String getName() {
			return name;
		}

		public String getType() {
			return type;
		}

		public Double getWeight() {
			return weight;
		}

		public Double getConfidence() {
			return confidence;
		}
	}
}