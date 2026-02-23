package io.github.jukejuke.qiniu;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

/**
 * 七牛云对象存储工具类
 * 提供文件上传、下载、删除等操作
 */
@Slf4j
public class QiniuUtils {
    
    private final Auth auth;
    private final UploadManager uploadManager;
    private final BucketManager bucketManager;
    private final QiniuConfig config;
    
    /**
     * 构造方法
     * @param config 七牛云配置
     */
    public QiniuUtils(QiniuConfig config) {
        this.config = config;
        this.auth = Auth.create(config.getAccessKey(), config.getSecretKey());
        
        // 根据配置创建区域对象
        Configuration cfg = new Configuration(getRegion(config.getRegion()));
        cfg.connectTimeout = config.getUploadTimeout();
        cfg.readTimeout = config.getUploadTimeout();
        cfg.writeTimeout = config.getUploadTimeout();
        
        // 创建上传管理器
        UploadManager manager;
        try {
            // 配置断点续传
            String tempDir = System.getProperty("java.io.tmpdir");
            manager = new UploadManager(cfg, new FileRecorder(tempDir));
        } catch (IOException e) {
            log.error("初始化上传管理器失败", e);
            manager = new UploadManager(cfg);
        }
        this.uploadManager = manager;
        
        // 创建桶管理器
        this.bucketManager = new BucketManager(auth, cfg);
    }
    
    /**
     * 根据区域字符串获取区域对象
     * @param regionStr 区域字符串（如：z0-华东, z1-华北, z2-华南, na0-北美, as0-亚太）
     * @return 区域对象
     */
    private Region getRegion(String regionStr) {
        if ("z0".equals(regionStr)) {
            return Region.huadong();
        } else if ("z1".equals(regionStr)) {
            return Region.huabei();
        } else if ("z2".equals(regionStr)) {
            return Region.huanan();
        } else if ("na0".equals(regionStr)) {
            return Region.beimei();
        } else if ("as0".equals(regionStr)) {
            return Region.xinjiapo();
        } else {
            // 默认华东
            return Region.huadong();
        }
    }
    
    /**
     * 生成上传凭证
     * @return 上传凭证
     */
    public String generateUploadToken() {
        return auth.uploadToken(config.getBucket());
    }
    
    /**
     * 生成带过期时间的上传凭证
     * @param expireSeconds 过期时间（秒）
     * @return 上传凭证
     */
    public String generateUploadToken(long expireSeconds) {
        return auth.uploadToken(config.getBucket(), null, expireSeconds, new StringMap());
    }
    
    /**
     * 上传文件（从本地文件）
     * @param localFilePath 本地文件路径
     * @param key 存储键
     * @return 上传结果
     * @throws QiniuException 七牛云异常
     */
    public DefaultPutRet uploadFile(String localFilePath, String key) throws QiniuException {
        String upToken = generateUploadToken();
        Response response = uploadManager.put(localFilePath, key, upToken);
        return response.jsonToObject(DefaultPutRet.class);
    }
    
    /**
     * 上传文件（从字节数组）
     * @param data 字节数组
     * @param key 存储键
     * @return 上传结果
     * @throws QiniuException 七牛云异常
     */
    public DefaultPutRet uploadBytes(byte[] data, String key) throws QiniuException {
        String upToken = generateUploadToken();
        Response response = uploadManager.put(data, key, upToken);
        return response.jsonToObject(DefaultPutRet.class);
    }
    
    /**
     * 上传文件（从输入流）
     * @param inputStream 输入流
     * @param key 存储键
     * @param fileSize 文件大小
     * @return 上传结果
     * @throws QiniuException 七牛云异常
     */
    public DefaultPutRet uploadStream(InputStream inputStream, String key, long fileSize) throws QiniuException {
        String upToken = generateUploadToken();
        Response response = uploadManager.put(inputStream, key, upToken, new StringMap(), null);
        return response.jsonToObject(DefaultPutRet.class);
    }
    
    /**
     * 生成公开访问链接
     * @param key 存储键
     * @return 访问链接
     */
    public String getPublicUrl(String key) {
        return config.getDomain() + "/" + key;
    }
    
    /**
     * 生成私有访问链接（带签名）
     * @param key 存储键
     * @return 带签名的访问链接
     */
    public String getPrivateUrl(String key) {
        return getPrivateUrl(key, config.getDownloadExpire());
    }
    
    /**
     * 生成私有访问链接（带签名和自定义过期时间）
     * @param key 存储键
     * @param expireSeconds 过期时间（秒）
     * @return 带签名的访问链接
     */
    public String getPrivateUrl(String key, long expireSeconds) {
        String baseUrl = config.getDomain() + "/" + key;
        return auth.privateDownloadUrl(baseUrl, expireSeconds);
    }
    
    /**
     * 删除文件
     * @param key 存储键
     * @return 是否删除成功
     */
    public boolean deleteFile(String key) {
        try {
            Response response = bucketManager.delete(config.getBucket(), key);
            return response.isOK();
        } catch (QiniuException e) {
            log.error("删除文件失败, key: {}", key, e);
            return false;
        }
    }
    
    /**
     * 重命名文件
     * @param oldKey 旧存储键
     * @param newKey 新存储键
     * @return 是否重命名成功
     */
    public boolean renameFile(String oldKey, String newKey) {
        try {
            Response response = bucketManager.move(config.getBucket(), oldKey, config.getBucket(), newKey);
            return response.isOK();
        } catch (QiniuException e) {
            log.error("重命名文件失败, oldKey: {}, newKey: {}", oldKey, newKey, e);
            return false;
        }
    }
    
    /**
     * 复制文件
     * @param sourceKey 源存储键
     * @param targetKey 目标存储键
     * @return 是否复制成功
     */
    public boolean copyFile(String sourceKey, String targetKey) {
        try {
            Response response = bucketManager.copy(config.getBucket(), sourceKey, config.getBucket(), targetKey);
            return response.isOK();
        } catch (QiniuException e) {
            log.error("复制文件失败, sourceKey: {}, targetKey: {}", sourceKey, targetKey, e);
            return false;
        }
    }
    
    /**
     * 移动文件到另一个存储空间
     * @param sourceKey 源存储键
     * @param targetBucket 目标存储空间
     * @param targetKey 目标存储键
     * @return 是否移动成功
     */
    public boolean moveFile(String sourceKey, String targetBucket, String targetKey) {
        try {
            Response response = bucketManager.move(config.getBucket(), sourceKey, targetBucket, targetKey);
            return response.isOK();
        } catch (QiniuException e) {
            log.error("移动文件失败, sourceKey: {}, targetBucket: {}, targetKey: {}", sourceKey, targetBucket, targetKey, e);
            return false;
        }
    }
    
    /**
     * 复制文件到另一个存储空间
     * @param sourceKey 源存储键
     * @param targetBucket 目标存储空间
     * @param targetKey 目标存储键
     * @return 是否复制成功
     */
    public boolean copyFile(String sourceKey, String targetBucket, String targetKey) {
        try {
            Response response = bucketManager.copy(config.getBucket(), sourceKey, targetBucket, targetKey);
            return response.isOK();
        } catch (QiniuException e) {
            log.error("复制文件失败, sourceKey: {}, targetBucket: {}, targetKey: {}", sourceKey, targetBucket, targetKey, e);
            return false;
        }
    }
}
