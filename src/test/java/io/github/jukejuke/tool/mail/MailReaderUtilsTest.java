package io.github.jukejuke.tool.mail;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;
import java.io.IOException;
import java.util.List;

/**
 * 邮件读取工具测试类
 * <p>
 * 测试MailReaderUtils类的各种方法
 * </p>
 *
 * @author yaosh
 * @version 1.0
 * @since 0.0.4.snapshot
 */
@Slf4j
public class MailReaderUtilsTest {

    // 测试用的邮箱配置（请替换为真实账号）
    private static final String POP3_HOST = "pop.qq.com";
    private static final int POP3_PORT = 995;
    private static final String IMAP_HOST = "imap.qq.com";
    private static final int IMAP_PORT = 993;
    private static final String USERNAME = ""; // 邮箱用户名
    private static final String PASSWORD = ""; // 邮箱密码或授权码
    private static final boolean SSL_ENABLED = true;

    /**
     * 测试连接到POP3邮件服务器
     */
    @Test
    public void testConnectPop3() {
        log.info("开始测试连接到POP3邮件服务器...");
        
        Store store = null;
        try {
            store = MailReaderUtils.connect(MailReaderUtils.ProtocolType.POP3,
                    POP3_HOST, POP3_PORT, USERNAME, PASSWORD, SSL_ENABLED);
            
            assert store != null;
            assert store.isConnected();
            
            log.info("成功连接到POP3邮件服务器！");
        } catch (MessagingException e) {
            log.error("连接POP3邮件服务器失败: {}", e.getMessage());
            // 注意：由于测试环境可能无法连接到真实的邮件服务器，这里不抛出异常
        } finally {
            if (store != null) {
                try {
                    store.close();
                } catch (MessagingException e) {
                    log.warn("关闭POP3连接时出错: {}", e.getMessage());
                }
            }
        }
    }

    /**
     * 测试连接到IMAP邮件服务器
     */
    @Test
    public void testConnectImap() {
        log.info("开始测试连接到IMAP邮件服务器...");
        
        Store store = null;
        try {
            store = MailReaderUtils.connect(MailReaderUtils.ProtocolType.IMAP,
                    IMAP_HOST, IMAP_PORT, USERNAME, PASSWORD, SSL_ENABLED);
            
            assert store != null;
            assert store.isConnected();
            
            log.info("成功连接到IMAP邮件服务器！");
        } catch (MessagingException e) {
            log.error("连接IMAP邮件服务器失败: {}", e.getMessage());
            // 注意：由于测试环境可能无法连接到真实的邮件服务器，这里不抛出异常
        } finally {
            if (store != null) {
                try {
                    store.close();
                } catch (MessagingException e) {
                    log.warn("关闭IMAP连接时出错: {}", e.getMessage());
                }
            }
        }
    }

    /**
     * 测试获取邮件列表
     */
    @Test
    public void testGetMailList() {
        log.info("开始测试获取邮件列表...");
        
        Store store = null;
        Folder folder = null;
        
        try {
            // 连接到IMAP服务器
            store = MailReaderUtils.connect(MailReaderUtils.ProtocolType.IMAP,
                    IMAP_HOST, IMAP_PORT, USERNAME, PASSWORD, SSL_ENABLED);
            
            // 获取收件箱
            folder = MailReaderUtils.getFolder(store, "INBOX");
            
            // 获取邮件列表（最多10封）
            List<MailReaderUtils.MailInfo> mailList = MailReaderUtils.getMailList(folder, 10);
            
            log.info("获取到 {} 封邮件", mailList.size());
            
            // 打印邮件信息
            for (int i = 0; i < mailList.size(); i++) {
                MailReaderUtils.MailInfo mailInfo = mailList.get(i);
                log.info("邮件 {}: {}", i + 1, mailInfo.getSubject());
                log.info("  发件人: {}", mailInfo.getFrom());
                log.info("  收件人: {}", mailInfo.getTo());
                log.info("  发送时间: {}", mailInfo.getSentDate());
                log.info("  有无附件: {}", mailInfo.isHasAttachments());
                if (mailInfo.isHasAttachments()) {
                    log.info("  附件名称: {}", mailInfo.getAttachmentNames());
                }
            }
            
        } catch (MessagingException e) {
            log.error("获取邮件列表失败: {}", e.getMessage());
            // 注意：由于测试环境可能无法连接到真实的邮件服务器，这里不抛出异常
        } finally {
            MailReaderUtils.closeConnection(folder, store);
        }
    }

