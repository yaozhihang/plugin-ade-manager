package org.citydb.plugins.ade_manager.cleanup;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.platform.SqlBuilder;
import org.citydb.database.schema.mapping.AbstractProperty;
import org.citydb.database.schema.mapping.FeatureProperty;
import org.citydb.database.schema.mapping.FeatureType;
import org.citydb.database.schema.mapping.SchemaMapping;

public class CitydbCleanupScriptGenerator {
	private File outputFile;
	
	public CitydbCleanupScriptGenerator(File outputFile) {
		this.outputFile = outputFile;		
	}
	
	/* TODO: Generate DB-scripts for cleaning up the CityGML tables "ADDRESS", "IMPLICT_GEOMETRY", "SURFACE_DATA", "TEX_IMAGE", "WATERBOUNDARY_SURFACE"
	 * "OPENING", "BRIDGE_OPENING", and "TUNNEL_OPENING", as well as the ADE tables which are mapped from GML <<DataType>> and <<Union>>.
	 */ 

	public void doProzess(SchemaMapping mainSchemaMapping, Platform databasePlatform) throws CcgException {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(outputFile);
			SqlBuilder sqlBuilder = new SqlBuilder(databasePlatform) {};		
			sqlBuilder.setWriter(writer);
			
			List<FeatureType> featureTypes = mainSchemaMapping.getFeatureTypes();
			for (FeatureType featureType : featureTypes) {
				for (AbstractProperty property : featureType.getProperties()) {
					if (property instanceof FeatureProperty) {
						FeatureType targetFeatureType = ((FeatureProperty) property).getType();
						if (targetFeatureType.getId().equalsIgnoreCase( "AddressType")) {
							printComment(featureType.getId() + "-->" + targetFeatureType.getId(), databasePlatform, writer);							
						}
					}
				}					
			}
		} catch (IOException e) {
			throw new CcgException("Failed to open file '" + outputFile.getName() + "' for writing.", e);
		} finally {
			writer.close();	
		}		
	}
	
	public File getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}

	private void printComment(String text, Platform platform, PrintWriter writer) throws IOException
    {
        if (platform.isSqlCommentsOn())
        {
        	PlatformInfo platformInfo = platform.getPlatformInfo();
        	writer.print(platformInfo.getCommentPrefix());
        	writer.print(" ");
        	writer.print(text);
        	writer.print(" ");
        	writer.print(platformInfo.getCommentSuffix());
        	writer.println();
        }
    }
}
