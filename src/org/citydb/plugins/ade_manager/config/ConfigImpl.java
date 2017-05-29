/*
 * 3D City Database - The Open Source CityGML Database
 * http://www.3dcitydb.org/
 * 
 * (C) 2013 - 2016,
 * Chair of Geoinformatics,
 * Technische Universitaet Muenchen, Germany
 * http://www.gis.bgu.tum.de/
 * 
 * The 3D City Database is jointly developed with the following
 * cooperation partners:
 * 
 * virtualcitySYSTEMS GmbH, Berlin <http://www.virtualcitysystems.de/>
 * M.O.S.S. Computer Grafik Systeme GmbH, Muenchen <http://www.moss.de/>
 * 
 * The 3D City Database Importer/Exporter program is free software:
 * you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 */
package org.citydb.plugins.ade_manager.config;

import java.io.File;

import javax.xml.bind.annotation.XmlType;

import org.citydb.api.plugin.extension.config.PluginConfig;

@XmlType(name="ADEManagerType", propOrder={			
		"xmlSchemaInputPath",		
		"transformationOutputPath",	
		"inputGraphPath",		
		"outputGraphPath",
		"adeName",		
		"adeDescription",	
		"adeVersion",	
		"adeDbPrefix",	
		"initialObjectclassId",
		"schemaMappingPath",	
		"dbScriptPath"	
})

public class ConfigImpl extends PluginConfig {
	private String xmlSchemaInputPath;
	private String transformationOutputPath;
	private String inputGraphPath;
	private String outputGraphPath;
	
	private String adeName;
	private String adeDescription;
	private String adeVersion;
	private String adeDbPrefix;
	private int initialObjectclassId;
	
	private String schemaMappingPath;
	private String dbScriptPath;
	
	public ConfigImpl() {
		inputGraphPath = "graphs" + File.separator + "Working_Graph.ggx";
		outputGraphPath = "graphs" + File.separator + "Working_Graph2.ggx";
		initialObjectclassId = 10000;
	}

	public String getTransformationOutputPath() {
		return transformationOutputPath;
	}

	public void setTransformationOutputPath(String transformationOutputPath) {
		this.transformationOutputPath = transformationOutputPath;					
	}

	public String getXMLschemaInputPath() {
		return xmlSchemaInputPath;
	}

	public void setXMLschemaInputPath(String xmlSchemaInputPath) {
		this.xmlSchemaInputPath = xmlSchemaInputPath;		
	}

	public String getInputGraphPath() {
		return inputGraphPath;
	}

	public String getOutputGraphPath() {
		return outputGraphPath;
	}

	public String getAdeName() {
		return adeName;
	}

	public void setAdeName(String adeName) {
		this.adeName = adeName;
	}

	public String getAdeDescription() {
		return adeDescription;
	}

	public void setAdeDescription(String adeDescription) {
		this.adeDescription = adeDescription;
	}

	public String getAdeVersion() {
		return adeVersion;
	}

	public void setAdeVersion(String adeVersion) {
		this.adeVersion = adeVersion;
	}

	public String getAdeDbPrefix() {
		return adeDbPrefix;
	}

	public void setAdeDbPrefix(String adeDbPrefix) {
		this.adeDbPrefix = adeDbPrefix;
	}

	public int getInitialObjectclassId() {
		return initialObjectclassId;
	}

	public void setInitialObjectclassId(int initialObjectclassId) {
		this.initialObjectclassId = initialObjectclassId;
	}

	public String getSchemaMappingPath() {
		return schemaMappingPath;
	}

	public void setSchemaMappingPath(String schemaMappingPath) {
		this.schemaMappingPath = schemaMappingPath;
	}

	public String getDbScriptPath() {
		return dbScriptPath;
	}

	public void setDbScriptPath(String dbScriptPath) {
		this.dbScriptPath = dbScriptPath;
	}

}
