package org.citygml.ade.metadata;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.citydb.api.database.DatabaseConfigurationException;
import org.citydb.api.database.DatabaseVersionException;
import org.citydb.api.event.EventDispatcher;
import org.citydb.api.registry.ObjectRegistry;
import org.citydb.config.Config;
import org.citydb.config.ConfigUtil;
import org.citydb.config.project.Project;
import org.citydb.config.project.database.DBConnection;
import org.citydb.database.DatabaseConnectionPool;
import org.citydb.database.schema.mapping.AppSchema;
import org.citydb.database.schema.mapping.SchemaMapping;
import org.citydb.database.schema.mapping.SchemaMappingException;
import org.citydb.database.schema.mapping.SchemaMappingValidationException;
import org.citydb.database.schema.util.SchemaMappingUtil;
import org.citydb.log.Logger;
import org.citygml.ade.metadata.database.DBMetadataImportException;
import org.citygml.ade.metadata.database.DBMetadataImporter;
import org.citygml.ade.metadata.database.DBUtil;

public class Main {
	private final Logger LOG = Logger.getInstance();
	
	public static void main(String[] args) {
		Main mainClass = new Main();
		mainClass.doMain();
	}
	
	public void doMain() {
		String impExpConfigpPath = "resources" + File.separator + "impExpConfig_postgis.xml";
		String sourceAdeSchemaMappingPath = "resources" + File.separator + "test-ade-schema.xml";
		String updatedAdeSchemaMappingPath = "output" + File.separator + "test-ade-schema2.xml";
		
		// initialize object registry and event dispatcher, which are required in order to launch the DatabaseConnectionPool
		ObjectRegistry registry = ObjectRegistry.getInstance();
		EventDispatcher eventDispatcher = new EventDispatcher();		
		registry.setEventDispatcher(eventDispatcher);
										
		// read a pre-defined configuration file of the Import/Export tool to retrieve database connection settings
		LOG.info("Loading project settings");
		File configFile = new File(impExpConfigpPath);
		if (!configFile.exists()) {
			LOG.error("Failed to find config file '" + configFile + "'");
			System.exit(1);
		} else if (!configFile.canRead() || !configFile.canWrite()) {
			LOG.error("Insufficient access rights to config file '" + configFile + "'");
			System.exit(1);
		}

		String projectFileName = configFile.getName();
		String confPath = configFile.getParent();			
		Config config = new Config();
		config.getInternal().setConfigPath(confPath);
		config.getInternal().setConfigProject(projectFileName);
		File projectFile = new File(confPath, projectFileName);
				
		Project configProject = config.getProject();
		
		try {
			Object object = ConfigUtil.unmarshal(projectFile, JAXBContext.newInstance(Project.class));
			configProject = (Project)object;
		} catch (JAXBException jaxbE) {
			LOG.error("Project settings '" + projectFile + "' could not be loaded: " + jaxbE.getMessage());
			System.exit(1);
		} catch (IOException e) {
			LOG.error("Failed to read project settings file '" + projectFile + '\'');
			System.exit(1);
		} finally {
			config.setProject(configProject);
		}
		
		// initialize database connection
		LOG.info("Connecting to 3DCityDB database instance...");
		DatabaseConnectionPool dbPool = DatabaseConnectionPool.getInstance();
		DBConnection dbConn = config.getProject().getDatabase().getActiveConnection();
		dbConn.setInternalPassword(dbConn.getPassword());
		
		try {
			dbPool.connect(config);
		} catch (DatabaseConfigurationException | SQLException e) {
			LOG.error("Connection to database could not be established: " + e.getMessage());
			System.exit(1);
		} catch (DatabaseVersionException e) {
			LOG.error("Connection to database could not be established: " + e.getMessage());
			System.exit(1);
		}
		
		// read ADE's schema mapping file
		LOG.info("Loading ADE's schema mapping file...");
		SchemaMapping adeSchemaMapping = null;		
		List<String> adeSchemaIds = new ArrayList<String>();
		String adeRootSchemaId = null;		
		JAXBContext mappingContext = null;	
		
		try {
			mappingContext = JAXBContext.newInstance(SchemaMapping.class);			
			Unmarshaller um = mappingContext.createUnmarshaller();			
			adeSchemaMapping = (SchemaMapping) um.unmarshal(new File(sourceAdeSchemaMappingPath));						

			Iterator<AppSchema> adeSchemas = adeSchemaMapping.getAppSchemas().iterator();
			while (adeSchemas.hasNext()) {
				AppSchema adeSchema = adeSchemas.next();
				adeSchemaIds.add(adeSchema.getId());
			}				
			adeRootSchemaId = adeSchemaMapping.getAppSchemas().get(0).getId();	
		}  catch (JAXBException e) {
			LOG.error("Failed to unmarshal input resource into a schema mapping." + e.getMessage());
			System.exit(1);
		}
		
		// update the schema mapping instance with newly generated objectclass ids 
		try {
			adeSchemaMapping = DBUtil.regenerateObjectclassIds(dbPool, adeSchemaMapping);
		} catch (SQLException e) {
			LOG.error("Faild to generate new object class id using the database sequence: " + e.getMessage());
			System.exit(1);
		}

		// write the updated schema mapping to a new XML file
		try {
			Marshaller mappingMarshaller = mappingContext.createMarshaller();
			mappingMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			File mappingSchemaFile = new File("output" + File.separator + "test-ade-schema2.xml");
			mappingMarshaller.marshal(adeSchemaMapping, mappingSchemaFile);
		} catch (JAXBException e) {
			LOG.error("Failed to marshal the updated schema mapping into XML file." + e.getMessage());
			System.exit(1);
		}      
		
		// re-read the updated ADE's schema mapping file along with the shared 3DCityDB's schema mapping file
		try {									
			SchemaMapping schemaMapping = SchemaMappingUtil.unmarshal(SchemaMappingUtil.class.getResource("/resources/3dcitydb/3dcitydb-schema.xml"), mappingContext);
			adeSchemaMapping = SchemaMappingUtil.unmarshal(schemaMapping, new File(updatedAdeSchemaMappingPath), mappingContext);										
		} catch (JAXBException e) {
			LOG.error(e.getMessage());
			System.exit(1);
		} catch (SchemaMappingException | SchemaMappingValidationException e) {
			LOG.error("The 3DCityDB schema mapping is invalid: " + e.getMessage());
			System.exit(1);
		}		
		
		// import schema content	
		LOG.info("Importing metadata into database...");
		DBMetadataImporter importer;
		try {
			importer = new DBMetadataImporter(dbPool);
			importer.doProcess( adeSchemaMapping, adeSchemaIds, adeRootSchemaId);
		} catch (SQLException | DBMetadataImportException e) {
			LOG.error(e.getMessage());			
			Throwable cause = e.getCause();
			while (cause != null) {
				LOG.error("Cause: " + cause.getMessage());
				cause = cause.getCause();
			}
			System.exit(1);
		}			
					
		dbPool.disconnect();
		LOG.info("Import Finished");
	}

}
