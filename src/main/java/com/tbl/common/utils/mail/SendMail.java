package com.tbl.common.utils.mail;

import com.tbl.common.config.StaticConfig;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * *邮箱
 */
public class SendMail {

    //host  smtp服务器地址  端口号  port  发送人   user  发送人  password   密码  receive  收件人   subject     主题   context     发送内容
    public static void send(String receive, String subject, String context) throws Exception {

        Properties prop = new Properties();
        prop.setProperty("mail.smtp.host", StaticConfig.getHost());
        prop.setProperty("mail.transport.protocol", "smtp");
        prop.setProperty("mail.smtp.auth", "true");
        prop.setProperty("mail.smtp.port", StaticConfig.getPort());
//        prop.setProperty("mail.smtp.starttls.enable", "true");
        //使用JavaMail发送邮件的5个步骤
        //1、创建session
        Session session = Session.getInstance(prop);
        //开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
        session.setDebug(true);
        //2、通过session得到transport对象
        Transport ts = session.getTransport();
        //3、使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给smtp服务器，用户名和密码都通过验证之后才能够正常发送邮件给收件人。
        ts.connect(StaticConfig.getHost(), StaticConfig.getUser(), StaticConfig.getPassword());
        //4、创建邮件
        Message message = createSimpleMail(session, StaticConfig.getUser(), receive, subject, context);
        //5、发送邮件
        ts.sendMessage(message, message.getAllRecipients());
        ts.close();
    }

    public static MimeMessage createSimpleMail(Session session, String user, String receive, String subject, String context)
            throws Exception {
        //创建邮件对象
        MimeMessage message = new MimeMessage(session);
        //指明邮件的发件人
        message.setFrom(new InternetAddress(user));
        //指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
        //message.setRecipient(Message.RecipientType.TO, new InternetAddress(receive));

        //  收件人（可以增加多个收件人、抄送、密送）
        InternetAddress[] sendTo = InternetAddress.parse(receive);
        message.setRecipients(MimeMessage.RecipientType.TO, sendTo);

        //邮件的标题
        message.setSubject(subject);
        //邮件的文本内容
        message.setContent(context, "text/html;charset=UTF-8");
        //返回创建好的邮件对象
        return message;
    }

}