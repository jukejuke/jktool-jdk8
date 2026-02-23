package io.github.jukejuke.tool.file;

import lombok.extern.slf4j.Slf4j;

/**
 * 文件下载工具测试类
 */
@Slf4j
public class DownloadUtilTest {

    public static void main(String[] args) {
        // 测试URL
        String testUrl = "https://icon2.xinrui-cn.com/private/icon1757908622481.jpg?imageView2/2/w/200&e=1789021560&token=FlOC6ljJCmETSQOmc9-7jo8eVzY3Z-xM50oa6Ql_:S7B7Mc_-yKhCK4AEpTk9hG94VjQ=";
        
        // 测试1：下载到临时目录
        log.info("=== Test 1: Download to temporary directory ===");
        String tempPath = DownloadUtil.downloadToTempDir(testUrl);
        if (tempPath != null) {
            log.info("Downloaded to temp directory: {}", tempPath);
            log.info("File size: {} bytes", FileUtils.getFileSize(tempPath));
        } else {
            log.error("Failed to download to temp directory");
        }
        
        // 测试2：下载到指定目录
        log.info("\n=== Test 2: Download to specified directory ===");
        String customDir = System.getProperty("user.home") + "\\Downloads\\test";
        String customPath = DownloadUtil.download(testUrl, customDir);
        if (customPath != null) {
            log.info("Downloaded to custom directory: {}", customPath);
            log.info("File size: {} bytes", FileUtils.getFileSize(customPath));
        } else {
            log.error("Failed to download to custom directory");
        }
        
        // 测试3：下载到指定目录并指定文件名
        log.info("\n=== Test 3: Download to specified directory with custom file name ===");
        String customFileName = "test_image.jpg";
        String customNamedPath = DownloadUtil.download(testUrl, customDir, customFileName);
        if (customNamedPath != null) {
            log.info("Downloaded to custom directory with custom name: {}", customNamedPath);
            log.info("File size: {} bytes", FileUtils.getFileSize(customNamedPath));
        } else {
            log.error("Failed to download to custom directory with custom name");
        }
        
        log.info("\n=== All tests completed ===");
    }
}
