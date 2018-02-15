package com.qianwang.web.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


public class SortUtil {
	public static String getSortStr(Map<String,String> params){
		List<String>orders = new ArrayList<String>();
		List<String>columns = new ArrayList<String>();
		for(String key : params.keySet()){
			if(key.startsWith("order")){
				orders.add(key);
			} else if(key.startsWith("columns")){
				columns.add(key);
			}
		}
		
		List<String> orderList = new ArrayList<String>();
		for(int i = 0; i < orders.size() / 2; i++){
			int columnIndex = Integer.parseInt(params.get("order[" + i + "][column]"));
			String column = params.get("columns[" + columnIndex + "][data]");
			String dir = params.get("order[" + i + "][dir]");
			orderList.add(underscoreName(column) + " " + dir);
		}
		return StringUtils.join(orderList, ",");

	}
	/** 
     * 转换为下划线 
     *  
     * @param camelCaseName 
     * @return 
     */  
    public static String underscoreName(String camelCaseName) {  
        StringBuilder result = new StringBuilder();  
        if (camelCaseName != null && camelCaseName.length() > 0) {  
            result.append(camelCaseName.substring(0, 1).toLowerCase());  
            for (int i = 1; i < camelCaseName.length(); i++) {  
                char ch = camelCaseName.charAt(i);  
                if (Character.isUpperCase(ch)) {  
                    result.append("_");  
                    result.append(Character.toLowerCase(ch));  
                } else {  
                    result.append(ch);  
                }  
            }  
        }  
        return result.toString();  
    }  
  
    /** 
     * 转换为驼峰 
     *  
     * @param underscoreName 
     * @return 
     */  
    public static String camelCaseName(String underscoreName) {  
        StringBuilder result = new StringBuilder();  
        if (underscoreName != null && underscoreName.length() > 0) {  
            boolean flag = false;  
            for (int i = 0; i < underscoreName.length(); i++) {  
                char ch = underscoreName.charAt(i);  
                if ("_".charAt(0) == ch) {  
                    flag = true;  
                } else {  
                    if (flag) {  
                        result.append(Character.toUpperCase(ch));  
                        flag = false;  
                    } else {  
                        result.append(ch);  
                    }  
                }  
            }  
        }  
        return result.toString();  
    }  
    
    public static void main(String args[]) {
    	SortUtil util = new SortUtil();
    	System.out.println(util.underscoreName("startTime"));
    }
}
