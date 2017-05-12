package de.tum.gis.ade;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.citydb.api.database.DatabaseConfigurationException;
import org.citydb.api.database.DatabaseType;
import org.citydb.api.database.DatabaseVersionException;
import org.citydb.api.event.EventDispatcher;
import org.citydb.api.registry.ObjectRegistry;
import org.citydb.config.Config;
import org.citydb.config.ConfigUtil;
import org.citydb.config.project.Project;
import org.citydb.config.project.database.DBConnection;
import org.citydb.database.DatabaseConnectionPool;
import org.citydb.log.Logger;
import org.citydb.util.Util;
import org.w3c.dom.Document;

import de.tum.gis.ade.vcsMapping.AbstractObjectType;
import de.tum.gis.ade.vcsMapping.AppSchema;
import de.tum.gis.ade.vcsMapping.Extension;
import de.tum.gis.ade.vcsMapping.FeatureType;
import de.tum.gis.ade.vcsMapping.Join;
import de.tum.gis.ade.vcsMapping.Namespace;
import de.tum.gis.ade.vcsMapping.SchemaMapping;
import de.tum.gis.ade.vcsMapping.SchemaMappingException;

public class Main {
	private final Logger LOG = Logger.getInstance();
	
	public static void main(String[] args) {
		Main mainClass = new Main();
		mainClass.doMain();
	}
	
	public void doMain() {
		// initialize object registry and event dispatcher
		ObjectRegistry registry = ObjectRegistry.getInstance();
		EventDispatcher eventDispatcher = new EventDispatcher();		
		registry.setEventDispatcher(eventDispatcher);
										
		// initialize config
		LOG.info("Loading project settings");
		String confPath = null;
		String projectFileName = null;
		File configFile = new File("resource" + File.separator + "projectConfig.xml");
		if (!configFile.exists()) {
			LOG.error("Failed to find config file '" + configFile + "'");
			LOG.error("Aborting...");
			System.exit(1);
		} else if (!configFile.canRead() || !configFile.canWrite()) {
			LOG.error("Insufficient access rights to config file '" + configFile + "'");
			LOG.error("Aborting...");
			System.exit(1);
		}

		projectFileName = configFile.getName();
		confPath = configFile.getParent();
		if (confPath == null)
			confPath = System.getProperty("user.home");
		
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
		DatabaseConnectionPool dbPool = DatabaseConnectionPool.getInstance();
		DBConnection dbConn = config.getProject().getDatabase().getActiveConnection();
		dbConn.setInternalPassword(dbConn.getPassword());
		
		try {
			dbPool.connect(config);
		} catch (DatabaseConfigurationException | SQLException e) {
			LOG.error("Connection to database could not be established: " + e.getMessage());
			System.exit(1);
		} catch (DatabaseVersionException e) {
			LOG.error(e.getMessage());
			LOG.error("Supported versions are '" + Util.collection2string(e.getSupportedVersions(), ", ") + "'.");
			LOG.error("Connection to database could not be established.");
			System.exit(1);
		}

		// initialize schemaMapping
		SchemaMapping schemaMapping = null;
		try {
			JAXBContext mappingContext = JAXBContext.newInstance(SchemaMapping.class);
			Unmarshaller um = mappingContext.createUnmarshaller();
			Object object = um.unmarshal(new File("resource" + File.separator + "test-ade-schema.xml"));
			schemaMapping = (SchemaMapping) object;
			LOG.info("Completed Loading database schema mapping");
		} catch (JAXBException | SchemaMappingException e) {
			LOG.error("Failed to read 3DCityDB schema mapping file: " + e.getMessage());
			System.exit(0);
		}
		
		try {
			List<Long> schemaIds = schemaInsert(dbPool, schemaMapping);
			List<Long> featureIds = objectclassInsert(dbPool, schemaMapping);
		} catch (SQLException sqlE) {
			LOG.error(sqlE.getMessage());
		} catch (IOException ioE) {
			LOG.error(ioE.getMessage());
		}			
		
	}
	
