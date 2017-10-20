package org.citydb.plugins.ade_manager.cleanup;

import java.io.File;

import org.apache.ddlutils.platform.postgresql.PostgreSqlPlatform;

public class Test {
	public static void main(String[] args) throws CcgException {		
		File outputFile = new File("test");
		CitydbCleanupScriptGenerator generator = new CitydbCleanupScriptGenerator(null, outputFile);
		generator.doProzess(new PostgreSqlPlatform());
		System.out.println("Finished!");
	}

}
