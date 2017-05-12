package de.tum.gis.ade.vcsMapping;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlType(name = "dataType", propOrder = {
    "attributes",
    "mappingAdapter"
})
public class DataType {

    @XmlElements({
        @XmlElement(name = "attribute", type = SimpleAttribute.class),
        @XmlElement(name = "complexAttribute", type = ComplexAttribute.class)
    })
    protected List<AbstractAttribute> attributes;
    protected MappingAdapter mappingAdapter;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;
    @XmlAttribute
	protected String table;
    
    @XmlTransient
	protected SchemaMapping schemaMapping;
    
    protected DataType() {
    	
    }
    
    public String getTable() {
		return table;
	}

	public void setTable(String value) {
		this.table = value;
	}

	public boolean isSetTable() {
		return (this.table!= null);
	}

    public List<AbstractAttribute> getAttributes() {
        if (attributes == null) {
            attributes = new ArrayList<AbstractAttribute>();
        }
        return this.attributes;
    }
    
    public AbstractAttribute getAttribute(String name, String namespaceURI) {
		AppSchema schema = schemaMapping.getAppSchema(namespaceURI);
		if (schema != null) {
			for (AbstractAttribute attribute : getAttributes()) {
				if (attribute.getSchema() == schema) {
					String path = attribute.getPath();
					if (path.startsWith("@"))
						path = path.substring(1, path.length());

					if (path.equals(name))
						return attribute;
				}
			}
		}

		return null;
	}

    public boolean isSetAttributes() {
        return ((this.attributes!= null)&&(!this.attributes.isEmpty()));
    }

    public void addAttribute(AbstractAttribute attribute) {
    	if (attribute instanceof SimpleAttribute)
    		((SimpleAttribute)attribute).setParentDataType(this);
    	
    	getAttributes().add(attribute);
    }
    
    public void unsetAttributes() {
        this.attributes = null;
    }

    public MappingAdapter getMappingAdapter() {
        return mappingAdapter;
    }

    public void setMappingAdapter(MappingAdapter value) {
        this.mappingAdapter = value;
    }

    public boolean isSetMappingAdapter() {
        return (this.mappingAdapter!= null);
    }

    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }

    public boolean isSetId() {
        return id != null && !id.isEmpty();
    }

}
