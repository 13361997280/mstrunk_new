package com.report.service;

import com.report.Config;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.util.Properties;

/**
 * @author song.j
 * @create 2017-08-09 16:16:35
 **/
public class SendEmailService {

    private static JavaMailSenderImpl mailSender = getInit();

    public static JavaMailSenderImpl getInit() {
        if (mailSender == null) {
            synchronized (SendEmailService.class) {
                JavaMailSenderImpl newMailSender = new JavaMailSenderImpl();
                newMailSender.setHost(Config.get().propertiesKey.get("mail.stmp.host"));
                Properties properties = new Properties();
                properties.put("mail.smtp.auth", true);
                properties.put("mail.smtp.timeout", 25000);
                newMailSender.setJavaMailProperties(properties);

                newMailSender.setUsername(Config.get().propertiesKey.get("mail.user"));
                newMailSender.setPassword(Config.get().propertiesKey.get("mail.password"));
                mailSender = newMailSender;
                return mailSender;
            }
        }
        return mailSender;
    }

    public void sendEmail() {
        EsDataService esDataService = new EsDataService();

        ExcelService excelService = new ExcelService();

        String filePath = excelService.handlExcelPath();

        String content = esDataService.getContent();

        String subject = "oneday模块pv/uv日报";

        String sendTos = Config.get().propertiesKey.get("send.to");

        String[] sendToArray = sendTos.split(",");

        String sendFrom = getInit().getUsername();

//            System.out.println("发送邮件至:" + sendTo);
        send(content, sendToArray, sendFrom, subject, filePath);
    }


    public void send(String massage, String[] sendTo, String sendFrom, String subject, String filePath) {
        MimeMessage mailMessage = getInit().createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true, "utf-8");
            File file = new File(filePath);
            messageHelper.addAttachment(MimeUtility.encodeWord(file.getName()), file);
            messageHelper.setTo(sendTo);
            messageHelper.setFrom(sendFrom, "no-reply");
            messageHelper.setSubject(subject);
            messageHelper.setText(massage, true);
            getInit().send(mailMessage);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("邮箱地址不正确");
        }
    }

}
