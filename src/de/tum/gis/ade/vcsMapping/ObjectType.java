package de.tum.gis.ade.vcsMapping;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "objectType")
public class ObjectType
    extends AbstractObjectType<ObjectType>
{
	
	protected ObjectType() {

	}
	
	@Override
	public List<ObjectType> listSubTypes(boolean skipAbstractTypes) {
		return listSubTypes(schemaMapping.getObjectTypes(), skipAbstractTypes);
	}

	@Override
	public List<ObjectType> listSuperTypes(boolean includeType) {
		List<ObjectType> superTypes = new ArrayList<ObjectType>();
		ObjectType objectType = this;
		if (includeType)
			superTypes.add(objectType);

		while (objectType.isSetExtension()) {
			objectType = objectType.getExtension().getBase();
			superTypes.add(objectType);
		}

		return superTypes;
	}

	@Override
	public boolean hasSharedTable(boolean skipAbstractTypes) {
		return hasSharedTable(schemaMapping.getObjectTypes(), skipAbstractTypes);
	}

	@Override
	public PathElementType getElementType() {
		return PathElementType.OBJECT_TYPE;
	}
	
}
