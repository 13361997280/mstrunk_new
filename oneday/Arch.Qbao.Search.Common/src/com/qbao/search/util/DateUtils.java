package com.qbao.search.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author song.j
 * @create 2017-09-15 16:16:14
 **/
public class DateUtils {

    public static String getWeekDayStr(Date date){
        int day = getWeekDay(date);
        String weekStr = "未知";
        switch (day){
            case 1:
                weekStr="星期天";
                break;
            case 2:
                weekStr="星期一";
                break;
            case 3:
                weekStr="星期二";
                break;
            case 4:
                weekStr="星期三";
                break;
            case 5:
                weekStr="星期四";
                break;
            case 6:
                weekStr="星期五";
                break;
            case 7:
                weekStr="星期六";
                break;
        }
        return weekStr;
    }

    /**
     * 根据指定时间返回星期几
     *
     * @param date 指定时间
     * @return
     */
    public static int getWeekDay(Date date) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        return cd.get(Calendar.DAY_OF_WEEK);
    }

    public static int getCurrentHour(){
        String hour = new SimpleDateFormat("HH").format(new Date());
        return Integer.valueOf(hour);
    }


}
