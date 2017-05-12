package de.tum.gis.ade.vcsMapping;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

import org.citygml4j.model.module.citygml.CityGMLVersion;

@XmlEnum
@XmlType(name = "cityGMLContext")
public enum CityGMLContext {

	@XmlEnumValue("citygml-2.0")
    CITYGML_2_0(CityGMLVersion.v2_0_0),
    @XmlEnumValue("citygml-1.0")
    CITYGML_1_0(CityGMLVersion.v1_0_0);
    
    private final CityGMLVersion version;

    CityGMLContext(CityGMLVersion version) {
        this.version = version;
    }

    public CityGMLVersion getCityGMLVersion() {
        return version;
    }

    public static CityGMLContext fromCityGMLVersion(CityGMLVersion version) {
		if (version == CityGMLVersion.v1_0_0)
			return CITYGML_1_0;
		else
			return CITYGML_2_0;
	}

}
