package io.github.jukejuke.tool.file;

import io.github.jukejuke.tool.log.LogUtil;
import io.github.jukejuke.tool.string.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * FileUtils 测试类
 */
public class FileUtilsTest {

    public static void main(String[] args) throws IOException {
        FileUtilsTest test = new FileUtilsTest();
        
        // 测试目录操作
        test.testDirectoryOperations();
        
        // 测试文件操作
        test.testFileOperations();
        
        // 测试文件读写
        test.testFileReadWrite();
        
        // 测试文件复制和移动
        test.testFileCopyMove();
        
        // 测试文件信息获取
        test.testFileInfo();
        
        // 测试文件列出
        test.testFileListing();
    }

    private void testDirectoryOperations() {
        LogUtil.info("=== 测试目录操作 ===");
        
        String testDir = "test-dir";
        String subDir = testDir + "/sub-dir";
        
        // 创建目录
        boolean mkdirsResult = FileUtils.mkdirs(subDir);
        LogUtil.info("创建目录 " + subDir + ": " + mkdirsResult);
        
        // 检查目录是否存在
        boolean dirExists = FileUtils.isDirectory(subDir);
        LogUtil.info("目录 " + subDir + " 存在: " + dirExists);
        
        // 清理
        boolean deleteResult = FileUtils.deleteDirectory(testDir);
        LogUtil.info("删除目录 " + testDir + ": " + deleteResult);
        
        // 再次检查目录是否存在
        dirExists = FileUtils.isDirectory(testDir);
        LogUtil.info("目录 " + testDir + " 存在: " + dirExists);
    }

    private void testFileOperations() {
        LogUtil.info("\n=== 测试文件操作 ===");
        
        String testFile = "test-file.txt";
        
        // 创建文件
        boolean createResult = FileUtils.createFile(testFile);
        LogUtil.info("创建文件 " + testFile + ": " + createResult);
        
        // 检查文件是否存在
        boolean fileExists = FileUtils.isFile(testFile);
        LogUtil.info("文件 " + testFile + " 存在: " + fileExists);
        
        // 删除文件
        boolean deleteResult = FileUtils.deleteFile(testFile);
        LogUtil.info("删除文件 " + testFile + ": " + deleteResult);
        
        // 再次检查文件是否存在
        fileExists = FileUtils.isFile(testFile);
        LogUtil.info("文件 " + testFile + " 存在: " + fileExists);
    }

    private void testFileReadWrite() {
        LogUtil.info("\n=== 测试文件读写 ===");
        
        String testFile = "test-read-write.txt";
        String content = "Hello, FileUtils!\nThis is a test.";
        
        // 写入文件
        boolean writeResult = FileUtils.writeFile(testFile, content);
        LogUtil.info("写入文件 " + testFile + ": " + writeResult);
        
        // 读取文件
        String readContent = FileUtils.readFile(testFile);
        LogUtil.info("读取文件内容: \n" + readContent);
        
        // 追加写入
        String appendContent = "\nThis is an appended line.";
        boolean appendResult = FileUtils.writeFile(testFile, appendContent, true);
        LogUtil.info("追加写入文件: " + appendResult);
        
        // 再次读取文件
        readContent = FileUtils.readFile(testFile);
        LogUtil.info("追加后文件内容: \n" + readContent);
        
        // 读取文件行
        List<String> lines = FileUtils.readFileLines(testFile);
        LogUtil.info("文件行列表: " + lines);
        
        // 清空文件
        boolean clearResult = FileUtils.clearFile(testFile);
        LogUtil.info("清空文件: " + clearResult);
        
        // 检查文件是否为空
        readContent = FileUtils.readFile(testFile);
        LogUtil.info("清空后文件内容: \"" + readContent + "\" (长度: " + readContent.length() + ")");
        
        // 清理
        FileUtils.deleteFile(testFile);
    }

