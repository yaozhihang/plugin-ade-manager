package org.citydb.plugins.ade_manager.transformation.schemaMapping;

import java.io.File;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.namespace.QName;

import org.citydb.database.schema.mapping.AbstractExtension;
import org.citydb.database.schema.mapping.AbstractProperty;
import org.citydb.database.schema.mapping.AbstractType;
import org.citydb.database.schema.mapping.AbstractTypeProperty;
import org.citydb.database.schema.mapping.AppSchema;
import org.citydb.database.schema.mapping.CityGMLContext;
import org.citydb.database.schema.mapping.ComplexAttribute;
import org.citydb.database.schema.mapping.ComplexAttributeType;
import org.citydb.database.schema.mapping.ComplexProperty;
import org.citydb.database.schema.mapping.ComplexType;
import org.citydb.database.schema.mapping.ComplexTypeExtension;
import org.citydb.database.schema.mapping.FeatureProperty;
import org.citydb.database.schema.mapping.FeatureType;
import org.citydb.database.schema.mapping.FeatureTypeExtension;
import org.citydb.database.schema.mapping.GeometryProperty;
import org.citydb.database.schema.mapping.GeometryType;
import org.citydb.database.schema.mapping.InjectedComplexAttribute;
import org.citydb.database.schema.mapping.InjectedComplexProperty;
import org.citydb.database.schema.mapping.InjectedFeatureProperty;
import org.citydb.database.schema.mapping.InjectedGeometryProperty;
import org.citydb.database.schema.mapping.InjectedObjectProperty;
import org.citydb.database.schema.mapping.InjectedSimpleAttribute;
import org.citydb.database.schema.mapping.Join;
import org.citydb.database.schema.mapping.JoinTable;
import org.citydb.database.schema.mapping.Metadata;
import org.citydb.database.schema.mapping.Namespace;
import org.citydb.database.schema.mapping.ObjectProperty;
import org.citydb.database.schema.mapping.ObjectType;
import org.citydb.database.schema.mapping.ObjectTypeExtension;
import org.citydb.database.schema.mapping.PropertyInjection;
import org.citydb.database.schema.mapping.SchemaMapping;
import org.citydb.database.schema.mapping.SchemaMappingException;
import org.citydb.database.schema.mapping.SimpleAttribute;
import org.citydb.database.schema.mapping.SimpleType;
import org.citydb.database.schema.util.SchemaMappingUtil;

import org.citydb.plugins.ade_manager.config.ConfigImpl;
import org.citydb.plugins.ade_manager.transformation.graph.GraphNodeArcType;

import agg.attribute.AttrInstance;
import agg.xt_basis.Arc;
import agg.xt_basis.GraGra;
import agg.xt_basis.Node;
import agg.xt_basis.Type;

public class SchemaMappingCreator {	
	private GraGra graphGrammar;	
	private SchemaMapping citygmlSchemaMapping;
	private SchemaMapping adeSchemaMapping;
	
	private ConfigImpl config;
	
	public SchemaMappingCreator(GraGra graphGrammar, ConfigImpl config) {
		this.graphGrammar = graphGrammar;
		this.config = config;				
	}
	
	public SchemaMapping createSchemaMapping() throws Exception {			
		JAXBContext ctx = JAXBContext.newInstance(SchemaMapping.class);
		citygmlSchemaMapping = SchemaMappingUtil.unmarshal(SchemaMappingUtil.class.getResource("/resources/3dcitydb/3dcitydb-schema.xml"), ctx);
				
		adeSchemaMapping = new SchemaMapping();
		adeSchemaMapping.setMetadata(this.generateMetadata());
		
		AppSchema appSchema = generateApplicationSchema();			
		adeSchemaMapping.addSchema(appSchema);
		this.generateComplexTypeMapping(adeSchemaMapping, appSchema);
		
		processTopLevelFeatures(adeSchemaMapping);
		
		File mappingSchemaFile = new File(config.getTransformationOutputPath(), "schema-mapping.xml");			
		SchemaMappingUtil.marshal(adeSchemaMapping, mappingSchemaFile, ctx);
		
		return adeSchemaMapping;
	} 
	
