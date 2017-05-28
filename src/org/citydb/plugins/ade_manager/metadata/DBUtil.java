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
import org.citydb.database.schema.mapping.AbstractObjectType;
import org.citydb.database.schema.mapping.SchemaMapping;
import org.citydb.plugins.ade_manager.gui.components.adeTable.ADERow;

public class DBUtil {

	public static SchemaMapping regenerateObjectclassIds(DatabaseConnectionPool dbPool, SchemaMapping schemaMapping) {
		int initialObjectclassid = schemaMapping.getMetadata().getInitialObjectClassId();		
		
		Iterator<AbstractObjectType<?>> iter = schemaMapping.getAbstractObjectTypes().iterator();
		while (iter.hasNext()) {
			AbstractObjectType<?> objectclass = iter.next();
			objectclass.setObjectClassId(initialObjectclassid++);
		}
		
		return schemaMapping;
	}
	
	public static void validateSchemaMapping (DatabaseConnectionPool dbPool, SchemaMapping schemaMapping) throws SQLException {
		Iterator<AbstractObjectType<?>> iter = schemaMapping.getAbstractObjectTypes().iterator();
		while (iter.hasNext()) {
			AbstractObjectType<?> objectclass = iter.next();
			int objectclassId = objectclass.getObjectClassId();
			if (!validateObjectclassId(dbPool, objectclassId))
				throw new SQLException("The objectclass Id '" + objectclassId + "'" + " is invalid, because it has already been reserved by other class");			
		}
	}
	
	public static boolean validateObjectclassId (DatabaseConnectionPool dbPool, int objectclassId) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		boolean isValid = true;

		try {
			conn = dbPool.getConnection();						
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from objectclass where id = " + objectclassId);
			
			if (rs.next())
				isValid = false;
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
		
		return isValid;
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
