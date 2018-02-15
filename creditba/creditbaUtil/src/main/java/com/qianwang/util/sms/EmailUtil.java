package com.qianwang.util.sms;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;

/**
 * @author song.j
 * @create 2017-10-16 14:14:31
 **/
public class EmailUtil {

    private JavaMailSenderImpl mailSender = null;

    private String sendTo;

    private String subject = "creditBa server";

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public JavaMailSenderImpl getMailSender() {
        return mailSender;
    }

    public void setMailSender(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }


    /**
     * 发送邮件 发送到配置文件配置的邮箱账号
     *
     * @param message 消息
     */
    public void sendEmail(String message,boolean html) {
        sendEmail(message, sendTo.split(","), subject,html);
    }

    /**
     * 发送邮件 发送到配置文件配置的邮箱账号
     *
     * @param message 消息
     */
    public void sendEmail(String message) {
        sendEmail(message, false);
    }

    /**
     * 发送邮件 发送到配置文件配置的邮箱账号
     *
     * @param message 消息
     * @param subject 标题
     */
    public void sendEmail(String message, String subject) {
        sendEmail(message, sendTo.split(","), subject,false);
    }


    public void sendEmail(String massage, String[] sendTo, String subject,boolean html) {
        MimeMessage mailMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true, "utf-8");
            messageHelper.setTo(sendTo);
            messageHelper.setFrom(mailSender.getUsername(), "noreply");
            messageHelper.setSubject(subject);
            messageHelper.setText(massage, html);
            mailSender.send(mailMessage);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("send email error ");
        }
    }



}