	private Metadata generateMetadata() {
		Metadata metadata = new Metadata(config.getAdeName(), config.getAdeDbPrefix());
		metadata.setDescription(config.getAdeDescription());
		metadata.setVersion(config.getAdeVersion());
		metadata.setInitialObjectClassId(config.getInitialObjectclassId());
		return metadata;
	}
	
	private AppSchema generateApplicationSchema(){		
		Node schemaNode = this.getSchemaNode();
		
		AttrInstance attrInstance = schemaNode.getAttribute();
		String xmlns = (String) attrInstance.getValueAt("xmlns");
		String namespaceUri = (String) attrInstance.getValueAt("namespaceUri");
		
		Namespace namespace1 = new Namespace(xmlns, CityGMLContext.CITYGML_1_0);
		namespace1.setURI(namespaceUri);
		Namespace namespace2 = new Namespace(xmlns, CityGMLContext.CITYGML_2_0);
		namespace2.setURI(namespaceUri);

		AppSchema appSchema = new AppSchema(xmlns, adeSchemaMapping);
		appSchema.addNamespace(namespace1);
		appSchema.addNamespace(namespace2);
		
		return appSchema;
	}
	
	private Node getSchemaNode() {
		Enumeration<Type> e = this.graphGrammar.getTypes();
		
		while(e.hasMoreElements()){
			Type nodeType = e.nextElement();
			if (nodeType.getName().equalsIgnoreCase(GraphNodeArcType.Schema)) {
				List<Node> nodes = this.graphGrammar.getGraph().getNodes(nodeType);
				if (!nodes.isEmpty())
					return nodes.get(0);				
			};
		}
		
		return null;
	}
	
	private void generateComplexTypeMapping(SchemaMapping schemaMapping, AppSchema appSchema) throws SchemaMappingException{
		List<Node> nodeList = getFeautreOrObjectOrComplexTypeNodes();
		Iterator<Node> iter = nodeList.iterator();

		while (iter.hasNext()) {
			Node objectNode = iter.next();
			AbstractType<?> featureOrObjectOrComplexType = this.getOrCreateFeatureOrObjectOrComplexType(objectNode, schemaMapping, appSchema);	
			
			if (featureOrObjectOrComplexType == null)
				continue;
			
			processFeatureOrObjectOrComplexType(objectNode, featureOrObjectOrComplexType, schemaMapping, appSchema);	
			
			if (checkADEHookClass(objectNode)){ 
				AbstractExtension<?> extension = featureOrObjectOrComplexType.getExtension();
				String table = featureOrObjectOrComplexType.getTable();
				Join join = new Join(table, "ID", "ID");
				PropertyInjection propertyInjection = new PropertyInjection(table, join);
				propertyInjection.setDefaultBase((FeatureType) extension.getBase());
				schemaMapping.addPropertyInjection(propertyInjection);
				
				List<AbstractProperty> properties = featureOrObjectOrComplexType.getProperties();
				Iterator<AbstractProperty> propertyIter = properties.iterator();
				while(propertyIter.hasNext()) {
					AbstractProperty property = propertyIter.next();
					String path = property.getPath();
					AppSchema schema = property.getSchema();
					if (property instanceof FeatureProperty) {
						FeatureType featureType = ((FeatureProperty) property).getType();
						propertyInjection.addProperty(new InjectedFeatureProperty(path, featureType, schema));
					}
					else if (property instanceof ObjectProperty) {
						ObjectType objectType = ((ObjectProperty) property).getType();
						propertyInjection.addProperty(new InjectedObjectProperty(path, objectType, schema));
					}
					else if (property instanceof ComplexProperty) {
						ComplexType complexType = ((ComplexProperty) property).getType();
						InjectedComplexProperty injectedProperty = new InjectedComplexProperty(path, schema);
						if (complexType.isSetId())
							injectedProperty.setRefType(complexType);
						else
							injectedProperty.setInlineType(complexType);
						propertyInjection.addProperty(injectedProperty);
					}
					else if (property instanceof ComplexAttribute) {
						ComplexAttributeType complexAttributeType = ((ComplexAttribute) property).getType();
						InjectedComplexAttribute injectedProperty = new InjectedComplexAttribute(path, schema);
						injectedProperty.setInlineType(complexAttributeType);
						propertyInjection.addProperty(injectedProperty);
					}
					else if (property instanceof SimpleAttribute) {
						String column = ((SimpleAttribute) property).getColumn();
						SimpleType type = ((SimpleAttribute) property).getType();
						InjectedSimpleAttribute injectedProperty = new InjectedSimpleAttribute(path, column, type, schema);
						propertyInjection.addProperty(injectedProperty);
					}
					else if (property instanceof GeometryProperty) {
						GeometryType type = ((GeometryProperty) property).getType();
						InjectedGeometryProperty injectedProperty = new InjectedGeometryProperty(path, type, schema);
						if (((GeometryProperty) property).isSetInlineColumn())
							injectedProperty.setInlineColumn(((GeometryProperty) property).getInlineColumn());
						if (((GeometryProperty) property).isSetRefColumn())
							injectedProperty.setRefColumn(((GeometryProperty) property).getRefColumn());
						propertyInjection.addProperty(injectedProperty);
					}
				}
			}				
		}		
	}
	