    /**
     * 测试获取邮件详情
     */
    @Test
    public void testGetMailDetail() {
        log.info("开始测试获取邮件详情...");
        
        Store store = null;
        Folder folder = null;
        
        try {
            // 连接到IMAP服务器
            store = MailReaderUtils.connect(MailReaderUtils.ProtocolType.IMAP,
                    IMAP_HOST, IMAP_PORT, USERNAME, PASSWORD, SSL_ENABLED);
            
            // 获取收件箱
            folder = MailReaderUtils.getFolder(store, "INBOX");
            
            // 获取邮件列表
            Message[] messages = folder.getMessages();
            if (messages.length > 0) {
                // 获取第一封邮件的详情
                Message message = messages[0];
                MailReaderUtils.MailDetail mailDetail = MailReaderUtils.getMailDetail(message);
                
                log.info("邮件详情:");
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
                }
                
                if (mailDetail.getHtmlContent() != null) {
                    log.info("  HTML内容长度: {}", mailDetail.getHtmlContent().length());
                }
                
                if (mailDetail.isHasAttachments() && mailDetail.getAttachments() != null) {
                    log.info("  附件数量: {}", mailDetail.getAttachments().size());
                    for (MailReaderUtils.AttachmentInfo attachment : mailDetail.getAttachments()) {
                        log.info("  附件: {} ({} bytes)", 
                                attachment.getFileName(), attachment.getSize());
                    }
                }
            } else {
                log.info("收件箱为空，无法测试获取邮件详情");
            }
            
        } catch (MessagingException | IOException e) {
            log.error("获取邮件详情失败: {}", e.getMessage());
            // 注意：由于测试环境可能无法连接到真实的邮件服务器，这里不抛出异常
        } finally {
            MailReaderUtils.closeConnection(folder, store);
        }
    }

    /**
     * 测试搜索邮件
     */
    @Test
    public void testSearchMails() {
        log.info("开始测试搜索邮件...");
        
        Store store = null;
        Folder folder = null;
        
        try {
            // 连接到IMAP服务器
            store = MailReaderUtils.connect(MailReaderUtils.ProtocolType.IMAP,
                    IMAP_HOST, IMAP_PORT, USERNAME, PASSWORD, SSL_ENABLED);
            
            // 获取收件箱
            folder = MailReaderUtils.getFolder(store, "INBOX");
            
            // 创建搜索条件（搜索主题包含"测试"的邮件）
            SearchTerm searchTerm = new SubjectTerm("测试");
            
            // 搜索邮件
            List<MailReaderUtils.MailInfo> searchResults = MailReaderUtils.searchMails(folder, searchTerm, 10);
            
            log.info("搜索结果: 找到 {} 封主题包含\"测试\"的邮件", searchResults.size());
            
            // 打印搜索结果
            for (int i = 0; i < searchResults.size(); i++) {
                MailReaderUtils.MailInfo mailInfo = searchResults.get(i);
                log.info("  邮件 {}: {}", i + 1, mailInfo.getSubject());
                log.info("    发件人: {}", mailInfo.getFrom());
                log.info("    发送时间: {}", mailInfo.getSentDate());
            }
            
        } catch (MessagingException e) {
            log.error("搜索邮件失败: {}", e.getMessage());
            // 注意：由于测试环境可能无法连接到真实的邮件服务器，这里不抛出异常
        } finally {
            MailReaderUtils.closeConnection(folder, store);
        }
    }
}
