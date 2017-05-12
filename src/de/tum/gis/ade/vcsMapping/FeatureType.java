package de.tum.gis.ade.vcsMapping;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "featureType", propOrder = {
		"adeHook"
})
public class FeatureType
extends AbstractObjectType<FeatureType>
{

	@XmlAttribute
	protected Boolean topLevel;
	protected ADEHook adeHook;

	protected FeatureType() {

	}

	@Override
	public List<FeatureType> listSubTypes(boolean skipAbstractTypes) {
		return listSubTypes(schemaMapping.getFeatureTypes(), skipAbstractTypes);
	}

	@Override
	public List<FeatureType> listSuperTypes(boolean includeType) {
		List<FeatureType> superTypes = new ArrayList<FeatureType>();
		FeatureType featureType = this;
		if (includeType)
			superTypes.add(featureType);

		while (featureType.isSetExtension()) {
			featureType = featureType.getExtension().getBase();
			superTypes.add(featureType);
		}

		return superTypes;
	}

	@Override
	public boolean hasSharedTable(boolean skipAbstractTypes) {
		return hasSharedTable(schemaMapping.getFeatureTypes(), skipAbstractTypes);
	}

	public boolean isTopLevel() {
		if (topLevel == null) {
			return false;
		} else {
			return topLevel;
		}
	}

	public void setTopLevel(boolean value) {
		this.topLevel = value;
	}

	public boolean isSetTopLevel() {
		return (this.topLevel != null);
	}

	public void unsetTopLevel() {
		this.topLevel = null;
	}

	public ADEHook getADEHook() {
		return adeHook;
	}
	
	public boolean isSetADEHook() {
		return (this.adeHook != null);
	}

	public void setADEHook(ADEHook adeHook) {
		this.adeHook = adeHook;
	}

	@Override
	public PathElementType getElementType() {
		return PathElementType.FEATURE_TYPE;
	}

}
