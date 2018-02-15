package com.qianwang.util.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;


public class JsonUtil {
	
 /**
  * JSON字符串转MAP
  * @param jsonStr
  * @return
  */
 public static Map<String, Object> jsonToMap(String jsonStr) {
		JSONObject jsonObj = JSONObject.fromObject(jsonStr);
		Iterator<String> nameItr = jsonObj.keys();
		String name;
		Map<String, Object> outMap = new HashMap<String, Object>();
		while (nameItr.hasNext()) {
			name = nameItr.next();
			outMap.put(name, jsonObj.getString(name));
		}
		return outMap;
 }
 
 /**
  * JSON字符串数组转List<Map>
  * @param jsonArray
  * @return
  */
 public static List<Map<String, Object>> jsonArryToListMap(String[] jsonArray) {
	 List<Map<String, Object>> resultList =  new ArrayList<Map<String,Object>>(); 
	 if (jsonArray.length > 0) {
		 for (String json:jsonArray) {
			 Map<String, Object> map = jsonToMap(json);
			 if (!map.isEmpty()) {
				 resultList.add(map);
			 }
			 
		 }
	 }
	 return resultList;
 }
}
