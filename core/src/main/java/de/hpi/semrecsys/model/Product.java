package de.hpi.semrecsys.model;

import de.hpi.semrecsys.DBObject;
import de.hpi.semrecsys.ProductTable;
import de.hpi.semrecsys.similarity.AttributeEntityMapping;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Product created from a set of {@link ProductTable} objects
 * is represented as set of attributes {@link Attribute}
 * @author Michael Wolowyk
 *
 */
public class Product implements DBObject {

	private String productId = null;
	private String storeProductId = null;
	private String title;
	Logger log = Logger.getLogger(getClass());

	Map<String, List<Attribute>> attributes = new HashMap<String, List<Attribute>>();
	Set<Category> categories = new HashSet<Category>();
	AttributeEntityMapping attributeEntityMapping;

	private List<String> imgPathes = new ArrayList<String>();

	public Product() {
	}

	public Product(String productId, List<ProductTable> productLines) {
		this.productId = productId;
		initAttributes(productLines);
	}

	public Product(String productId, List<ProductTable> productLines, Set<Category> categories) {
		this(productId, productLines);
		this.categories = categories;
	}

	private void initAttributes(List<ProductTable> productLines) {
		for (ProductTable productLine : productLines) {
			this.storeProductId = productLine.getId().getProductId();
			log.debug("processing product: " + this.storeProductId + " (" + productLine.getId().getEntityId() + "): " +  productLine.getValue());
			if (productLine.getValue() != null && !productLine.getValue().isEmpty()) {
				Attribute attribute = new Attribute(productLine);

				String attributeCode = attribute.getAttributeCode();

				String value = attribute.getValue();
				if (attributeCode.equalsIgnoreCase("name") || attributeCode.contains("title")) {
					this.title = value;
				}
				if (attributeCode.equalsIgnoreCase("img")) {
					this.imgPathes.add(value);
					continue;

				}

				List<Attribute> attributeList = attributes.get(attributeCode);
				if (attributeList == null) {
					attributeList = new ArrayList<Attribute>();
				}
				attributeList.add(attribute);
				attributes.put(attributeCode, attributeList);
			}
		}

	}

	/**
	 * Returns mapping between {@link Attribute}s and {@link Entity}s
	 * @param attributeEntityMapping
	 */
	public void setAttributeEntityMapping(AttributeEntityMapping attributeEntityMapping) {
		this.attributeEntityMapping = attributeEntityMapping;
	}

	public AttributeEntityMapping getAttributeEntityMapping() {
		return attributeEntityMapping;
	}

	public String getProductId() {
		return productId;
	}

	public Map<String, List<Attribute>> getAttributes() {
		return attributes;
	}

	public List<Attribute> getFlatAttributes(){
		return attributes.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
	}

	public void setProductId(String entity_id) {

		this.productId = entity_id;
	}

	public String getStoreProductId() {
		return storeProductId;
	}

	public void setStoreProductId(String storeProductId) {
		this.storeProductId = storeProductId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public List<String> getImgPathes() {
		return imgPathes;
	}

	protected String normalizeStringVal(Object objValue) {
		String value = null;
		if (objValue != null && objValue.getClass().equals(String.class)) {
			value = String.valueOf(objValue);

			value = value.replaceAll("\\<.*?>", "").replaceAll("-", " "); // .replaceAll(",",
																			// "").replaceAll("\\.",
																			// "");

		}

		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		return true;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public Serializable getId() {
		return this.productId;
	}

	@Override
	public String toString() {
		return toSimpleString();
		// return "[" + "entity_id = " + entityId + ", title=" + title + "]";
	}

	public String toSimpleString() {
		return "Product: " + getStoreProductId() + "\t" + getTitle();
	}

}
