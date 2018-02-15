package org.citydb.plugins.ade_manager.script;

import org.citydb.api.database.DatabaseType;
import org.citydb.plugins.ade_manager.script.adapter.oracle.OracleCleanupGenerator;
import org.citydb.plugins.ade_manager.script.adapter.postgis.PostgisCleanupGenerator;

public class DeleteScriptsGeneratorFactory {
	
	public IDeleteScriptsGenerator createDatabaseAdapter(DatabaseType databaseType) {
		switch (databaseType) {
		case ORACLE:
			return new OracleCleanupGenerator();
		case POSTGIS:
			return new PostgisCleanupGenerator();
		}
		
		return null;
	}
}
