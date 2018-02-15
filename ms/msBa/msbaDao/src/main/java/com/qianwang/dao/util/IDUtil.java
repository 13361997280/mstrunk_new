package com.qianwang.dao.util;

import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;

/**
 * 主键生成工具
 * 
 * @author zou
 * 
 */
public class IDUtil {
	/**
	 * 返回主键
	 * 
	 * @return 主键
	 */
	public static long getNextLongID() {
		DataFieldMaxValueIncrementer incrementer = (DataFieldMaxValueIncrementer) SpringContextHolder
				.getBean("dataFieldMaxValueIncrementer");
		return incrementer.nextLongValue();
	}
}