    private void testFileCopyMove() {
        LogUtil.info("\n=== 测试文件复制和移动 ===");
        
        String sourceFile = "source.txt";
        String copyFile = "copy.txt";
        String moveFile = "move.txt";
        
        // 创建源文件
        FileUtils.writeFile(sourceFile, "This is a source file.");
        LogUtil.info("创建源文件 " + sourceFile);
        
        // 复制文件
        boolean copyResult = FileUtils.copyFile(sourceFile, copyFile);
        LogUtil.info("复制文件 " + sourceFile + " 到 " + copyFile + ": " + copyResult);
        
        // 检查复制文件是否存在
        boolean copyExists = FileUtils.isFile(copyFile);
        LogUtil.info("复制文件 " + copyFile + " 存在: " + copyExists);
        
        // 移动文件
        boolean moveResult = FileUtils.moveFile(copyFile, moveFile);
        LogUtil.info("移动文件 " + copyFile + " 到 " + moveFile + ": " + moveResult);
        
        // 检查移动文件是否存在
        boolean moveExists = FileUtils.isFile(moveFile);
        LogUtil.info("移动文件 " + moveFile + " 存在: " + moveExists);
        
        // 检查原复制文件是否不存在
        boolean copyStillExists = FileUtils.isFile(copyFile);
        LogUtil.info("原复制文件 " + copyFile + " 存在: " + copyStillExists);
        
        // 清理
        FileUtils.deleteFile(sourceFile);
        FileUtils.deleteFile(moveFile);
    }

    private void testFileInfo() {
        LogUtil.info("\n=== 测试文件信息获取 ===");
        
        String testFile = "test-info.txt";
        FileUtils.writeFile(testFile, "Test file content for information.");
        
        // 获取文件大小
        long fileSize = FileUtils.getFileSize(testFile);
        LogUtil.info("文件大小: " + fileSize + " 字节");
        
        // 获取文件名
        String fileName = FileUtils.getFileName(testFile);
        LogUtil.info("文件名: " + fileName);
        
        // 获取文件扩展名
        String extension = FileUtils.getFileExtension(testFile);
        LogUtil.info("文件扩展名: " + extension);
        
        // 获取文件名（不含扩展名）
        String nameWithoutExt = FileUtils.getFileNameWithoutExtension(testFile);
        LogUtil.info("文件名（不含扩展名）: " + nameWithoutExt);
        
        // 获取父目录
        String parentDir = FileUtils.getParentDirectory(testFile);
        LogUtil.info("父目录: " + parentDir);
        
        // 获取最后修改时间
        long lastModified = FileUtils.getLastModifiedTime(testFile);
        LogUtil.info("最后修改时间: " + lastModified);
        
        // 清理
        FileUtils.deleteFile(testFile);
    }

    private void testFileListing() {
        LogUtil.info("\n=== 测试文件列出 ===");
        
        String testDir = "test-listing";
        FileUtils.mkdirs(testDir + "/subdir");
        
        // 创建测试文件
        FileUtils.writeFile(testDir + "/file1.txt", "Content 1");
        FileUtils.writeFile(testDir + "/file2.txt", "Content 2");
        FileUtils.writeFile(testDir + "/file3.java", "public class Test {}");
        FileUtils.writeFile(testDir + "/subdir/file4.txt", "Content 4");
        FileUtils.writeFile(testDir + "/subdir/file5.java", "public class SubTest {}");
        
        // 列出目录下的文件
        List<String> files = FileUtils.listFiles(testDir, false);
        LogUtil.info("目录 " + testDir + " 下的文件（非递归）: " + files);
        
        // 递归列出目录下的文件
        List<String> allFiles = FileUtils.listFiles(testDir, true);
        LogUtil.info("目录 " + testDir + " 下的文件（递归）: " + allFiles);
        
        // 按扩展名列出文件
        List<String> txtFiles = FileUtils.listFilesByExtension(testDir, "txt", true);
        LogUtil.info("目录 " + testDir + " 下的 .txt 文件: " + txtFiles);
        
        List<String> javaFiles = FileUtils.listFilesByExtension(testDir, "java", true);
        LogUtil.info("目录 " + testDir + " 下的 .java 文件: " + javaFiles);
        
        // 清理
        FileUtils.deleteDirectory(testDir);
    }
}
