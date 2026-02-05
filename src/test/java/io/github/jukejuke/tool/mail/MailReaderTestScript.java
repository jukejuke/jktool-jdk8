package io.github.jukejuke.tool.mail;

import lombok.extern.slf4j.Slf4j;

import javax.mail.*;
import java.io.IOException;
import java.util.List;

/**
 * 邮件读取测试脚本
 * <p>
 * 用于测试MailReaderUtils的功能，特别是getMailDetail方法
 * </p>
 *
 * @author yaosh
 * @version 1.0
 * @since 0.0.4.snapshot
 */
@Slf4j
public class MailReaderTestScript {

    // 测试用的邮箱配置
    private static final String IMAP_HOST = "imap.qq.com";
    private static final int IMAP_PORT = 993;
    private static final String USERNAME = ""; // 邮箱用户名
    private static final String PASSWORD = ""; // 邮箱密码或授权码
    private static final boolean SSL_ENABLED = true;

    public static void main(String[] args) {
        log.info("开始测试邮件读取功能...");

        Store store = null;
        Folder folder = null;

        try {
            // 连接到IMAP服务器
            store = MailReaderUtils.connect(MailReaderUtils.ProtocolType.IMAP,
                    IMAP_HOST, IMAP_PORT, USERNAME, PASSWORD, SSL_ENABLED);

            if (store == null || !store.isConnected()) {
                log.error("无法连接到邮件服务器");
                return;
            }

            log.info("成功连接到邮件服务器");

            // 获取收件箱
            folder = MailReaderUtils.getFolder(store, "INBOX");

            // 获取邮件列表
            List<MailReaderUtils.MailInfo> mailList = MailReaderUtils.getMailList(folder, 5);
            log.info("获取到 {} 封邮件", mailList.size());

            if (mailList.size() > 0) {
                // 获取第一封邮件
                Message[] messages = folder.getMessages();
                if (messages.length > 0) {
                    Message message = messages[0];
                    log.info("测试获取邮件详情，邮件主题: {}", mailList.get(0).getSubject());

                    // 获取邮件详情
                    MailReaderUtils.MailDetail mailDetail = MailReaderUtils.getMailDetail(message);

                    // 打印邮件详情
                    log.info("邮件详情获取完成:");
                    log.info("  主题: {}", mailDetail.getSubject());
                    log.info("  发件人: {}", mailDetail.getFrom());
                    log.info("  收件人: {}", mailDetail.getTo());
                    log.info("  发送时间: {}", mailDetail.getSentDate());
                    log.info("  有无附件: {}", mailDetail.isHasAttachments());

                    if (mailDetail.getTextContent() != null) {
                        log.info("  文本内容长度: {}", mailDetail.getTextContent().length());
                        if (mailDetail.getTextContent().length() > 0) {
                            log.info("  文本内容前100字符: {}",
                                    mailDetail.getTextContent().substring(0, Math.min(100, mailDetail.getTextContent().length())));
                        }
                    } else {
                        log.warn("  文本内容为null");
                    }

                    if (mailDetail.getHtmlContent() != null) {
                        log.info("  HTML内容长度: {}", mailDetail.getHtmlContent().length());
                        if (mailDetail.getHtmlContent().length() > 0) {
                            log.info("  HTML内容前100字符: {}",
                                    mailDetail.getHtmlContent().substring(0, Math.min(100, mailDetail.getHtmlContent().length())));
                        }
                    } else {
                        log.warn("  HTML内容为null");
                    }
                }
            } else {
                log.warn("收件箱为空，无法测试获取邮件详情");
            }

        } catch (MessagingException | IOException e) {
            log.error("测试邮件读取功能失败: {}", e.getMessage(), e);
        } finally {
            // 关闭连接
            MailReaderUtils.closeConnection(folder, store);
        }

        log.info("邮件读取功能测试完成!");
    }
}
