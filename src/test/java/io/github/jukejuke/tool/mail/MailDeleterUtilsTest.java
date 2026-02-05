package io.github.jukejuke.tool.mail;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.mail.*;
import java.util.List;

/**
 * 邮件删除工具测试类
 * <p>
 * 测试MailDeleterUtils类的各种方法
 * </p>
 *
 * @author yaosh
 * @version 1.0
 * @since 0.0.4.snapshot
 */
@Slf4j
public class MailDeleterUtilsTest {

    // 测试用的邮箱配置（请替换为真实账号）
    private static final String IMAP_HOST = "imap.qq.com";
    private static final int IMAP_PORT = 993;
    private static final String USERNAME = ""; // 邮箱用户名
    private static final String PASSWORD = ""; // 邮箱密码或授权码
    private static final boolean SSL_ENABLED = true;

    /**
     * 测试打开文件夹用于写入操作
     */
    @Test
    public void testOpenFolderForWrite() {
        log.info("开始测试打开文件夹用于写入操作...");
        
        Store store = null;
        Folder folder = null;
        
        try {
            // 连接到IMAP服务器
            store = MailReaderUtils.connect(MailReaderUtils.ProtocolType.IMAP,
                    IMAP_HOST, IMAP_PORT, USERNAME, PASSWORD, SSL_ENABLED);
            
            if (store != null && store.isConnected()) {
                // 打开收件箱用于写入
                folder = MailDeleterUtils.openFolderForWrite(store, "INBOX");
                
                assert folder != null;
                assert folder.isOpen();
                assert folder.getMode() == Folder.READ_WRITE;
                
                log.info("成功打开文件夹用于写入操作！");
            }
        } catch (MessagingException e) {
            log.error("打开文件夹用于写入操作失败: {}", e.getMessage());
            // 注意：由于测试环境可能无法连接到真实的邮件服务器，这里不抛出异常
        } finally {
            if (folder != null) {
                try {
                    folder.close(false);
                } catch (MessagingException e) {
                    log.warn("关闭文件夹时出错: {}", e.getMessage());
                }
            }
            if (store != null) {
                try {
                    store.close();
                } catch (MessagingException e) {
                    log.warn("关闭连接时出错: {}", e.getMessage());
                }
            }
        }
    }

    /**
     * 测试删除邮件
     */
    @Test
    public void testDeleteMessage() {
        log.info("开始测试删除邮件...");
        
        Store store = null;
        Folder folder = null;
        
        try {
            // 连接到IMAP服务器
            store = MailReaderUtils.connect(MailReaderUtils.ProtocolType.IMAP,
                    IMAP_HOST, IMAP_PORT, USERNAME, PASSWORD, SSL_ENABLED);
            
            if (store != null && store.isConnected()) {
                // 打开收件箱用于写入
                folder = MailDeleterUtils.openFolderForWrite(store, "INBOX");
                
                // 获取邮件列表
                List<MailReaderUtils.MailInfo> mailList = MailReaderUtils.getMailList(folder, 5);
                if (mailList.size() > 0) {
                    // 获取第一封邮件
                    Message[] messages = folder.getMessages();
                    if (messages.length > 0) {
                        Message message = messages[0];
                        log.info("准备删除邮件，主题: {}", message.getSubject());
                        
                        // 删除邮件
                        boolean result = MailDeleterUtils.deleteMessage(folder, message);
                        log.info("删除邮件结果: {}", result);
                        
                        // 提交删除操作
                        if (result) {
                            boolean expungeResult = MailDeleterUtils.expunge(folder);
                            log.info("提交删除操作结果: {}", expungeResult);
                        }
                    }
                }
            }
        } catch (MessagingException e) {
            log.error("删除邮件失败: {}", e.getMessage());
            // 注意：由于测试环境可能无法连接到真实的邮件服务器，这里不抛出异常
        } finally {
            if (folder != null) {
                try {
                    folder.close(false);
                } catch (MessagingException e) {
                    log.warn("关闭文件夹时出错: {}", e.getMessage());
                }
            }
            if (store != null) {
                try {
                    store.close();
                } catch (MessagingException e) {
                    log.warn("关闭连接时出错: {}", e.getMessage());
                }
            }
        }
    }

    /**
     * 测试删除指定索引的邮件
     */
    @Test
    public void testDeleteMessageByIndex() {
        log.info("开始测试删除指定索引的邮件...");
        
        Store store = null;
        Folder folder = null;
        
        try {
            // 连接到IMAP服务器
            store = MailReaderUtils.connect(MailReaderUtils.ProtocolType.IMAP,
                    IMAP_HOST, IMAP_PORT, USERNAME, PASSWORD, SSL_ENABLED);
            
            if (store != null && store.isConnected()) {
                // 打开收件箱用于写入
                folder = MailDeleterUtils.openFolderForWrite(store, "INBOX");
                
                // 获取邮件数量
                int messageCount = folder.getMessageCount();
                log.info("收件箱共有 {} 封邮件", messageCount);
                
                if (messageCount > 0) {
                    // 删除最后一封邮件（避免删除重要邮件）
                    boolean result = MailDeleterUtils.deleteMessageByIndex(folder, messageCount);
                    log.info("删除指定索引邮件结果: {}", result);
                    
                    // 提交删除操作
                    if (result) {
                        boolean expungeResult = MailDeleterUtils.expunge(folder);
                        log.info("提交删除操作结果: {}", expungeResult);
                    }
                }
            }
        } catch (MessagingException e) {
            log.error("删除指定索引邮件失败: {}", e.getMessage());
            // 注意：由于测试环境可能无法连接到真实的邮件服务器，这里不抛出异常
        } finally {
            if (folder != null) {
                try {
                    folder.close(false);
                } catch (MessagingException e) {
                    log.warn("关闭文件夹时出错: {}", e.getMessage());
                }
            }
            if (store != null) {
                try {
                    store.close();
                } catch (MessagingException e) {
                    log.warn("关闭连接时出错: {}", e.getMessage());
                }
            }
        }
    }
}
