package com.qianwang.util.json;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 树json格式转换工具类
 * 
 * @author zou
 * 
 */
public class JsonTreeUtil {
	
	/**
	 * 日志文件
	 */
	private static final Log log = LogFactory.getLog(JsonTreeUtil.class);
	
	/**
	 * 本地线程
	 */
    private static ThreadLocal<Set<Integer>> tl = new ThreadLocal<Set<Integer>>(); // map  

	/**
	 * 转换list为tree格式的json字符串
	 * 
	 * @param l 树节点集合
	 * @param cfield 树节点对象中子节点字段标识
	 * @param pfield 树节点对象中父节点字段标识
	 * @return 格式化的树json串
	 */
	public static String formatTreeJson(List<? extends Object> l, String cfield, String pfield) {
		formatTreeList(l,cfield,pfield);
		return writeListAsString(l);
	}
	
	/**
	 * 转换list为tree格式集合
	 * 
	 * @param l 树节点集合
	 * @param cfield 树节点对象中子节点字段标识
	 * @param pfield 树节点对象中父节点字段标识
	 */
	public static void formatTreeList(List<? extends Object> l, String cfield, String pfield) {
		try {
			for (Object p : l) {
				String cvalue = BeanUtils.getProperty(p, cfield);
				for (Object c : l) {
					String pvalue = BeanUtils.getProperty(c, pfield);
					if (cvalue.equals(pvalue)) {
						setTreeJson(l,p,cfield,pfield);
					}
				}
			}
		} catch (IllegalAccessException e) {
			log.error("error in setTreeJson" + e);
		} catch (InvocationTargetException e) {
			log.error("error in setTreeJson" + e);
		} catch (NoSuchMethodException e) {
			log.error("error in setTreeJson" + e);
		}
		removeChild(l);
	}

	/**
	 * 转换list为tree格式的json字符串
	 * 
	 * @param l 树节点集合
	 * @param s 开始查询节点
	 * @param cfield 树节点对象中子节点字段标识
	 * @param pfield 树节点对象中父节点字段标识
	 * @return 格式化的树json串
	 */
	public static String formatTreeJson(List<? extends Object> l, Object s, String cfield, String pfield) {
		formatTreeList(l,s,cfield,pfield);
		return writeListAsString(l);
	}
	
	/**
	 * 转换list为tree格式集合
	 * 
	 * @param l 树节点集合
	 * @param s 开始查询节点
	 * @param cfield 树节点对象中子节点字段标识
	 * @param pfield 树节点对象中父节点字段标识
	 */
	public static void formatTreeList(List<? extends Object> l, Object s, String cfield, String pfield) {
		try {
			setTreeJson(l,s,cfield,pfield);
		} catch (IllegalAccessException e) {
			log.error("error in setTreeJson" + e);
		} catch (InvocationTargetException e) {
			log.error("error in setTreeJson" + e);
		} catch (NoSuchMethodException e) {
			log.error("error in setTreeJson" + e);
		}
		removeChild(l);
	}
	
	/**
	 * 格式化集合
	 * @param l
	 * @return
	 */
	private static String writeListAsString(List<? extends Object> l){
		ObjectMapper mapper = new ObjectMapper(); 
		try {
			return mapper.writeValueAsString(l);
		} catch (JsonProcessingException e) {
			return null;
		}
	}
	
	/**
	 * 删除集合中的多余子节点
	 * @param l
	 */
	private static void removeChild(List<? extends Object> l){
		Set<Integer> rmIndex = tl.get();int n = 0;
		if(rmIndex == null || rmIndex.size() <=0) return;
		Iterator<? extends Object> it = l.iterator();
		while (it.hasNext()) {
			it.next();
			for (int k : rmIndex) {
				if(n == k){
					it.remove();
				}
			}
			n++;
		}
	}
	
	/**
	 * 转换list为tree格式的json字符串
	 * 
	 * @param l 树节点集合
	 * @param s 开始查询节点
	 * @param cfield 树节点对象中子节点字段标识
	 * @param pfield 树节点对象中父节点字段标识
	 * @return 格式化的树json串
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	private static void setTreeJson(List<? extends Object> l, Object s, String cfield, String pfield) throws IllegalAccessException,
		InvocationTargetException, NoSuchMethodException {
		List<Object> cl = null;
		String cvalue = BeanUtils.getProperty(s, cfield);
		int i = 0,m = 0;
		Set<Integer> rmIndex = tl.get();
		if(rmIndex == null){
			 rmIndex = new HashSet<Integer>();
			 tl.set(rmIndex);
		}
		for (Object c : l) {
			String pvalue = BeanUtils.getProperty(c, pfield);
			if (cvalue.equals(pvalue)) {
				if (i++ == 0) {
					cl = new ArrayList<Object>();
				}
				cl.add(c);
				rmIndex.add(m);
				setTreeJson(l, c, cfield, pfield);
			}
			m++;
		}
		BeanUtils.setProperty(s, "children", cl);
	}
}