	private void processFeatureOrObjectOrComplexType(Node objectNode, AbstractType<?> featureOrObjectOrComplexType, SchemaMapping schemaMapping, AppSchema appSchema) throws SchemaMappingException {
		Iterator<Arc> arcIter = objectNode.getOutgoingArcs();
		
		while(arcIter.hasNext()) {
			Arc arc = arcIter.next();
			Node targetNode = (Node) arc.getTarget();				

			// process extension
			if (targetNode.getType().getName().equalsIgnoreCase(GraphNodeArcType.Extension)) {
				this.generateExtension(featureOrObjectOrComplexType, targetNode, schemaMapping, appSchema);
			}	
			// process featureOrObjectOrDataProperty
			if (targetNode.getType().getName().equalsIgnoreCase(GraphNodeArcType.ComplexTypeProperty)) {
				this.generateFeatureOrObjectOrComplexTypeProperty(featureOrObjectOrComplexType, targetNode, schemaMapping, appSchema);
			}	
			// process simple attribute
			if (targetNode.getType().getName().equalsIgnoreCase(GraphNodeArcType.SimpleAttribute) || targetNode.getType().getName().equalsIgnoreCase(GraphNodeArcType.EnumerationProperty)) {
				SimpleAttribute simpleAttribute = this.generateSimpleAttribute(targetNode, appSchema);
				featureOrObjectOrComplexType.addProperty(simpleAttribute);
			}	
			// process complex basic data property
			if (targetNode.getType().getName().equalsIgnoreCase(GraphNodeArcType.ComplexAttribute)) {
				this.generateComplexAttribute(featureOrObjectOrComplexType, targetNode, appSchema);
			}	
			// process geometry property
			if (targetNode.getType().getName().equalsIgnoreCase(GraphNodeArcType.BrepGeometryProperty) 
					|| targetNode.getType().getName().equalsIgnoreCase(GraphNodeArcType.PointOrLineGeometryProperty) 
						|| targetNode.getType().getName().equalsIgnoreCase(GraphNodeArcType.HybridGeometryProperty)) {
				this.generateGeometryProperty(featureOrObjectOrComplexType, targetNode, appSchema);
			}
	
			AbstractExtension<?> extension = featureOrObjectOrComplexType.getExtension();
			if (extension == null) {
				if (featureOrObjectOrComplexType instanceof FeatureType) {
					FeatureType abstractGMLFeature = citygmlSchemaMapping.getFeatureType(new QName("http://www.opengis.net/gml", "_Feature"));
					FeatureTypeExtension featureTypeExtension = new FeatureTypeExtension(abstractGMLFeature);	
					((FeatureType) featureOrObjectOrComplexType).setExtension(featureTypeExtension);	
					featureTypeExtension.setJoin(new Join(abstractGMLFeature.getTable(), "ID", "ID"));
				}
				else if (featureOrObjectOrComplexType instanceof ObjectType) {
					ObjectType abstractGMLObject = citygmlSchemaMapping.getObjectType(new QName("http://www.opengis.net/gml", "_GML"));
					ObjectTypeExtension ObjectTypeExtension = new ObjectTypeExtension((ObjectType) abstractGMLObject);
					abstractGMLObject.setExtension(ObjectTypeExtension);
					ObjectTypeExtension.setJoin(new Join(abstractGMLObject.getTable(), "ID", "ID"));
				}
			}
		}		
	}
	
