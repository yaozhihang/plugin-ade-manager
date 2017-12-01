package org.citydb.plugins.ade_manager.cleanup;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformInfo;
import org.citydb.database.schema.mapping.AbstractJoin;
import org.citydb.database.schema.mapping.AbstractProperty;
import org.citydb.database.schema.mapping.FeatureProperty;
import org.citydb.database.schema.mapping.FeatureType;
import org.citydb.database.schema.mapping.JoinTable;
import org.citydb.database.schema.mapping.SchemaMapping;

public class CitydbCleanupScriptGenerator {
	private final String space = " ";
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

			List<FeatureType> featureTypes = mainSchemaMapping.getFeatureTypes();
			List<BinaryJoinEntry> entryList = new ArrayList<BinaryJoinEntry>();
			
			for (FeatureType featureType : featureTypes) {
				for (AbstractProperty property : featureType.getProperties()) {
					if (property instanceof FeatureProperty) {
						FeatureProperty featureProperty = ((FeatureProperty) property);
						FeatureType targetFeatureType = featureProperty.getType();
						if (targetFeatureType.getId().equalsIgnoreCase( "AddressType")) {
							AbstractJoin join = featureProperty.getJoin();
							if (join instanceof JoinTable) {
								String joinTable = ((JoinTable) join).getTable();
								String targetTable = ((JoinTable) join).getInverseJoin().getTable();
								String sourceJoinColumn = ((JoinTable) join).getJoin().getFromColumn();
								String targetJoinColumn = ((JoinTable) join).getInverseJoin().getFromColumn();
								BinaryJoinEntry entry = new BinaryJoinEntry(joinTable, targetTable, sourceJoinColumn, targetJoinColumn);
								
								entryList.add(entry);
								
							//	printComment(entry.getSourceTable() + "-->" + entry.getJoinTable() + "<--" + entry.getTargetTable(), databasePlatform, writer);	
							}													
						}
					}
				}					
			}
			buildSql(entryList);
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

	/*
	 * 'SELECT ad.id FROM %I.address ad
       LEFT OUTER JOIN %I.address_to_building ad2b ON ad2b.address_id = ad.id
       LEFT OUTER JOIN %I.address_to_bridge ad2brd ON ad2brd.address_id = ad.id
       LEFT OUTER JOIN %I.opening o ON o.address_id = ad.id
       LEFT OUTER JOIN %I.bridge_opening brdo ON brdo.address_id = ad.id
       WHERE ad2b.building_id IS NULL
         AND ad2brd.bridge_id IS NULL
         AND o.address_id IS NULL
         AND brdo.address_id IS NULL',
	 */
	private void buildSql(List<BinaryJoinEntry> entryList) {		
		String aliasSourceTablePrefix = "s";
		String aliasJoinTablePrefix = "j";
		String aliasTargetTable = "t";
		String targetTable = entryList.get(0).getTargetTable();
		
		StringBuilder joinBuilder = new StringBuilder();		
		joinBuilder.append("select").append(space).append(aliasTargetTable).append(".id FROM %I.")
		.append(targetTable).append(space).append(aliasTargetTable).append(space).append(System.lineSeparator());;
		
		StringBuilder clauseBuilder = new StringBuilder();
		clauseBuilder.append("WHERE").append(space);
		
		int index = 1;
		for (BinaryJoinEntry entry: entryList) {
			String aliasJoinTable = aliasJoinTablePrefix + index;
			joinBuilder.append("LEFT OUTER JOIN %I.").append(entry.getJoinTable()).append(space).append(aliasJoinTable).append(space).append("ON")
			.append(space).append(aliasJoinTable).append(".").append(entry.getTargetJoinColumn()).append(space).append("=").append(space)
			.append(aliasSourceTablePrefix).append(".id").append(space).append(System.lineSeparator());
			
			if (index > 1)
				clauseBuilder.append("AND").append(space);				
			clauseBuilder.append(aliasJoinTable).append(".").append(entry.getSourceJoinColumn()).append(space).append("IS NULL").append(space).append(System.lineSeparator());;
			
			index++;			
		}
		
		joinBuilder.append(clauseBuilder.toString());
		
		System.out.println(joinBuilder.toString());
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
	
	private class BinaryJoinEntry {
		private String joinTable;
		private String targetTable;
		private	String sourceJoinColumn;
		private String targetJoinColumn;
		
		protected BinaryJoinEntry(String joinTable, String targetTable, String sourceJoinColumn, String targetJoinColumn){
			this.joinTable = joinTable;
			this.targetTable = targetTable;
			this.sourceJoinColumn = sourceJoinColumn;
			this.targetJoinColumn = targetJoinColumn;
		}
		
		protected String getJoinTable() {
			return joinTable;
		}

		protected String getTargetTable() {
			return targetTable;
		}
		
		protected String getSourceJoinColumn() {
			return sourceJoinColumn;
		}

		protected String getTargetJoinColumn() {
			return targetJoinColumn;
		}

	}
}
