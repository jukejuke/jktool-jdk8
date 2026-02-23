package io.github.jukejuke.qiniu;

import com.qiniu.common.QiniuException;
import com.qiniu.storage.model.DefaultPutRet;

import java.io.IOException;

/**
 * 七牛云工具类使用示例
 */
public class QiniuUtilsExample {
    
    public static void main(String[] args) {
        // 1. 初始化配置
        QiniuConfig config = new QiniuConfig();
        config.setAccessKey("your-access-key");
        config.setSecretKey("your-secret-key");
        config.setBucket("your-bucket");
        config.setDomain("your-domain.com"); // 如：bucket.test.com
        config.setRegion("z0"); // z0-华东, z1-华北, z2-华南, na0-北美, as0-亚太
        
        // 2. 创建工具类实例
        QiniuUtils qiniuUtils = new QiniuUtils(config);
        
        try {
            // 3. 上传文件示例
            System.out.println("=== 上传文件示例 ===");
            
            // 3.1 从本地文件上传
            String localFilePath = "C:\\Users\\Administrator\\Downloads\\test\\icon1757908622481.jpg";
            String key = "test/test.jpg";
            DefaultPutRet putRet = qiniuUtils.uploadFile(localFilePath, key);
            System.out.println("上传成功，key: " + putRet.key);
            System.out.println("上传成功，hash: " + putRet.hash);
            
            // 3.2 从字节数组上传
            /*
            byte[] data = "Hello Qiniu".getBytes();
            String key2 = "test/hello.txt";
            DefaultPutRet putRet2 = qiniuUtils.uploadBytes(data, key2);
            System.out.println("上传成功，key: " + putRet2.key);
            */
            
            // 3.3 从输入流上传
            /*
            File file = new File(localFilePath);
            try (FileInputStream fis = new FileInputStream(file)) {
                String key3 = "test/test2.jpg";
                DefaultPutRet putRet3 = qiniuUtils.uploadStream(fis, key3, file.length());
                System.out.println("上传成功，key: " + putRet3.key);
            }
            */
            
            // 4. 生成访问链接示例
            System.out.println("\n=== 生成访问链接示例 ===");
            
            // 4.1 生成公开访问链接（适用于公开空间）
            String publicUrl = qiniuUtils.getPublicUrl(key);
            System.out.println("公开访问链接: " + publicUrl);
            
            // 4.2 生成私有访问链接（适用于私有空间）
            String privateUrl = qiniuUtils.getPrivateUrl(key);
            System.out.println("私有访问链接: " + privateUrl);
            
            // 4.3 生成带自定义过期时间的私有访问链接
            String privateUrlWithExpire = qiniuUtils.getPrivateUrl(key, 7200); // 2小时过期
            System.out.println("带过期时间的私有访问链接: " + privateUrlWithExpire);
            
            // 5. 文件操作示例
            System.out.println("\n=== 文件操作示例 ===");
            
            // 5.1 重命名文件
            /*
            String newKey = "test/test_rename.jpg";
            boolean renameSuccess = qiniuUtils.renameFile(key, newKey);
            System.out.println("重命名文件成功: " + renameSuccess);
            */
            
            // 5.2 复制文件
            /*
            String copyKey = "test/test_copy.jpg";
            boolean copySuccess = qiniuUtils.copyFile(key, copyKey);
            System.out.println("复制文件成功: " + copySuccess);
            */
            
            // 5.3 删除文件
            /*
            boolean deleteSuccess = qiniuUtils.deleteFile(key);
            System.out.println("删除文件成功: " + deleteSuccess);
            */
            
        } catch (QiniuException e) {
            System.err.println("七牛云操作失败: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IO操作失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
