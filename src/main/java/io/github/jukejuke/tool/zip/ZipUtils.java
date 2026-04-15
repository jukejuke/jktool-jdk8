package io.github.jukejuke.tool.zip;

import io.github.jukejuke.tool.string.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * ZIP压缩解压工具类
 * 提供目录或文件的压缩打包功能
 */
@Slf4j
public class ZipUtils {

    /** 默认字符集 */
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * 压缩单个文件
     * @param sourceFilePath 源文件路径
     * @param zipFilePath 压缩文件路径
     * @return 是否压缩成功
     */
    public static boolean zipFile(String sourceFilePath, String zipFilePath) {
        return zipFile(sourceFilePath, zipFilePath, DEFAULT_CHARSET);
    }

    /**
     * 压缩单个文件
     * @param sourceFilePath 源文件路径
     * @param zipFilePath 压缩文件路径
     * @param charset 字符集
     * @return 是否压缩成功
     */
    public static boolean zipFile(String sourceFilePath, String zipFilePath, Charset charset) {
        if (StringUtils.isEmpty(sourceFilePath) || StringUtils.isEmpty(zipFilePath)) {
            log.error("源文件路径或压缩文件路径不能为空");
            return false;
        }
        File sourceFile = new File(sourceFilePath);
        if (!sourceFile.exists() || !sourceFile.isFile()) {
            log.error("源文件不存在或不是文件: {}", sourceFilePath);
            return false;
        }
        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos, charset)) {
            addFileToZip(sourceFile, sourceFile.getName(), zos);
            log.info("文件压缩成功: {} -> {}", sourceFilePath, zipFilePath);
            return true;
        } catch (IOException e) {
            log.error("文件压缩失败", e);
            return false;
        }
    }

    /**
     * 压缩目录
     * @param sourceDirPath 源目录路径
     * @param zipFilePath 压缩文件路径
     * @return 是否压缩成功
     */
    public static boolean zipDirectory(String sourceDirPath, String zipFilePath) {
        return zipDirectory(sourceDirPath, zipFilePath, DEFAULT_CHARSET);
    }

    /**
     * 压缩目录
     * @param sourceDirPath 源目录路径
     * @param zipFilePath 压缩文件路径
     * @param charset 字符集
     * @return 是否压缩成功
     */
    public static boolean zipDirectory(String sourceDirPath, String zipFilePath, Charset charset) {
        if (StringUtils.isEmpty(sourceDirPath) || StringUtils.isEmpty(zipFilePath)) {
            log.error("源目录路径或压缩文件路径不能为空");
            return false;
        }
        File sourceDir = new File(sourceDirPath);
        if (!sourceDir.exists() || !sourceDir.isDirectory()) {
            log.error("源目录不存在或不是目录: {}", sourceDirPath);
            return false;
        }
        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos, charset)) {
            addDirectoryToZip(sourceDir, sourceDir.getName(), zos);
            log.info("目录压缩成功: {} -> {}", sourceDirPath, zipFilePath);
            return true;
        } catch (IOException e) {
            log.error("目录压缩失败", e);
            return false;
        }
    }

    /**
     * 压缩多个文件或目录
     * @param sourcePaths 源文件或目录路径列表
     * @param zipFilePath 压缩文件路径
     * @return 是否压缩成功
     */
    public static boolean zipFiles(String[] sourcePaths, String zipFilePath) {
        return zipFiles(sourcePaths, zipFilePath, DEFAULT_CHARSET);
    }

    /**
     * 压缩多个文件或目录
     * @param sourcePaths 源文件或目录路径列表
     * @param zipFilePath 压缩文件路径
     * @param charset 字符集
     * @return 是否压缩成功
     */
    public static boolean zipFiles(String[] sourcePaths, String zipFilePath, Charset charset) {
        if (sourcePaths == null || sourcePaths.length == 0 || StringUtils.isEmpty(zipFilePath)) {
            log.error("源文件列表或压缩文件路径不能为空");
            return false;
        }
        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos, charset)) {
            for (String sourcePath : sourcePaths) {
                File sourceFile = new File(sourcePath);
                if (!sourceFile.exists()) {
                    log.warn("源文件不存在，跳过: {}", sourcePath);
                    continue;
                }
                if (sourceFile.isFile()) {
                    addFileToZip(sourceFile, sourceFile.getName(), zos);
                } else if (sourceFile.isDirectory()) {
                    addDirectoryToZip(sourceFile, sourceFile.getName(), zos);
                }
            }
            log.info("多文件压缩成功: -> {}", zipFilePath);
            return true;
        } catch (IOException e) {
            log.error("多文件压缩失败", e);
            return false;
        }
    }

    /**
     * 解压ZIP文件
     * @param zipFilePath ZIP文件路径
     * @param destDirPath 目标目录路径
     * @return 是否解压成功
     */
    public static boolean unzip(String zipFilePath, String destDirPath) {
        return unzip(zipFilePath, destDirPath, DEFAULT_CHARSET);
    }

    /**
     * 解压ZIP文件
     * @param zipFilePath ZIP文件路径
     * @param destDirPath 目标目录路径
     * @param charset 字符集
     * @return 是否解压成功
     */
    public static boolean unzip(String zipFilePath, String destDirPath, Charset charset) {
        if (StringUtils.isEmpty(zipFilePath) || StringUtils.isEmpty(destDirPath)) {
            log.error("ZIP文件路径或目标目录路径不能为空");
            return false;
        }
        File zipFile = new File(zipFilePath);
        if (!zipFile.exists() || !zipFile.isFile()) {
            log.error("ZIP文件不存在或不是文件: {}", zipFilePath);
            return false;
        }
        Path destPath = Paths.get(destDirPath);
        try {
            if (!Files.exists(destPath)) {
                Files.createDirectories(destPath);
            }
            try (FileInputStream fis = new FileInputStream(zipFile);
                 ZipInputStream zis = new ZipInputStream(fis, charset)) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    Path entryPath = destPath.resolve(entry.getName()).normalize();
                    if (!entryPath.startsWith(destPath)) {
                        log.error("ZIP文件包含非法路径，跳过: {}", entry.getName());
                        continue;
                    }
                    if (entry.isDirectory()) {
                        Files.createDirectories(entryPath);
                    } else {
                        Path parentPath = entryPath.getParent();
                        if (parentPath != null && !Files.exists(parentPath)) {
                            Files.createDirectories(parentPath);
                        }
                        try (FileOutputStream fos = new FileOutputStream(entryPath.toFile())) {
                            byte[] buffer = new byte[8192];
                            int len;
                            while ((len = zis.read(buffer)) > 0) {
                                fos.write(buffer, 0, len);
                            }
                        }
                    }
                    zis.closeEntry();
                }
            }
            log.info("ZIP文件解压成功: {} -> {}", zipFilePath, destDirPath);
            return true;
        } catch (IOException e) {
            log.error("ZIP文件解压失败", e);
            return false;
        }
    }

    /**
     * 添加文件到ZIP输出流
     * @param file 文件对象
     * @param entryName ZIP条目名称
     * @param zos ZIP输出流
     * @throws IOException IO异常
     */
    private static void addFileToZip(File file, String entryName, ZipOutputStream zos) throws IOException {
        ZipEntry zipEntry = new ZipEntry(entryName);
        zos.putNextEntry(zipEntry);
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[8192];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }
        }
        zos.closeEntry();
    }

    /**
     * 添加目录到ZIP输出流
     * @param dir 目录对象
     * @param entryName ZIP条目名称
     * @param zos ZIP输出流
     * @throws IOException IO异常
     */
    private static void addDirectoryToZip(File dir, String entryName, ZipOutputStream zos) throws IOException {
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            String newEntryName = entryName + "/" + file.getName();
            if (file.isFile()) {
                addFileToZip(file, newEntryName, zos);
            } else if (file.isDirectory()) {
                addDirectoryToZip(file, newEntryName, zos);
            }
        }
    }
}
