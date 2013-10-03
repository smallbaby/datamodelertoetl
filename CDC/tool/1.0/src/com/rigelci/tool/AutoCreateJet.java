package com.rigelci.tool;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Text;
import org.dom4j.io.SAXReader;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;

/**
 * 1. 自动生成table_meta 2. 生成.sql文件 3. 生成yaml文件 4. 接收参数情况：1. 工程名：etl_task
 * cdc_cust_feature cdc_dm cdc_publish 2. 模型层：DataFlow 3. 模型中表的ID
 * 
 * @author zhangkai05
 * 
 */
public class AutoCreateJet {

	public static Map<String, String> modelM = new HashMap<String, String>();

	public static void main(String[] args) throws Exception, DocumentException {
		String project = "etl_task";
		String modelName = "DataFlow";
		String db_name = "";
		String tableId = "48F1DAB7-42AA-22E7-0564-EBABF3E4504D";
		SAXReader reader = new SAXReader();
		String filePath = 
				"E:/onedata_1/onedata/rel/" + modelM.get(modelName)
				+ "/table/seg_0/" + tableId + ".xml";
		//System.out.println(filePath);
		Document dc = reader.read(new File(filePath));
		makeMeta(project, dc,"ods");
	}

	/**
	 * 根据table path 自动返回脚本
	 * @param filepath
	 * @param project
	 * @param modelName
	 * @return
	 * @throws Exception
	 */
	public static Map<String,String> autoCreateMeta(String filepath,String project,String modelName,String dbName) throws Exception {
		SAXReader reader = new SAXReader();
		Document dc = reader.read(new File(filepath));
		return makeMeta(project, dc,dbName);
	}
	
	
	static {
		modelM.put("DataFlow", "6C4237561-9B5EAAA16246");
		modelM.put("cdc_cust_feature", "$db_cdc_cust_feature");
		modelM.put("dwa", "$db_dwa");
		modelM.put("dwd", "$db_dwd");
		modelM.put("ods", "$db_ods");
		modelM.put("LOGDT024", "STRING");
		modelM.put("LOGDT011", "INT");
		modelM.put("LOGDT020", "BIGINT");
		modelM.put("date_type_default", "STRING");
		
	}

	/**
	 * 生成yaml文件
	 * 
	 * @param table_name
	 */
	public static String makeyaml(String db, String table_name) {
		StringBuffer sb = new StringBuffer("parent: EtlTask\n");
		sb.append("task_name: " + db + "_" + table_name + "_t\n");
		sb.append("transform: \n");
		sb.append("  - step: hive.hivesql\n");
		sb.append("    file: sql/" + table_name + ".sql\n");
		sb.append("    time_offset: -1D\n");
		return sb.toString();
	}

	/**
	 * 生成建表语句
	 * 
	 * @param table_name
	 * @param table_comment
	 * @param columnList
	 * @return
	 */
	public static String makeCreateTable(String project, String table_name,
			String table_comment, List<String> columnList,String dbName) {
		int len = 0;
		int tempLen = 0;
		String[] cols = null;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < columnList.size(); i++) {
			cols = columnList.get(i).split("\t");
			tempLen = cols[0].length();
			if (tempLen > len) {
				len = tempLen;
			}
		}

		for (int i = 0; i < columnList.size(); i++) {
			cols = columnList.get(i).split("\t");
			boolean end = false;
			String dd = ",";
			if (i == 0) {
				sb.append("USE ").append(dbName).append(";\n")
						.append("CREATE TABLE ").append(table_name)
						.append("(\n");
			}
			if (i != columnList.size() - 1) {
				end = false;
			} else {
				end = true;
				dd = "";
			}

			sb.append(cols[0]).append(getEmptys(len - cols[0].length() + 2))
					.append(cols[1])
					.append(getEmptys(6 - cols[1].length() + 2))
					.append("COMMENT").append(getEmptys(1)).append("'")
					.append(cols[2]).append("'").append(dd).append("\n");

			if (end) {
				sb.append(")\n");
				sb.append("COMMENT '" + table_comment + "'\n");
				sb.append("PARTITIONED BY(pdate STRING)\n");
				sb.append("ROW FORMAT DELIMITED\n");
				sb.append("FIELDS TERMINATED BY '\\t'\n");
				sb.append("LINES TERMINATED BY '\\n'\n");
				sb.append("STORED AS RCFILE ;\n");
			}
		}

