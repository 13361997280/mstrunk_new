package com.report;


import com.report.service.ExcelService;
import com.report.service.SendEmailService;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author song.j
 * @create 2017-08-09 11:11:02
 **/
public class Main {
    public static void main(String[] args) {

       long date =  com.utils.DateUtils.getCurrentDate(org.apache.commons.lang.time.DateUtils.addDays(new Date(), 1));

        System.out.println(new Date(date  + 3600 * 1000 * 9));

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("脚本执行开始");
                SendEmailService sendEmailService =new SendEmailService();
                sendEmailService.sendEmail();
            }
        },new Date(date  + 3600 * 1000 * 9),3600 * 1000 * 24);

//        SendEmailService sendEmailService =new SendEmailService();
//        sendEmailService.sendEmail();

//        ExcelService excelService = new ExcelService();
//            excelService.main();
    }
}
