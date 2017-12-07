package org.citydb.plugins.ade_manager.script.adapter.oracle;

import java.util.List;

import org.citydb.plugins.ade_manager.script.DeleteFuncNameEnum;
import org.citydb.plugins.ade_manager.script.adapter.AbstractCleanupGenerator;

public class OracleCleanupGenerator extends AbstractCleanupGenerator {

	@Override
	protected String buildCleanupFuncSql(List<JoinEntry> entryList, String deleteFuncName, String cleanupFuncName) {
		String sqlProcedure = buildCleanupQuerySql(entryList, 3);
		StringBuilder funcBuilder = new StringBuilder();
		funcBuilder.append(dent).append("function ").append(cleanupFuncName).append("(schema_name varchar2 := user) return id_array").append(lineBreak)
		.append(dent).append("is").append(lineBreak)
		.append(dent).append(dent).append("deleted_id number;").append(lineBreak)
		.append(dent).append(dent).append("deleted_ids id_array := id_array();").append(lineBreak)
		.append(dent).append(dent).append("record_cur ref_cursor;").append(lineBreak)
		.append(dent).append(dent).append("record_id number;").append(lineBreak)
		.append(dent).append("begin").append(lineBreak)
		.append(dent).append(dent).append("open record_cur for ").append(sqlProcedure).append("';").append(lineBreak)
		.append(dent).append(dent).append("loop").append(lineBreak)
		.append(dent).append(dent).append(dent).append("fetch record_cur into record_id;").append(lineBreak)
		.append(dent).append(dent).append(dent).append("exit when record_cur%notfound;").append(lineBreak)
		.append(dent).append(dent).append(dent).append("deleted_id := ").append(deleteFuncName).append("(record_id, schema_name)").append(lineBreak)
		.append(dent).append(dent).append(dent).append("deleted_ids.extend;").append(lineBreak)
		.append(dent).append(dent).append(dent).append("deleted_ids(deleted_ids.count) := deleted_id;").append(lineBreak)		
		.append(dent).append(dent).append("end loop;").append(lineBreak)
		.append(dent).append(dent).append("close record_cur;")
		.append(dent).append(lineBreak)
		.append(dent).append(dent).append("return deleted_ids;").append(lineBreak)
		.append(dent).append("exception").append(lineBreak)
		.append(dent).append(dent).append("when others then").append(lineBreak)
		.append(dent).append(dent).append(dent).append("dbms_output.put_line('").append(cleanupFuncName).append(": ' || SQLERRM);").append(lineBreak)		
		.append(dent).append("end;");

		return funcBuilder.toString();
	}
	
	@Override
	protected String buildCleanupQuerySql(List<JoinEntry> entryList, int dentNumber) {		
		String codeDent = "";
		for (int i = 0; i < dentNumber; i++)
			codeDent = codeDent + dent;
		
		String targetTable = entryList.get(0).getTargetTable();
		
		StringBuilder joinBuilder = new StringBuilder();		
		joinBuilder.append("'select").append(space).append(aliasTargetTable).append(".id FROM").append(space).append("' || schema_name || '").append(".")
		.append(targetTable).append(space).append(aliasTargetTable).append(lineBreak);
		
		StringBuilder clauseBuilder = new StringBuilder();
		clauseBuilder.append(codeDent).append("WHERE").append(space);
		
		int index = 1;
		for (JoinEntry entry: entryList) {
			String aliasJoinTable = aliasJoinTablePrefix + index;
			
			joinBuilder.append(codeDent).append("LEFT OUTER JOIN").append(space).append("' || schema_name || '").append(".")
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
		StringBuilder builder = new StringBuilder();
		
		builder.append("CREATE OR REPLACE PACKAGE citydb_delete").append(lineBreak)
		.append("AS").append(lineBreak);
		for (DeleteFuncNameEnum n : DeleteFuncNameEnum.values()) {	
			String funcName = n.toString();
			builder.append(dent).append("function ").append(funcName);
			if (funcName.equalsIgnoreCase("delete_surface_geometry"))
				builder.append("(pid number, clean_apps int := 0, schema_name varchar2 := user) return id_array;").append(lineBreak);
			else if (funcName.equalsIgnoreCase("delete_implicit_geometry"))
				builder.append("(pid number, clean_apps int := 0, schema_name varchar2 := user) return number;").append(lineBreak);
			else if (funcName.equalsIgnoreCase("delete_cityobject"))
				builder.append("(pid number, delete_members int := 0, cleanup int := 0, schema_name varchar2 := user) return number;").append(lineBreak);
			else if (funcName.equalsIgnoreCase("delete_cityobjectgroup"))
				builder.append("(pid number, delete_members int := 0, schema_name varchar2 := user) return number;").append(lineBreak);
			else
				builder.append("(pid number, schema_name varchar2 := user) return number;").append(lineBreak);			
		}
		builder.append("END citydb_delete;").append(lineBreak)
		.append("/").append(lineBreak)
		.append(lineBreak)
		.append("CREATE OR REPLACE PACKAGE BODY citydb_delete").append(lineBreak)
		.append("AS").append(lineBreak)
		.append(lineBreak);
		
		return builder.toString();
	}

	@Override
	protected String buildPackageEnd() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("END citydb_delete;").append(lineBreak).append("/").append(lineBreak);
		
		return builder.toString();
	}