		//System.out.println(sb.toString());
		return sb.toString();
	}

	/**
	 * 生成.sql文件
	 * 
	 * @param columnList
	 */
	public static String makeSql(String table_name, List<String> columnList,String dbName) {
		StringBuffer sb = new StringBuffer();
		int len = 0;
		int tempLen = 0;
		for (int i = 0; i < columnList.size(); i++) {
			tempLen = columnList.get(i).length();
			if (tempLen > len) {
				len = tempLen;
			}
		}

		for (int i = 0; i < columnList.size(); i++) {
			if (i == 0) {
				sb.append("USE ").append(dbName).append(";\n").append("INSERT OVERWRITE TABLE $db_xxx.xxxxx  PARTITION(pdate='$YYYY-$mm-$dd')\n")
						.append("SELECT \n");
			}
			if (i != columnList.size() - 1) {
				sb.append(columnList.get(i))
						.append(getEmptys(len - columnList.get(i).length() + 2))
						.append("AS ").append(columnList.get(i)).append(",\n");
			} else {
				sb.append(columnList.get(i))
						.append(getEmptys(len - columnList.get(i).length() + 2))
						.append("AS ").append(columnList.get(i)).append("\n");
				sb.append("FROM $dwa.").append(table_name)
						.append(" WHERE pdate=\"$YYYY-$mm-$dd\";");
			}
		}

		return sb.toString();

	}

	public static String getEmptys(Integer len) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < len; i++) {
			sb.append(" ");
		}
		return sb.toString();
	}

	/**
	 * 生成table meta文件
	 * 
	 * @param dc
	 */
	public static Map<String,String> makeMeta(String projectName, Document dc,String dbName) {
		Element rootNode = dc.getRootElement();
		// 表名 英文
		String table_name = rootNode.elementText("abbreviation");
		// 表名 中文
		String table_comment = rootNode.attributeValue("name");
		Iterator iter = rootNode.elementIterator("columns");
		List<String> ellist = new ArrayList<String>();
		List<String> columnList = new ArrayList<String>();
		List<String> columnInfoList = new ArrayList<String>();
		boolean b = false;
		ellist.add("app_name: " + projectName + "\ntb_name: " + table_name
				+ "\nfield_list:");
		String type = "STRING";
		while (iter.hasNext()) {
			Iterator it = ((Element) iter.next()).elementIterator("Column");
			while (it.hasNext()) {
				Element e = (Element) it.next();
				String s = e.attributeValue("name");
				System.out.println(s);
				String dtype = "";
				try {
					dtype = ((Text) e.element("logicalDatatype").content().get(0)).getText();
				}catch(Exception ee) {
					dtype = modelM.get("date_type_default");
				}
				
				s = s.replaceAll("→", "");
				if (!"linkid".equals(s) && !"link_id".equals(s)) {
					b = true;
				} else {
					b = false;
				}
				// commentInRDBMS
				if (b) { // status flag type
					if (modelM.get(dtype) != null) {
						type = modelM.get(dtype);
					}
					
					columnList.add(s);
					columnInfoList.add(s
							+ "\t"
							+ type
							+ "\t"
							+ ((Text) e.element("commentInRDBMS").content()
									.get(0)).getText());
					ellist.add("\n  - field: "
							+ s
							+ "\n"
							+ "    type: "
							+ type
							+ "\n    comment: "
							+ ((Text) e.element("commentInRDBMS").content()
									.get(0)).getText());
				}
			}
		}
		
		if (!"cdc_publish".equals(projectName)) {
			ellist.add("\ntype: hive");
			ellist.add("\nfile_path: $db_" + dbName);
			ellist.add("\nmodel: hive");
			ellist.add("\nduration: -1");
			ellist.add("\nencoding: utf8");
		} else {
			ellist.add("\ntype: local");
			ellist.add("\nfile_path: ${publish_path}/fc/" + table_name + "/"
					+ table_name + "_${YYYYmmdd}");
			ellist.add("\nduration: 30");
			ellist.add("\nmodel: snapshot");
		}
		
		StringBuffer sb = new StringBuffer();
		for (String s : ellist) {
			sb.append(s);
		}
		String sql = makeSql(table_name, columnList,dbName);
		String yaml = makeyaml(dbName,table_name);
		String ctsql = makeCreateTable(projectName, table_name, table_comment, columnInfoList, dbName);
		Map<String,String> resMap = new HashMap<String,String>();
		resMap.put("sql", sql);
		resMap.put("yaml", yaml);
		resMap.put("ctsql", ctsql);
		resMap.put("meta", sb.toString());
		return resMap;
	}

}

/**
 * 
 * @author zhangkai05
 *
 */
class Column {
	private String name;
	private String type;
	private String comment;
	public Column() {
		
	}
	public Column(String name, String type, String comment) {
		this.name = name;
		this.type = type;
		this.comment = comment;
	}
	public String toString() {
		return "";
	}
	
}
