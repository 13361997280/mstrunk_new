package com.qianwang.dao.util;

public class DataSourceContextHolder {
	// 38
	public static final String DW_32 = "dw32";
	// 64
	public static final String DW = "dw";
	// 14.64
	public static final String DW_14_64_3306 = "dw_14_64_3306";
	public static final String DW_14_63_3306 = "dw_14_63_3306";
	// 93 auntion ticket
	public static final String DW_93 = "dw93";
	// 94 hongbao qbaochou merchant
	public static final String DW_94 = "dw94";
	// 93 3308
	public static final String DW_93_3308 = "dw93_3308";
	// 65
	public static final String DW_65 = "dw65";

	public static final String DW_66_3306 = "dw66_3306";
	// event opensdk
	public static final String DW_93_3307 = "dw93_3307";

	public static final String BAOJUAN_195 = "baojuan_195";

	public static final String IMPALA_30 = "IMPALA_30";

	public static final String DW_14_44 = "dw14_44";
	
	//lbs日志库  14.63
	public static final String QBASS30_3306 = "qbass30_3306";

	public static final String LBS_LOG_63 = "lbs_log_63";

	public static final String BAOQUAN_129 = "baoquan129_3306";

	public static final String QBASS132_3306 = "qbass132_3306";
	
	public static final String DW14_36 = "dw14_36";
	
	public static final String DW14_36_3307 = "dw14_36_3307";

	public static final String DW14_65_3306 = "dw14_65_3306";


	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

	public static void setDbType(String dbType) {
		contextHolder.set(dbType);
	}

	public static String getDbType() {
		return ((String) contextHolder.get());
	}

	public static void clearDbType() {
		contextHolder.remove();
	}
}