	@Override
	protected String buildDeleteSurfaceGeometryFuncSql() {
		StringBuilder builder = new StringBuilder();
		builder.append(dent).append("function delete_surface_geometry(pid number, clean_apps int := 0, schema_name varchar2 := user) return id_array").append(lineBreak)
		.append(dent).append("is").append(lineBreak)
		.append(dent).append(dent).append("deleted_id number;").append(lineBreak)
		.append(dent).append(dent).append("deleted_ids id_array := id_array();").append(lineBreak)
		.append(dent).append(dent).append("dummy_ids id_array := id_array();").append(lineBreak)
		.append(dent).append("begin").append(lineBreak)
		.append(dent).append(dent).append("execute immediate 'delete from ' || schema_name || '.surface_geometry where id=:1 returning id into :2' using pid, out deleted_id;").append(lineBreak)
		.append(dent).append(dent).append("deleted_ids.extend;").append(lineBreak)
		.append(dent).append(dent).append("deleted_ids(deleted_ids.count) := deleted_id;").append(lineBreak)
		.append(lineBreak)
		.append(dent).append(dent).append("if clean_apps <> 0 then").append(lineBreak)
		.append(dent).append(dent).append(dent).append(dent).append("dummy_ids := cleanup_appearances(0, schema_name);").append(lineBreak)
		.append(dent).append(dent).append("end if;").append(lineBreak)
		.append(lineBreak)
		.append(dent).append(dent).append("return deleted_ids;").append(lineBreak)
		.append(dent).append("exception").append(lineBreak)
		.append(dent).append(dent).append("when no_data_found then").append(lineBreak)
		.append(dent).append(dent).append(dent).append("return deleted_ids;").append(lineBreak)
		.append(dent).append(dent).append("when others then").append(lineBreak)
		.append(dent).append(dent).append(dent).append("dbms_output.put_line('delete_surface_geometry (id: ' || pid || '): ' || SQLERRM);").append(lineBreak)
		.append(dent).append("end;");
		
		return builder.toString();
	}

