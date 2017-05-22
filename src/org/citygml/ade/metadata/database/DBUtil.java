package org.citygml.ade.metadata.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import org.citydb.api.database.DatabaseType;
import org.citydb.database.DatabaseConnectionPool;
import org.citydb.database.schema.mapping.FeatureType;
import org.citydb.database.schema.mapping.ObjectType;
import org.citydb.database.schema.mapping.SchemaMapping;

public class DBUtil {

	public static SchemaMapping regenerateObjectclassIds(DatabaseConnectionPool dbPool, SchemaMapping schemaMapping) throws SQLException {
		int initialObjectclassid = 10000;
		
		Iterator<ObjectType> objectIter = schemaMapping.getObjectTypes().iterator();			
		while (objectIter.hasNext()) {
			ObjectType objectType = objectIter.next();				
			objectType.setObjectClass(initialObjectclassid++);					
		}
		
		Iterator<FeatureType> featureIter = schemaMapping.getFeatureTypes().iterator();
		while (featureIter.hasNext()) {
			FeatureType featureType = featureIter.next();				
			featureType.setObjectClass(initialObjectclassid++);				
		}		
		
		return schemaMapping;
	}
	
	public static long getSequenceID(DatabaseConnectionPool dbPool, DBSequenceType seqType) throws SQLException {
		StringBuilder query = new StringBuilder();
		DatabaseType dbType = dbPool.getActiveDatabaseAdapter().getDatabaseType();
						
		if (dbType == DatabaseType.ORACLE) {
			query.append("select ").append(seqType).append(".nextval from dual");
		}
		else if (dbType == DatabaseType.POSTGIS){
			query.append("select nextval('").append(seqType).append("')");
		}
		
		long id = 0;
		PreparedStatement pstsmt = null;
		ResultSet rs = null;
		
		try {
			pstsmt = dbPool.getConnection().prepareStatement(query.toString());
			rs = pstsmt.executeQuery();
			
			if (rs.next())
				id = rs.getLong(1);						
		} finally {			
			if (rs != null) { 
				rs.close();
			}	
			if (pstsmt != null) { 
				pstsmt.close(); 
			}
		}

		return id;
	}
}
