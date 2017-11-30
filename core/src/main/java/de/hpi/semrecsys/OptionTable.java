package de.hpi.semrecsys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "option")
public class OptionTable extends BaseEntity implements DBObject {

	private int id;
	private String optionValue;
	private AttributeTable attributeTable;

	public OptionTable() {
	}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id", unique = true, nullable = false)
    @Override
    public Serializable getId() {
        return id;
    }

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "value")
	public String getOptionValue() {
		return this.optionValue;
	}

	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}

    @ManyToOne
    @JoinColumn(name = "attribute_id", nullable = false)
    public AttributeTable getAttributeTable() {
        return attributeTable;
    }

    public void setAttributeTable(AttributeTable attributeTable) {
        this.attributeTable = attributeTable;
    }
}