	@Override
	protected String buildDeleteImplicitGeometryFuncSql() {
		StringBuilder builder = new StringBuilder();
		builder.append(dent).append("function delete_implicit_geometry(pid number, clean_apps int := 0, schema_name varchar2 := user) return id_array").append(lineBreak)
		.append(dent).append("is").append(lineBreak)
		.append(dent).append(dent).append("deleted_id number;").append(lineBreak)
		.append(dent).append(dent).append("deleted_ids id_array := id_array();").append(lineBreak)
		.append(dent).append(dent).append("dummy_ids id_array := id_array();").append(lineBreak)
		.append(dent).append(dent).append("implicit_geometry_rec implicit_geometry%rowtype;").append(lineBreak)
		.append(dent).append(dent).append("dummy_surfGeom_ids id_array := id_array();").append(lineBreak)
		.append(dent).append("begin").append(lineBreak)
		.append(dent).append(dent).append("execute immediate 'select * from ' || schema_name || '.implicit_geometry where id=:1' into implicit_geometry_rec using pid;").append(lineBreak)
		.append(dent).append(dent).append("if implicit_geometry_rec.relative_brep_id is not null then").append(lineBreak)
		.append(dent).append(dent).append(dent).append("dummy_surfGeom_ids := delete_surface_geometry(implicit_geometry_rec.relative_brep_id, schema_name);").append(lineBreak)
		.append(dent).append(dent).append("end if;").append(lineBreak)
		.append(lineBreak)
		.append(dent).append(dent).append("execute immediate 'delete from ' || schema_name || '.implicit_geometry where id=:1 returning id into :2' using pid, out deleted_id;").append(lineBreak)
		.append(dent).append(dent).append("deleted_ids.extend;").append(lineBreak)
		.append(dent).append(dent).append("deleted_ids(deleted_ids.count) := deleted_id;").append(lineBreak)
		.append(lineBreak)
		.append(dent).append(dent).append("if clean_apps <> 0 then").append(lineBreak)
		.append(dent).append(dent).append(dent).append(dent).append("dummy_ids := cleanup_appearances(0, schema_name);").append(lineBreak)
		.append(dent).append(dent).append("end if;").append(lineBreak)
		.append(lineBreak)
		.append(dent).append(dent).append("return deleted_ids;").append(lineBreak)
		.append(dent).append("exception").append(lineBreak)
		.append(dent).append(dent).append("when no_data_found then").append(lineBreak)
		.append(dent).append(dent).append(dent).append("return deleted_ids;").append(lineBreak)
		.append(dent).append(dent).append("when others then").append(lineBreak)
		.append(dent).append(dent).append(dent).append("dbms_output.put_line('delete_implicit_geometry (id: ' || pid || '): ' || SQLERRM);").append(lineBreak)
		.append(dent).append("end;");
		
		return builder.toString();
	}

	@Override
	protected String buildDeleteGridCoverageFuncSql() {
		StringBuilder builder = new StringBuilder();
		builder.append(dent).append("function delete_grid_coverage(pid number, schema_name varchar2 := user) return number").append(lineBreak)
		.append(dent).append("is").append(lineBreak)
		.append(dent).append(dent).append("deleted_id number;").append(lineBreak)
		.append(dent).append("begin").append(lineBreak)
		.append(dent).append(dent).append("execute immediate 'delete from ' || schema_name || '.grid_coverage where id=:1 returning id into :2' using pid, out deleted_id;").append(lineBreak)
		.append(dent).append(dent).append("return deleted_id;").append(lineBreak)
		.append(dent).append("exception").append(lineBreak)
		.append(dent).append(dent).append("when no_data_found then").append(lineBreak)
		.append(dent).append(dent).append(dent).append("return deleted_id;").append(lineBreak)
		.append(dent).append(dent).append("when others then").append(lineBreak)
		.append(dent).append(dent).append(dent).append("dbms_output.put_line('delete_grid_coverage (id: ' || pid || '): ' || SQLERRM);").append(lineBreak)
		.append(dent).append("end;");
		
		return builder.toString();
	}

