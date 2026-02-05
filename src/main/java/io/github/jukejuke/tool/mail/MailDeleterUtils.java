package io.github.jukejuke.tool.mail;

import lombok.extern.slf4j.Slf4j;

import javax.mail.*;
import javax.mail.search.SearchTerm;
import java.util.List;

/**
 * 邮件删除工具类
 * <p>
 * 提供删除邮件、删除符合条件的邮件和清空文件夹的功能
 * </p>
 *
 * @author yaosh
 * @version 1.0
 * @since 0.0.4.snapshot
 */
@Slf4j
public class MailDeleterUtils {

    /**
     * 打开文件夹用于写入操作（如删除邮件）
     *
     * @param store Store对象
     * @param folderName 文件夹名称，如"INBOX"
     * @return Folder对象
     * @throws MessagingException 邮件操作异常
     */
    public static Folder openFolderForWrite(Store store, String folderName) throws MessagingException {
        Folder folder = store.getFolder(folderName);
        if (folder == null) {
            throw new MessagingException("文件夹不存在: " + folderName);
        }
        
        // 以读写模式打开文件夹
        folder.open(Folder.READ_WRITE);
        log.info("以读写模式打开文件夹: {}", folderName);
        return folder;
    }

    /**
     * 删除指定的邮件
     *
     * @param folder 邮箱文件夹
     * @param message 要删除的邮件
     * @return 是否删除成功
     * @throws MessagingException 邮件操作异常
     */
    public static boolean deleteMessage(Folder folder, Message message) throws MessagingException {
        if (folder == null || message == null) {
            log.warn("文件夹或邮件对象为null");
            return false;
        }

        if (!folder.isOpen() || folder.getMode() != Folder.READ_WRITE) {
            log.warn("文件夹未以读写模式打开");
            return false;
        }

        try {
            // 标记邮件为已删除
            message.setFlag(Flags.Flag.DELETED, true);
            log.info("标记邮件为已删除，主题: {}", message.getSubject());
            
            return true;
        } catch (MessagingException e) {
            log.error("删除邮件失败: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 删除指定索引的邮件
     *
     * @param folder 邮箱文件夹
     * @param messageIndex 邮件索引（从1开始）
     * @return 是否删除成功
     * @throws MessagingException 邮件操作异常
     */
    public static boolean deleteMessageByIndex(Folder folder, int messageIndex) throws MessagingException {
        if (folder == null) {
            log.warn("文件夹对象为null");
            return false;
        }

        if (!folder.isOpen() || folder.getMode() != Folder.READ_WRITE) {
            log.warn("文件夹未以读写模式打开");
            return false;
        }

        int messageCount = folder.getMessageCount();
        if (messageIndex < 1 || messageIndex > messageCount) {
            log.warn("邮件索引超出范围: {}，文件夹共有 {} 封邮件", messageIndex, messageCount);
            return false;
        }

        try {
            // 获取指定索引的邮件
            Message message = folder.getMessage(messageIndex);
            return deleteMessage(folder, message);
        } catch (MessagingException e) {
            log.error("删除邮件失败，索引: {}", messageIndex, e);
            return false;
        }
    }

    /**
     * 删除指定索引范围的邮件
     *
     * @param folder 邮箱文件夹
     * @param startIndex 开始索引（从1开始）
     * @param endIndex 结束索引（包含）
     * @return 删除的邮件数量
     * @throws MessagingException 邮件操作异常
     */
    public static int deleteMessagesByIndexRange(Folder folder, int startIndex, int endIndex) throws MessagingException {
        if (folder == null) {
            log.warn("文件夹对象为null");
            return 0;
        }

        if (!folder.isOpen() || folder.getMode() != Folder.READ_WRITE) {
            log.warn("文件夹未以读写模式打开");
            return 0;
        }

        int messageCount = folder.getMessageCount();
        if (startIndex < 1 || endIndex > messageCount || startIndex > endIndex) {
            log.warn("邮件索引范围无效: {}-{}，文件夹共有 {} 封邮件", startIndex, endIndex, messageCount);
            return 0;
        }

        try {
            // 获取指定范围的邮件
            Message[] messages = folder.getMessages(startIndex, endIndex);
            int deletedCount = 0;

            for (Message message : messages) {
                if (deleteMessage(folder, message)) {
                    deletedCount++;
                }
            }

            log.info("删除了 {} 封邮件，范围: {}-{}", deletedCount, startIndex, endIndex);
            return deletedCount;
        } catch (MessagingException e) {
            log.error("删除邮件范围失败: {}-{}", startIndex, endIndex, e);
            return 0;
        }
    }

    /**
     * 删除符合搜索条件的邮件
     *
     * @param folder 邮箱文件夹
     * @param searchTerm 搜索条件
     * @return 删除的邮件数量
     * @throws MessagingException 邮件操作异常
     */
    public static int deleteMessagesBySearchTerm(Folder folder, SearchTerm searchTerm) throws MessagingException {
        if (folder == null || searchTerm == null) {
            log.warn("文件夹或搜索条件为null");
            return 0;
        }

        if (!folder.isOpen() || folder.getMode() != Folder.READ_WRITE) {
            log.warn("文件夹未以读写模式打开");
            return 0;
        }

        try {
            // 搜索符合条件的邮件
            Message[] messages = folder.search(searchTerm);
            log.info("搜索到 {} 封符合条件的邮件", messages.length);

            int deletedCount = 0;
            for (Message message : messages) {
                if (deleteMessage(folder, message)) {
                    deletedCount++;
                }
            }

            log.info("删除了 {} 封符合条件的邮件", deletedCount);
            return deletedCount;
        } catch (MessagingException e) {
            log.error("删除符合条件的邮件失败: {}", e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 删除文件夹中的所有邮件
     *
     * @param folder 邮箱文件夹
     * @return 删除的邮件数量
     * @throws MessagingException 邮件操作异常
     */
    public static int deleteAllMessages(Folder folder) throws MessagingException {
        if (folder == null) {
            log.warn("文件夹对象为null");
            return 0;
        }

        if (!folder.isOpen() || folder.getMode() != Folder.READ_WRITE) {
            log.warn("文件夹未以读写模式打开");
            return 0;
        }

        int messageCount = folder.getMessageCount();
        if (messageCount == 0) {
            log.info("文件夹为空，无需删除");
            return 0;
        }

        try {
            // 获取所有邮件
            Message[] messages = folder.getMessages();
            int deletedCount = 0;

            for (Message message : messages) {
                if (deleteMessage(folder, message)) {
                    deletedCount++;
                }
            }

            log.info("删除了文件夹中的所有 {} 封邮件", deletedCount);
            return deletedCount;
        } catch (MessagingException e) {
            log.error("删除文件夹中所有邮件失败: {}", e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 清空垃圾箱
     *
     * @param store Store对象
     * @param trashFolderName 垃圾箱文件夹名称，如"Trash"或"Deleted Items"
     * @return 是否清空成功
     * @throws MessagingException 邮件操作异常
     */
    public static boolean emptyTrash(Store store, String trashFolderName) throws MessagingException {
        Folder trashFolder = null;
        try {
            // 打开垃圾箱
            trashFolder = openFolderForWrite(store, trashFolderName);
            
            // 删除所有邮件
            int deletedCount = deleteAllMessages(trashFolder);
            log.info("清空垃圾箱，删除了 {} 封邮件", deletedCount);
            
            return true;
        } catch (MessagingException e) {
            log.error("清空垃圾箱失败: {}", e.getMessage(), e);
            return false;
        } finally {
            if (trashFolder != null && trashFolder.isOpen()) {
                try {
                    trashFolder.close(true); // true表示 expunge，即永久删除标记为已删除的邮件
                    log.info("关闭垃圾箱并永久删除已标记的邮件");
                } catch (MessagingException e) {
                    log.warn("关闭垃圾箱时出错: {}", e.getMessage());
                }
            }
        }
    }

    /**
     * 提交删除操作（永久删除标记为已删除的邮件）
     *
     * @param folder 邮箱文件夹
     * @return 是否提交成功
     * @throws MessagingException 邮件操作异常
     */
    public static boolean expunge(Folder folder) throws MessagingException {
        if (folder == null) {
            log.warn("文件夹对象为null");
            return false;
        }

        if (!folder.isOpen()) {
            log.warn("文件夹未打开");
            return false;
        }

        try {
            // 提交删除操作，永久删除标记为已删除的邮件
            folder.expunge();
            log.info("提交删除操作，永久删除标记为已删除的邮件");
            return true;
        } catch (MessagingException e) {
            log.error("提交删除操作失败: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 关闭文件夹并提交删除操作
     *
     * @param folder 邮箱文件夹
     * @param expunge 是否提交删除操作（永久删除标记为已删除的邮件）
     * @throws MessagingException 邮件操作异常
     */
    public static void closeFolder(Folder folder, boolean expunge) throws MessagingException {
        if (folder != null && folder.isOpen()) {
            folder.close(expunge);
            log.info("关闭文件夹{}", expunge ? "并提交删除操作" : "");
        }
    }
}