	private AbstractType<?> getOrCreateFeatureOrObjectOrComplexType(Node featureObjectNode, SchemaMapping schemaMapping, AppSchema appSchema) throws SchemaMappingException{
		String path = (String) featureObjectNode.getAttribute().getValueAt("path");	
		String namespaceUri = (String) featureObjectNode.getAttribute().getValueAt("namespaceUri");
		
		if (checkInlineType(featureObjectNode)) 
			return new ComplexType(path, appSchema, schemaMapping);
					
		AbstractType<?> featureOrObjectOrComplexType = citygmlSchemaMapping.getAbstractObjectType(new QName(namespaceUri, path));
		
		if (featureOrObjectOrComplexType != null) {
			return featureOrObjectOrComplexType;
		}
		else {
			featureOrObjectOrComplexType = citygmlSchemaMapping.getComplexType(new QName(namespaceUri, path));
			if (featureOrObjectOrComplexType != null)
				return featureOrObjectOrComplexType;
		}			
			
		featureOrObjectOrComplexType = schemaMapping.getAbstractObjectType(new QName(namespaceUri, path));
		if (featureOrObjectOrComplexType != null) {
			return featureOrObjectOrComplexType;
		}
		else {
			featureOrObjectOrComplexType = schemaMapping.getComplexType(new QName(namespaceUri, path));
			if (featureOrObjectOrComplexType != null)
				return featureOrObjectOrComplexType;
		}	
		 		
		String tableName = null;
		Iterator<Arc> arcIter = featureObjectNode.getOutgoingArcs();		
		while(arcIter.hasNext()) {
			Arc arc = arcIter.next();
			Node targetNode = (Node) arc.getTarget();							
			if (targetNode.getType().getName().equalsIgnoreCase(GraphNodeArcType.DataTable)) {
				tableName = (String) targetNode.getAttribute().getValueAt("name");
			}		
		}		
		
		String featureOrObjectId = (String) featureObjectNode.getAttribute().getValueAt("name");
		boolean isAbstract = (boolean) featureObjectNode.getAttribute().getValueAt("isAbstract");			
		String derivedFrom = (String) featureObjectNode.getAttribute().getValueAt("derivedFrom");			
						
		if (derivedFrom.equalsIgnoreCase("_Feature") || derivedFrom.equalsIgnoreCase("_CityObject")) {
			featureOrObjectOrComplexType = new FeatureType(featureOrObjectId, path, tableName, appSchema, schemaMapping);
			schemaMapping.addFeatureType((FeatureType) featureOrObjectOrComplexType);
		}
		else if (derivedFrom.equalsIgnoreCase("_GML")) {
			featureOrObjectOrComplexType = new ObjectType(featureOrObjectId, path, tableName, appSchema, schemaMapping);
			schemaMapping.addObjectType((ObjectType) featureOrObjectOrComplexType);
		}
		else if (derivedFrom.equalsIgnoreCase("_Object")) {
			featureOrObjectOrComplexType = new ComplexType(path, appSchema, schemaMapping);
			featureOrObjectOrComplexType.setId(featureOrObjectId);
			schemaMapping.addComplexType((ComplexType) featureOrObjectOrComplexType);			
		} else if (derivedFrom.equalsIgnoreCase("HookClass")) {
			featureOrObjectOrComplexType = new FeatureType(featureOrObjectId, path, tableName, appSchema, schemaMapping);
		}
				
		if (featureOrObjectOrComplexType != null)
			featureOrObjectOrComplexType.setAbstract(isAbstract);	

		return featureOrObjectOrComplexType;
	}
	