	private long getSchemaSequenceID(DatabaseConnectionPool dbPool) throws SQLException {
		StringBuilder query = new StringBuilder();
		DatabaseType dbType = dbPool.getActiveDatabaseAdapter().getDatabaseType();
						
		if (dbType == DatabaseType.ORACLE) {
			query.append("select schema_seq.nextval from dual");
		}
		else if (dbType == DatabaseType.POSTGIS){
			query.append("select nextval('schema_seq')");
		}

		PreparedStatement pstsmt = dbPool.getConnection().prepareStatement(query.toString());
		
		ResultSet rs = null;
		long id = 0;

		try {
			rs = pstsmt.executeQuery();
			if (rs.next())
				id = rs.getLong(1);
		} catch (SQLException sqlEx) {
			throw sqlEx;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException sqlEx) {
					throw sqlEx;
				}
				rs = null;
			}
		}

		return id;
	}
	
	private List<Long> schemaInsert(DatabaseConnectionPool dbPool, SchemaMapping schemaMapping) throws SQLException, IOException {
		List<Long> insertedIds = new ArrayList<Long>();
		List<AppSchema> appSchemas = schemaMapping.getAppSchemas();
		Iterator<AppSchema> schemaIter = appSchemas.iterator();
		
		String insertQueryString = "INSERT INTO SCHEMA"
				+ "(ID, IS_ROOT_SCHEMA, NAME, NAMESPACE_URI, DB_PREFIX, VERSION, XML_PREFIX, XML_SCHEMA_LOCATION, XML_SCHEMAFILE, XML_SCHEMAFILE_TYPE, XML_SCHEMAMAPPING_FILE, DROP_DB_SCRIPT) VALUES"
				+ "(?,?,?,?,?,?,?,?,?,?,?,?)";
		
		while (schemaIter.hasNext()) {
			AppSchema adeSchema = schemaIter.next();			
			List<Namespace> namespaces = adeSchema.getNamespaces();
			Iterator<Namespace> namespaceIter = namespaces.iterator();
			while (namespaceIter.hasNext()) {
				Namespace adeNamespace = namespaceIter.next();
				PreparedStatement insertStmt = dbPool.getConnection().prepareStatement(insertQueryString);
				
				int index = 1;
				long seqId = getSchemaSequenceID (dbPool);
				insertedIds.add(seqId);
				insertStmt.setLong(index++, seqId);
				insertStmt.setInt(index++, 0);
				insertStmt.setString(index++, adeSchema.getName());
				insertStmt.setString(index++, adeNamespace.getURI());
				insertStmt.setString(index++, adeSchema.getId());
				insertStmt.setNull(index++, Types.VARCHAR);
				insertStmt.setString(index++, adeSchema.getId());
				insertStmt.setString(index++, adeNamespace.getURI());
				insertStmt.setObject(index++, null);
				insertStmt.setNull(index++, Types.VARCHAR);
				insertStmt.setString(index++, new String(Files.readAllBytes(Paths.get("resource" + File.separator + "test-ade-schema.xml"))));
				insertStmt.setNull(index++, Types.CLOB);
				
				try {
					insertStmt.executeUpdate();
				} finally {
					insertStmt.close();
				}		
			}		
		}
		
		return insertedIds;
	}
	
	private List<Long> objectclassInsert(DatabaseConnectionPool dbPool, SchemaMapping schemaMapping) throws SQLException, IOException {
		List<Long> insertedIds = new ArrayList<Long>();
		List<FeatureType> featureTypes = schemaMapping.getFeatureTypes();
		Iterator<FeatureType> featureIter = featureTypes.iterator();
		
		String insertQueryString = "INSERT INTO OBJECTCLASS"
				+ "(ID, IS_ADE_CLASS, CLASSNAME, TABLENAME, SUPERCLASS_ID, BASECLASS_ID) VALUES"
				+ "(?,?,?,?,?,?)";
		
		while (featureIter.hasNext()) {
			FeatureType featureType = featureIter.next();	
			Extension<FeatureType> extension = featureType.getExtension();

			if (extension != null) {	
				FeatureType parentFeature = extension.getBase();
				if (parentFeature != null) {
					System.out.println(parentFeature.getTable());
				}
				else {
					Join join = (Join) extension.getJoin();
					if (join != null) {
						System.out.println(join.getTable());
					}
					else {
						System.out.println(featureType.getTable());
					}					
				}
			}			
			
			PreparedStatement insertStmt = dbPool.getConnection().prepareStatement(insertQueryString);
			
			int index = 1;
			long seqId = getSchemaSequenceID (dbPool);
			insertedIds.add(seqId);
			insertStmt.setLong(index++, seqId);
			insertStmt.setInt(index++, 1);

/*			try {
				insertStmt.executeUpdate();
			} finally {
				insertStmt.close();
			}	*/	
			
		}
		
		return insertedIds;
	}

}
