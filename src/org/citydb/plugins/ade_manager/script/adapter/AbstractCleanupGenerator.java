package org.citydb.plugins.ade_manager.script.adapter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import org.citydb.database.schema.mapping.SchemaMapping;
import org.citydb.plugins.ade_manager.script.DsgException;
import org.citydb.plugins.ade_manager.script.IDeleteScriptsGenerator;

public abstract class AbstractCleanupGenerator implements IDeleteScriptsGenerator {
	protected final String lineBreak = System.lineSeparator();
	protected final String space = " ";
	protected final String dent = "  ";
	
	@Override
	public void doProcess(SchemaMapping mainSchemaMapping, File outputFile) throws DsgException {	
		StringBuilder scriptBuilder = new StringBuilder();

		writeToFile(scriptBuilder.toString(), outputFile);
	}
	
	private void writeToFile(String sql, File outputFile) throws DsgException {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(outputFile);
			System.out.println(sql);
			writer.println(sql);
		} catch (IOException e) {
			throw new DsgException("Failed to open file '" + outputFile.getName() + "' for writing.", e);
		} finally {
			writer.close();	
		}	
	}

	private String buildComment(String text) {
		{
			StringBuilder builder = new StringBuilder();
			builder.append("/*").append(lineBreak).append(dent).append(text).append(lineBreak).append("*/").append(lineBreak);			
			return builder.toString();
	    }
	}  
	
}
