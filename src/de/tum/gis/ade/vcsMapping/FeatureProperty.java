package de.tum.gis.ade.vcsMapping;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "featureProperty", propOrder = {
    "join"
})
public class FeatureProperty
    extends AbstractObjectTypeProperty
    implements Joinable
{

    @XmlElements({
        @XmlElement(type = Join.class),
        @XmlElement(name = "joinTable", type = JoinTable.class)
    })
    protected AbstractJoin join;
    @XmlAttribute(name = "target", required = true)
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected FeatureType type;
    
    protected FeatureProperty() {
    	
    }

    @Override
    public AbstractJoin getJoin() {
        return join;
    }

    public void setJoin(AbstractJoin value) {
        this.join = value;
    }

    @Override
    public boolean isSetJoin() {
        return (this.join!= null);
    }

    @Override
    public FeatureType getObjectType() {
        return type;
    }

    public void setObjectType(FeatureType value) {
        this.type = value;
    }

    @Override
    public boolean isSetObjectType() {
        return (this.type!= null);
    }
    
    @Override
	public PathElementType getElementType() {
		return PathElementType.FEATURE_PROPERTY;
	}

}
