package org.citydb.plugins.ade_manager.script.adapter.postgis;

import java.util.List;
import org.citydb.plugins.ade_manager.script.adapter.AbstractCleanupGenerator;

public class PostgisCleanupGenerator extends AbstractCleanupGenerator {

	@Override
	protected String buildCleanupFuncSql(List<JoinEntry> entryList, String deleteFuncName, String cleanupFuncName) {
		String sqlProcedure = buildCleanupQuerySql(entryList, 2);
		StringBuilder funcBuilder = new StringBuilder();
		funcBuilder.append("CREATE OR REPLACE FUNCTION citydb_pkg.").append(cleanupFuncName).append("(schema_name TEXT DEFAULT 'citydb') RETURNS SETOF INTEGER AS").append(lineBreak)
		.append("$$").append(lineBreak)
		.append("DECLARE").append(lineBreak)
		.append(dent).append("deleted_id INTEGER;").append(lineBreak)
		.append(dent).append("record_id INTEGER;").append(lineBreak)
		.append("BEGIN").append(System.lineSeparator())
		.append(dent).append("FOR record_id IN EXECUTE format(").append(lineBreak)
		.append(dent).append(dent).append(sqlProcedure);
		
		for (int i = 0; i < entryList.size(); i++) {
			funcBuilder.append(",").append(space).append("schema_name");
		}
		funcBuilder.append(") LOOP").append(lineBreak);
		
		funcBuilder.append(dent).append(dent).append("deleted_id := citydb_pkg.").append(deleteFuncName).append("(record_id, schema_name);").append(lineBreak)
		.append(dent).append(dent).append("RETURN NEXT deleted_id;").append(lineBreak)
		.append(dent).append("END LOOP;").append(lineBreak)
		.append(lineBreak)
		.append(dent).append("RETURN;").append(lineBreak)
		.append(lineBreak)
		.append(dent).append("EXCEPTION").append(lineBreak)
		.append(dent).append(dent).append(" WHEN OTHERS THEN").append(lineBreak)
		.append(dent).append(dent).append(dent).append("RAISE NOTICE ").append("'").append(cleanupFuncName).append(" %', SQLERRM;").append(lineBreak)
		.append("END;").append(lineBreak)
		.append("$$").append(lineBreak)
		.append("LANGUAGE plpgsql;");
		
		return funcBuilder.toString();
	}
	
	@Override
	protected String buildCleanupQuerySql(List<JoinEntry> entryList, int dentNumber) {		
		String codeDent = "";
		for (int i = 0; i < dentNumber; i++)
			codeDent = codeDent + dent;
		
		String targetTable = entryList.get(0).getTargetTable();
		
		StringBuilder joinBuilder = new StringBuilder();		
		joinBuilder.append("select").append(space).append(aliasTargetTable).append(".id FROM").append(space)
		.append("%I.")
		.append(targetTable).append(space).append(aliasTargetTable).append(space).append(System.lineSeparator());
		
		StringBuilder clauseBuilder = new StringBuilder();
		clauseBuilder.append(codeDent).append("WHERE").append(space);
		
		int index = 1;
		for (JoinEntry entry: entryList) {
			String aliasJoinTable = aliasJoinTablePrefix + index;
			
			joinBuilder.append(codeDent).append("LEFT OUTER JOIN").append(space)
			.append("%I.")
			.append(entry.getJoinTable()).append(space).append(aliasJoinTable).append(space).append("ON")
			.append(space).append(aliasJoinTable).append(".").append(entry.getTargetJoinColumn()).append(space).append("=").append(space)
			.append(aliasTargetTable).append(".id").append(space).append(System.lineSeparator());
			
			if (index > 1)
				clauseBuilder.append(codeDent).append("AND").append(space);	
			
			clauseBuilder.append(aliasJoinTable).append(".").append(entry.getSourceJoinColumn()).append(space).append("IS NULL");
			
			if (index < entryList.size())
				clauseBuilder.append(codeDent).append(space).append(System.lineSeparator());
			
			index++;			
		}	
		
		joinBuilder.append(clauseBuilder.toString());
		
		return joinBuilder.toString();
	}

	@Override
	protected String buildPackageHead() {
		return "";
	}

	@Override
	protected String buildPackageEnd() {
		return "";
	}

	@Override
	protected String buildDeleteSurfaceGeometryFuncSql() {
		// TODO
		return "";
	}

	@Override
	protected String buildDeleteImplicitGeometryFuncSql() {
		// TODO
		return "";
	}

	@Override
	protected String buildDeleteGridCoverageFuncSql() {
		// TODO
		return "";
	}

	@Override
	protected String buildDeleteCityModelFuncSql() {
		// TODO 
		return "";
	}

	@Override
	protected String buildDeleteGenericAttribFuncSql() {
		// TODO
		return "";
	}

	@Override
	protected String buildDeleteExternalReferenceFuncSql() {
		// TODO 
		return "";
	}

	@Override
	protected String buildDeleteAppearanceFuncSql() {
		// TODO 
		return "";
	}

	@Override
	protected String buildDeleteSurfaceDataFuncSql() {
		// TODO 
		return "";
	}

	@Override
	protected String buildDeleteCityObjectFuncSql() {
		// TODO 
		return "";
	}
	
}
