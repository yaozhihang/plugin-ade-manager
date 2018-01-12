package org.citydb.plugins.ade_manager.script.adapter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.citydb.database.schema.mapping.AbstractExtension;
import org.citydb.database.schema.mapping.AbstractJoin;
import org.citydb.database.schema.mapping.AbstractProperty;
import org.citydb.database.schema.mapping.FeatureProperty;
import org.citydb.database.schema.mapping.FeatureType;
import org.citydb.database.schema.mapping.ImplicitGeometryProperty;
import org.citydb.database.schema.mapping.Join;
import org.citydb.database.schema.mapping.JoinTable;
import org.citydb.database.schema.mapping.SchemaMapping;
import org.citydb.database.schema.mapping.SimpleAttribute;
import org.citydb.database.schema.mapping.TableRole;
import org.citydb.plugins.ade_manager.script.CcgException;
import org.citydb.plugins.ade_manager.script.ICleanupGenerator;

public abstract class AbstractCleanupGenerator implements ICleanupGenerator {
	protected final String aliasJoinTablePrefix = "j";
	protected final String aliasTargetTable = "t";
	protected final String lineBreak = System.lineSeparator();
	protected final String space = " ";
	protected final String dent = "  ";
	
	@SuppressWarnings("serial")
	protected final  List<String> cleanupTables = new ArrayList<String>() {
		{
			add("appearance");
			add("address");
			add("surface_data");
			add("waterboundary_surface");		
			add("opening");
			add("tunnel_opening");
			add("bridge_opening");
			add("tex_image");
			add("implicit_geometry");
		}
	};
	
	/** 
	 *  Generate DB-scripts for cleaning up the CityGML tables "ADDRESS", "IMPLICT_GEOMETRY", "SURFACE_DATA", "TEX_IMAGE", "WATERBOUNDARY_SURFACE"
	 * "OPENING", "BRIDGE_OPENING", and "TUNNEL_OPENING", as well as the ADE tables which are mapped from GML <<DataType>> and <<Union>>.
	 */ 
	@Override
	public void doProcess(SchemaMapping mainSchemaMapping, File outputFile) throws CcgException {	
		StringBuilder scriptBuilder = new StringBuilder();
		
		List<String> declaredFuncNameList = new ArrayList<String>(); 
	
		String scriptBody = buildScriptBody(mainSchemaMapping, declaredFuncNameList);
		String packageHead = buildPackageHead(declaredFuncNameList);
		String packageEnd = buildPackageEnd();
		
		scriptBuilder.append(packageHead);
		scriptBuilder.append(scriptBody);
		scriptBuilder.append(packageEnd);
		
		writeToFile(scriptBuilder.toString(), outputFile);
	}
	
