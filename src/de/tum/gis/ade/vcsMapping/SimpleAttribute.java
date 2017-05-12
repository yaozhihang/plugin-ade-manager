package de.tum.gis.ade.vcsMapping;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "attribute", propOrder = {
	    "join"
	})
public class SimpleAttribute
	extends AbstractAttribute
	implements Joinable
{

	protected Join join;
	@XmlAttribute(required = true)
	protected String column;
	@XmlAttribute(required = true)
	protected SimpleType type;
	
	@XmlTransient
	protected AbstractObjectType<?> objectType;
	@XmlTransient
	protected DataType dataType;
	@XmlTransient
	protected String name;

	public SimpleAttribute() {

	}
	
	public boolean hasParentObjectType() {
		return objectType != null;
	}
	
	public AbstractObjectType<?> getParentObjectType() {
		return objectType;
	}

	public void setParentObjectType(AbstractObjectType<?> objectType) {
		this.objectType = objectType;
		dataType = null;
	}

	public boolean hasParentDataType() {
		return dataType != null;
	}
	
	public DataType getParentDataType() {
		return dataType;
	}
	
	public void setParentDataType(DataType dataType) {
		this.dataType = dataType;
		objectType = null;
	}

	@Override
    public Join getJoin() {
        return join;
    }

    public void setJoin(Join value) {
        this.join = value;
    }

    @Override
    public boolean isSetJoin() {
        return (this.join!= null);
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

	public SimpleType getType() {
		return type;
	}

	public void setType(SimpleType value) {
		this.type = value;
	}

	public boolean isSetType() {
		return (this.type!= null);
	}
	
	public String getName() {
		if (name == null)
			name = path.startsWith("@") ? path.substring(1, path.length()) : path;

		return name;
	}

	@Override
	public PathElementType getElementType() {
		return PathElementType.SIMPLE_ATTRIBUTE;
	}

}