	private List<Node> getFeautreOrObjectOrComplexTypeNodes() {
		Enumeration<Type> e = this.graphGrammar.getTypes();
		
		while(e.hasMoreElements()){
			Type nodeType = e.nextElement();				
			if (nodeType.getName().equalsIgnoreCase(GraphNodeArcType.ComplexType)) {
				List<Node> nodes = this.graphGrammar.getGraph().getNodes(nodeType);
				return nodes;
			};
		}
		
		return null;
	}
	
	private void generateExtension(AbstractType<?> subType, Node extensionNode, SchemaMapping schemaMapping, AppSchema appSchema) throws SchemaMappingException{
		Iterator<Arc> arcIter = extensionNode.getOutgoingArcs();
		AbstractExtension<?> extension = null;
		
		while(arcIter.hasNext()) {
			Arc arc = arcIter.next();
			Node targetNode = (Node) arc.getTarget();
	
			if (targetNode.getType().getName().equalsIgnoreCase(GraphNodeArcType.ComplexType)) {
				AbstractType<?> superType = this.getOrCreateFeatureOrObjectOrComplexType(targetNode, schemaMapping, appSchema);
				if (subType instanceof FeatureType) {
					FeatureTypeExtension featureTypeExtension = new FeatureTypeExtension((FeatureType) superType);	
					((FeatureType) subType).setExtension(featureTypeExtension);
					extension = featureTypeExtension;
				}
				else if (subType instanceof ObjectType) {
					ObjectTypeExtension objectTypeExtension = new ObjectTypeExtension((ObjectType) superType);
					((ObjectType) subType).setExtension(objectTypeExtension);
					extension = objectTypeExtension;
				}
				else if (subType instanceof ComplexType) {
					ComplexTypeExtension complexTypeExtension = new ComplexTypeExtension((ComplexType) superType);
					((ComplexType) subType).setExtension((ComplexTypeExtension) complexTypeExtension);
					extension = complexTypeExtension;
				}
			}
			
			if (targetNode.getType().getName().equalsIgnoreCase(GraphNodeArcType.Join) && extension != null) {
				Join extensionJoin = this.createJoin(subType.getTable(), targetNode, subType.getTable());
				extension.setJoin(extensionJoin);
			}
		}
	}
	
	private Join createJoin(String localTableName, Node joinNode, String otherTableName) {
		Iterator<Arc> iter = joinNode.getOutgoingArcs();
		String joinToColumnName = null;
		String joinFromColumnName = null;
		String joinFromTableName = null;
		String joinToTableName = null;

		while (iter.hasNext()) {
			Arc arc = iter.next();
			if (arc.getType().getName().equalsIgnoreCase(GraphNodeArcType.JoinFrom)) {
				Node joinFromColumnNode = (Node) arc.getTarget();
				joinFromColumnName = (String)joinFromColumnNode.getAttribute().getValueAt("name");
				Node joinFromTableNode = (Node)joinFromColumnNode.getOutgoingArcs().next().getTarget();				
				joinFromTableName= (String)joinFromTableNode.getAttribute().getValueAt("name");
			}
			else if (arc.getType().getName().equalsIgnoreCase(GraphNodeArcType.JoinTo)) {
				Node joinToColumnNode = (Node) arc.getTarget();
				joinToColumnName = (String)joinToColumnNode.getAttribute().getValueAt("name");
				Node joinToTableNode = (Node)joinToColumnNode.getOutgoingArcs().next().getTarget();				
				joinToTableName= (String)joinToTableNode.getAttribute().getValueAt("name");
			}
		}
		
		String tableName = joinFromTableName;
		if (tableName.equalsIgnoreCase(localTableName))
			tableName = joinToTableName;

		return new Join(tableName, joinFromColumnName, joinToColumnName);
	}

