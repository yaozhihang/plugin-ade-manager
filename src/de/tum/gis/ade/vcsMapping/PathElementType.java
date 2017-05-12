package de.tum.gis.ade.vcsMapping;

import java.util.EnumSet;

public enum PathElementType {
	FEATURE_TYPE,
	FEATURE_PROPERTY,
	SIMPLE_ATTRIBUTE,
	COMPLEX_ATTRIBUTE,
	GEOMETRY_PROPERTY,
	OBJECT_TYPE,
	OBJECT_PROPERTY,
	IMPLICIT_GEOMETRY_PROPERTY;
	
	public static final EnumSet<PathElementType> OBJECT_TYPES = EnumSet.of(FEATURE_TYPE, OBJECT_TYPE);
	public static final EnumSet<PathElementType> OBJECT_PROPERTY_TYPES = EnumSet.of(FEATURE_PROPERTY, OBJECT_PROPERTY, IMPLICIT_GEOMETRY_PROPERTY);
}
