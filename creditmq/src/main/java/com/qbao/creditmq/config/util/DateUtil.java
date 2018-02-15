package com.qbao.creditmq.config.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DateUtil {
	// 默认显示日期的格式
	public static final String DATAFORMAT_STR = "yyyy-MM-dd";
	// 默认显示日期的格式
	public static final String YYYY_MM_DATAFORMAT_STR = "yyyy-MM";
	// 默认显示日期时间的格式
	public static final String DATATIMEF_STR = "yyyy-MM-dd HH:mm:ss";
	// 默认显示日期时间的格式
	public static final String DATATIMEF_STR2 = "yyyyMMdd HH:mm:ss";
	// 默认显示日期时间的格式 精确到毫秒
	public static final String DATATIMEF_STR_MIS = "yyyyMMddHHmmssSSS";
	// 默认显示日期时间的格式 精确到分钟
	public static final String DATATIMEF_STR_MI = "yyyy-MM-dd HH:mm";

	public static final String DATATIMEF_STR_MDHm = "MM.dd HH:mm";

	public static final String HH_STR = "HH";

	// 精确到秒
	public static final String DATATIMEF_STR_SEC = "yyyyMMddHHmmss";
	// 默认显示简体中文日期的格式
	public static final String ZHCN_DATAFORMAT_STR = "yyyy年MM月dd日";
	// 默认显示简体中文日期时间的格式
	public static final String ZHCN_DATATIMEF_STR = "yyyy年MM月dd日HH时mm分ss秒";
	// 默认显示简体中文日期时间的格式
	public static final String ZHCN_DATATIMEF_STR_4yMMddHHmm = "yyyy年MM月dd日HH时mm分";

	// 默认显示月份和日期的格式
	public static final String MONTHANDDATE_STR = "MM.dd";
	
	public static final String DATATIMEF_STR_MIN = "yyyyMMddHHmm";
	
	public  static final String HOUR_END = " 23:59:59";
	
	public  static final String HOUR_START = " 00:00:00";
	private static SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DATAFORMAT_STR);
	private static SimpleDateFormat sdf2 = new SimpleDateFormat(DATAFORMAT_STR);
	private DateUtil() {
	}

	public static Date now() {
		return Calendar.getInstance(Locale.CHINESE).getTime();
	}

	public static Date daysAfter(Date baseDate, int increaseDate) {
		Calendar calendar = Calendar.getInstance(Locale.CHINESE);
		calendar.setTime(baseDate);
		calendar.add(Calendar.DATE, increaseDate);
		return calendar.getTime();
	}
	
	public static Date hoursAfter(Date baseDate, int increaseHours) {
		Calendar calendar = Calendar.getInstance(Locale.CHINESE);
		calendar.setTime(baseDate);
		calendar.add(Calendar.HOUR_OF_DAY, increaseHours);
		return calendar.getTime();
	}

	public static Date minuteAfter(Date baseDate, int increaseMonths) {
		Calendar calendar = Calendar.getInstance(Locale.CHINESE);
		calendar.setTime(baseDate);
		calendar.add(Calendar.MINUTE, increaseMonths);
		return calendar.getTime();
	}

	public static Date monthAfter(Date baseDate, int increaseMonths) {
		Calendar calendar = Calendar.getInstance(Locale.CHINESE);
		calendar.setTime(baseDate);
		calendar.add(Calendar.MONTH, increaseMonths);
		return calendar.getTime();
	}

	public static Date getInternalDateByDay(Date d, int days) {
		Calendar now = Calendar.getInstance(TimeZone.getDefault());
		now.setTime(d);
		now.add(Calendar.DATE, days);
		return now.getTime();
	}

	public static Date getInternalDateByMinute(Date d, int minutes) {
		Calendar now = Calendar.getInstance(TimeZone.getDefault());
		now.setTime(d);
		now.add(Calendar.MINUTE, minutes);
		return now.getTime();
	}

	/**
	 * 将Date转换成字符串“yyyy-mm-dd hh:mm:ss”的字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToDateString(Date date) {
		return dateToDateString(date, DATATIMEF_STR);
	}

	/**
	 * 将Date转换成字符串“yyyy-mm-dd hh:mm:ss”的字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToDateString2(Date date) {
		return dateToDateString(date, DATATIMEF_STR2);
	}

	/**
	 * 将Date转换成formatStr格式的字符串
	 * 
	 * @param date
	 * @param formatStr
	 * @return
	 */
	public static String dateToDateString(Date date, String formatStr) {
		if (date == null) {
			return null;
		}
		java.text.DateFormat df = getDateFormat(formatStr);
		return date != null ? df.format(date) : "";
	}

	/**
	 * 按照默认formatStr的格式，转化dateTimeStr为Date类型 dateTimeStr必须是formatStr的形式
	 * 
	 * @param dateTimeStr
	 * @param formatStr
	 * @return
	 */
	public static Date getDate(String dateTimeStr, String formatStr) {
		try {
			if (dateTimeStr == null || dateTimeStr.equals("")) {
				return null;
			}
			java.text.DateFormat sdf = new SimpleDateFormat(formatStr);
			Date d = sdf.parse(dateTimeStr);
			return d;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getCurDate() {
		return dateToDateString(Calendar.getInstance().getTime(),
				DATAFORMAT_STR);
	}

	public static String getCurHour() {
		return dateToDateString(Calendar.getInstance().getTime(), HH_STR);
	}

	public static int getThisMonth() {
		Calendar c = Calendar.getInstance(Locale.CHINESE);
		int month = c.get(Calendar.MONTH) + 1;
		return month;

	}

	public static int getThisWeek() {
		Calendar c = Calendar.getInstance(Locale.CHINESE);
		c.setFirstDayOfWeek(Calendar.MONDAY);
		int week = c.get(Calendar.WEEK_OF_YEAR);
		return week;

	}

	public static SimpleDateFormat getDateFormat(final String formatStr) {
		return new SimpleDateFormat(formatStr);
	}

	@SuppressWarnings("deprecation")
	public static String getFirstDateOfMonth(Date now) {
		SimpleDateFormat df1 = new SimpleDateFormat(DATATIMEF_STR);
		Date da = new Date(now.getYear(), now.getMonth(), 01);
		return df1.format(da);
	}

	@SuppressWarnings("deprecation")
	public static String getLastDateOfMonth(Date now) {
		SimpleDateFormat df1 = new SimpleDateFormat(DATATIMEF_STR);
		Date da = new Date(now.getYear(), now.getMonth(), 31);
		return df1.format(da);
	}

	/**
	 * 获取两个毫秒间隔的分钟
	 * 
	 * @param t1
	 * @param t2
	 * @return
	 */
	public static int getMinutesBetweenMillis(long t1, long t2) {
		return (int) ((t1 - t2) / (60 * 1000));
	}

	/**
	 * 判断目标时间是否处于某一时间段内
	 * 
	 * @param target
	 * @param begin
	 * @param end
	 * @return
	 */
	public static boolean compareTargetTime(Date target, String begin,
			String end) {
		// 格式化时间 暂时不考虑传入参数的判断，其他地方如果要调用，最好扩展判断一下入参问题
		String targetTime = dateToDateString(target, DATATIMEF_STR).substring(
				11);// HH:mm:ss
		if (targetTime.compareTo(begin) >= 0 && end.compareTo(targetTime) >= 0) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param time1
	 * @param time2
	 * @return time1 小于 time 2 返回 true
	 */
	public static boolean compareTime(Date time1, Date time2) {
		long start = time1.getTime();
		long end = time2.getTime();
		if (start < end) {
			return true;
		}

		return false;
	}

	/**
	 * 取得两个时间段的时间间隔 return t2 与t1的间隔天数 throws ParseException
	 * 如果输入的日期格式不是0000-00-00 格式抛出异常
	 */
	public static int getBetweenDays(String t1, String t2)
			throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		int betweenDays = 0;
		Date d1 = format.parse(t1);
		Date d2 = format.parse(t2);
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(d1);
		c2.setTime(d2);
		// 保证第二个时间一定大于第一个时间
		if (c1.after(c2)) {
			c1 = c2;
			c2.setTime(d1);
		}
		int betweenYears = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
		betweenDays = c2.get(Calendar.DAY_OF_YEAR)
				- c1.get(Calendar.DAY_OF_YEAR);
		for (int i = 0; i < betweenYears; i++) {
			c1.set(Calendar.YEAR, (c1.get(Calendar.YEAR) + 1));
			betweenDays += c1.getMaximum(Calendar.DAY_OF_YEAR);
		}
		return betweenDays;
	}

	/**
	 * 格式化时间 yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String getFormatDate(Date date) {
		return new SimpleDateFormat().format(date);
	}

	/**
	 * 按照默认formatStr的格式，转化dateTimeStr为Date类型 dateTimeStr必须是formatStr的形式
	 * 
	 * @param dateTimer
	 * @param formatStr
	 * @return
	 */
	public static Date getFormatDate(Date dateTimer, String formatStr) {
		try {
			if (dateTimer == null) {
				return null;
			}
			java.text.DateFormat sdf = new SimpleDateFormat(formatStr);
			String timeStr = sdf.format(dateTimer);
			Date formateDate = sdf.parse(timeStr);
			return formateDate;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取两个时间之间相差的天数
	 * 
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static long getQuot(String time1, String time2) {
		long quot = 0;
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date1 = ft.parse(time1);
			Date date2 = ft.parse(time2);
			quot = date1.getTime() - date2.getTime();
			quot = quot / 1000 / 60 / 60 / 24;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return quot;
	}

	/**
	 * 获取和当前时间相差的分钟数
	 * 
	 * @param begin
	 * @return
	 */
	public static long getDiffenceValue(Date begin) {
		long value = 0;
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		value = (now.getTime() - begin.getTime()) / 1000 / 60;
		return value;
	}

	public static long getMillsBetweenTwoDate(Date date1, Date date2) {
		return date1.getTime() - date2.getTime();
	}

	/**
	 * 求多少天前/后的日期
	 * 
	 * @param field
	 *            单位：年，月，日
	 * @param day
	 *            多少天
	 * @return
	 */
	public static final Date addDate(int field, int day) {
		Calendar nowCalendar = Calendar.getInstance(Locale.CHINESE);
		nowCalendar.setTime(DateUtil.now());
		nowCalendar.add(field, day);
		return nowCalendar.getTime();
	}


    /**
     * |
     *
     * @param date
     * @return
     */
    public static String getNextDay(Date date, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, i);
        date = calendar.getTime();
        return dateToDateString(date, DATAFORMAT_STR);

    }
	/**
	 * 时间格式校验 2016-05
	 * @param date
	 * @return
	 */
	public static boolean isDate(String date)
	{
		/**
		 * 判断日期格式和范围
		 */
		String rexp = "[0-9]{4}-[0-9]{2}";
		Pattern pat = Pattern.compile(rexp);
		Matcher mat = pat.matcher(date);
		boolean dateType = mat.matches();
		return dateType;
	}

	/**
	 * 获取每月第一天
	 * @return
	 */
	public static Date getFirstDay(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		Date firstDay = null;
		try {
			firstDay = format.parse(format.format(new Date()).concat("-01"));
		}catch(Exception e){
			firstDay = new Date();
		}
		return firstDay;
	}
	/**
	 * 得到指定月的天数
	 * */
	public static int getMonthLastDay(Date d)
	{
		String date=formatDate(d);
		String [] yearAndDay=date.split("-");
		int year=Integer.valueOf(yearAndDay[0]);
		int month=Integer.valueOf(yearAndDay[1]);
		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, 1);//把日期设置为当月第一天
		a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}
	public static String formatDate(Date date){
		synchronized(sdf){
			return sdf.format(date);
		}
	}
	public static String formatDate(Date date,SimpleDateFormat format){
			return format.format(date);
	}
	public static Date parse(String strDate) {
		synchronized(sdf2){
			try{
				return sdf2.parse(strDate);
			}catch(Exception e){
				Calendar cale = Calendar.getInstance();
				//设置为1号,当前日期既为本月第一天
				cale.set(Calendar.DAY_OF_MONTH,0);
				return cale.getTime();
			}
		}
	}
	/**
	 * redis秒数
	 * @return
	 */
	public static Long getRedisExpired(){
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date thisDate=new Date();
		return (DateUtil.parse(dateFormat.format(DateUtil.getInternalDateByDay(thisDate,1))).getTime()-thisDate.getTime())/1000;
	}
}