	private void generateFeatureOrObjectOrComplexTypeProperty(AbstractType<?> localType, Node featureOrObjectOrComplexTypePropertyNode, 
			SchemaMapping schemaMapping, AppSchema appSchema) throws SchemaMappingException {

		AbstractTypeProperty<?> property = null;
		Iterator<Arc> arcIter = featureOrObjectOrComplexTypePropertyNode.getOutgoingArcs();
		while(arcIter.hasNext()) {
			Arc arc = arcIter.next();

			Node targetNode = (Node) arc.getTarget();
			if (targetNode.getType().getName().equalsIgnoreCase(GraphNodeArcType.ComplexType)) {				
				AbstractType<?> targetType = this.getOrCreateFeatureOrObjectOrComplexType(targetNode, schemaMapping, appSchema);
				
				String propertyPath = (String) featureOrObjectOrComplexTypePropertyNode.getAttribute().getValueAt("path");
				
				if (targetType instanceof FeatureType) {
					property = new FeatureProperty(propertyPath, (FeatureType) targetType, appSchema);					
				}
				else if (targetType instanceof ObjectType) {
					property = new ObjectProperty(propertyPath, (ObjectType) targetType, appSchema);	
				}
				else if (targetType instanceof ComplexType) {
					property = new ComplexProperty(propertyPath, appSchema);	
					if (checkInlineType(targetNode)) {
						((ComplexProperty)property).setInlineType((ComplexType) targetType);
						this.processFeatureOrObjectOrComplexType(targetNode, targetType, schemaMapping, appSchema);
					}						
					else {
						((ComplexProperty)property).setRefType((ComplexType) targetType);
					}						
				}				
				localType.addProperty(property);				
			}
			
			if (targetNode.getType().getName().equalsIgnoreCase(GraphNodeArcType.Join)) {
				Join propertyJoin = this.createJoin(localType.getTable(), targetNode, localType.getTable());
				property.setJoin(propertyJoin);
			}
			
			if (targetNode.getType().getName().equalsIgnoreCase(GraphNodeArcType.JoinTable)) {
				JoinTable propertyJoinTable = this.createJoinTable(targetNode);
				property.setJoin(propertyJoinTable);
			}
		}		
	}
	
	private JoinTable createJoinTable(Node joinTableNode) {
		String tableName = (String)joinTableNode.getAttribute().getValueAt("name");
		JoinTable joinTable = new JoinTable(tableName);

		Iterator<Arc> arcIter = joinTableNode.getIncomingArcs();
		while(arcIter.hasNext()) {
			Arc arc = arcIter.next();
			Node primarykeyNode = (Node) arc.getSource();
			if (primarykeyNode.getType().getName().equalsIgnoreCase(GraphNodeArcType.PrimaryKeyColumn)) {
				Iterator<Arc> arcIter2 = primarykeyNode.getIncomingArcs();
				while (arcIter2.hasNext()) {
					Arc arc2 = arcIter2.next();
					Node joinNode = (Node) arc2.getSource();
					if (joinNode.getType().getName().equalsIgnoreCase(GraphNodeArcType.Join)) {
						Join join = this.createJoin(tableName, joinNode, joinTable.getTable());
						joinTable.addJoin(join);
					}					
				}				
			}
		}
		
		return joinTable;
	}
	
	private boolean checkInlineType(Node featureOrObjectOrComplexTypeNode) {
		Object isInline = featureOrObjectOrComplexTypeNode.getAttribute().getValueAt("isInline");
		
		if (isInline != null) {
			if ((boolean)isInline)
				return true;
		}
		
		return false;
	}
	
	private boolean checkADEHookClass(Node featureOrObjectOrComplexTypeNode) {
		String derivedFrom = (String) featureOrObjectOrComplexTypeNode.getAttribute().getValueAt("derivedFrom");
		
		return derivedFrom.equalsIgnoreCase("HookClass");
	}
	
