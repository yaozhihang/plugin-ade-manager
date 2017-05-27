package org.citydb.plugins.ade_manager.transformation;

import org.citydb.api.event.Event;
import org.citydb.api.event.EventHandler;
import org.citydb.log.Logger;
import org.citydb.modules.citygml.exporter.controller.CityGMLExportException;
import org.citydb.plugins.ade_manager.config.ConfigImpl;
import org.citydb.plugins.ade_manager.transformation.graph.GraphTransformationManager;
import org.citydb.plugins.ade_manager.transformation.schemaMapping.SchemaMappingCreator;
import org.citydb.plugins.ade_manager.transformation.sql.DBScriptGenerator;
import org.citygml4j.xml.schema.Schema;
import org.citygml4j.xml.schema.SchemaHandler;
import agg.xt_basis.GraGra;

public class Manager implements EventHandler {
	private final Logger LOG = Logger.getInstance();
	
	private SchemaHandler schemaHandler;
	private Schema schema;
	private ConfigImpl config;

	public Manager(SchemaHandler schemaHandler, Schema schema, ConfigImpl config) {
		this.schemaHandler = schemaHandler;
		this.schema = schema;	
		this.config = config;
    }
	
	public void doProcess() throws TransformationException { 
		
		LOG.info("Map XML schema elements to a graph");
		GraphTransformationManager aggGraphTransformationManager = new GraphTransformationManager(schemaHandler, schema, config);
		GraGra graph = aggGraphTransformationManager.executeGraphTransformation();

		LOG.info("Generate Oracle and PostGIS database schema in SQL scripts ");
		DBScriptGenerator databaseScriptCreator = new DBScriptGenerator(graph, config);
		databaseScriptCreator.createDatabaseScripts();   
		
		LOG.info("Create 3dcitydb XML SchemaMapping file");
		SchemaMappingCreator schemaMappingCreator = new SchemaMappingCreator(graph, config);
    	try {
			schemaMappingCreator.createSchemaMapping();
		} catch (Exception e) {
			throw new TransformationException("Error occurred while creating the XML schema Mapping file.", e);
		}  	
	}
	
	@Override
	public void handleEvent(Event event) throws Exception {

	}

}
