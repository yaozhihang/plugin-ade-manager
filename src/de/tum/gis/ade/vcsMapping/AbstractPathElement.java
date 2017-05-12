package de.tum.gis.ade.vcsMapping;

import java.util.HashMap;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

import org.citygml4j.model.module.citygml.CityGMLVersion;

@XmlType(name = "abstractPathElement")
@XmlSeeAlso({
    FeatureType.class,
    AbstractProperty.class
})
public abstract class AbstractPathElement {

    @XmlAttribute(required = true)
    protected String path;
    @XmlAttribute(required = true)
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected AppSchema schema;
    @XmlAttribute
    protected Boolean queryable;
    
    @XmlTransient
    private HashMap<String, Object> localProperties;

	public abstract PathElementType getElementType();

    public String getPath() {
        return path;
    }

    public void setPath(String value) {
        this.path = value;
    }

    public boolean isSetPath() {
        return (this.path!= null);
    }

    public AppSchema getSchema() {
        return schema;
    }

    public void setSchema(AppSchema schema) {
        this.schema = schema;
    }

    public boolean isSetSchema() {
        return (this.schema!= null);
    }

    public boolean isQueryable() {
        if (queryable == null) {
            return true;
        } else {
            return queryable;
        }
    }

    public void setQueryable(boolean value) {
        this.queryable = value;
    }

    public boolean isSetQueryable() {
        return (this.queryable!= null);
    }

    public void unsetQueryable() {
        this.queryable = null;
    }
    
    public Object getLocalProperty(String name) {
		if (localProperties != null)
			return localProperties.get(name);
			
		return null;
	}

	public void setLocalProperty(String name, Object value) {
		if (localProperties == null)
			localProperties = new HashMap<String, Object>();
		
		localProperties.put(name, value);
	}

	public boolean hasLocalProperty(String name) {
		return localProperties != null && localProperties.containsKey(name);
	}

	public Object unsetLocalProperty(String name) {
		if (localProperties != null)
			return localProperties.remove(name);
		
		return null;
	}
	
	public boolean matchesName(String name, String namespaceURI) {
		return schema.matchesNamespaceURI(namespaceURI) && path.equals(name);
	}
	
	public boolean matchesName(QName name) {
		return matchesName(name.getLocalPart(), name.getNamespaceURI());
	}
	
	public boolean isAvailableFor(CityGMLVersion version) {
		return schema.isAvailableFor(version);
	}
	
}
