package org.citydb.plugins.ade_manager.script.adapter;

import java.io.File;
import javax.xml.bind.JAXBException;

import org.citydb.database.schema.mapping.SchemaMapping;
import org.citydb.database.schema.mapping.SchemaMappingException;
import org.citydb.database.schema.mapping.SchemaMappingValidationException;
import org.citydb.database.schema.util.SchemaMappingUtil;
import org.citydb.plugins.ade_manager.script.DsgException;

public class Test {
	public static void main(String[] args) throws DsgException {	
		SchemaMapping mainSchemaMapping = null;
		try {
			mainSchemaMapping = SchemaMappingUtil.getInstance()
					.unmarshal(SchemaMappingUtil.class.getResource("/resources/3dcitydb/3dcitydb-schema.xml"));
		} catch (SchemaMappingException | SchemaMappingValidationException | JAXBException e) {
			throw new DsgException("Failed to load the 3DCityDB schema-mapping file", e);
		}
		
		SchemaMapping adeSchemaMapping = null;
		try {
			adeSchemaMapping = SchemaMappingUtil.getInstance().unmarshal(mainSchemaMapping, new File("samples" + File.separator + "TestADE" + File.separator + "schema-mapping.xml"));	
			mainSchemaMapping.merge(adeSchemaMapping);
		} catch (SchemaMappingException | SchemaMappingValidationException | JAXBException e) {
			throw new DsgException("Failed to load the ADE schema-mapping file", e);
		}		

		System.out.println("Finished");
	}

}