	private String buildScriptBody(SchemaMapping mainSchemaMapping, List<String> declaredFuncNameList) {		
		
		String deleteSurfaceGeometryFunction = buildDeleteSurfaceGeometryFuncSql();
		declaredFuncNameList.add("delete_surface_geometry");
		String deleteImplicitGeometryFunction = buildDeleteImplicitGeometryFuncSql();
		declaredFuncNameList.add("delete_implicit_geometry");
		String deleteGridCoverageFunction = buildDeleteGridCoverageFuncSql();
		declaredFuncNameList.add("delete_grid_coverage");
		String deleteCityModelFunction = buildDeleteCityModelFuncSql();
		declaredFuncNameList.add("delete_citymodel");
		String deleteGenericAttribFunction = buildDeleteGenericAttribFuncSql();
		declaredFuncNameList.add("delete_genericattrib");
		String deleteExternalReferenceFunction = buildDeleteExternalReferenceFuncSql();
		declaredFuncNameList.add("delete_external_reference");
		String deleteAppearanceFunction = buildDeleteAppearanceFuncSql();
		declaredFuncNameList.add("delete_appearance");
		String deleteSurfaceDataFunction = buildDeleteSurfaceDataFuncSql();
		declaredFuncNameList.add("delete_surface_data");
		String deleteAddressFunction = buildDeleteAddressFuncSql();
		declaredFuncNameList.add("delete_address");
		String deleteCityObjectFunction = buildDeleteCityObjectFuncSql();
		declaredFuncNameList.add("delete_cityobject");	
		
		declaredFuncNameList.add(lineBreak);
		
		List<FeatureType> featureTypes = mainSchemaMapping.getFeatureTypes();
		
		List<JoinEntry> appearanceList = new ArrayList<JoinEntry>();
		List<JoinEntry> addressList = new ArrayList<JoinEntry>();
		List<JoinEntry> surfaceDataList = new ArrayList<JoinEntry>();
		List<JoinEntry> waterBoundaryList = new ArrayList<JoinEntry>();
		List<JoinEntry> buildingOpeningList = new ArrayList<JoinEntry>();
		List<JoinEntry> tunnelOpeningList = new ArrayList<JoinEntry>();
		List<JoinEntry> bridgeOpeningList = new ArrayList<JoinEntry>();	
		List<JoinEntry> texImageList = new ArrayList<JoinEntry>();
		List<JoinEntry> implicitGeometryList = new ArrayList<JoinEntry>();
		
		Map<String, String> deleteFeatureFuncList = new HashMap<String, String>();
				
		for (FeatureType featureType : featureTypes) {
			AbstractExtension<FeatureType> featureExtension = featureType.getExtension();
			if (featureExtension != null) {
				if (featureType.getObjectClassId() > 1000 || featureExtension.getBase().getObjectClassId() >= 3) {
					String featureTable = featureType.getTable();
					
					String deleteFuncName = "delete_" + featureTable;
					if (featureTable.equalsIgnoreCase("waterboundary_surface"))
						deleteFuncName = "delete_waterbnd_surface";
					else if (featureTable.equalsIgnoreCase("solitary_vegetat_object"))
						deleteFuncName = "delete_solitary_veg_obj";
					else if (featureTable.equalsIgnoreCase("transportation_complex"))
						deleteFuncName = "delete_transport_complex";
					
					String deleteFeatureFuncSql = buildDeleteFeatureFuncsSql(deleteFuncName);
					if (!deleteFeatureFuncList.containsKey(deleteFuncName) && !declaredFuncNameList.contains(deleteFuncName)) {
						deleteFeatureFuncList.put(deleteFuncName, deleteFeatureFuncSql);
						declaredFuncNameList.add(deleteFuncName);
					}					
				}
			}

			for (AbstractProperty property : featureType.getProperties()) {
				JoinEntry entry = null;
				String joinTable = featureType.getTable();
				String targetTable = null;
				AbstractJoin join = null;
				
				if (property instanceof FeatureProperty) {			
					targetTable = ((FeatureProperty) property).getType().getTable();
					join = ((FeatureProperty) property).getJoin();
					entry = createJoinEntry(join, joinTable);
				}
				else if (property instanceof SimpleAttribute) {
					AbstractJoin attributeJoin = property.getJoin();
					if (attributeJoin instanceof Join) {
						targetTable = ((Join) attributeJoin).getTable();
						join = attributeJoin;
						entry = createJoinEntry(join, joinTable);
					}						
				}
				else if (property instanceof ImplicitGeometryProperty) {
					targetTable = "implicit_geometry";
					int lod = ((ImplicitGeometryProperty) property).getLod();
					System.out.println(joinTable + "---" + property.getPath());
					String targetJoinColumn = "lod" + lod + "_implicit_rep_id";
					entry = new JoinEntry(joinTable, targetTable, "id", targetJoinColumn);
				}
				
				if (cleanupTables.contains(targetTable)) {										
					if (entry != null) {
						if (targetTable.equalsIgnoreCase("Appearance")) {
							appearanceList.add(entry);
						}
						else if (targetTable.equalsIgnoreCase("Address")) {
							addressList.add(entry);
						}
						else if (targetTable.equalsIgnoreCase("Surface_Data")) {
							surfaceDataList.add(entry);
						}
						else if (targetTable.equalsIgnoreCase("waterboundary_surface")) {
							waterBoundaryList.add(entry);
						}
						else if (targetTable.equalsIgnoreCase("Opening")) {
							buildingOpeningList.add(entry);
						}
						else if (targetTable.equalsIgnoreCase("Tunnel_Opening")) {
							tunnelOpeningList.add(entry);
						}
						else if (targetTable.equalsIgnoreCase("Bridge_Opening")) {
							bridgeOpeningList.add(entry);
						}
						else if (targetTable.equalsIgnoreCase("tex_image")) {
							texImageList.add(entry);
						}
						else if (targetTable.equalsIgnoreCase("implicit_geometry")) {
							implicitGeometryList.add(entry);
						}
					}	
				}
			}					
		}
		
		declaredFuncNameList.add(lineBreak);
		
		String cleanupAppearanceProcedure = buildCleanupFuncSql(appearanceList, "delete_appearance", "cleanup_appearances");
		declaredFuncNameList.add("cleanup_appearances");
		String cleanupAddressProcedure = buildCleanupFuncSql(addressList, "delete_address", "cleanup_addresses");
		declaredFuncNameList.add("cleanup_addresses");
		String cleanupSurfaceDataProcedure = buildCleanupFuncSql(surfaceDataList, "delete_surface_data", "cleanup_surface_data");
		declaredFuncNameList.add("cleanup_surface_data");
		String cleanupWaterBoundaryProcedure = buildCleanupFuncSql(waterBoundaryList, "delete_waterbnd_surface", "cleanup_waterbnd_surfaces");
		declaredFuncNameList.add("cleanup_waterbnd_surfaces");
		String cleanupBuildingOpeningProcedure = buildCleanupFuncSql(buildingOpeningList, "delete_opening", "cleanup_building_openings");
		declaredFuncNameList.add("cleanup_building_openings");
		String cleanupTunnelOpeningProcedure = buildCleanupFuncSql(tunnelOpeningList, "delete_tunnel_openings", "cleanup_tunnel_openings");
		declaredFuncNameList.add("cleanup_tunnel_openings");
		String cleanupBridgeOpeningProcedure = buildCleanupFuncSql(bridgeOpeningList, "delete_bridge_openings", "cleanup_bridge_openings");
		declaredFuncNameList.add("cleanup_bridge_openings");
		String cleanupTexImageProcedure = buildCleanupFuncSql(texImageList, "delete_tex_images", "cleanup_tex_images");
		declaredFuncNameList.add("cleanup_tex_images");
		String cleanupImplicitGeometryProcedure = buildCleanupFuncSql(implicitGeometryList, "delete_implicit_geometry", "cleanup_implicit_geometries");
		declaredFuncNameList.add("cleanup_implicit_geometries");
		
		StringBuilder outputBuilder = new StringBuilder()
		.append(buildComment("Function for deleting surface geometry"))
		.append(deleteSurfaceGeometryFunction)
		.append(lineBreak).append(lineBreak);
		
		outputBuilder.append(buildComment("Function for deleting implicit geometry"))
		.append(deleteImplicitGeometryFunction)
		.append(lineBreak).append(lineBreak);
		
		outputBuilder.append(buildComment("Function for deleting grid coverage"))
		.append(deleteGridCoverageFunction)
		.append(lineBreak).append(lineBreak);
		
		outputBuilder.append(buildComment("Function for deleting city model"))
		.append(deleteCityModelFunction)
		.append(lineBreak).append(lineBreak);
		
		outputBuilder.append(buildComment("Function for deleting generic attribute"))
		.append(deleteGenericAttribFunction)
		.append(lineBreak).append(lineBreak);
		
		outputBuilder.append(buildComment("Function for deleting external reference"))
		.append(deleteExternalReferenceFunction)
		.append(lineBreak).append(lineBreak);
		
		outputBuilder.append(buildComment("Function for deleting appearance"))
		.append(deleteAppearanceFunction)
		.append(lineBreak).append(lineBreak);
		
		outputBuilder.append(buildComment("Function for deleting surface data"))
		.append(deleteSurfaceDataFunction)
		.append(lineBreak).append(lineBreak);
		
		outputBuilder.append(buildComment("Function for deleting address"))
		.append(deleteAddressFunction)
		.append(lineBreak).append(lineBreak);
		
		outputBuilder.append(buildComment("Function for deleting city object"))
		.append(deleteCityObjectFunction)
		.append(lineBreak).append(lineBreak);
		
		for (String deleteFuncName: deleteFeatureFuncList.keySet()) {
			String funcSql = deleteFeatureFuncList.get(deleteFuncName);
			outputBuilder.append(buildComment("Function of " + deleteFuncName))
			.append(funcSql)
			.append(lineBreak).append(lineBreak);
		}
		
		outputBuilder.append(buildComment("Function for cleaning up appearances"))
		.append(cleanupAppearanceProcedure)
		.append(lineBreak).append(lineBreak);
		
		outputBuilder.append(buildComment("Function for cleaning up addresses"))
		.append(cleanupAddressProcedure)
		.append(lineBreak).append(lineBreak);
		
		outputBuilder.append(buildComment("Function for cleaning up surface data"))
		.append(cleanupSurfaceDataProcedure)
		.append(lineBreak).append(lineBreak);
		
		outputBuilder.append(buildComment("Function for cleaning up waterboundary surfaces"))
		.append(cleanupWaterBoundaryProcedure)
		.append(lineBreak).append(lineBreak);
		
		outputBuilder.append(buildComment("Function for cleaning up building opening data"))
		.append(cleanupBuildingOpeningProcedure)
		.append(lineBreak).append(lineBreak);
		
		outputBuilder.append(buildComment("Function for cleaning up tunnel opening data"))
		.append(cleanupTunnelOpeningProcedure)
		.append(lineBreak).append(lineBreak);
		
		outputBuilder.append(buildComment("Function for cleaning up bridge opening data"))
		.append(cleanupBridgeOpeningProcedure)
		.append(lineBreak).append(lineBreak);
		
		outputBuilder.append(buildComment("Function for cleaning up texture images"))
		.append(cleanupTexImageProcedure)
		.append(lineBreak).append(lineBreak);
		
		outputBuilder.append(buildComment("Function for cleaning up implicit geometries"))
		.append(cleanupImplicitGeometryProcedure)
		.append(lineBreak).append(lineBreak);
		
		return outputBuilder.toString();
	}
	
