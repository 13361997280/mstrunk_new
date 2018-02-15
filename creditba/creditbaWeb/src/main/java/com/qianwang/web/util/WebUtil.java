package com.qianwang.web.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.dubbo.common.utils.IOUtils;
import com.qianwang.util.json.JsonUtil;

/**
 * @author wangjg
 */
public class WebUtil {
	
	public static Map<String, String> getParameterMap(HttpServletRequest request) {
		return getParameterMap(request.getParameterMap());
	}
	
	public static Map<String, Object> getBodyParameterMap(HttpServletRequest request) {
		try {
			BufferedReader reader = request.getReader();
			String json = IOUtils.read(reader);
			return JsonUtil.jsonToMap(json);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Map<String, String> getParameterMap(Map<String, String[]> ms) {
		Map<String, String> result = new HashMap<String, String>();
		if (ms == null || ms.size() == 0)
			return result;
		for (Map.Entry<String, String[]> m : ms.entrySet()) {
			result.put(m.getKey(), m.getValue()[0]);
			if(m.getValue().length > 1){
				result.put(m.getKey(), join(m.getValue()));
			}
		}
		return result;
	}
	
	//当参数是多个数组时，组成maplist
	public static List<Map<String, String>> getParameterMapList(HttpServletRequest request, String primaryKey) {
		Map<String, String[]> ms = request.getParameterMap();
		
		List<Map<String, String>> maplist = new ArrayList<Map<String, String>>();
		
		String[] values = ms.get(primaryKey);
		for(String val : values){
			Map<String, String> map = new HashMap<String, String>();
			map.put(primaryKey, val);
			maplist.add(map);
		}
		
		for (Map.Entry<String, String[]> m : ms.entrySet()) {
			String name = m.getKey();
			String[] val = m.getValue();
			
			for(int i=0;i<maplist.size();i++){
				Map<String, String> map = maplist.get(i);
				map.put(name, val[i]);
			}
		}
		
		return maplist;
	}
	
	private static String join(String[] ss){
		StringBuilder sb = new StringBuilder();
		for(String s:ss){
			sb.append(s);
			sb.append(',');
		}
		return sb.substring(0, sb.length()-1);
	}
	
}
