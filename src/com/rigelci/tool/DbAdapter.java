package com.rigelci.tool;

import java.util.HashMap;
import java.util.Map;

/**
 * DB 适配器
 * @author zhangkai05
 *
 */
public class DbAdapter {
	private static Map<String,Map<String,String>> dbMap;
	
	/**
	 * 初始化适配器
	 */
	public DbAdapter() {
		// MySql
		Map<String,Map<String,String>> dbMap = new HashMap<String,Map<String,String>>();
		Map<String,String> column = new HashMap<String,String>();
		column.put("int", "INT");
		column.put("bigint", "BIGINT");
		column.put("string", "VARCHAR");
		column.put("date", "DATE");
		dbMap.put("mysql", column);
		// HIVE
		column.clear();
		column.put("int", "INT");
		column.put("bigint", "BIGINT");
		column.put("string", "STRING");
		column.put("date", "STRING");
		dbMap.put("hive", column);
	}
}
