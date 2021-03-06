package de.hpi.semrecsys;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

// default package
// Generated Jun 23, 2014 2:32:33 PM by Hibernate Tools 3.4.0.CR1

/**
 * ProductCategory generated by hbm2java
 */
@Entity
@Table(name = "product_category")
public class ProductCategory implements DBObject {

	private ProductCategoryId id;

	public ProductCategory() {
	}

	public ProductCategory(ProductCategoryId id) {
		this.id = id;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "type", column = @Column(name = "type", nullable = false, length = 3)),
			@AttributeOverride(name = "entityId", column = @Column(name = "entity_id", nullable = false)),
			@AttributeOverride(name = "attributeId", column = @Column(name = "attribute_id", nullable = false)),
			@AttributeOverride(name = "attributeCode", column = @Column(name = "attribute_code", nullable = false, length = 8)),
			@AttributeOverride(name = "optionId", column = @Column(name = "option_id", nullable = false)),
			@AttributeOverride(name = "value", column = @Column(name = "value")) })
	public ProductCategoryId getId() {
		return this.id;
	}

	public void setId(ProductCategoryId id) {
		this.id = id;
	}

}
