package org.citygml.ade.metadata.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.citydb.database.DatabaseConnectionPool;
import org.citydb.database.schema.mapping.AbstractExtension;
import org.citydb.database.schema.mapping.AbstractObjectType;
import org.citydb.database.schema.mapping.AppSchema;
import org.citydb.database.schema.mapping.FeatureType;
import org.citydb.database.schema.mapping.Namespace;
import org.citydb.database.schema.mapping.ObjectType;
import org.citydb.database.schema.mapping.SchemaMapping;

public class DBMetadataImporter {	
	private final DatabaseConnectionPool dbPool;
	
	private PreparedStatement psInsertSchema;
	private PreparedStatement psInsertSchemaToObjectclass;
	private PreparedStatement psInsertSchemaReferencing;
	private PreparedStatement psInsertObjectclass;
	
	private SchemaMapping schemaMapping;
	private List<String> adeSchemaIds;
	private String adeRootSchemaId;
		
	public DBMetadataImporter(DatabaseConnectionPool dbPool) throws SQLException {
		this.dbPool = dbPool;

		String insertSchemaQueryStr = "INSERT INTO SCHEMA"
				+ "(ID, IS_ADE_ROOT, NAME, NAMESPACE_URI, DB_PREFIX, VERSION, XML_PREFIX, XML_SCHEMA_LOCATION, XML_SCHEMAFILE, XML_SCHEMAFILE_TYPE, XML_SCHEMAMAPPING_FILE, DROP_DB_SCRIPT) VALUES"
				+ "(?,?,?,?,?,?,?,?,?,?,?,?)";
		psInsertSchema = dbPool.getConnection().prepareStatement(insertSchemaQueryStr);
		
		String insertSchemaReferencingQueryString = "INSERT INTO schema_referencing" + "(REFERENCED_ID, REFERENCING_ID) VALUES" + "(?,?)";
		psInsertSchemaReferencing = dbPool.getConnection().prepareStatement(insertSchemaReferencingQueryString);
				
		String insertObjectclassQueryString = "INSERT INTO OBJECTCLASS" + "(ID, IS_ADE_CLASS, CLASSNAME, TABLENAME, SUPERCLASS_ID, BASECLASS_ID) VALUES" + "(?,?,?,?,?,?)";
		psInsertObjectclass = dbPool.getConnection().prepareStatement(insertObjectclassQueryString);
		
		String insertSchemaToObjectclassQueryString = "INSERT INTO schema_to_objectclass" + "(SCHEMA_ID, OBJECTCLASS_ID) VALUES" + "(?,?)";
		psInsertSchemaToObjectclass = dbPool.getConnection().prepareStatement(insertSchemaToObjectclassQueryString);
	}
	
	public void doProcess(SchemaMapping schemaMapping, List<String> adeSchemaIds, String adeRootSchemaId) throws DBMetadataImportException {
		this.schemaMapping = schemaMapping;
		this.adeSchemaIds = adeSchemaIds;
		this.adeRootSchemaId = adeRootSchemaId;
		
		Map<String, List<Long>> insertedSchemas = null;
		try {
			insertedSchemas = insertSchemas();
			psInsertSchema.close();
		} catch (SQLException e) {
			throw new DBMetadataImportException("Failed to import metadata into 'SCHEMA' table.", e);
		}
		
		try {
			insertSchemaReferencing(insertedSchemas, adeRootSchemaId);
			psInsertSchemaReferencing.close();
		} catch (SQLException e) {
			throw new DBMetadataImportException("Failed to import metadata into 'SCHEMA_REFERENCING' table.", e);
		}
		
		Map<Long, String> objectObjectclassIds;
		try {
			objectObjectclassIds = insertObjectclasses();
			psInsertObjectclass.close();
		} catch (SQLException e) {
			throw new DBMetadataImportException("Failed to import metadata into 'OBJECTCLASS' table.", e);
		}
		
		try {
			insertSchemaToObjectclass(objectObjectclassIds, insertedSchemas);
			psInsertSchemaToObjectclass.close();
		} catch (SQLException e) {
			throw new DBMetadataImportException("Failed to import metadata into 'SCHEMA_TO_OBJECTCLASS' table.", e);
		}	
	}

	private Map<String, List<Long>> insertSchemas() throws SQLException {				
		Map<String, List<Long>> insertedSchemas = new HashMap<String, List<Long>>();

		Iterator<AppSchema> schemaIter = schemaMapping.getAppSchemas().iterator();		
		while (schemaIter.hasNext()) {
			AppSchema adeSchema = schemaIter.next();			
	
			if (!adeSchemaIds.contains(adeSchema.getId()))
				continue;
			
			List<Long> insertedIds = new ArrayList<Long>();
			Iterator<Namespace> namespaceIter = adeSchema.getNamespaces().iterator();
			while (namespaceIter.hasNext()) {
				Namespace adeNamespace = namespaceIter.next();
	
				long seqId = DBUtil.getSequenceID(dbPool, DBSequenceType.schema_seq);
				insertedIds.add(seqId);

				int index = 1;
				psInsertSchema.setLong(index++, seqId);
				psInsertSchema.setInt(index++, adeSchema.getId().equalsIgnoreCase(adeRootSchemaId)?1:0);
				psInsertSchema.setString(index++, adeSchema.getName());
				psInsertSchema.setString(index++, adeNamespace.getURI());
				psInsertSchema.setString(index++, adeSchema.getId());
				psInsertSchema.setNull(index++, Types.VARCHAR);
				psInsertSchema.setString(index++, adeSchema.getId());
				psInsertSchema.setString(index++, adeNamespace.getURI());
				psInsertSchema.setObject(index++, null);
				psInsertSchema.setNull(index++, Types.VARCHAR);
				psInsertSchema.setNull(index++, Types.CLOB);
				psInsertSchema.setNull(index++, Types.CLOB);
				
				psInsertSchema.executeUpdate();
			}					
			insertedSchemas.put(adeSchema.getId(), insertedIds);			
		}	
		
		return insertedSchemas;
	}
	
