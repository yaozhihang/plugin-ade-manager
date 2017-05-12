package de.tum.gis.ade.vcsMapping;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "abstractProperty")
@XmlSeeAlso({
    FeatureProperty.class,
    GeometryProperty.class,
    SimpleAttribute.class,
    ComplexAttribute.class
})
public abstract class AbstractProperty
    extends AbstractPathElement
{

	@XmlAttribute
	protected Integer minOccurs = 0;

	public Integer getMinOccurs() {
		return minOccurs;
	}

	public void setMinOccurs(Integer minOccurs) {
		this.minOccurs = minOccurs;
	}
	
}
