package util;

import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;

import javax.imageio.IIOException;



public class Map2List {
	public static List	getList(Map map,String keyOrValue){
		List rtn=new ArrayList();
		Iterator iter=map.entrySet().iterator();
		while(iter.hasNext()){
			Entry entry=(Entry) iter.next();
			Object obj = null;
			if(keyOrValue.equals("key")){
				obj= entry.getKey();
			}else if(keyOrValue.equals("value")){
				obj=entry.getValue();
			}else {
				System.err.println("pleae input key or value");
				break;
			}
			rtn.add(obj);
		}
		return rtn;
		
	}

	public static Map toMap(Object javaBean) {
		Map result = new HashMap();
		Method[] methods = javaBean.getClass().getDeclaredMethods();
		for (Method method : methods) {
			try {
				if (method.getName().startsWith("get")) {
					String field = method.getName();
					field = field.substring(field.indexOf("get") + 3);
					field = field.toLowerCase().charAt(0) + field.substring(1);
					Object value = method.invoke(javaBean, (Object[]) null);
					result.put(field, null == value ? "" : value.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
