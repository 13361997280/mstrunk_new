package com.qbao;

import java.util.ArrayList;
import java.util.List;

/**
 * @author song.j
 * @create 2017-09-12 15:15:54
 **/
public class Demo {


    public static void main(String[] args) {

//        TResultOnedayTimeaxisDMapper mapper = DbFactory.createBean(TResultOnedayTimeaxisDMapper.class, DataSourceEnvironment.ONEDAY);


        List list = new ArrayList();

//        for (int i = 0; i < 1000; i++) {
//            list.add(new TResultOnedayTimeaxisD(new Date(), i, i, new BigDecimal(i+0.5), "2017-09-14"));
//
//        }
//        list.add(new TResultOnedayTimeaxisD(new Date(), 7, 152259, new BigDecimal(9.14), "2017-09-14"));
//        list.add(new TResultOnedayTimeaxisD(new Date(), 7, 152259, new BigDecimal(9.14), "2017-09-14"));
//        list.add(new TResultOnedayTimeaxisD(new Date(), 7, 152259, new BigDecimal(9.14), "2017-09-14"));
//        list.add(new TResultOnedayTimeaxisD(new Date(), 7, 152259, new BigDecimal(9.14), "2017-09-14"));
//        list.add(new TResultOnedayTimeaxisD(new Date(), 7, 152259, new BigDecimal(9.14), "2017-09-14"));
//        list.add(new TResultOnedayTimeaxisD(new Date(), 7, 152259, new BigDecimal(9.14), "2017-09-14"));
//        list.add(new TResultOnedayTimeaxisD(new Date(), 7, 152259, new BigDecimal(9.14), "2017-09-14"));
//        list.add(new TResultOnedayTimeaxisD(new Date(), 7, 152259, new BigDecimal(9.14), "2017-09-14"));
//        list.add(new TResultOnedayTimeaxisD(new Date(), 7, 152259, new BigDecimal(9.14), "2017-09-14"));

        for (int i = 0; i < 100000; i++) {
//            mapper.insertBatch(list);
        }


    }


//    jdbc.driver=com.mysql.jdbc.Driver
//
//    oneday.jdbc.url=jdbc:mysql://192.168.14.107:3306/oneday
//    oneday.jdbc.username=canal
//    oneday.jdbc.password=canal
//
//
//    data.jdbc.url=jdbc:mysql://127.0.0.1:3306/test
//    data.jdbc.username=root
//    data.jdbc.password=root


}