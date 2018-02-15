package com.qianwang.util.lang;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 计算工具类
 * @author zou
 *
 */
public class CalculateUtil {
	/**
	 * 日志文件
	 */
	private static final Log log = LogFactory.getLog(CalculateUtil.class);
	
	/**
	 * 计算环比并格式为2位小数的百分比数,currentValue或者compareValue任一为空是返回null
	 * @param currentValue 当前值
	 * @param compareValue 比较值
	 * @return 返回格式化为2位小数的百分比数
	 */
	public static String lrRatio(Double currentValue,Double compareValue){
		if(currentValue == null || compareValue == null || compareValue == 0){
			return null;
		}
		Double ratio = (currentValue - compareValue) * 1.0/compareValue;
		return String.format("%1$.2f%%", ratio * 100);
	}
	
	/**
	 * 计算环比并格式为2位小数的百分比数,currentValue或者compareValue任一为空是返回null
	 * @param currentValue 当前值
	 * @param compareValue 比较值
	 * @return 返回格式化为2位小数的百分比数
	 */
	public static String lrRatio(Integer currentValue,Integer compareValue){
		if(currentValue == null || compareValue == null || compareValue == 0){
			return null;
		}
		return lrRatio((double)currentValue.intValue(),(double)compareValue.intValue());
	}
	

	/**
	 * 按平均分算法添加等级 只支持数字型分级
	 * @param l
	 * @param compareField 划分等级的字段名 
	 * @param levelField 设置等级字段名
	 * @param levelNum  划分的等级数
	 */
	public static void addAvgLevel(List<? extends Object> l,String compareField,String levelField,int levelNum){
		Double max = 0.0,min = 0.0;
		try {
			for (Object obj : l) {
				Double c = Double.parseDouble(BeanUtils.getProperty(obj, compareField));
				if(c > max){
					max = c;
				}
				if(c < min){
					min = c;
				}
			}
			Double diff = max-min;
			for (Object obj : l) {
				Double c = Double.parseDouble(BeanUtils.getProperty(obj, compareField));
				int level = 1;
				for(int i = levelNum - 1;i >= 0;i--){
					if(c-min >= i*diff/levelNum){
						level = i + 1;
						break;
					}
				}
				BeanUtils.setProperty(obj, levelField, level);
			}
		} catch (NumberFormatException e) {
			log.error("error in addAvgLevel : " + e);
		} catch (IllegalAccessException e) {
			log.error("error in addAvgLevel : " + e);
		} catch (InvocationTargetException e) {
			log.error("error in addAvgLevel : " + e);
		} catch (NoSuchMethodException e) {
			log.error("error in addAvgLevel : " + e);
		}
	}
}
