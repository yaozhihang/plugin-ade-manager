package org.citydb.plugins.ade_manager.transformation.sql;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

public class NameShortener {

	public NameShortener() {}

	public static final List<String> sqlKeywordList = Arrays.asList("ACCESS", "ADD", "ALL",
			"ALTER", "AND", "ANY", "AS", "ASC", "AUDIT", "BETWEEN", "BY", "CHAR", "CHECK", "CLUSTER",
			"COLUMN", "COMMENT", "COMPRESS", "CONNECT", "CREATE", "CURRENT", "DATE", "DECIMAL", "DEFAULT",
			"DELETE", "DESC", "DISTINCT", "DROP", "ELSE", "EXCLUSIVE", "EXISTS", "FILE", "FLOAT", "FOR",
			"FROM", "GRANT", "GROUP", "HAVING", "IDENTIFIED", "IMMEDIATE", "IN", "INCREMENT", "INDEX",
			"INITIAL", "INSERT", "INTEGER", "INTERSECT", "INTO", "IS", "LEVEL", "LIKE", "LOCK", "LONG", "MAXEXTENTS",
			"MINUS", "MLSLABEL", "MODE", "MODIFY", "NOAUDIT", "NOCOMPRESS", "NOT", "NOWAIT", "NULL", "NUMBER",
			"OF", "OFFLINE", "ON", "ONLINE", "OPTION", "OR", "ORDER", "PCTFREE", "PRIOR", "PRIVILEGES", "PUBLIC",
			"RAW", "RENAME", "RESOURCE", "REVOKE", "ROW", "ROWID", "ROWNUM", "ROWS", "SELECT", "SESSION", "SET",
			"SHARE", "SIZE", "SMALLINT", "START", "SUCCESSFUL", "SYNONYM", "SYSDATE", "TABLE ", "THEN", "TO",
			"TRIGGER", "UID", "UNION", "UNIQUE", "UPDATE", "USER", "VALIDATE", "VALUES", "VARCHAR", "VARCHAR2",
			"VIEW", "WHENEVER", "WHERE", "WITH");
	
	public static String shortenDbObjectName(String inputName, int maximumLength) {						
		return shortenDbObjectName(inputName, maximumLength, 0);
	}
	
	public static String shortenDbObjectName(String inputName, int maximumLength, int suffix) {
	//	inputName = splitCamelCase(inputName);
		if (suffix == 1) {
			inputName = inputName + suffix;
		}			
		else if (suffix > 1) {
			inputName.replace("FK" + String.valueOf(suffix-1), "FK" + String.valueOf(suffix));
			inputName.replace("ID" + String.valueOf(suffix-1), "ID" + String.valueOf(suffix));
			inputName.replace("IDX" + String.valueOf(suffix-1), "IDX" + String.valueOf(suffix));
		}			
		
		String result = cleanUpUndercores(inputName);
				
		if (result.length() <= maximumLength)
			result = processSQLKeywords(result);	
		else 
			result = shortenString(cleanUpUndercores(result), maximumLength);				
		
		return result;
	}
	
	private static String splitCamelCase(String s) {
		return s.replaceAll(String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])",
				"(?<=[A-Za-z])(?=[^A-Za-z])"), "_");
	}
	
	private static String processSQLKeywords(String inputString){
		String outputString = inputString;

		if (sqlKeywordList.contains(inputString.toUpperCase())) {
			outputString = inputString + "_";
		}
		
		return outputString;
	}	
	
	private static String cleanUpUndercores(String inputString) {
		String outputString = removeUndercorePrefix(inputString);
		outputString = removeDuplicatedUndercore(outputString);
		outputString = removeUndercoreSuffix(outputString);

		return outputString;
	}
	
	private static String removeUndercorePrefix(String inputString){
		if (inputString.indexOf("_") == 0) 
			return removeUndercorePrefix(inputString.substring(1));
		
		return inputString;
	}
	
	private static String removeUndercoreSuffix(String inputString){
		int inputStringLength = inputString.length();
		if (inputString.lastIndexOf("_") == (inputStringLength - 1)) 
			return removeUndercoreSuffix(inputString.substring(0, inputStringLength - 1));
		
		return inputString;
	}
	
	private static String removeDuplicatedUndercore(String inputString){
		if (inputString.indexOf("__") > 0) 
			return removeDuplicatedUndercore(inputString.replace("__", "_"));
		
		return inputString;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static String shortenString(String inputString, int maximumLength) {
		String outputString = "";
		List<Queue> listOfCharQueue = new ArrayList<>();
		List<Queue> newListOfCharQueue = new ArrayList<>();
		
		String[] stringGroup = inputString.split("_");
		for (String s: stringGroup) {
			char[] charArray = s.toCharArray();	
			Queue charQueue = new ArrayDeque<>();			
			for (char charItem: charArray) {
				charQueue.add(charItem);
			}
			listOfCharQueue.add(charQueue);
			newListOfCharQueue.add(new ArrayDeque());
		}
		
		shortenCharArray(newListOfCharQueue, listOfCharQueue, maximumLength);
		
		Iterator<Queue> iter = newListOfCharQueue.iterator();
		while (iter.hasNext()) {
			Queue queue = iter.next();
			while (!queue.isEmpty()) {
				Object item = queue.poll();
				outputString = outputString.concat(String.valueOf((char)item));
			}
			if (iter.hasNext()) {
				outputString = outputString.concat("_");
			}			
		}
		return outputString;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void shortenCharArray(List<Queue> newListOfCharQueue, List<Queue> listOfCharQueue, int maximumLength) {	
		Iterator<Queue> iter = listOfCharQueue.iterator();
	
		while (iter.hasNext()) {
			Queue queue = iter.next();
			int index = listOfCharQueue.indexOf(queue);
			Queue newQueue = newListOfCharQueue.get(index);
			
			Object item = queue.poll();			
			if (item != null) 
				newQueue.add(item);
			
			if (calculateTotalLength(newListOfCharQueue) == maximumLength) 
				return;
		}
		
		if (calculateTotalLength(newListOfCharQueue) <= maximumLength) {
			shortenCharArray(newListOfCharQueue, listOfCharQueue, maximumLength);
		}
	}
	
	@SuppressWarnings("rawtypes")
	private static int calculateTotalLength(List<Queue> newListOfCharQueue) {
		Iterator<Queue> iter = newListOfCharQueue.iterator();
		int totalLength = 0;
		
		while (iter.hasNext()) {
			Queue queue = iter.next();
			totalLength = totalLength + queue.size();
			if (iter.hasNext()) 
				totalLength = totalLength + 1;			
		}
		
		return totalLength;
	}
}