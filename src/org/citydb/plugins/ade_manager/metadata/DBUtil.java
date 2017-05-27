package org.citydb.plugins.ade_manager.metadata;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.citydb.api.database.DatabaseType;
import org.citydb.database.DatabaseConnectionPool;
import org.citydb.database.schema.mapping.FeatureType;
import org.citydb.database.schema.mapping.ObjectType;
import org.citydb.database.schema.mapping.SchemaMapping;
import org.citydb.plugins.ade_manager.gui.components.adeTable.ADERow;

public class DBUtil {

	public static SchemaMapping regenerateObjectclassIds(DatabaseConnectionPool dbPool, SchemaMapping schemaMapping) throws SQLException {
		int initialObjectclassid = schemaMapping.getMetadata().getInitialObjectClassId();
		
		Iterator<ObjectType> objectIter = schemaMapping.getObjectTypes().iterator();			
		while (objectIter.hasNext()) {
			ObjectType objectType = objectIter.next();				
			objectType.setObjectClassId(initialObjectclassid++);					
		}
		
		Iterator<FeatureType> featureIter = schemaMapping.getFeatureTypes().iterator();
		while (featureIter.hasNext()) {
			FeatureType featureType = featureIter.next();				
			featureType.setObjectClassId(initialObjectclassid++);				
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
	
	public static List<ADERow> getADEList(DatabaseConnectionPool dbPool) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<ADERow> ades = new ArrayList<ADERow>();

		try {
			conn = dbPool.getConnection();						
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from ade order by id");
			
			while (rs.next()) {
				String name = rs.getString(2);
				String description = rs.getString(3);
				String version = rs.getString(4);
				String dbPrefix = rs.getString(5);
				ades.add(new ADERow(name, description, version, dbPrefix));					
			}
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					throw e;
				}

				rs = null;
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					throw e;
				}

				stmt = null;
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw e;
				}

				conn = null;
			}
		}

		return ades;
	}
}
