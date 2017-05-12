package de.tum.gis.ade.vcsMapping;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.citygml4j.model.module.citygml.CityGMLVersion;

@XmlType(name = "schema", propOrder = {
		"namespaces"
})
public class AppSchema {

	@XmlElement(name="namespace", required = true)
	protected List<Namespace> namespaces;
	@XmlAttribute(required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	@XmlID
	@XmlSchemaType(name = "ID")
	protected String id;
	@XmlAttribute(required = true)
	protected String name;
	
	@XmlTransient
	protected final List<ObjectType> objectTypes;
	@XmlTransient
	protected final List<FeatureType> featureTypes;

	protected AppSchema() {
		objectTypes = new ArrayList<>();
		featureTypes = new ArrayList<>();
	}

	public List<Namespace> getNamespaces() {
		if (namespaces == null) {
			namespaces = new ArrayList<>();
		}
		return namespaces;
	}
	
	public void unsetNamespaces() {
		this.namespaces = null;
	}
	
	public boolean isSetNamespace() {
		return ((this.namespaces!= null)&&(!this.namespaces.isEmpty()));
	}

	public void setNamespaces(List<Namespace> namespaces) {
		this.namespaces = namespaces;
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

	public String getName() {
		return name;
	}

	public void setName(String value) {
		this.name = value;
	}

	public boolean isSetName() {
		return (this.name!= null);
	}

	public boolean matchesNamespaceURI(String namespaceURI) {
		for (Namespace namespace : getNamespaces()) {
			if (namespace.getURI().equals(namespaceURI))
				return true;
		}
		
		return false;
	}
	
	public Namespace getNamespace(CityGMLVersion version) {
		for (Namespace namespace : getNamespaces()) {
			if (namespace.getContext() != null && namespace.getContext().getCityGMLVersion() == version)
				return namespace;
		}
		
		return null;
	}
	
	public CityGMLVersion getCityGMLVersion(String namespaceURI) {
		for (Namespace namespace : getNamespaces()) {
			if (namespace.getURI().equals(namespaceURI) && namespace.getContext() != null)
				return namespace.getContext().getCityGMLVersion();
		}
		
		return null;
	}
	
	public boolean isAvailableFor(CityGMLVersion version) {
		return getNamespace(version) != null;
	}

	public List<ObjectType> getObjectTypes() {
		return objectTypes;
	}

	public List<FeatureType> getFeatureTypes() {
		return featureTypes;
	}

}
