package org.citydb.plugins.ade_manager.transformation;

import org.citydb.api.event.Event;
import org.citydb.api.event.EventHandler;
import org.citydb.database.schema.mapping.SchemaMapping;
import org.citydb.log.Logger;
import org.citydb.plugins.ade_manager.config.ConfigImpl;
import org.citydb.plugins.ade_manager.transformation.graph.GraphTransformationManager;
import org.citydb.plugins.ade_manager.transformation.schemaMapping.SchemaMappingCreator;
import org.citydb.plugins.ade_manager.transformation.sql.DBScriptGenerator;
import org.citygml4j.xml.schema.Schema;
import org.citygml4j.xml.schema.SchemaHandler;
import agg.xt_basis.GraGra;

public class TransformationManager implements EventHandler {
	private final Logger LOG = Logger.getInstance();
	
	private SchemaHandler schemaHandler;
	private Schema schema;
	private ConfigImpl config;

	public TransformationManager(SchemaHandler schemaHandler, Schema schema, ConfigImpl config) {
		this.schemaHandler = schemaHandler;
		this.schema = schema;	
		this.config = config;
    }
	
	public void doProcess() throws TransformationException { 
		
		LOG.info("Mapping XML schema elements to a graph...");
		GraphTransformationManager aggGraphTransformationManager = new GraphTransformationManager(schemaHandler, schema, config);
		GraGra graph = aggGraphTransformationManager.executeGraphTransformation();

		LOG.info("Creating 3dcitydb XML SchemaMapping file...");
		SchemaMappingCreator schemaMappingCreator = new SchemaMappingCreator(graph, config);
		SchemaMapping adeSchemaMapping = null;
    	try {
    		adeSchemaMapping = schemaMappingCreator.createSchemaMapping();
		} catch (Exception e) {
			throw new TransformationException("Error occurred while creating the XML schema Mapping file.", e);
		}  	
    	
    	LOG.info("Generating Oracle and PostGIS database schema in SQL scripts...");
		DBScriptGenerator databaseScriptCreator = new DBScriptGenerator(graph, adeSchemaMapping, config);
		databaseScriptCreator.createDatabaseScripts(); 
	}
	
	@Override
	public void handleEvent(Event event) throws Exception {

	}

}
