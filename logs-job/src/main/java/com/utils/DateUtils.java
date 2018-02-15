package com.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author song.j
 * @create 2017-08-09 11:11:50
 **/
public class DateUtils {

    public static final String FORMAT_Y_M_D = "yyyy-MM-dd";

    public static long getCurrentDate(Date date){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_Y_M_D);

        String dateStr = simpleDateFormat.format(date);

        try {
            return  simpleDateFormat.parse(dateStr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void main(String[] args) {
//        System.out.println(getCurrentDate(new Date()));
//        System.out.println(getCurrentDate(org.apache.commons.lang.time.DateUtils.addDays(new Date(),-1)));
        StringBuffer content = new StringBuffer();
        content.append("hi all:");
        content.append("\n");
        content.append("    附件是");
        Date cur = new Date();


        content.append(getDateByFormat(cur,"MM") + "月"+getDateByFormat(cur,"dd")+"日");
        content.append("oneday项目pv/uv");

        System.out.println(content.toString());
    }

    public static String getDateByFormat(Date date,String format){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    public static Date getYestDate(){
        Date yestDate = org.apache.commons.lang.time.DateUtils.addDays(new Date(),-1);

        return yestDate;
    }

    public static String getYestDateStr(){
        return getDateByFormat(getYestDate(),FORMAT_Y_M_D);
    }
}
