package io.github.jukejuke.tool.mail;

import lombok.extern.slf4j.Slf4j;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.search.SearchTerm;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 邮件读取工具类
 * <p>
 * 提供连接邮件服务器、读取邮件列表和邮件详情的功能
 * </p>
 *
 * @author yaosh
 * @version 1.0
 * @since 0.0.4.snapshot
 */
@Slf4j
public class MailReaderUtils {

    /**
     * 邮件协议类型
     */
    public enum ProtocolType {
        POP3,
        IMAP
    }

    /**
     * 邮件信息类，用于存储邮件的基本信息
     */
    public static class MailInfo {
        private String messageId;
        private String from;
        private String to;
        private String subject;
        private Date sentDate;
        private Date receivedDate;
        private boolean hasAttachments;
        private List<String> attachmentNames;
        private String contentType;

        // Getters and Setters
        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public Date getSentDate() {
            return sentDate;
        }

        public void setSentDate(Date sentDate) {
            this.sentDate = sentDate;
        }

        public Date getReceivedDate() {
            return receivedDate;
        }

        public void setReceivedDate(Date receivedDate) {
            this.receivedDate = receivedDate;
        }

        public boolean isHasAttachments() {
            return hasAttachments;
        }

        public void setHasAttachments(boolean hasAttachments) {
            this.hasAttachments = hasAttachments;
        }

        public List<String> getAttachmentNames() {
            return attachmentNames;
        }

        public void setAttachmentNames(List<String> attachmentNames) {
            this.attachmentNames = attachmentNames;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        @Override
        public String toString() {
            return "MailInfo{" +
                    "messageId='" + messageId + '\'' +
                    ", from='" + from + '\'' +
                    ", to='" + to + '\'' +
                    ", subject='" + subject + '\'' +
                    ", sentDate=" + sentDate +
                    ", receivedDate=" + receivedDate +
                    ", hasAttachments=" + hasAttachments +
                    ", attachmentNames=" + attachmentNames +
                    ", contentType='" + contentType + '\'' +
                    '}';
        }
    }

    /**
     * 邮件详情类，包含邮件的完整信息
     */
    public static class MailDetail extends MailInfo {
        private String textContent;
        private String htmlContent;
        private List<AttachmentInfo> attachments;

        // Getters and Setters
        public String getTextContent() {
            return textContent;
        }

        public void setTextContent(String textContent) {
            this.textContent = textContent;
        }

        public String getHtmlContent() {
            return htmlContent;
        }

        public void setHtmlContent(String htmlContent) {
            this.htmlContent = htmlContent;
        }

        public List<AttachmentInfo> getAttachments() {
            return attachments;
        }

        public void setAttachments(List<AttachmentInfo> attachments) {
            this.attachments = attachments;
        }

        @Override
        public String toString() {
            return "MailDetail{" +
                    "textContent='" + (textContent != null ? textContent.substring(0, Math.min(100, textContent.length())) + "..." : "null") + '\'' +
                    ", htmlContent='" + (htmlContent != null ? htmlContent.substring(0, Math.min(100, htmlContent.length())) + "..." : "null") + '\'' +
                    ", attachments=" + attachments +
                    "} " + super.toString();
        }
    }

    /**
     * 附件信息类
     */
    public static class AttachmentInfo {
        private String fileName;
        private String contentType;
        private long size;
        private MimeBodyPart bodyPart;

        // Getters and Setters
        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public MimeBodyPart getBodyPart() {
            return bodyPart;
        }

        public void setBodyPart(MimeBodyPart bodyPart) {
            this.bodyPart = bodyPart;
        }

        @Override
        public String toString() {
            return "AttachmentInfo{" +
                    "fileName='" + fileName + '\'' +
                    ", contentType='" + contentType + '\'' +
                    ", size=" + size +
                    '}';
        }
    }

