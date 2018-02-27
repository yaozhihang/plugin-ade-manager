package org.citydb.plugins.ade_manager.script;

import org.citydb.api.database.DatabaseType;
import org.citydb.plugins.ade_manager.script.adapter.oracle.OracleDeleteScriptGenerator;
import org.citydb.plugins.ade_manager.script.adapter.postgis.PostgisDeleteGeneratorGenerator;

public class DeleteScriptGeneratorFactory {
	
	public IDeleteScriptGenerator createDatabaseAdapter(DatabaseType databaseType) {
		switch (databaseType) {
		case ORACLE:
			return new OracleDeleteScriptGenerator();
		case POSTGIS:
			return new PostgisDeleteGeneratorGenerator();
		}
		
		return null;
	}
}
