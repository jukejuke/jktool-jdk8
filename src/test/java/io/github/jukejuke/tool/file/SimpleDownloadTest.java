package io.github.jukejuke.tool.file;

import java.io.File;

/**
 * 简单的文件下载测试类（不依赖SLF4J）
 */
public class SimpleDownloadTest {

    public static void main(String[] args) {
        // 测试URL
        String testUrl = "https://icon2.xinrui-cn.com/private/icon1757908622481.jpg?imageView2/2/w/200&e=1789021560&token=FlOC6ljJCmETSQOmc9-7jo8eVzY3Z-xM50oa6Ql_:S7B7Mc_-yKhCK4AEpTk9hG94VjQ=";
        
        // 测试目录
        String testDir = System.getProperty("user.home") + File.separator + "Downloads" + File.separator + "test";
        
        System.out.println("=== Testing SimpleDownloadUtil ===");
        System.out.println("Test URL: " + testUrl);
        System.out.println("Save directory: " + testDir);
        
        // 执行下载
        String resultPath = SimpleDownloadUtil.download(testUrl, testDir);
        
        if (resultPath != null) {
            System.out.println("\n✅ Download successful!");
            System.out.println("Downloaded file: " + resultPath);
            
            // 检查文件是否存在
            File downloadedFile = new File(resultPath);
            if (downloadedFile.exists()) {
                System.out.println("File exists: " + downloadedFile.exists());
                System.out.println("File size: " + downloadedFile.length() + " bytes");
            } else {
                System.out.println("❌ File does not exist after download");
            }
        } else {
            System.out.println("\n❌ Download failed");
        }
        
        System.out.println("\n=== Test completed ===");
    }
}
