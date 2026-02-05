package io.github.jukejuke.tool.mail;

import lombok.extern.slf4j.Slf4j;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.Date;
import java.util.Properties;

/**
 * 邮件发送工具类
 * <p>
 * 提供发送简单文本邮件、HTML格式邮件和带附件邮件的功能
 * </p>
 * 
 * @author yaosh
 * @version 1.0
 * @since 0.0.4.snapshot
 */
@Slf4j
public class MailUtils {

    /**
     * 发送简单文本邮件
     *
     * @param host     邮件服务器主机地址
     * @param port     邮件服务器端口
     * @param username 发件人邮箱用户名
     * @param password 发件人邮箱密码或授权码
     * @param from     发件人邮箱地址
     * @param to       收件人邮箱地址，多个地址用逗号分隔
     * @param subject  邮件主题
     * @param content  邮件内容
     * @param ssl      是否使用SSL加密
     * @return 是否发送成功
     */
    public static boolean sendTextMail(String host, int port, String username, String password,
                                      String from, String to, String subject, String content, boolean ssl) {
        return sendMail(host, port, username, password, from, to, subject, content, null, ssl, false);
    }

    /**
     * 发送HTML格式邮件
     *
     * @param host     邮件服务器主机地址
     * @param port     邮件服务器端口
     * @param username 发件人邮箱用户名
     * @param password 发件人邮箱密码或授权码
     * @param from     发件人邮箱地址
     * @param to       收件人邮箱地址，多个地址用逗号分隔
     * @param subject  邮件主题
     * @param content  HTML格式的邮件内容
     * @param ssl      是否使用SSL加密
     * @return 是否发送成功
     */
    public static boolean sendHtmlMail(String host, int port, String username, String password,
                                      String from, String to, String subject, String content, boolean ssl) {
        return sendMail(host, port, username, password, from, to, subject, content, null, ssl, true);
    }

    /**
     * 发送带附件的邮件
     *
     * @param host     邮件服务器主机地址
     * @param port     邮件服务器端口
     * @param username 发件人邮箱用户名
     * @param password 发件人邮箱密码或授权码
     * @param from     发件人邮箱地址
     * @param to       收件人邮箱地址，多个地址用逗号分隔
     * @param subject  邮件主题
     * @param content  邮件内容
     * @param attachments 附件文件数组
     * @param ssl      是否使用SSL加密
     * @param isHtml   是否为HTML格式邮件
     * @return 是否发送成功
     */
    public static boolean sendMailWithAttachments(String host, int port, String username, String password,
                                                String from, String to, String subject, String content,
                                                File[] attachments, boolean ssl, boolean isHtml) {
        return sendMail(host, port, username, password, from, to, subject, content, attachments, ssl, isHtml);
    }

    /**
     * 发送邮件的核心方法
     *
     * @param host     邮件服务器主机地址
     * @param port     邮件服务器端口
     * @param username 发件人邮箱用户名
     * @param password 发件人邮箱密码或授权码
     * @param from     发件人邮箱地址
     * @param to       收件人邮箱地址，多个地址用逗号分隔
     * @param subject  邮件主题
     * @param content  邮件内容
     * @param attachments 附件文件数组
     * @param ssl      是否使用SSL加密
     * @param isHtml   是否为HTML格式邮件
     * @return 是否发送成功
     */
    private static boolean sendMail(String host, int port, String username, String password,
                                   String from, String to, String subject, String content,
                                   File[] attachments, boolean ssl, boolean isHtml) {
        try {
            // 创建邮件会话
            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", String.valueOf(port));
            props.put("mail.smtp.auth", "true");
            
            // 配置SSL或STARTTLS
            if (ssl) {
                // 端口465使用直接SSL
                if (port == 465) {
                    props.put("mail.smtp.ssl.enable", "true");
                    props.put("mail.smtp.ssl.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                } 
                // 端口587使用STARTTLS
                else if (port == 587) {
                    props.put("mail.smtp.starttls.enable", "true");
                    props.put("mail.smtp.starttls.required", "true");
                }
                // 其他端口默认使用SSL
                else {
                    props.put("mail.smtp.ssl.enable", "true");
                    props.put("mail.smtp.ssl.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                }
            }
            
            // 创建认证对象
            Authenticator authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            };
            
            // 获取会话
            Session session = Session.getInstance(props, authenticator);
            session.setDebug(false);
            
            // 创建邮件
            MimeMessage message = new MimeMessage(session);
            
            // 设置发件人
            message.setFrom(new InternetAddress(from));
            
            // 设置收件人
            InternetAddress[] toAddresses = InternetAddress.parse(to);
            message.setRecipients(Message.RecipientType.TO, toAddresses);
            
            // 设置邮件主题
            message.setSubject(subject, "UTF-8");
            
            // 设置邮件发送时间
            message.setSentDate(new Date());
            
            // 创建邮件内容
            Multipart multipart = new MimeMultipart();
            
            // 创建正文部分
            BodyPart contentPart = new MimeBodyPart();
            if (isHtml) {
                contentPart.setContent(content, "text/html; charset=UTF-8");
            } else {
                contentPart.setText(content);
            }
            multipart.addBodyPart(contentPart);
            
            // 添加附件
            if (attachments != null && attachments.length > 0) {
                for (File attachment : attachments) {
                    if (attachment.exists() && attachment.isFile()) {
                        MimeBodyPart attachmentPart = new MimeBodyPart();
                        attachmentPart.attachFile(attachment);
                        attachmentPart.setFileName(MimeUtility.encodeText(attachment.getName(), "UTF-8", "B"));
                        multipart.addBodyPart(attachmentPart);
                    }
                }
            }
            
            // 设置邮件内容
            message.setContent(multipart);
            
            // 发送邮件
            Transport.send(message);
            
            log.info("邮件发送成功，发件人: {}, 收件人: {}, 主题: {}", from, to, subject);
            return true;
        } catch (Exception e) {
            log.error("邮件发送失败，发件人: {}, 收件人: {}, 主题: {}", from, to, subject, e);
            return false;
        }
    }
}
