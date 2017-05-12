package de.tum.gis.ade.vcsMapping;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.citygml4j.model.module.citygml.CoreModule;

@XmlType(name = "implicitGeometryProperty")
public class ImplicitGeometryProperty
    extends AbstractObjectTypeProperty
    implements Joinable
{
	@XmlAttribute(required = true)
	protected int lod = -1;

    @XmlTransient
    protected Join join;
	@XmlTransient
    protected ObjectType objectType;
	@XmlTransient
	protected String table;
	
    protected ImplicitGeometryProperty() {

    }

    @Override
    public Join getJoin() {
        return join;
    }

    @Override
    public boolean isSetJoin() {
        return (this.join!= null);
    }

	@Override
	public ObjectType getObjectType() {
        return objectType;
    }

	@Override
    public boolean isSetObjectType() {
        return objectType != null;
    }
	
	public int getLod() {
		return lod;
	}

	public String getTable() {
		return table;
	}

	protected final void populate(SchemaMapping schemaMapping) {
		objectType = new ObjectType();
		objectType.path = MappingConstants.IMPLICIT_GEOMETRY_PATH;
		objectType.objectClass = 59;
		objectType.schema = schemaMapping.getAppSchema(CoreModule.v2_0_0.getNamespaceURI());
		objectType.schemaMapping = schemaMapping;
		
		// join implicit_geometry table
		join = new Join();
		join.table = "implicit_geometry";
		join.fromColumn = "lod" + lod + "_implicit_rep_id";
		join.toColumn = "id";
		
		// join source table
		Join join = new Join();
		join.table = table;
		join.toColumn = "lod" + lod + "_implicit_rep_id";
		join.fromColumn = "id";
		
		SimpleAttribute mimeType = new SimpleAttribute();
		mimeType.path = "mimeType";
		mimeType.column = "mime_type";
		mimeType.type = SimpleType.STRING;
		mimeType.schema = objectType.schema;
		objectType.addProperty(mimeType);
		
		SimpleAttribute transformationMatrix = new SimpleAttribute();
		transformationMatrix.path = "transformationMatrix";
		transformationMatrix.column = "lod" + lod + "_implicit_transformation";
		transformationMatrix.type = SimpleType.STRING;
		transformationMatrix.schema = objectType.schema;
		transformationMatrix.join = join;
		objectType.addProperty(transformationMatrix);
		
		SimpleAttribute libraryObject = new SimpleAttribute();
		libraryObject.path = "libraryObject";
		libraryObject.column = "reference_to_library";
		libraryObject.type = SimpleType.STRING;
		libraryObject.schema = objectType.schema;
		objectType.addProperty(libraryObject);
		
		GeometryProperty relativeGMLGeometry = new GeometryProperty();
		relativeGMLGeometry.path = "relativeGMLGeometry";
		relativeGMLGeometry.refColumn = "relative_brep_id";
		relativeGMLGeometry.type = GeometryType.ABSTRACT_GEOMETRY;
		relativeGMLGeometry.schema = objectType.schema;
		objectType.addProperty(relativeGMLGeometry);
		
		GeometryProperty referencePoint = new GeometryProperty();
		referencePoint.path = "referencePoint";
		referencePoint.refColumn = "lod" + lod + "_implicit_ref_point";
		referencePoint.type = GeometryType.POINT;
		referencePoint.schema = objectType.schema;
		referencePoint.join = join;
		objectType.addProperty(referencePoint);
	}
    
    @Override
	public PathElementType getElementType() {
		return PathElementType.IMPLICIT_GEOMETRY_PROPERTY;
	}

}
