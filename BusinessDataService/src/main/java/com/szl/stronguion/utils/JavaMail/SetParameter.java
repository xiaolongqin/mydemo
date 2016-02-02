package com.szl.stronguion.utils.JavaMail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * Created by 郭皓
 * on 15-04-9
 * at 下午1:23.
 */
public class SetParameter {
    //获取参数
    public Properties getProperties() {
        Properties properties = new Properties();
        try {
            properties.load(SetParameter.class.getClassLoader().getResourceAsStream("javaMail.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
    //发送重置密码邮件
    public boolean resetPassMail(String toAddress, boolean flag,String content) throws MessagingException {
        Properties properties = getProperties();
        Session sendMailSession = null;
        /**判断是否需要身份验证
         * flag -->判断是否需要验证
         */
        MyAuthenticator authenticator = null;
        properties.setProperty("validate", String.valueOf(flag));
        if (properties.getProperty("validate").equals("true")) {
            //如果需要身份验证，则创建一个密码验证器
            authenticator = new MyAuthenticator(properties.getProperty("userName"), properties.getProperty("password"));

        }
        //根据邮件会话属性和密码验证器构造一个发送邮件的session
        sendMailSession = Session.getDefaultInstance(properties, authenticator);

        try {
            //根据session创建一个邮件信息
            Message mailMessage = new MimeMessage(sendMailSession);

            //创建邮件发送者地址
            Address from = new InternetAddress(properties.getProperty("fromAddress"));
            //设置邮件消息的发送者
            mailMessage.setFrom(from);

            properties.setProperty("mail.smtp.auth","true");
            //创建邮件的接受者地址，并设置到邮件消息中
            properties.setProperty("toAddress",toAddress);
            Address to = new InternetAddress(properties.getProperty("toAddress"));
            mailMessage.setRecipient(Message.RecipientType.TO, to);

            //设置邮件消息的主题
            properties.setProperty("subject","密码找回-数之云");
            mailMessage.setSubject(properties.getProperty("subject"));
            //设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());
            // 设置邮件消息的主要内容
            properties.setProperty("content",content);
            String mailContent = properties.getProperty("content");
            mailMessage.setText(mailContent);
            //发送邮件
            Transport transport = sendMailSession.getTransport("smtp");//定义发送协议
            transport.connect(properties.getProperty("mail.smtp.host"), Integer.valueOf(properties.getProperty("mail.smtp.port")), properties.getProperty("userName"), properties.getProperty("password"));//登录邮箱
            transport.send(mailMessage, mailMessage.getRecipients(Message.RecipientType.TO));//发送邮件
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }


    //发送认证邮件
    public boolean setMailMessage(String toAddress, boolean flag,String content) throws MessagingException {
        Properties properties = getProperties();
         Session sendMailSession = null;
        /**判断是否需要身份验证
         * flag -->判断是否需要验证
         */
        MyAuthenticator authenticator = null;
        properties.setProperty("validate", String.valueOf(flag));
        if (properties.getProperty("validate").equals("true")) {
            //如果需要身份验证，则创建一个密码验证器
            authenticator = new MyAuthenticator(properties.getProperty("userName"), properties.getProperty("password"));

        }
        //根据邮件会话属性和密码验证器构造一个发送邮件的session
         sendMailSession = Session.getDefaultInstance(properties, authenticator);

        try {
            //根据session创建一个邮件信息
            Message mailMessage = new MimeMessage(sendMailSession);

            //创建邮件发送者地址
            Address from = new InternetAddress(properties.getProperty("fromAddress"));
            //设置邮件消息的发送者
            mailMessage.setFrom(from);

            properties.setProperty("mail.smtp.auth","true");
            //创建邮件的接受者地址，并设置到邮件消息中
            properties.setProperty("toAddress",toAddress);
            Address to = new InternetAddress(properties.getProperty("toAddress"));
            mailMessage.setRecipient(Message.RecipientType.TO, to);

            //设置邮件消息的主题
//            mailMessage.setSubject(properties.getProperty("subject"));
            mailMessage.setSubject("Business-Data-Service 注册验证邮件");
            //设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());
            // 设置邮件消息的主要内容
            properties.setProperty("content",content);
            String mailContent = properties.getProperty("content");
            mailMessage.setText(mailContent);
            //发送邮件
            Transport transport = sendMailSession.getTransport("smtp");//定义发送协议
            transport.connect(properties.getProperty("mail.smtp.host"), Integer.valueOf(properties.getProperty("mail.smtp.port")), properties.getProperty("userName"), properties.getProperty("password"));//登录邮箱
            transport.sendMessage(mailMessage, mailMessage.getRecipients(Message.RecipientType.TO));//发送邮件
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
    //发送消息邮件
    public boolean setMsg2User(String toAddress, boolean flag,String content) throws MessagingException{
        Properties properties = getProperties();
        Session sendMailSession = null;
        /**判断是否需要身份验证
         * flag -->判断是否需要验证
         */
        MyAuthenticator authenticator = null;
        properties.setProperty("validate", String.valueOf(flag));
        if (properties.getProperty("validate").equals("true")) {
            //如果需要身份验证，则创建一个密码验证器
            authenticator = new MyAuthenticator(properties.getProperty("userName"), properties.getProperty("password"));

        }
        //根据邮件会话属性和密码验证器构造一个发送邮件的session
        sendMailSession = Session.getDefaultInstance(properties, authenticator);

        try {
            //根据session创建一个邮件信息
            Message mailMessage = new MimeMessage(sendMailSession);

            //创建邮件发送者地址
            Address from = new InternetAddress(properties.getProperty("fromAddress"));
            //设置邮件消息的发送者
            mailMessage.setFrom(from);

            //创建邮件的接受者地址，并设置到邮件消息中
            properties.setProperty("toAddress",toAddress);
            Address to = new InternetAddress(properties.getProperty("toAddress"));
            mailMessage.setRecipient(Message.RecipientType.TO, to);

            //设置邮件消息的主题
            properties.setProperty("subject","The new message from RDS!");
            mailMessage.setSubject(properties.getProperty("subject"));
            //设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());
            // 设置邮件消息的主要内容
            properties.setProperty("content",content);
            String mailContent = properties.getProperty("content");
            mailMessage.setText(mailContent);
            //发送邮件
            Transport transport = sendMailSession.getTransport("smtp");//定义发送协议
            transport.connect(properties.getProperty("mail.smtp.host"), Integer.valueOf(properties.getProperty("mail.smtp.port")), properties.getProperty("userName"), properties.getProperty("password"));//登录邮箱
            transport.send(mailMessage, mailMessage.getRecipients(Message.RecipientType.TO));//发送邮件
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    //????setMsg2User需要做成通用函数
    //OSS分享消息邮件
    public boolean setMsg2Oss(String toAddress, boolean flag,String content) throws MessagingException{
        Properties properties = getProperties();
        Session sendMailSession = null;
        /**判断是否需要身份验证
         * flag -->判断是否需要验证
         */
        MyAuthenticator authenticator = null;
        properties.setProperty("validate", String.valueOf(flag));
        if (properties.getProperty("validate").equals("true")) {
            //如果需要身份验证，则创建一个密码验证器
            authenticator = new MyAuthenticator(properties.getProperty("userName"), properties.getProperty("password"));

        }
        //根据邮件会话属性和密码验证器构造一个发送邮件的session
        sendMailSession = Session.getDefaultInstance(properties, authenticator);

        try {
            //根据session创建一个邮件信息
            Message mailMessage = new MimeMessage(sendMailSession);

            //创建邮件发送者地址
            Address from = new InternetAddress(properties.getProperty("fromAddress"));
            //设置邮件消息的发送者
            mailMessage.setFrom(from);

            //创建邮件的接受者地址，并设置到邮件消息中
            properties.setProperty("toAddress",toAddress);
            Address to = new InternetAddress(properties.getProperty("toAddress"));
            mailMessage.setRecipient(Message.RecipientType.TO, to);

            //设置邮件消息的主题
            properties.setProperty("subject","The new message from OSS!");
            mailMessage.setSubject(properties.getProperty("subject"));
            //设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());
            // 设置邮件消息的主要内容
            properties.setProperty("content",content);
            String mailContent = properties.getProperty("content");
            mailMessage.setText(mailContent);
            //发送邮件
            Transport transport = sendMailSession.getTransport("smtp");//定义发送协议
            transport.connect(properties.getProperty("mail.smtp.host"), Integer.valueOf(properties.getProperty("mail.smtp.port")), properties.getProperty("userName"), properties.getProperty("password"));//登录邮箱
            transport.send(mailMessage, mailMessage.getRecipients(Message.RecipientType.TO));//发送邮件
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

}
