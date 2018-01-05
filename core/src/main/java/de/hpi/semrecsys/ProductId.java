package de.hpi.semrecsys;

import javax.persistence.Column;

// default package
// Generated May 26, 2014 4:38:29 PM by Hibernate Tools 3.4.0.CR1

/**
 * ProductId generated by hbm2java
 */
public class ProductId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String entityId;
	private String productId;
	private String attributeCode;
	private long optionId;

	public ProductId() {
	}

	public ProductId(String entityId, String attributeCode, long optionId) {
		this.entityId = entityId;
		this.attributeCode = attributeCode;
		this.optionId = optionId;
		this.productId = entityId;
	}

	@Column(name = "product_id", nullable = false)
	public String getProductId() {
		return this.productId;
	}

	@Column(name = "entity_id", nullable = false)
	public String getEntityId() {
		return this.entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	@Column(name = "attribute_code", nullable = false)
	public String getAttributeCode() {
		return this.attributeCode;
	}

	public void setAttributeCode(String attributeCode) {
		this.attributeCode = attributeCode;
	}

	@Column(name = "option_id", nullable = false)
	public long getOptionId() {
		return this.optionId;
	}

	public void setOptionId(long optionId) {
		this.optionId = optionId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ProductId))
			return false;
		ProductId castOther = (ProductId) other;

		return (this.getEntityId() == castOther.getEntityId())
				&& ((this.getAttributeCode() == castOther.getAttributeCode()) || (this.getAttributeCode() != null
						&& castOther.getAttributeCode() != null && this.getAttributeCode().equals(
						castOther.getAttributeCode()))) && (this.getOptionId() == castOther.getOptionId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getEntityId().hashCode();
		result = 37 * result + (getAttributeCode() == null ? 0 : this.getAttributeCode().hashCode());
		result = 37 * result + (int) this.getOptionId();
		return result;
	}

	@Override
	public String toString() {
		return entityId + ", " + attributeCode + ", " + optionId;
	}

}
