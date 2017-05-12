package de.tum.gis.ade.vcsMapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

import org.citygml4j.model.citygml.CityGMLClass;
import org.citygml4j.model.module.Module;
import org.citygml4j.model.module.citygml.CityGMLModule;
import org.citygml4j.model.module.citygml.CityGMLVersion;

@XmlRootElement
@XmlType(name = "schemaMapping", propOrder = {
		"schema",
		"dataType",
		"objectType",
		"featureType"
})
public class SchemaMapping {

	@XmlElementWrapper(name="applicationSchemas", required = true)
	@XmlElement(required = true)
	protected List<AppSchema> schema;
	@XmlElementWrapper(name="dataTypes", required = false)
	@XmlElement(required = true)
	protected List<DataType> dataType;
	@XmlElementWrapper(name="objectTypes", required = false)
	@XmlElement(required = true)
	protected List<ObjectType> objectType;
	@XmlElementWrapper(name="featureTypes", required = true)
	@XmlElement(required = true)
	protected List<FeatureType> featureType;

	@XmlTransient
	protected final HashMap<String, AppSchema> uriToSchemaMap;

	protected SchemaMapping() {
		uriToSchemaMap = new HashMap<>();
	}

	public List<AppSchema> getAppSchemas() {
		if (schema == null) {
			schema = new ArrayList<AppSchema>();
		}
		return this.schema;
	}

	public AppSchema getAppSchema(String namespaceURI) {
		return uriToSchemaMap.get(namespaceURI);
	}

	public boolean isSetAppSchemas() {
		return ((this.schema!= null)&&(!this.schema.isEmpty()));
	}

	public void unsetAppSchemas() {
		this.schema = null;
	}

	public List<DataType> getDataTypes() {
		if (dataType == null) {
			dataType = new ArrayList<DataType>();
		}
		return this.dataType;
	}

	public boolean isSetDataTypes() {
		return ((this.dataType!= null)&&(!this.dataType.isEmpty()));
	}

	public void unsetDataTypes() {
		this.dataType = null;
	}

	public List<ObjectType> getObjectTypes() {
		if (objectType == null) {
			objectType = new ArrayList<ObjectType>();
		}
		return this.objectType;
	}

	public ObjectType getObjectType(String name, String namespaceURI) {
		AppSchema schema = getAppSchema(namespaceURI);		
		if (schema != null) {
			for (ObjectType objectType : schema.getObjectTypes()) {
				if (objectType.getPath().equals(name))
					return objectType;
			}
		}

		return null;
	}

	public ObjectType getObjectType(QName name) {
		return getObjectType(name.getLocalPart(), name.getNamespaceURI());
	}

	public boolean isSetObjectTypes() {
		return ((this.objectType!= null)&&(!this.objectType.isEmpty()));
	}

	public void unsetObjectTypes() {
		this.objectType = null;
	}	

	public List<FeatureType> getFeatureTypes() {
		if (featureType == null) {
			featureType = new ArrayList<FeatureType>();
		}
		return this.featureType;
	}

	public List<FeatureType> listTopLevelFeatureTypes(boolean onlyQueryable) {
		List<FeatureType> result = new ArrayList<FeatureType>();
		for (FeatureType featureType : getFeatureTypes()) {
			if (!featureType.isTopLevel())
				continue;

			if (!onlyQueryable || featureType.isQueryable())
				result.add(featureType);
		}

		return result;
	}
	
	public List<FeatureType> listFeatureTypesByTable(String tableName, boolean skipAbstractTypes) {
		List<FeatureType> result = new ArrayList<FeatureType>();
		for (FeatureType featureType : getFeatureTypes()) {
			if (featureType.getTable().equalsIgnoreCase(tableName)) {
				if (featureType.isAbstract() && skipAbstractTypes)
					continue;
				
				result.add(featureType);
			}
		}
		
		return result;
	}

	public FeatureType getFeatureType(String name, String namespaceURI) {
		AppSchema schema = getAppSchema(namespaceURI);		
		if (schema != null) {
			for (FeatureType featureType : schema.getFeatureTypes()) {
				if (featureType.getPath().equals(name))
					return featureType;
			}
		}

		return null;
	}

	public FeatureType getFeatureType(QName name) {
		return getFeatureType(name.getLocalPart(), name.getNamespaceURI());
	}

	public FeatureType getFeatureType(CityGMLClass cityGMLClass, CityGMLModule module) {
		QName name = module.getFeatureName(cityGMLClass);
		return name != null ? getFeatureType(name) : null;
	}
	
	public FeatureType getFeatureType(CityGMLClass cityGMLClass, CityGMLVersion version) {
		for (CityGMLModule module : version.getCityGMLModules()) {
			FeatureType featureType = getFeatureType(cityGMLClass, module);
			if (featureType != null)
				return featureType;
		}
		
		return null;
	}

	public FeatureType getCommonSuperType(Collection<FeatureType> featureTypes) {
		if (featureTypes != null && !featureTypes.isEmpty()) {
			if (featureTypes.size() == 1)
				return featureTypes.iterator().next();

			Iterator<FeatureType> iter = featureTypes.iterator();
			List<FeatureType> candidates = iter.next().listSuperTypes(true);
			while (iter.hasNext())
				candidates.retainAll(iter.next().listSuperTypes(true));

			return !candidates.isEmpty() ? candidates.get(0) : null;
		}

		return null;
	}

	public boolean isSetFeatureTypes() {
		return ((this.featureType!= null)&&(!this.featureType.isEmpty()));
	}

	public void unsetFeatureTypes() {
		this.featureType = null;
	}

	public AbstractObjectType<?> getAbstractObjectType(String name, String namespaceURI) {
		AbstractObjectType<?> type = getFeatureType(name, namespaceURI);
		if (type == null)
			type = getObjectType(name, namespaceURI);

		return type;
	}

	public AbstractObjectType<?> getAbstractObjectType(QName name) {
		return getAbstractObjectType(name.getLocalPart(), name.getNamespaceURI());
	}

	public void setSchemas(List<AppSchema> schema) {
		this.schema = schema;
	}

	public void setDataTypes(List<DataType> dataType) {
		this.dataType = dataType;
	}

	public void setObjectTypes(List<ObjectType> objectType) {
		this.objectType = objectType;
	}
	
	public void setFeatureTypes(List<FeatureType> featureType) {
		this.featureType = featureType;
	}

	public Map<String, String> getNamespaceContext(CityGMLVersion version) {
		Map<String, String> context = new HashMap<>();

		// register CityGML namespaces
		for (Module module : version.getModules())
			context.put(module.getNamespacePrefix(), module.getNamespaceURI());

		// register app schema namespaces
		for (AppSchema schema : getAppSchemas()) {
			Namespace namespace = schema.getNamespaces().get(0);
			if (namespace != null)
				context.put(schema.getId(), namespace.getURI());
		}

		return context;
	}

	public Map<String, String> getNamespaceContext() {
		return getNamespaceContext(CityGMLVersion.v2_0_0);
	}

}
