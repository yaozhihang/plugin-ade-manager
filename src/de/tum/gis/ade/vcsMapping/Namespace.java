package de.tum.gis.ade.vcsMapping;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlType(name = "namespace")
public class Namespace {

    @XmlAttribute(required = true)
    protected CityGMLContext context;
    @XmlValue
    protected String uri;
    
    protected Namespace() {
    	
    }

	public CityGMLContext getContext() {
		return context;
	}
	
	public boolean isSetContext() {
		return context != null;
	}

	public void setContext(CityGMLContext context) {
		this.context = context;
	}

	public String getURI() {
		return uri != null ? uri : "";
	}

	public void setURI(String uri) {
		this.uri = uri;
	}
	
	public boolean isSetURI() {
		return uri != null;
	}

}
