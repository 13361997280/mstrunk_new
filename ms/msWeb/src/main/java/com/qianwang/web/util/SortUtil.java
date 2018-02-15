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
			orderList.add(column + " " + dir);
		}
		return StringUtils.join(orderList, ",");

	}
}
