package org.citydb.plugins.ade_manager.cleanup;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.platform.SqlBuilder;
import org.citydb.database.schema.mapping.FeatureType;
import org.citydb.database.schema.mapping.SchemaMapping;
import org.citydb.database.schema.mapping.SchemaMappingException;
import org.citydb.database.schema.mapping.SchemaMappingValidationException;
import org.citydb.database.schema.util.SchemaMappingUtil;

public class CitydbCleanupScriptGenerator {
	private List<SchemaMapping> adeSchemaMappings;
	private File outputFile;
	
	public CitydbCleanupScriptGenerator(List<SchemaMapping> adeSchemaMappings, File outputFile) {
		this.adeSchemaMappings = adeSchemaMappings;
		this.outputFile = outputFile;		
	}

	public void doProzess(Platform databasePlatform) throws CcgException {
		SchemaMapping mainSchemaMapping = null;
		try {
			mainSchemaMapping = SchemaMappingUtil.getInstance()
					.unmarshal(SchemaMappingUtil.class.getResource("/resources/3dcitydb/3dcitydb-schema.xml"));
		} catch (SchemaMappingException | SchemaMappingValidationException | JAXBException e) {
			throw new CcgException("Failed to load the 3DCityDB schema-mapping file", e);
		}
		
		for (SchemaMapping schemaMapping : getAdeSchemaMappings()) {
			try {
				mainSchemaMapping.merge(schemaMapping);
			} catch (SchemaMappingException e) {
				throw new CcgException("Failed to load the schema-mapping file of the ADE: " + schemaMapping.getMetadata().getName(), e);
			}
		}
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(outputFile);
			SqlBuilder sqlBuilder = new SqlBuilder(databasePlatform) {};		
			sqlBuilder.setWriter(writer);
			
			List<FeatureType> featureTypes = mainSchemaMapping.getFeatureTypes();
			for (FeatureType featureType : featureTypes) {
				
				/* TODO: Generate DB-scripts for cleaning up the CityGML tables "ADDRESS", "IMPLICT_GEOMETRY", "SURFACE_DATA", "TEX_IMAGE", "WATERBOUNDARY_SURFACE"
				 * "OPENING", "BRIDGE_OPENING", and "TUNNEL_OPENING", as well as the ADE tables which are mapped from GML <<DataType>> and <<Union>>.
				 */ 
				printComment(featureType.getId(), databasePlatform, writer);
			}
		} catch (IOException e) {
			throw new CcgException("Failed to open file '" + outputFile.getName() + "' for writing.", e);
		} finally {
			writer.close();	
		}		
	}
	
	public List<SchemaMapping> getAdeSchemaMappings() {
		if (adeSchemaMappings == null)
			return new ArrayList<SchemaMapping>();
		
		return adeSchemaMappings;
	}

	public void setAdeSchemaMappings(List<SchemaMapping> adeSchemaMappings) {
		this.adeSchemaMappings = adeSchemaMappings;
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
