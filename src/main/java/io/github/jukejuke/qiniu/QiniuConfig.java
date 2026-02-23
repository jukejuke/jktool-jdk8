package io.github.jukejuke.qiniu;

import lombok.Data;

/**
 * 七牛云配置类
 * 用于存储七牛云对象存储的配置信息
 */
@Data
public class QiniuConfig {
    
    /**
     * 七牛云 Access Key
     */
    private String accessKey;
    
    /**
     * 七牛云 Secret Key
     */
    private String secretKey;
    
    /**
     * 存储空间名称
     */
    private String bucket;
    
    /**
     * 域名（CDN 加速域名或存储桶默认域名）
     */
    private String domain;
    
    /**
     * 区域（如：z0-华东, z1-华北, z2-华南, na0-北美, as0-亚太）
     */
    private String region;
    
    /**
     * 上传超时时间（毫秒）
     */
    private int uploadTimeout = 30000;
    
    /**
     * 下载链接过期时间（秒）
     */
    private long downloadExpire = 3600;
}
