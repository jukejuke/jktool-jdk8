package io.github.jukejuke.tool.mail;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * 邮件发送工具测试类
 * <p>
 * 使用QQ邮箱进行测试，需要注意：
 * 1. QQ邮箱需要开启SMTP服务
 * 2. 需要使用QQ邮箱的授权码，而不是登录密码
 * </p>
 * 
 * @author yaosh
 * @version 1.0
 * @since 0.0.4.snapshot
 */
@Slf4j
public class MailUtilsTest {

    // QQ邮箱SMTP服务器配置
    private static final String SMTP_HOST = "smtp.qq.com";
    private static final int SMTP_PORT = 465; // QQ邮箱SMTP端口（SSL）
    private static final boolean SSL_ENABLED = true;

    // 测试用的邮箱账号（请替换为真实账号）
    private static final String FROM_EMAIL = "your_qq_email@qq.com"; // 发件人QQ邮箱
    private static final String USERNAME = "your_qq_email@qq.com"; // 用户名（通常与发件人邮箱相同）
    private static final String PASSWORD = "your_qq_authorization_code"; // QQ邮箱授权码，不是登录密码

    // 收件人邮箱（请替换为真实账号）
    private static final String TO_EMAIL = "recipient_email@example.com";

    /**
     * 测试发送简单文本邮件
     */
    @Test
    public void testSendTextMail() {
        log.info("开始测试发送简单文本邮件...");
        
        String subject = "测试邮件 - 简单文本";
        String content = "这是一封测试邮件，使用JK-Tool的邮件发送工具发送。\n\n测试内容：简单文本邮件\n发送时间：" + new java.util.Date();
        
        // 注意：在运行此测试前，请确保已替换上述常量为真实的邮箱信息
        if (FROM_EMAIL.equals("your_qq_email@qq.com") || PASSWORD.equals("your_qq_authorization_code")) {
            log.warn("请先替换测试类中的邮箱账号和授权码为真实信息");
            return;
        }
        
        boolean result = MailUtils.sendTextMail(
                SMTP_HOST,
                SMTP_PORT,
                USERNAME,
                PASSWORD,
                FROM_EMAIL,
                TO_EMAIL,
                subject,
                content,
                SSL_ENABLED
        );
        
        log.info("简单文本邮件发送结果：{}", result);
    }

    /**
     * 测试发送HTML格式邮件
     */
    @Test
    public void testSendHtmlMail() {
        log.info("开始测试发送HTML格式邮件...");
        
        String subject = "测试邮件 - HTML格式";
        String content = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>测试邮件</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h1>测试邮件</h1>\n" +
                "    <p>这是一封测试邮件，使用JK-Tool的邮件发送工具发送。</p>\n" +
                "    <p>测试内容：HTML格式邮件</p>\n" +
                "    <p>发送时间：" + new java.util.Date() + "</p>\n" +
                "    <p><a href=\"https://github.com/jukejuke/jk-tool\">JK-Tool GitHub地址</a></p>\n" +
                "    <table border=\"1\" cellpadding=\"5\" cellspacing=\"0\">\n" +
                "        <tr>\n" +
                "            <th>测试项</th>\n" +
                "            <th>结果</th>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td>HTML格式</td>\n" +
                "            <td>支持</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td>链接</td>\n" +
                "            <td>支持</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td>表格</td>\n" +
                "            <td>支持</td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "</body>\n" +
                "</html>";
        
        // 注意：在运行此测试前，请确保已替换上述常量为真实的邮箱信息
        if (FROM_EMAIL.equals("your_qq_email@qq.com") || PASSWORD.equals("your_qq_authorization_code")) {
            log.warn("请先替换测试类中的邮箱账号和授权码为真实信息");
            return;
        }
        
        boolean result = MailUtils.sendHtmlMail(
                SMTP_HOST,
                SMTP_PORT,
                USERNAME,
                PASSWORD,
                FROM_EMAIL,
                TO_EMAIL,
                subject,
                content,
                SSL_ENABLED
        );
        
        log.info("HTML格式邮件发送结果：{}", result);
    }

    /**
     * 测试发送带附件的邮件
     */
    @Test
    public void testSendMailWithAttachments() {
        log.info("开始测试发送带附件的邮件...");
        
        String subject = "测试邮件 - 带附件";
        String content = "这是一封测试邮件，使用JK-Tool的邮件发送工具发送。\n\n测试内容：带附件的邮件\n发送时间：" + new java.util.Date();
        
        // 创建测试附件（使用项目中的README.md文件作为附件）
        File attachment = new File("README.md");
        File[] attachments = null;
        
        if (attachment.exists()) {
            attachments = new File[]{attachment};
            log.info("使用README.md作为测试附件");
        } else {
            log.warn("README.md文件不存在，将发送无附件邮件");
        }
        
        // 注意：在运行此测试前，请确保已替换上述常量为真实的邮箱信息
        if (FROM_EMAIL.equals("your_qq_email@qq.com") || PASSWORD.equals("your_qq_authorization_code")) {
            log.warn("请先替换测试类中的邮箱账号和授权码为真实信息");
            return;
        }
        
        boolean result = MailUtils.sendMailWithAttachments(
                SMTP_HOST,
                SMTP_PORT,
                USERNAME,
                PASSWORD,
                FROM_EMAIL,
                TO_EMAIL,
                subject,
                content,
                attachments,
                SSL_ENABLED,
                false // 非HTML格式
        );
        
        log.info("带附件邮件发送结果：{}", result);
    }
}
