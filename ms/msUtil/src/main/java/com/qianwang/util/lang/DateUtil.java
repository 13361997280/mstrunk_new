package com.qianwang.util.lang;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

/**
 * date工具类
 * @author zou
 *
 */
public class DateUtil extends DateUtils{
	
	public static String DATE_COMPUTE_TYPE_YEAR = "YEAR";
	public static String DATE_COMPUTE_TYPE_MONTH = "MONTH";
	public static String DATE_COMPUTE_TYPE_DAY = "DAY";
	public static String DATE_COMPUTE_TYPE_HOUR = "HOUR";
	public static String DATE_COMPUTE_TYPE_MIN = "MINUTE";

	public static String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static String SHORT_TIMESTAMP_PATTERN = "yyyy-MM-dd";
	/**
	 * 根据指定时间返回格式化时，分，秒的日期
	 * @param date 指定时间
	 * @return
	 */
	public static Date getShortDate(Date date){
		 return getShortDate(date,0);
	}
	
	/**
	 * 根据指定时间返回格式化时，分，秒的日期
	 * @param date 指定时间
	 * @param interval 日期间隔，间隔为天
	 * @return
	 */
	public static Date getShortDate(Date date,int interval){
		 Calendar cd = Calendar.getInstance(Locale.CHINESE);   
		 cd.setTime(date);
		 cd.set(Calendar.DATE, cd.get(Calendar.DATE) + interval);
		 cd.set(Calendar.HOUR_OF_DAY, 0);
		 cd.set(Calendar.MINUTE, 0);
		 cd.set(Calendar.SECOND, 0);
		 cd.set(Calendar.MILLISECOND, 0);
		 return cd.getTime();
	}
	
	/**
	 * 根据指定时间返回月份
	 * @param date 指定时间
	 * @return
	 */
	public static int getMonth(Date date){
		 Calendar cd = Calendar.getInstance();  
		 cd.setTime(date);
		 return  cd.get(Calendar.MONTH);
	}
	
	/**
	 * 根据指定时间返回年份
	 * @param date 指定时间
	 * @return
	 */
	public static int getYear(Date date){
		 Calendar cd = Calendar.getInstance();   
		 cd.setTime(date);
		 return  cd.get(Calendar.YEAR);
	}
	
	/**
	 * 返回格式化时，分，秒的日期
	 * @return
	 */
	public static Date getShortDate(){ 
		 return getShortDate(new Date());
	}
	
	/**
	 * 日期格式字符串转换为日期对象 
	 * @param strDate
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String strDate, String pattern) throws ParseException {
			SimpleDateFormat format = new SimpleDateFormat(pattern);
			Date nowDate = format.parse(strDate);
			return nowDate;
	}
	
	/**
	 * 日期格式化成指定格式字符串
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formatDate(Date date, String pattern) {
		if (date == null){
			return "";
		}
		if (StringUtils.isBlank(pattern)){
			return formatDate(date,SHORT_TIMESTAMP_PATTERN);
		}
		return new SimpleDateFormat(pattern).format(date);
	}
	
	/**
	 * 日期加减函数
	 * number:为正数是相加,为负数是相减
	 * @param date
	 * @param type
	 * @param number
	 * @return
	 */
	public static Date dateCompute(Date date,String type,int number){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if(type.equals(DATE_COMPUTE_TYPE_YEAR)) calendar.add(Calendar.YEAR,number);
		if(type.equals(DATE_COMPUTE_TYPE_MONTH))calendar.add(Calendar.MONTH,number);
		if(type.equals(DATE_COMPUTE_TYPE_DAY))  calendar.add(Calendar.DAY_OF_MONTH,number);
		if(type.equals(DATE_COMPUTE_TYPE_HOUR)) calendar.add(Calendar.HOUR_OF_DAY,number);
		if(type.equals(DATE_COMPUTE_TYPE_MIN)) calendar.add(Calendar.MINUTE,number);
		Date computDate =  calendar.getTime();
		return computDate;
	}

	
	/**
	 * 获取两个日期间的天数
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Long getDaysBetween(Date startDate, Date endDate) {
		Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.setTime(startDate);
		fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
		fromCalendar.set(Calendar.MINUTE, 0);
		fromCalendar.set(Calendar.SECOND, 0);
		fromCalendar.set(Calendar.MILLISECOND, 0);

		Calendar toCalendar = Calendar.getInstance();
		toCalendar.setTime(endDate);
		toCalendar.set(Calendar.HOUR_OF_DAY, 0);
		toCalendar.set(Calendar.MINUTE, 0);
		toCalendar.set(Calendar.SECOND, 0);
		toCalendar.set(Calendar.MILLISECOND, 0);

		return (toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24);
	}

	/**
	 * date 转换成 yyyy年MM月dd日
	 * @param date
	 * @return
	 */
	public static String dateToStr(Date date){
		String dateStr = formatDate(date, null);
		String[] strings = dateStr.split("-");
		return strings[0] + "年" + strings[1] + "月" + strings[2] + "日";
	}

}
