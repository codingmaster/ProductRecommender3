package de.hpi.semrecsys;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class ProductTable extends BaseEntity implements DBObject {

	private ProductId id;
	private String type;
	private int attributeId;
	private String value;
	private String valueWithEntities;

	public ProductTable() {
	}

	public ProductTable(ProductId id, String type, int attributeId) {
		this.id = id;
		this.type = type;
		this.attributeId = attributeId;
	}

	public ProductTable(ProductId id, String type, int attributeId, String value) {
		this.id = id;
		this.type = type;
		this.attributeId = attributeId;
		this.value = value;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "entityId", column = @Column(name = "entity_id", nullable = false)),
			@AttributeOverride(name = "attributeCode", column = @Column(name = "attribute_code", nullable = false)),
			@AttributeOverride(name = "optionId", column = @Column(name = "option_id", nullable = false)) })
	public ProductId getId() {
		return this.id;
	}

	public void setId(ProductId id) {
		this.id = id;
	}

	@Column(name = "type", nullable = false, length = 8)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "attribute_id", nullable = false)
	public int getAttributeId() {
		return this.attributeId;
	}

	public void setAttributeId(int attributeId) {
		this.attributeId = attributeId;
	}

	@Column(name = "value")
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValueWithEntities() {
		return valueWithEntities;
	}

	@Column(name = "value_with_entities")
	public void setValueWithEntities(String valueWithEntities) {
		this.valueWithEntities = valueWithEntities;
	}
}
