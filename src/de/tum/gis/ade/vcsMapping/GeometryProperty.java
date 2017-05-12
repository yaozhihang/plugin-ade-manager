package de.tum.gis.ade.vcsMapping;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "geometryProperty", propOrder = {
	    "join"
	})
public class GeometryProperty
	extends AbstractProperty
	implements Joinable
{

	protected Join join;
	@XmlAttribute(required = false)
	protected String refColumn;
	@XmlAttribute(required = false)
	protected String inlineColumn;
	@XmlAttribute(required = true)
	protected GeometryType type;
	@XmlAttribute
	protected Integer srsDimension = 3;

	protected GeometryProperty() {

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

	public String getRefColumn() {
		return refColumn;
	}

	public void setRefColumn(String refColumn) {
		this.refColumn = refColumn;
	}
	
	public boolean isSetRefColumn() {
		return refColumn != null;
	}

	public String getInlineColumn() {
		return inlineColumn;
	}

	public void setInlineColumn(String inlineColumn) {
		this.inlineColumn = inlineColumn;
	}
	
	public boolean isSetInlineColumn() {
		return inlineColumn != null;
	}

	public GeometryType getType() {
		return type;
	}

	public void setType(GeometryType value) {
		this.type = value;
	}

	public boolean isSetType() {
		return (this.type!= null);
	}

	public int getSrsDimension() {
		if (srsDimension == null)
			return 3;
		
		return srsDimension.intValue();
	}

	public void setSrsDimension(int srsDimension) {
		if (srsDimension >= 2 && srsDimension <= 3)
			this.srsDimension = srsDimension;
	}

	@Override
	public PathElementType getElementType() {
		return PathElementType.GEOMETRY_PROPERTY;
	}

}
