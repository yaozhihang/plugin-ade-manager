package de.tum.gis.ade.vcsMapping;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "condition")
public class Condition {

	@XmlAttribute(required = true)
	protected String column;
	@XmlAttribute(required = true)
	protected String value;
	@XmlAttribute(required = true)
	protected SimpleType type;
	
	protected Condition() {
		
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String value) {
		this.column = value;
	}

	public boolean isSetColumn() {
		return (this.column!= null);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isSetValue() {
		return (this.value!= null);
	}

	public SimpleType getType() {
		return type;
	}

	public void setType(SimpleType value) {
		this.type = value;
	}

	public boolean isSetType() {
		return (this.type!= null);
	}

}