	private SimpleAttribute generateSimpleAttribute(Node simpleAttributeNode, AppSchema appSchema) {		
		String path = (String) simpleAttributeNode.getAttribute().getValueAt("path");
		String typeName = (String) simpleAttributeNode.getAttribute().getValueAt("primitiveDataType");

		Iterator<Arc> arcIter = simpleAttributeNode.getOutgoingArcs();
		
		while(arcIter.hasNext()) {
			Arc arc = arcIter.next();
			Node targetNode = (Node) arc.getTarget();
			if (targetNode.getType().getName().equalsIgnoreCase(GraphNodeArcType.NormalDataColumn)) {
				String columnName = (String) targetNode.getAttribute().getValueAt("name");
				SimpleAttribute attribute = new SimpleAttribute(path, columnName, SimpleType.fromValue(typeName), appSchema);
				return attribute;
			}
		}		
		
		return null;
	}	
	
	private void generateComplexAttribute(AbstractType<?> localType, Node complexAttributeNode, AppSchema appSchema) {		
		String propertyPath = (String) complexAttributeNode.getAttribute().getValueAt("path");
		ComplexAttributeType attributeType = new ComplexAttributeType(adeSchemaMapping);
		Iterator<Arc> arcIter = complexAttributeNode.getOutgoingArcs();
		
		while(arcIter.hasNext()) {
			Arc arc = arcIter.next();
			Node targetNode = (Node) arc.getTarget();
			if (targetNode.getType().getName().equalsIgnoreCase(GraphNodeArcType.SimpleAttribute)) {
				SimpleAttribute simpleAttribute = this.generateSimpleAttribute(targetNode, appSchema);
				attributeType.addAttribute(simpleAttribute);
			}
		}
		
		ComplexAttribute complexAttribute = new ComplexAttribute(propertyPath, appSchema);
		complexAttribute.setInlineType(attributeType);
		localType.addProperty(complexAttribute);	
	}
	
	private void generateGeometryProperty(AbstractType<?> localType, Node geometryPropertyNode, AppSchema appSchema) {		
		String propertyPath = (String) geometryPropertyNode.getAttribute().getValueAt("path");
		String geometryTypeName = (String) geometryPropertyNode.getAttribute().getValueAt("geometryType");
		GeometryType geometryType = GeometryType.fromValue(geometryTypeName);
		
		GeometryProperty geometryProperty = new GeometryProperty(propertyPath, geometryType, appSchema);
		Iterator<Arc> arcIter = geometryPropertyNode.getOutgoingArcs();
		
		while(arcIter.hasNext()) {
			Arc arc = arcIter.next();
			Node targetNode = (Node) arc.getTarget();
			String columnName = (String) targetNode.getAttribute().getValueAt("name");
			if (columnName != null) {
				if (targetNode.getType().getName().equalsIgnoreCase(GraphNodeArcType.RefGeometryColumn))
					geometryProperty.setRefColumn(columnName);
				if (targetNode.getType().getName().equalsIgnoreCase(GraphNodeArcType.InlineGeometryColumn))
					geometryProperty.setInlineColumn(columnName);
			}			
		}		
		
		localType.addProperty(geometryProperty);
	}
	
	private void processTopLevelFeatures(SchemaMapping schemaMapping) {
		List<FeatureType> featureTypes = schemaMapping.getFeatureTypes();
		
		Iterator<FeatureType> iter = featureTypes.iterator();
		while (iter.hasNext()) {
			FeatureType featureType = iter.next();
			if (isTopLevel(featureType))
				featureType.setTopLevel(true);
		}
	}
	
	private boolean isTopLevel(FeatureType featureType) {
		FeatureTypeExtension extension = (FeatureTypeExtension) featureType.getExtension();
		if (extension != null) {			
			FeatureType superClass = extension.getBase();
			if (superClass.isTopLevel())
				return true;
			else
				return isTopLevel(superClass);			
		}
		else
			return false;
	}
}
