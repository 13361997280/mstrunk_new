package com.qianwang.util.lang;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
/**
 * 
 * @author wangjg
 *
 */
@SuppressWarnings("unchecked")
public class BeanHelper {
	
	public static Double sum(List<?> list, String sumProperty){
		Double result = 0.0;
		for(Object bean : list){
			try {
				Object value = getProperty(bean, sumProperty);
				result += Double.valueOf(""+value);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
		}
		return result;
	}
	
	public static <T> List<T> copyList(List<?> srcList, Class<T> clazz){
		if(srcList==null){
			return null;
		}
		List<T> result = new ArrayList<T>();
		for(Object orig:srcList){
			try {
				Object dest = clazz.newInstance();
				PropertyUtils.copyProperties(dest, orig);
				result.add((T) dest);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return result;
	}
	
	public static <T> T copyBean(Object orig, Class<T> clazz){
		try {
			Object dest = clazz.newInstance();
			PropertyUtils.copyProperties(dest, orig);
			return (T) dest;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static <T> T copyBean(Object orig, String...pps){
		if(orig==null){
			return null;
		}
		try {
			Object dest = orig.getClass().newInstance();
			for(String pp:pps){
				PropertyUtils.setProperty(dest, pp, PropertyUtils.getProperty(orig, pp));
			}
			return (T) dest;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	
	public static void copyBean(Object src, Object dest,String...pps){
		try {
			for(String pp:pps){
				PropertyUtils.setProperty(dest, pp, PropertyUtils.getProperty(src, pp));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static <T> T copyBean(Object src, Object dest,String[] srcPps,String[] destPps){
		try {
			for(int i=0;i<srcPps.length;i++){
				BeanUtils.setProperty(dest, destPps[i], PropertyUtils.getProperty(src, srcPps[i]));
			}
			return (T) dest;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static <T> List<T> getPropertyList(List<?> list, String property){
		List<T> plist = new ArrayList<T>();
		for(int i=0;i<list.size();i++){
			Object bean = list.get(i);
			try {
				T pv = (T) getProperty(bean, property);
				plist.add(pv);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return plist;
	}
	
	public static <T> List<T> getDistinctPropertyList(List<?> list, String property){
		List<T> plist = new ArrayList<T>();
		for(int i=0;i<list.size();i++){
			Object bean = list.get(i);
			try {
				Object pv = getProperty(bean, property);
				if(!plist.contains(pv)){
					plist.add((T) pv);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return plist;
	}
	
	public static Map listToMap(List<?> list, String property){
		Map<Object,Object> map = new HashMap<Object,Object>();
		for(Object bean:list){
			Object key = getProperty(bean, property);
			if(map.get(key)!=null){
				throw new RuntimeException("属性不唯一！"+property);
			}
			map.put(key, bean);
		}
		return map;
	}
	
	@SuppressWarnings("rawtypes")
	public static Map<Object,List> group(List list, String property){
		Map<Object,List> map = new LinkedHashMap<Object,List>();
		for(Object bean : list){
			Object key = getProperty(bean, property);
			List valueList = map.get(key);
			if(valueList==null){
				valueList = new ArrayList();
				map.put(key, valueList);
			}
			valueList.add(bean);
		}
		return map;
	}
	
	public static Map<String,List> group(List list, String...pps){
		Map<String,List> map = new LinkedHashMap<String,List>();
		for(Object bean : list){
			String key = getPpKey(bean);
			
			List valueList = map.get(key);
			if(valueList==null){
				valueList = new ArrayList();
				map.put(key, valueList);
			}
			valueList.add(bean);
		}
		return map;
	}
	
	public static String getPpKey(Object bean, String...pps){
		StringBuilder keySb = new StringBuilder();
		for(int i=0;i<pps.length;i++){
			String property = pps[i];
			try {
				if(i!=0){
					keySb.append(',');
				}
				Object pv = getProperty(bean, property);
				keySb.append(pv);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String key = keySb.toString();
		return key;
	}
	
	/**
	 * 合并src的非null值到dest
	 * @param src
	 * @param dest
	 */
	public static void mergeProperties(Object src, Object dest){
		try {
			PropertyDescriptor[] plist = PropertyUtils.getPropertyDescriptors(src.getClass());
			for(PropertyDescriptor pd:plist){
				if(pd.getReadMethod()!=null && pd.getWriteMethod()!=null){
					Object val = pd.getReadMethod().invoke(src);
					if(val!=null){
						PropertyUtils.setProperty(dest, pd.getName(), val);
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	static <T> T getProperty(Object bean, String property){
		try {
			return (T)PropertyUtils.getProperty(bean, property);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
