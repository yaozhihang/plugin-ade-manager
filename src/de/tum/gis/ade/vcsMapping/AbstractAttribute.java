package de.tum.gis.ade.vcsMapping;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "abstractAttribute")
@XmlSeeAlso({
    SimpleAttribute.class,
    ComplexAttribute.class
})
public abstract class AbstractAttribute
    extends AbstractProperty
{

	
}
