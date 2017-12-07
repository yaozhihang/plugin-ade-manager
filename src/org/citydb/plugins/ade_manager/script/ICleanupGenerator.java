package org.citydb.plugins.ade_manager.script;

import java.io.File;

import org.citydb.database.schema.mapping.SchemaMapping;

public interface ICleanupGenerator {
	public void doProcess(SchemaMapping mainSchemaMapping, File outputFile) throws CcgException;
}
