package de.tum.gis.ade.vcsMapping;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "abstractObjectTypeProperty")
public abstract class AbstractObjectTypeProperty
    extends AbstractProperty
{

    public abstract AbstractObjectType<?> getObjectType();
    public abstract boolean isSetObjectType();

}
