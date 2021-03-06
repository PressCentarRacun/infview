package metaschema;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class MetaschemaTransmogrifier {
	StringBuilder sb;
	void sobj() {
		sb.append("{");
	}
	void eobj() {
		if (sb.charAt(sb.length() - 1) == ',') {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append("}");
	}
	void sarr() {
		sb.append("[");
	}
	void earr() {
		if (sb.charAt(sb.length() - 1) == ',') {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append("]");
	}
	void str(String s) {
		/*String s2 = s.replace("\"", "\\\"");
		s2 = s2.replace("\'", "\\\'");*/
		sb.append('"');
		sb.append(s);
		sb.append('"');
	}
	void num(int n) {
		sb.append(Integer.toString(n));
	}
	void bool(boolean b) {
		sb.append(b ? "true" : "false");
	}
	void key(String k) {
		str(k);
		sb.append(':');
	}
	void kvbool(String k, boolean v) {
		str(k);
		sb.append(':');
		bool(v);
		sb.append(',');
	}
	void kvstr(String k, String v) {
		str(k);
		sb.append(':');
		str(v);
		sb.append(',');
	}
	void kvnum(String k, int v) {
		str(k);
		sb.append(':');
		num(v);
		sb.append(',');
	}
	void aestr(String s) {
		str(s);
		sb.append(',');
	}
	void aenum(int n) {
		num(n);
		sb.append(',');
	}
	void delim() {
		sb.append(',');
	}
	
	public String transmogrify(Connection db) throws SQLException {
		DatabaseMetaData mtdt = db.getMetaData();
		
		sb = new StringBuilder();
	
		ResultSet tbls = mtdt.getTables(null, null, null, new String[]{"TABLE"});
		
		sobj(); {
			kvstr("location", "nzm");
			kvstr("name", "nzm");
			kvstr("description", "nzm tbr!");
			kvstr("type", "mssql");
			key("packages"); {
				sarr(); { delim(); } earr();
			} delim();
			key("entities"); {
				sarr(); {
					while (tbls.next()) {
						String tableName = tbls.getString(tbls.findColumn("TABLE_NAME"));
						if (tableName.startsWith("trace") || tableName.equals("sysdiagrams")) {
							continue;
						}
						
						sobj(); {
							System.out.println("glory. i found ur table " + tableName + ". i will now to make entity from glory table " + tableName + " thanks raheed");
							
							kvstr("name", tableName);
							kvstr("url", tableName);
							kvstr("type", "sqlTable");
							
							ResultSet pk = mtdt.getPrimaryKeys(null, null, tableName);
							ArrayList<String> pks = new ArrayList<>();
							while (pk.next()) {
								pks.add(pk.getString(pk.findColumn("COLUMN_NAME")));
							}
							
							HashMap<String, ArrayList<String>> rels = new HashMap<>();
							key("attributes"); {
								sarr(); {
									ResultSet cols = mtdt.getColumns(null, null, tableName, null);	
									while (cols.next()) {
										String colName = cols.getString(cols.findColumn("COLUMN_NAME"));
										
										sobj(); {
											kvstr("name", colName);
											int typ = cols.getInt(cols.findColumn("DATA_TYPE"));
											String typs = "nzm";
											switch(typ) {
											case java.sql.Types.CHAR: typs = "char"; break;
											case java.sql.Types.VARCHAR: typs = "varchar"; break;
											case java.sql.Types.BOOLEAN: typs = "boolean"; break;
											case java.sql.Types.DATE:
											case java.sql.Types.TIME:
											case java.sql.Types.TIMESTAMP:
												typs = "datetime"; break;
											case java.sql.Types.INTEGER:
											case java.sql.Types.DECIMAL:
											case java.sql.Types.BIGINT:
												typs = "numeric"; break;
											default: System.out.println("        bahen ke takke! it is not good type, is " + Integer.toString(typ) + ", must to fix type or raheed not can create");
											}
											
											boolean pkey = pks.indexOf(colName) > -1;
											int length = cols.getInt(cols.findColumn("COLUMN_SIZE"));
											
											kvstr("type", typs);
											kvnum("length",  length);
											kvbool("primaryKey", pkey);
											kvbool("mandatory", cols.getInt(cols.findColumn("NULLABLE")) == DatabaseMetaData.columnNoNulls);
										} eobj(); delim();
									}
								} earr(); delim();
							}
							

							ResultSet importedKeys = mtdt.getImportedKeys(null, null, tableName);
							while (importedKeys.next()) {
								String otherTbl = importedKeys.getString("PKTABLE_NAME");
								
								ArrayList<String> relattrs;
								if (rels.containsKey(otherTbl)) {
									relattrs = rels.get(otherTbl);
								} else {
									relattrs = new ArrayList<>();
									rels.put(otherTbl, relattrs);
								}
								
								relattrs.add(importedKeys.getString("PKCOLUMN_NAME") + ";" + importedKeys.getString("FKCOLUMN_NAME"));
							}
							
							key("relations"); {
								sarr(); {
									for (HashMap.Entry<String, ArrayList<String>> entry : rels.entrySet()) {
										sobj(); {
											key("referencedAttributes"); {
												sarr(); {
													for (String attr : entry.getValue()) {
														aestr(entry.getKey() + "/" + attr.split(";")[0]);
													}
												} earr(); delim();
											}
											
											key("referringAttributes"); {
												sarr(); {
													for (String attr: entry.getValue()) {
														aestr(attr.split(";")[1]);
													}
												} earr(); delim();
											}
										} eobj(); delim();
									}
								} earr(); delim();
							} 
						} eobj(); delim();
					}
				} earr(); delim();
			}
		} eobj();
		return sb.toString();
	}
}
