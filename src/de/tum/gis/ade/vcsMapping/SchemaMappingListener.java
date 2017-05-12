package de.tum.gis.ade.vcsMapping;

import java.util.HashMap;

import javax.xml.bind.Unmarshaller.Listener;

public class SchemaMappingListener extends Listener {
	private SchemaMapping schemaMapping;
	private HashMap<Integer, AbstractObjectType<?>> objectClassIds = new HashMap<Integer, AbstractObjectType<?>>();

	@Override
	public void afterUnmarshal(Object target, Object parent) {
		if (schemaMapping == null && parent instanceof SchemaMapping)
			schemaMapping = (SchemaMapping)parent;		

		if (target instanceof AbstractObjectType<?>) {
			AbstractObjectType<?> type = (AbstractObjectType<?>)target;
			type.schemaMapping = schemaMapping;

			if (!type.isSetId())
				throw new SchemaMappingException("An object type must be assigned an id value.");
			else if (!type.isSetTable())
				throw new SchemaMappingException("An object type must be assigned a table.");
			else if (objectClassIds.put(type.objectClass, type) != null)
				throw new SchemaMappingException("The value " + type.objectClass + " of the attribute 'objectClass' is assigned to both '" 
						+ type.id + "' and '" + objectClassIds.get(type.getObjectClass()).id + "'.");

			if (type instanceof FeatureType)
				type.getSchema().featureTypes.add((FeatureType)type);
			else if (type instanceof ObjectType)
				type.getSchema().objectTypes.add((ObjectType)type);			
		}

		else if (target instanceof ImplicitGeometryProperty) {
			if (!(parent instanceof AbstractObjectType<?>) || !((AbstractObjectType<?>)parent).isSetTable())
				throw new SchemaMappingException("An implicit representation property must not be assigned to an inline object type.");

			ImplicitGeometryProperty property = (ImplicitGeometryProperty)target;
			property.table = ((AbstractObjectType<?>)parent).getTable();
			if (property.getLod() < 0 || property.getLod() > 4)
				throw new SchemaMappingException("LOD level of an implicit representation property must be between 0 and 4.");

			property.populate(schemaMapping);
		}

		else if (target instanceof DataType) {
			DataType dataType = ((DataType)target);
			dataType.schemaMapping = schemaMapping;
			boolean isInline = parent instanceof ComplexAttribute;

			if (!isInline) {
				if (!dataType.isSetId())
					throw new SchemaMappingException("A global data type must be assigned an id value.");
				else if (!dataType.isSetTable())
					throw new SchemaMappingException("A global data type must be assigned a table.");
			} else {
				if (dataType.isSetId())
					throw new SchemaMappingException("The attribute 'id' is not allowed for a data type that is given inline.");
				else if (dataType.isSetTable())
					throw new SchemaMappingException("The attribute 'table' is not allowed for a data type that is given inline.");
			}
		}

		else if (target instanceof SimpleAttribute) {
			if (parent instanceof AbstractObjectType<?>)
				((SimpleAttribute)target).objectType = (AbstractObjectType<?>)parent;
			else if (parent instanceof DataType)
				((SimpleAttribute)target).dataType = (DataType)parent;
		}

		else if (target instanceof ComplexAttribute) {
			ComplexAttribute complexAttribute = (ComplexAttribute)target;
			if (!complexAttribute.isSetType())
				throw new SchemaMappingException("The type of a complex attribute must either be given by reference or inline.");
			else if (complexAttribute.inlineType != null && complexAttribute.refType != null)
				throw new SchemaMappingException("The type of a complex attribute must either be given by reference or inline but not both.");

			for (AbstractAttribute attribute : complexAttribute.getType().getAttributes()) {
				if (attribute.getElementType() == PathElementType.SIMPLE_ATTRIBUTE && attribute.getPath().equals(".")) {
					((SimpleAttribute)attribute).name = complexAttribute.getPath();
					break;
				}
			}
		}

		else if (target instanceof GeometryProperty) {
			GeometryProperty geometryProperty = (GeometryProperty)target;
			if (!geometryProperty.isSetRefColumn() && !geometryProperty.isSetInlineColumn())
				throw new SchemaMappingException("A geometry property must provide a reference column or an inline column.");
			else if (geometryProperty.getSrsDimension() < 2 || geometryProperty.getSrsDimension() > 3)
				throw new SchemaMappingException("The SRS dimension of a geometry property must be between 2 and 3.");
			else if (geometryProperty.isSetRefColumn() && geometryProperty.getSrsDimension() == 2)
				throw new SchemaMappingException("The SRS dimension of a surface geometry property must be 2.");
		}

		else if (target instanceof AppSchema) {
			AppSchema schema = (AppSchema)target;
			if (!schema.isSetId())
				throw new SchemaMappingException("The schema " + schema.getName() + " lacks an id.");
			else if (!schema.isSetNamespace())
				throw new SchemaMappingException("The schema " + schema.getName() + " lacks a namespace declaration.");

			for (Namespace namespace : schema.getNamespaces()) {
				if (!namespace.isSetURI())
					throw new SchemaMappingException("The schema " + schema.getName() + " lacks a namespace URI declaration.");
				else if (!namespace.isSetContext())
					throw new SchemaMappingException("The schema " + schema.getName() + " lacks a CityGML context definition.");	

				schemaMapping.uriToSchemaMap.put(namespace.getURI(), schema);
			}
		}

		super.afterUnmarshal(target, parent);
	}

}