    /**
     * 连接到邮件服务器并获取Store对象
     *
     * @param protocol 邮件协议类型（POP3或IMAP）
     * @param host 邮件服务器主机地址
     * @param port 邮件服务器端口
     * @param username 邮箱用户名
     * @param password 邮箱密码或授权码
     * @param ssl 是否使用SSL加密
     * @return Store对象
     * @throws MessagingException 邮件操作异常
     */
    public static Store connect(ProtocolType protocol, String host, int port,
                               String username, String password, boolean ssl) throws MessagingException {
        log.info("开始连接到邮件服务器: {}://{}:{}", protocol.name(), host, port);

        // 创建会话属性
        Properties props = new Properties();
        String protocolName = protocol.name().toLowerCase();
        
        // 设置基本属性
        props.put("mail." + protocolName + ".host", host);
        props.put("mail." + protocolName + ".port", String.valueOf(port));
        props.put("mail." + protocolName + ".auth", "true");
        
        // 配置SSL
        if (ssl) {
            props.put("mail." + protocolName + ".ssl.enable", "true");
            props.put("mail." + protocolName + ".ssl.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        }

        // 创建会话
        Session session = Session.getInstance(props);
        session.setDebug(false);

        // 连接到服务器
        Store store = session.getStore(protocolName);
        store.connect(host, port, username, password);

        log.info("成功连接到邮件服务器: {}://{}:{}", protocol.name(), host, port);
        return store;
    }

    /**
     * 获取邮箱文件夹
     *
     * @param store Store对象
     * @param folderName 文件夹名称，如"INBOX"
     * @return Folder对象
     * @throws MessagingException 邮件操作异常
     */
    public static Folder getFolder(Store store, String folderName) throws MessagingException {
        Folder folder = store.getFolder(folderName);
        if (folder == null) {
            throw new MessagingException("文件夹不存在: " + folderName);
        }
        folder.open(Folder.READ_ONLY);
        return folder;
    }

    /**
     * 获取邮箱中的邮件列表
     *
     * @param folder 邮箱文件夹
     * @param maxCount 最大获取数量，0表示获取所有
     * @return 邮件信息列表
     * @throws MessagingException 邮件操作异常
     */
    public static List<MailInfo> getMailList(Folder folder, int maxCount) throws MessagingException {
        List<MailInfo> mailList = new ArrayList<>();
        
        if (!folder.isOpen()) {
            folder.open(Folder.READ_ONLY);
        }

        Message[] messages = folder.getMessages();
        log.info("邮箱文件夹 {} 中共有 {} 封邮件", folder.getName(), messages.length);

        int startIndex = 0;
        int endIndex = messages.length;
        
        if (maxCount > 0 && maxCount < messages.length) {
            startIndex = messages.length - maxCount;
        }

        for (int i = startIndex; i < endIndex; i++) {
            Message message = messages[i];
            MailInfo mailInfo = parseMailInfo(message);
            mailList.add(mailInfo);
        }

        return mailList;
    }

    /**
     * 解析邮件基本信息
     *
     * @param message 邮件消息对象
     * @return 邮件信息对象
     * @throws MessagingException 邮件操作异常
     */
    private static MailInfo parseMailInfo(Message message) throws MessagingException {
        MailInfo mailInfo = new MailInfo();
        
        // 邮件ID
        try {
            // 尝试使用反射获取邮件ID，兼容不同版本的JavaMail API
            try {
                java.lang.reflect.Method method = message.getClass().getMethod("getMessageID");
                Object result = method.invoke(message);
                if (result != null) {
                    mailInfo.setMessageId(result.toString());
                }
            } catch (Exception e) {
                // 尝试使用getMessageId()方法
                try {
                    java.lang.reflect.Method method = message.getClass().getMethod("getMessageId");
                    Object result = method.invoke(message);
                    if (result != null) {
                        mailInfo.setMessageId(result.toString());
                    }
                } catch (Exception ex) {
                    // 如果都失败了，就不设置邮件ID
                    log.warn("获取邮件ID失败: {}", ex.getMessage());
                }
            }
        } catch (Exception e) {
            // 如果反射也失败了，就不设置邮件ID
            log.warn("获取邮件ID失败: {}", e.getMessage());
        }
        
        // 发件人
        Address[] fromAddresses = message.getFrom();
        if (fromAddresses != null && fromAddresses.length > 0) {
            if (fromAddresses[0] instanceof InternetAddress) {
                mailInfo.setFrom(((InternetAddress) fromAddresses[0]).getAddress());
            } else {
                mailInfo.setFrom(fromAddresses[0].toString());
            }
        }
        
        // 收件人
        Address[] toAddresses = message.getRecipients(Message.RecipientType.TO);
        if (toAddresses != null && toAddresses.length > 0) {
            StringBuilder toBuilder = new StringBuilder();
            for (int i = 0; i < toAddresses.length; i++) {
                if (i > 0) {
                    toBuilder.append(", ");
                }
                if (toAddresses[i] instanceof InternetAddress) {
                    toBuilder.append(((InternetAddress) toAddresses[i]).getAddress());
                } else {
                    toBuilder.append(toAddresses[i].toString());
                }
            }
            mailInfo.setTo(toBuilder.toString());
        }
        
        // 主题
        try {
            mailInfo.setSubject(MimeUtility.decodeText(message.getSubject()));
        } catch (Exception e) {
            mailInfo.setSubject(message.getSubject());
        }
        
        // 发送时间
        mailInfo.setSentDate(message.getSentDate());
        
        // 接收时间
        mailInfo.setReceivedDate(message.getReceivedDate());
        
        // 内容类型
        mailInfo.setContentType(message.getContentType());
        
        // 检查是否有附件
        boolean hasAttachments = false;
        List<String> attachmentNames = new ArrayList<>();
        
        try {
            Object content = message.getContent();
            if (content instanceof MimeMultipart) {
                MimeMultipart multipart = (MimeMultipart) content;
                for (int i = 0; i < multipart.getCount(); i++) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()) ||
                            (bodyPart.getFileName() != null && !bodyPart.getFileName().isEmpty())) {
                        hasAttachments = true;
                        try {
                            attachmentNames.add(MimeUtility.decodeText(bodyPart.getFileName()));
                        } catch (Exception e) {
                            attachmentNames.add(bodyPart.getFileName());
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.warn("检查邮件附件时出错: {}", e.getMessage());
        }
        
        mailInfo.setHasAttachments(hasAttachments);
        mailInfo.setAttachmentNames(attachmentNames);
        
        return mailInfo;
    }

    /**
     * 获取邮件详情
     *
     * @param message 邮件消息对象
     * @return 邮件详情对象
     * @throws MessagingException 邮件操作异常
     * @throws IOException IO异常
     */
    public static MailDetail getMailDetail(Message message) throws MessagingException, IOException {
        MailDetail mailDetail = new MailDetail();
        
        // 先解析基本信息
        MailInfo mailInfo = parseMailInfo(message);
        // 复制基本信息到详情对象
        mailDetail.setMessageId(mailInfo.getMessageId());
        mailDetail.setFrom(mailInfo.getFrom());
        mailDetail.setTo(mailInfo.getTo());
        mailDetail.setSubject(mailInfo.getSubject());
        mailDetail.setSentDate(mailInfo.getSentDate());
        mailDetail.setReceivedDate(mailInfo.getReceivedDate());
        mailDetail.setHasAttachments(mailInfo.isHasAttachments());
        mailDetail.setAttachmentNames(mailInfo.getAttachmentNames());
        mailDetail.setContentType(mailInfo.getContentType());
        
        // 解析邮件内容和附件
        List<AttachmentInfo> attachments = new ArrayList<>();
        String textContent = null;
        String htmlContent = null;
        
        log.info("开始获取邮件详情，邮件主题: {}, 内容类型: {}", mailInfo.getSubject(), mailInfo.getContentType());
        
        Object content = message.getContent();
        log.info("邮件内容类型: {}", content != null ? content.getClass().getName() : "null");
        
        if (content == null) {
            log.warn("邮件内容为null");
        } else if (content instanceof String) {
            // 简单文本邮件
            textContent = (String) content;
            log.info("邮件内容为String类型，长度: {}", textContent.length());
        } else if (content instanceof MimeMultipart) {
            // 复杂邮件（可能包含附件）
            MimeMultipart multipart = (MimeMultipart) content;
            log.info("邮件内容为MimeMultipart类型，包含{}个部分", multipart.getCount());
            
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                log.info("处理第{}个BodyPart，内容类型: {}, 处置方式: {}, 文件名: {}", 
                        i+1, bodyPart.getContentType(), bodyPart.getDisposition(), bodyPart.getFileName());
                
                // 检查是否是附件
                if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()) ||
                        (bodyPart.getFileName() != null && !bodyPart.getFileName().isEmpty())) {
                    // 处理附件
                    AttachmentInfo attachmentInfo = new AttachmentInfo();
                    try {
                        attachmentInfo.setFileName(MimeUtility.decodeText(bodyPart.getFileName()));
                    } catch (Exception e) {
                        attachmentInfo.setFileName(bodyPart.getFileName());
                    }
                    attachmentInfo.setContentType(bodyPart.getContentType());
                    attachmentInfo.setSize(bodyPart.getSize());
                    attachmentInfo.setBodyPart((MimeBodyPart) bodyPart);
                    attachments.add(attachmentInfo);
                    log.info("处理附件: {}", attachmentInfo.getFileName());
                } else {
                    // 处理邮件正文
                    String bodyPartContent = getBodyPartContent(bodyPart);
                    log.info("处理正文部分，内容长度: {}, 内容类型: {}", 
                            bodyPartContent.length(), bodyPart.getContentType());
                    
                    // 尝试更灵活地判断内容类型
                    String contentType = bodyPart.getContentType().toLowerCase();
                    if (contentType.contains("text/plain")) {
                        textContent = bodyPartContent;
                        log.info("设置文本内容，长度: {}", textContent.length());
                    } else if (contentType.contains("text/html")) {
                        htmlContent = bodyPartContent;
                        log.info("设置HTML内容，长度: {}", htmlContent.length());
                    } else {
                        // 如果无法确定内容类型，尝试将其作为文本内容
                        if (textContent == null && !bodyPartContent.isEmpty()) {
                            textContent = bodyPartContent;
                            log.info("无法确定内容类型，将其作为文本内容，长度: {}", textContent.length());
                        }
                    }
                }
            }
        } else if (content instanceof Multipart) {
            // 处理其他类型的Multipart
            Multipart multipart = (Multipart) content;
            log.info("邮件内容为Multipart类型，包含{}个部分", multipart.getCount());
            
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                log.info("处理第{}个BodyPart，内容类型: {}, 处置方式: {}, 文件名: {}", 
                        i+1, bodyPart.getContentType(), bodyPart.getDisposition(), bodyPart.getFileName());
                
                // 检查是否是附件
                if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()) ||
                        (bodyPart.getFileName() != null && !bodyPart.getFileName().isEmpty())) {
                    // 处理附件
                    AttachmentInfo attachmentInfo = new AttachmentInfo();
                    try {
                        attachmentInfo.setFileName(MimeUtility.decodeText(bodyPart.getFileName()));
                    } catch (Exception e) {
                        attachmentInfo.setFileName(bodyPart.getFileName());
                    }
                    attachmentInfo.setContentType(bodyPart.getContentType());
                    attachmentInfo.setSize(bodyPart.getSize());
                    if (bodyPart instanceof MimeBodyPart) {
                        attachmentInfo.setBodyPart((MimeBodyPart) bodyPart);
                    }
                    attachments.add(attachmentInfo);
                    log.info("处理附件: {}", attachmentInfo.getFileName());
                } else {
                    // 处理邮件正文
                    String bodyPartContent = getBodyPartContent(bodyPart);
                    log.info("处理正文部分，内容长度: {}, 内容类型: {}", 
                            bodyPartContent.length(), bodyPart.getContentType());
                    
                    // 尝试更灵活地判断内容类型
                    String contentType = bodyPart.getContentType().toLowerCase();
                    if (contentType.contains("text/plain")) {
                        textContent = bodyPartContent;
                        log.info("设置文本内容，长度: {}", textContent.length());
                    } else if (contentType.contains("text/html")) {
                        htmlContent = bodyPartContent;
                        log.info("设置HTML内容，长度: {}", htmlContent.length());
                    } else {
                        // 如果无法确定内容类型，尝试将其作为文本内容
                        if (textContent == null && !bodyPartContent.isEmpty()) {
                            textContent = bodyPartContent;
                            log.info("无法确定内容类型，将其作为文本内容，长度: {}", textContent.length());
                        }
                    }
                }
            }
        } else {
            // 处理其他类型的内容
            log.info("邮件内容为其他类型: {}", content.getClass().getName());
            try {
                // 尝试将内容转换为字符串
                textContent = content.toString();
                log.info("将其他类型内容转换为字符串，长度: {}", textContent.length());
            } catch (Exception e) {
                log.warn("无法将内容转换为字符串: {}", e.getMessage());
            }
        }
        
        // 如果仍然没有获取到内容，尝试使用getText()方法（某些邮件库可能提供此方法）
        if (textContent == null && htmlContent == null) {
            try {
                java.lang.reflect.Method method = message.getClass().getMethod("getText");
                Object result = method.invoke(message);
                if (result != null) {
                    textContent = result.toString();
                    log.info("使用getText()方法获取内容，长度: {}", textContent.length());
                }
            } catch (Exception e) {
                log.warn("尝试使用getText()方法获取内容失败: {}", e.getMessage());
            }
        }
        
        log.info("邮件详情获取完成，文本内容长度: {}, HTML内容长度: {}, 附件数量: {}", 
                textContent != null ? textContent.length() : 0, 
                htmlContent != null ? htmlContent.length() : 0, 
                attachments.size());
        
        mailDetail.setTextContent(textContent);
        mailDetail.setHtmlContent(htmlContent);
        mailDetail.setAttachments(attachments);
        
        return mailDetail;
    }

    /**
     * 获取邮件正文部分的内容
     *
     * @param bodyPart 邮件正文部分
     * @return 正文内容
     * @throws MessagingException 邮件操作异常
     * @throws IOException IO异常
     */
    private static String getBodyPartContent(BodyPart bodyPart) throws MessagingException, IOException {
        log.info("开始获取BodyPart内容，内容类型: {}, 处置方式: {}, 文件名: {}", 
                bodyPart.getContentType(), bodyPart.getDisposition(), bodyPart.getFileName());
        
        Object content = bodyPart.getContent();
        log.info("BodyPart内容类型: {}", content != null ? content.getClass().getName() : "null");
        
        if (content == null) {
            log.warn("BodyPart内容为null");
            return "";
        } else if (content instanceof String) {
            String contentStr = (String) content;
            log.info("BodyPart内容为String类型，长度: {}", contentStr.length());
            return contentStr;
        } else if (content instanceof MimeMultipart) {
            // 处理嵌套的MimeMultipart
            MimeMultipart multipart = (MimeMultipart) content;
            log.info("BodyPart内容为MimeMultipart类型，包含{}个部分", multipart.getCount());
            
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart part = multipart.getBodyPart(i);
                String partContent = getBodyPartContent(part);
                if (!partContent.isEmpty()) {
                    sb.append(partContent);
                    sb.append("\n");
                }
            }
            String result = sb.toString();
            log.info("处理嵌套MimeMultipart完成，结果长度: {}", result.length());
            return result;
        } else if (content instanceof Multipart) {
            // 处理嵌套的Multipart
            Multipart multipart = (Multipart) content;
            log.info("BodyPart内容为Multipart类型，包含{}个部分", multipart.getCount());
            
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart part = multipart.getBodyPart(i);
                String partContent = getBodyPartContent(part);
                if (!partContent.isEmpty()) {
                    sb.append(partContent);
                    sb.append("\n");
                }
            }
            String result = sb.toString();
            log.info("处理嵌套Multipart完成，结果长度: {}", result.length());
            return result;
        } else {
            // 处理其他类型的内容
            log.info("BodyPart内容为其他类型: {}", content.getClass().getName());
            try {
                // 尝试将内容转换为字符串
                String contentStr = content.toString();
                log.info("将其他类型内容转换为字符串，长度: {}", contentStr.length());
                return contentStr;
            } catch (Exception e) {
                log.warn("无法将BodyPart内容转换为字符串: {}", e.getMessage());
                return "";
            }
        }
    }

    /**
     * 下载邮件附件
     *
     * @param attachmentInfo 附件信息
     * @param saveDir 保存目录
     * @return 保存的文件
     * @throws MessagingException 邮件操作异常
     * @throws IOException IO异常
     */
    public static File downloadAttachment(AttachmentInfo attachmentInfo, String saveDir) throws MessagingException, IOException {
        // 确保保存目录存在
        File dir = new File(saveDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        // 创建保存文件
        File file = new File(dir, attachmentInfo.getFileName());
        
        // 保存附件
        attachmentInfo.getBodyPart().saveFile(file);
        
        log.info("成功下载附件: {} 到 {}", attachmentInfo.getFileName(), file.getAbsolutePath());
        return file;
    }

    /**
     * 搜索邮件
     *
     * @param folder 邮箱文件夹
     * @param searchTerm 搜索条件
     * @param maxCount 最大获取数量，0表示获取所有
     * @return 邮件信息列表
     * @throws MessagingException 邮件操作异常
     */
    public static List<MailInfo> searchMails(Folder folder, SearchTerm searchTerm, int maxCount) throws MessagingException {
        List<MailInfo> mailList = new ArrayList<>();
        
        if (!folder.isOpen()) {
            folder.open(Folder.READ_ONLY);
        }
        
        // 搜索邮件
        Message[] messages = folder.search(searchTerm);
        log.info("搜索到 {} 封邮件", messages.length);
        
        int startIndex = 0;
        int endIndex = messages.length;
        
        if (maxCount > 0 && maxCount < messages.length) {
            startIndex = messages.length - maxCount;
        }
        
        for (int i = startIndex; i < endIndex; i++) {
            Message message = messages[i];
            MailInfo mailInfo = parseMailInfo(message);
            mailList.add(mailInfo);
        }
        
        return mailList;
    }

    /**
     * 关闭邮件连接
     *
     * @param folder 邮箱文件夹
     * @param store Store对象
     */
    public static void closeConnection(Folder folder, Store store) {
        try {
            if (folder != null && folder.isOpen()) {
                folder.close(false);
                log.info("已关闭邮箱文件夹");
            }
        } catch (Exception e) {
            log.warn("关闭邮箱文件夹时出错: {}", e.getMessage());
        }
        
        try {
            if (store != null && store.isConnected()) {
                store.close();
                log.info("已关闭邮件服务器连接");
            }
        } catch (Exception e) {
            log.warn("关闭邮件服务器连接时出错: {}", e.getMessage());
        }
    }
}
