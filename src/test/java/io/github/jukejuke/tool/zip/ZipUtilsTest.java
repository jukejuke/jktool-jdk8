package io.github.jukejuke.tool.zip;

import io.github.jukejuke.tool.file.FileUtils;
import io.github.jukejuke.tool.log.LogUtil;

import java.io.File;

/**
 * ZipUtils 测试类
 */
public class ZipUtilsTest {

    public static void main(String[] args) {
        ZipUtilsTest test = new ZipUtilsTest();
        
        // 测试压缩单个文件
        test.testZipFile();
        
        // 测试压缩目录
        test.testZipDirectory();
        
        // 测试压缩多个文件
        test.testZipFiles();
        
        // 测试解压
        test.testUnzip();
    }

    /**
     * 测试压缩单个文件
     */
    private void testZipFile() {
        LogUtil.info("=== 测试压缩单个文件 ===");
        
        String testFile = "test-single-file.txt";
        String zipFile = "test-single-file.zip";
        
        try {
            // 创建测试文件
            FileUtils.writeFile(testFile, "这是一个测试文件的内容。\n用于测试压缩功能。");
            LogUtil.info("创建测试文件: " + testFile);
            
            // 压缩文件
            boolean zipResult = ZipUtils.zipFile(testFile, zipFile);
            LogUtil.info("压缩文件结果: " + zipResult);
            
            // 检查压缩文件是否存在
            File zip = new File(zipFile);
            LogUtil.info(zip.getAbsolutePath());
            LogUtil.info("压缩文件存在: " + zip.exists());
            if (zip.exists()) {
                LogUtil.info("压缩文件大小: " + zip.length() + " 字节");
            }
        } finally {
            // 清理
            FileUtils.deleteFile(testFile);
            FileUtils.deleteFile(zipFile);
            LogUtil.info("清理完成\n");
        }
    }

    /**
     * 测试压缩目录
     */
    private void testZipDirectory() {
        LogUtil.info("=== 测试压缩目录 ===");
        
        String testDir = "test-dir";
        String zipFile = "test-dir.zip";
        
        try {
            // 创建测试目录和文件
            FileUtils.mkdirs(testDir + "/subdir1");
            FileUtils.mkdirs(testDir + "/subdir2");
            FileUtils.writeFile(testDir + "/file1.txt", "文件1的内容");
            FileUtils.writeFile(testDir + "/file2.txt", "文件2的内容");
            FileUtils.writeFile(testDir + "/subdir1/file3.txt", "子目录1的文件内容");
            FileUtils.writeFile(testDir + "/subdir2/file4.txt", "子目录2的文件内容");
            LogUtil.info("创建测试目录结构完成");
            
            // 压缩目录
            boolean zipResult = ZipUtils.zipDirectory(testDir, zipFile);
            LogUtil.info("压缩目录结果: " + zipResult);
            
            // 检查压缩文件是否存在
            File zip = new File(zipFile);
            LogUtil.info("压缩文件存在: " + zip.exists());
            if (zip.exists()) {
                LogUtil.info("压缩文件大小: " + zip.length() + " 字节");
            }
        } finally {
            // 清理
            FileUtils.deleteDirectory(testDir);
            FileUtils.deleteFile(zipFile);
            LogUtil.info("清理完成\n");
        }
    }

    /**
     * 测试压缩多个文件
     */
    private void testZipFiles() {
        LogUtil.info("=== 测试压缩多个文件 ===");
        
        String file1 = "test-multi-file1.txt";
        String file2 = "test-multi-file2.txt";
        String testDir = "test-multi-dir";
        String zipFile = "test-multi-files.zip";
        
        try {
            // 创建测试文件和目录
            FileUtils.writeFile(file1, "多文件测试 - 文件1");
            FileUtils.writeFile(file2, "多文件测试 - 文件2");
            FileUtils.mkdirs(testDir);
            FileUtils.writeFile(testDir + "/subfile.txt", "多文件测试 - 子目录文件");
            LogUtil.info("创建测试文件和目录完成");
            
            // 压缩多个文件
            String[] sourcePaths = {file1, file2, testDir};
            boolean zipResult = ZipUtils.zipFiles(sourcePaths, zipFile);
            LogUtil.info("压缩多个文件结果: " + zipResult);
            
            // 检查压缩文件是否存在
            File zip = new File(zipFile);
            LogUtil.info("压缩文件存在: " + zip.exists());
            if (zip.exists()) {
                LogUtil.info("压缩文件大小: " + zip.length() + " 字节");
            }
        } finally {
            // 清理
            FileUtils.deleteFile(file1);
            FileUtils.deleteFile(file2);
            FileUtils.deleteDirectory(testDir);
            FileUtils.deleteFile(zipFile);
            LogUtil.info("清理完成\n");
        }
    }

    /**
     * 测试解压
     */
    private void testUnzip() {
        LogUtil.info("=== 测试解压 ===");
        
        String testFile = "test-unzip-file.txt";
        String zipFile = "test-unzip.zip";
        String destDir = "test-unzip-dest";
        
        try {
            // 先创建一个压缩文件
            FileUtils.writeFile(testFile, "这是用于测试解压的文件内容");
            ZipUtils.zipFile(testFile, zipFile);
            LogUtil.info("创建测试压缩文件完成");
            
            // 解压
            boolean unzipResult = ZipUtils.unzip(zipFile, destDir);
            LogUtil.info("解压结果: " + unzipResult);
            
            // 检查解压后的文件是否存在
            File extractedFile = new File(destDir, testFile);
            LogUtil.info("解压后文件存在: " + extractedFile.exists());
            
            // 读取解压后的文件内容
            if (extractedFile.exists()) {
                String content = FileUtils.readFile(extractedFile.getAbsolutePath());
                LogUtil.info("解压后文件内容: " + content);
            }
        } finally {
            // 清理
            FileUtils.deleteFile(testFile);
            FileUtils.deleteFile(zipFile);
            FileUtils.deleteDirectory(destDir);
            LogUtil.info("清理完成");
        }
    }
}
