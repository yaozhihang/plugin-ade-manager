package de.tum.gis.ade.vcsMapping;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlType(name = "abstractObjectType", propOrder = {
		"extension",
		"properties"
})
@XmlSeeAlso({
	FeatureType.class,
	ObjectType.class
})
public abstract class AbstractObjectType<T extends AbstractObjectType<T>>
	extends AbstractPathElement
{

	@XmlAttribute(required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	@XmlID
	@XmlSchemaType(name = "ID")
	protected String id;
	@XmlAttribute(required = true)
	protected String table;
	@XmlAttribute(required = true)
	protected int objectClass;
	@XmlAttribute(name = "abstract")
	protected Boolean _abstract;
	protected Extension<T> extension;
	@XmlElements({
		@XmlElement(name = "attribute", type = SimpleAttribute.class),
		@XmlElement(name = "complexAttribute", type = ComplexAttribute.class),
		@XmlElement(name = "objectProperty", type = ObjectProperty.class),
		@XmlElement(name = "featureProperty", type = FeatureProperty.class),
		@XmlElement(name = "geometryProperty", type = GeometryProperty.class),
		@XmlElement(name = "implicitGeometryProperty", type = ImplicitGeometryProperty.class)
	})
	protected List<AbstractProperty> properties;

	@XmlTransient
	protected SchemaMapping schemaMapping;

	public String getId() {
		return id;
	}

	public void setId(String value) {
		this.id = value;
	}

	public boolean isSetId() {
		return id != null && !id.isEmpty();
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
	
	public abstract boolean hasSharedTable(boolean skipAbstractTypes);

	protected boolean hasSharedTable(List<T> candidates, boolean skipAbstractTypes) {
		for (T candidate : candidates) {
			if (candidate == this)
				continue;

			if (candidate.getTable().equals(table)) {
				if (skipAbstractTypes && candidate.isAbstract())
					continue;

				return true;
			}
		}

		return false;
	}

	public int getObjectClass() {
		return objectClass;
	}

	public void setObjectClass(int value) {
		this.objectClass = value;
	}

	public boolean isSetObjectClass() {
		return true;
	}

	public boolean isAbstract() {
		if (_abstract == null) {
			return false;
		} else {
			return _abstract;
		}
	}

	public void setAbstract(boolean value) {
		this._abstract = value;
	}

	public boolean isSetAbstract() {
		return (this._abstract!= null);
	}

	public void unsetAbstract() {
		this._abstract = null;
	}

	public Extension<T> getExtension() {
		return extension;
	}

	public void setExtension(Extension<T> value) {
		this.extension = value;
	}

	public boolean isSetExtension() {
		return (this.extension!= null);
	}
	
	public abstract List<T> listSubTypes(boolean skipAbstractTypes);
	public abstract List<T> listSuperTypes(boolean includeType);

	public boolean isSubTypeOf(AbstractObjectType<?> superType) {
		if (!isSetExtension())
			return false;

		T parent = getExtension().getBase();
		while (parent != null) {
			if (parent == superType)
				return true;

			parent = parent.isSetExtension() ? parent.getExtension().getBase() : null;
		}

		return false;
	}

	protected List<T> listSubTypes(List<T> candidates, boolean skipAbstractTypes) {
		List<T> result = new ArrayList<T>();
		for (T candidate : candidates) {
			if (skipAbstractTypes && candidate.isAbstract())
				continue;

			if (candidate.isSubTypeOf(this))
				result.add(candidate);
		}

		return result;
	}

	public List<AbstractProperty> getProperties() {
		if (properties == null) {
			properties = new ArrayList<AbstractProperty>();
		}
		return this.properties;
	}

	public void setProperties(List<AbstractProperty> properties) {
		this.properties = properties;
	}

	public List<AbstractProperty> listProperties(boolean onlyQueryable, boolean includeInherited) {
		List<AbstractProperty> result = new ArrayList<AbstractProperty>();
		AbstractObjectType<?> type = this;

		while (type != null) {
			for (AbstractProperty property : type.getProperties()) {
				if (!onlyQueryable || property.isQueryable())
					result.add(property);
			}

			type = includeInherited && type.isSetExtension() ? type.getExtension().getBase() : null;
		}

		return result;
	}

	public AbstractProperty getProperty(String name, String namespaceURI, boolean includeInherited) {
		AppSchema schema = schemaMapping.getAppSchema(namespaceURI);
		if (schema != null) {
			for (AbstractProperty property : listProperties(false, includeInherited)) {
				if (property.getSchema() == schema) {
					String path = property.getPath();
					if (path.startsWith("@"))
						path = path.substring(1, path.length());

					if (path.equals(name))
						return property;
				}
			}
		}

		return null;
	}

	public boolean isSetProperties() {
		return ((this.properties!= null)&&(!this.properties.isEmpty()));
	}
	
	public void addProperty(AbstractProperty property) {
		if (property instanceof SimpleAttribute)
			((SimpleAttribute)property).setParentObjectType(this);
		
		getProperties().add(property);
	}

	public void unsetProperties() {
		this.properties = null;
	}

}