	private void insertSchemaToObjectclass(Map<Long, String> objectObjectclassIds, Map<String, List<Long>> insertedSchemas) throws SQLException {
		Iterator<Long> objectclassIter = objectObjectclassIds.keySet().iterator();
	    while (objectclassIter.hasNext()) {
	        long objectclassId = objectclassIter.next();
	        String schemaId = objectObjectclassIds.get(objectclassId);
	        
	        List<Long> insertedSchemaIds = insertedSchemas.get(schemaId);
	        Iterator<Long> schemaIter = insertedSchemaIds.iterator();
	        while (schemaIter.hasNext()) {
	        	long insertedSchemaId = schemaIter.next();
		
	        	psInsertSchemaToObjectclass.setLong(1, insertedSchemaId);
	        	psInsertSchemaToObjectclass.setLong(2, objectclassId);	
	        	
	        	psInsertSchemaToObjectclass.executeUpdate();
	        }
	    }
	}
	
	private void insertSchemaReferencing(Map<String, List<Long>> insertedSchemas, String adeRootSchemaId) throws SQLException {	
		int numberOfCityGMLversions = schemaMapping.getAppSchemas().get(0).getNamespaces().size();
		for (int i = 0; i < numberOfCityGMLversions; i++) {
			long referencingId = insertedSchemas.get(adeRootSchemaId).get(i);
			
			Iterator<String> iter = insertedSchemas.keySet().iterator();
		    while (iter.hasNext()) {
		        String schemaId = iter.next();
		        if (!schemaId.equalsIgnoreCase(adeRootSchemaId)) {
		        	long referencedId = insertedSchemas.get(schemaId).get(i);
			        	
		        	psInsertSchemaReferencing.setLong(1, referencedId);	
		        	psInsertSchemaReferencing.setLong(2, referencingId);
		        	
		        	psInsertSchemaReferencing.executeUpdate();
		        }		        
		    }
		}
	}
	
	private Map<Long, String> insertObjectclasses() throws SQLException {
		Map<Long, String> insertedObjectclasses = new HashMap<Long, String>();

		// iterate through object types
		Iterator<ObjectType> objectIter = schemaMapping.getObjectTypes().iterator();			
		while (objectIter.hasNext()) {
			ObjectType objectType = objectIter.next();	
			
			if (!objectType.getSchema().getId().equals(adeRootSchemaId))
				continue;

			long objectclassId = objectType.getObjectClass();
			insertedObjectclasses.put(objectclassId, objectType.getSchema().getId());
			
			int index = 1;
			psInsertObjectclass.setLong(index++, objectclassId);
			psInsertObjectclass.setInt(index++, 1);
			psInsertObjectclass.setString(index++, objectType.getPath());
			psInsertObjectclass.setString(index++, objectType.getTable());
			
			AbstractExtension<ObjectType> objectExtension = objectType.getExtension();
			if (objectExtension != null) {
				int superclassId = objectExtension.getBase().getObjectClass();	
				int baseclassId = getBaseclassId(objectType);				
				psInsertObjectclass.setInt(index++, superclassId);
				psInsertObjectclass.setInt(index++, baseclassId);
			}	
			else {
				psInsertObjectclass.setInt(index++, 2);
				psInsertObjectclass.setInt(index++, 2);
			}

			psInsertObjectclass.executeUpdate();				
		}
				
		// iterate through feature types
		Iterator<FeatureType> featureIter = schemaMapping.getFeatureTypes().iterator();			
		while (featureIter.hasNext()) {
			FeatureType featureType = featureIter.next();	
			
			if (!featureType.getSchema().getId().equals(adeRootSchemaId))
				continue;	
			
			long objectclassId = featureType.getObjectClass();
			insertedObjectclasses.put(objectclassId, featureType.getSchema().getId());
			
			int index = 1;
			psInsertObjectclass.setLong(index++, objectclassId);
			psInsertObjectclass.setInt(index++, 1);
			psInsertObjectclass.setString(index++, featureType.getPath());
			psInsertObjectclass.setString(index++, featureType.getTable());
			
			AbstractExtension<FeatureType> featureExtension = featureType.getExtension();	
			if (featureExtension != null) {
				int superclassId = featureExtension.getBase().getObjectClass();	
				int baseclassId = getBaseclassId(featureType);				
				psInsertObjectclass.setInt(index++, superclassId);
				psInsertObjectclass.setInt(index++, baseclassId);
			}	
			else {
				psInsertObjectclass.setInt(index++, 2);
				psInsertObjectclass.setInt(index++, 2);
			}

			psInsertObjectclass.executeUpdate();							
		}
		
		return insertedObjectclasses;
	}
	
	private int getBaseclassId(AbstractObjectType<?> objectType) {
		int objectclassId = objectType.getObjectClass();
		
		if (objectclassId <= 3)
			return objectclassId;
		else 
			return getBaseclassId((AbstractObjectType<?>)objectType.getExtension().getBase());
	}	
	
}