	@Override
	protected String buildDeleteCityModelFuncSql() {
		StringBuilder builder = new StringBuilder();
		builder.append(dent).append("function delete_citymodel(pid number, delete_members int := 0, schema_name varchar2 := user) return number").append(lineBreak)
		.append(dent).append("is").append(lineBreak)
		.append(dent).append(dent).append("deleted_id number;").append(lineBreak)
		.append(dent).append(dent).append("dummy_id number;").append(lineBreak)
		.append(dent).append(dent).append("member_cur ref_cursor;").append(lineBreak)
		.append(dent).append(dent).append("member_id number;").append(lineBreak)
		.append(dent).append("begin").append(lineBreak)
		.append(dent).append(dent).append("if delete_members <> 0 then").append(lineBreak)
		.append(dent).append(dent).append(dent).append("open member_cur for 'select cityobject_id from ' || schema_name || '.cityobject_member where citymodel_id=:1' using pid;").append(lineBreak)
		.append(dent).append(dent).append(dent).append("loop").append(lineBreak)
		.append(dent).append(dent).append(dent).append(dent).append("fetch member_cur into member_id;").append(lineBreak)
		.append(dent).append(dent).append(dent).append(dent).append("exit when member_cur%notfound;").append(lineBreak)
		.append(dent).append(dent).append(dent).append(dent).append("begin").append(lineBreak)
		.append(dent).append(dent).append(dent).append(dent).append(dent).append("dummy_id := delete_cityobject(member_id, delete_members, 0, schema_name);").append(lineBreak)
		.append(dent).append(dent).append(dent).append(dent).append("exception").append(lineBreak)
		.append(dent).append(dent).append(dent).append(dent).append("when others then").append(lineBreak)
		.append(dent).append(dent).append(dent).append(dent).append(dent).append("dbms_output.put_line('pre_delete_citymodel: deletion of cityobject_member with ID ' || member_id || ' threw: ' || SQLERRM);").append(lineBreak)
		.append(dent).append(dent).append(dent).append(dent).append("end").append(lineBreak)
		.append(dent).append(dent).append(dent).append("end loop;").append(lineBreak)
		.append(dent).append(dent).append(dent).append("close member_cur;").append(lineBreak)
		.append(dent).append(dent).append("end if;").append(lineBreak)
		
		.append(dent).append(dent).append("execute immediate 'delete from ' || schema_name || '.citymodel where id=:1 returning id into :2' using pid, out deleted_id;").append(lineBreak)
		.append(dent).append(dent).append("return deleted_id;").append(lineBreak)
		.append(dent).append("exception").append(lineBreak)
		.append(dent).append(dent).append("when no_data_found then").append(lineBreak)
		.append(dent).append(dent).append(dent).append("return deleted_id;").append(lineBreak)
		.append(dent).append(dent).append("when others then").append(lineBreak)
		.append(dent).append(dent).append(dent).append("dbms_output.put_line('delete_citymodel (id: ' || pid || '): ' || SQLERRM);").append(lineBreak)
		.append(dent).append("end;");
		
		return builder.toString();
	}

	@Override
	protected String buildDeleteGenericAttribFuncSql() {
		StringBuilder builder = new StringBuilder();
		builder.append(dent).append("function delete_genericattrib(pid number, delete_members int := 0, schema_name varchar2 := user) return number").append(lineBreak)
		.append(dent).append("is").append(lineBreak)
		.append(dent).append(dent).append("deleted_id number;").append(lineBreak)
		.append(dent).append("begin").append(lineBreak)
		.append(dent).append(dent).append("execute immediate 'delete from ' || schema_name || '.cityobject_genericattrib where id=:1 returning id into :2' using pid, out deleted_id;").append(lineBreak)
		.append(dent).append(dent).append("return deleted_id;").append(lineBreak)
		.append(dent).append("exception").append(lineBreak)
		.append(dent).append(dent).append("when no_data_found then").append(lineBreak)
		.append(dent).append(dent).append(dent).append("return deleted_id;").append(lineBreak)
		.append(dent).append(dent).append("when others then").append(lineBreak)
		.append(dent).append(dent).append(dent).append("dbms_output.put_line('delete_genericattrib (id: ' || pid || '): ' || SQLERRM);").append(lineBreak)
		.append(dent).append("end;");
		
		return builder.toString();
	}

	@Override
	protected String buildDeleteExternalReferenceFuncSql() {
		StringBuilder builder = new StringBuilder();
		builder.append(dent).append("function delete_external_reference(pid number, schema_name varchar2 := user) return number").append(lineBreak)
		.append(dent).append("is").append(lineBreak)
		.append(dent).append(dent).append("deleted_id number;").append(lineBreak)
		.append(dent).append("begin").append(lineBreak)
		.append(dent).append(dent).append("execute immediate 'delete from ' || schema_name || '.external_reference where id=:1 returning id into :2' using pid, out deleted_id;").append(lineBreak)
		.append(dent).append(dent).append("return deleted_id;").append(lineBreak)
		.append(dent).append("exception").append(lineBreak)
		.append(dent).append(dent).append("when no_data_found then").append(lineBreak)
		.append(dent).append(dent).append(dent).append("return deleted_id;").append(lineBreak)
		.append(dent).append(dent).append("when others then").append(lineBreak)
		.append(dent).append(dent).append(dent).append("dbms_output.put_line('delete_genericattrib (id: ' || pid || '): ' || SQLERRM);").append(lineBreak)
		.append(dent).append("end;");
		
		return builder.toString();
	}

}
