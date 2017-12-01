package org.citydb.plugins.ade_manager.cleanup;

import java.io.File;
import javax.xml.bind.JAXBException;

import org.apache.ddlutils.platform.postgresql.PostgreSqlPlatform;
import org.citydb.database.schema.mapping.SchemaMapping;
import org.citydb.database.schema.mapping.SchemaMappingException;
import org.citydb.database.schema.mapping.SchemaMappingValidationException;
import org.citydb.database.schema.util.SchemaMappingUtil;

public class Test {
	public static void main(String[] args) throws CcgException {	
		SchemaMapping mainSchemaMapping = null;
		try {
			mainSchemaMapping = SchemaMappingUtil.getInstance()
					.unmarshal(SchemaMappingUtil.class.getResource("/resources/3dcitydb/3dcitydb-schema.xml"));
		} catch (SchemaMappingException | SchemaMappingValidationException | JAXBException e) {
			throw new CcgException("Failed to load the 3DCityDB schema-mapping file", e);
		}
		
		SchemaMapping adeSchemaMapping = null;
		try {
			adeSchemaMapping = SchemaMappingUtil.getInstance().unmarshal(mainSchemaMapping, new File("samples" + File.separator + "TestADE" + File.separator + "schema-mapping.xml"));	
			mainSchemaMapping.merge(adeSchemaMapping);
		} catch (SchemaMappingException | SchemaMappingValidationException | JAXBException e) {
			throw new CcgException("Failed to load the ADE schema-mapping file", e);
		}		
		
		File outputFile = new File("test");
		CitydbCleanupScriptGenerator generator = new CitydbCleanupScriptGenerator(outputFile);
		generator.doProzess(mainSchemaMapping, new PostgreSqlPlatform());
		
		System.out.println("Finished!");
	}

}
