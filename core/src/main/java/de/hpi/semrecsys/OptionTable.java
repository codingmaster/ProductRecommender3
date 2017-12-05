package de.hpi.semrecsys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "option")
public class OptionTable extends BaseEntity{

	private int id;
	private String value;
	private AttributeTable attributeTable;

	public OptionTable(String value){
	    this.value = value;
    }

	public OptionTable() {
	}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "value")
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
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
