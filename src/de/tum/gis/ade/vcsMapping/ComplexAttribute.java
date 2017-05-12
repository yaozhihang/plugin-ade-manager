package de.tum.gis.ade.vcsMapping;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "complexAttribute", propOrder = {
    "join",
    "inlineType"
})
public class ComplexAttribute
    extends AbstractAttribute
    implements Joinable
{

    protected Join join;
    @XmlAttribute(name = "type")
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected DataType refType;
    @XmlElement(name="type", required=false)
    protected DataType inlineType;
    
    protected ComplexAttribute() {

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

    public void setRefType(DataType refType) {
		this.refType = refType;
		inlineType = null;
	}

	public void setInlineType(DataType inlineType) {
		inlineType.id = null;
		this.inlineType = inlineType;
		refType = null;
	}

	public DataType getType() {
        return refType != null ? refType : inlineType;
    }

    public boolean isSetType() {
        return refType != null || inlineType != null;
    }
    
    @Override
	public PathElementType getElementType() {
		return PathElementType.COMPLEX_ATTRIBUTE;
	}

}