	private JoinEntry createJoinEntry(AbstractJoin join, String joinFromTable){
		String joinTable = null;
		String targetTable = null;
		String sourceJoinColumn = null;
		String targetJoinColumn = null;
		JoinEntry entry = null;
		
		if (join instanceof JoinTable) {
			joinTable = ((JoinTable) join).getTable();
			targetTable = ((JoinTable) join).getInverseJoin().getTable();
			sourceJoinColumn = ((JoinTable) join).getJoin().getFromColumn();
			targetJoinColumn = ((JoinTable) join).getInverseJoin().getFromColumn();							
		}
		else if (join instanceof Join){
			if (((Join) join).getToRole() == TableRole.PARENT) {
				joinTable = joinFromTable;
				targetTable = ((Join) join).getTable();
				sourceJoinColumn = ((Join) join).getToColumn();
				targetJoinColumn = ((Join) join).getFromColumn();
			}							
		}
		
		if (joinTable != null)
			entry = new JoinEntry(joinTable, targetTable, sourceJoinColumn, targetJoinColumn);
		
		return entry;
	}
	
	private void writeToFile(String sql, File outputFile) throws CcgException {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(outputFile);
			System.out.println(sql);
			writer.println(sql);
		} catch (IOException e) {
			throw new CcgException("Failed to open file '" + outputFile.getName() + "' for writing.", e);
		} finally {
			writer.close();	
		}	
	}

	private String buildComment(String text) {
		{
			StringBuilder builder = new StringBuilder();
			builder.append("/*").append(lineBreak).append(dent).append(text).append(lineBreak).append("*/").append(lineBreak);			
			return builder.toString();
	    }
	}  
	
	protected abstract String buildPackageHead(List<String> declaredFuncNameList);
	
	protected abstract String buildDeleteSurfaceGeometryFuncSql();
	protected abstract String buildDeleteImplicitGeometryFuncSql();
	protected abstract String buildDeleteGridCoverageFuncSql();
	protected abstract String buildDeleteCityModelFuncSql();
	protected abstract String buildDeleteGenericAttribFuncSql();
	protected abstract String buildDeleteExternalReferenceFuncSql();
	protected abstract String buildDeleteAppearanceFuncSql();
	protected abstract String buildDeleteSurfaceDataFuncSql();
	protected abstract String buildDeleteCityObjectFuncSql();
	protected abstract String buildDeleteAddressFuncSql();
	
	protected abstract String buildDeleteFeatureFuncsSql(String funcName);
	
	protected abstract String buildCleanupFuncSql(List<JoinEntry> entryList, String deleteFuncName, String cleanupFuncName);	
	protected abstract String buildCleanupQuerySql(List<JoinEntry> entryList, int dentNumber);
	
	protected abstract String buildPackageEnd();
	
	protected class JoinEntry {
		private String joinTable;
		private String targetTable;
		private	String sourceJoinColumn;
		private String targetJoinColumn;
		
		public JoinEntry(String joinTable, String targetTable, String sourceJoinColumn, String targetJoinColumn){
			this.joinTable = joinTable;
			this.targetTable = targetTable;
			this.sourceJoinColumn = sourceJoinColumn;
			this.targetJoinColumn = targetJoinColumn;
		}
		
		public String getJoinTable() {
			return joinTable;
		}

		public String getTargetTable() {
			return targetTable;
		}
		
		public String getSourceJoinColumn() {
			return sourceJoinColumn;
		}

		public String getTargetJoinColumn() {
			return targetJoinColumn;
		}
	}
	
}
