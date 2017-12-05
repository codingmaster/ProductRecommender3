package de.hpi.semrecsys;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "attribute")
public class AttributeTable extends BaseEntity{

	private String type;
	private int id;
	private String attributeCode;
	private List<OptionTable> options = new ArrayList<>();

	public AttributeTable() {
	}

    public AttributeTable(String attributeCode, String type) {
	    this.attributeCode = attributeCode;
	    this.type = type;
    }

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id", unique = true, nullable = false)
	public int getId() {
		return id;
	}

	@Column(name = "type", nullable = false, length = 8)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "code")
	public String getAttributeCode() {
		return this.attributeCode;
	}

	public void setAttributeCode(String attributeCode) {
		this.attributeCode = attributeCode;
	}

	@OneToMany(mappedBy = "attributeTable", cascade = CascadeType.ALL)
	public List<OptionTable> getOptions() {
		return options;
	}

	public void setOptions(List<OptionTable> options) {
		this.options = options;
	}
}
