package io.github.jukejuke.tool.file;

import io.github.jukejuke.tool.string.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * 文件管理工具类
 * 提供常用的文件和目录操作功能
 */
public class FileUtils {

    /** 默认字符集 */
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * 创建目录（支持递归创建）
     * @param directoryPath 目录路径
     * @return 是否创建成功
     */
    public static boolean mkdirs(String directoryPath) {
        if (StringUtils.isEmpty(directoryPath)) {
            return false;
        }
        File directory = new File(directoryPath);
        return directory.exists() || directory.mkdirs();
    }

    /**
     * 删除目录（支持递归删除）
     * @param directoryPath 目录路径
     * @return 是否删除成功
     */
    public static boolean deleteDirectory(String directoryPath) {
        if (StringUtils.isEmpty(directoryPath)) {
            return false;
        }
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            return false;
        }

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file.getAbsolutePath());
                } else {
                    file.delete();
                }
            }
        }
        return directory.delete();
    }

    /**
     * 删除文件
     * @param filePath 文件路径
     * @return 是否删除成功
     */
    public static boolean deleteFile(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        return file.exists() && file.isFile() && file.delete();
    }

    /**
     * 创建文件（如果目录不存在则创建目录）
     * @param filePath 文件路径
     * @return 是否创建成功
     */
    public static boolean createFile(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return false;
        }
        try {
            File file = new File(filePath);
            if (file.exists()) {
                return true;
            }
            // 创建父目录
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 写入文件内容（覆盖模式）
     * @param filePath 文件路径
     * @param content 文件内容
     * @return 是否写入成功
     */
    public static boolean writeFile(String filePath, String content) {
        return writeFile(filePath, content, DEFAULT_CHARSET, false);
    }

    /**
     * 写入文件内容
     * @param filePath 文件路径
     * @param content 文件内容
     * @param append 是否追加模式
     * @return 是否写入成功
     */
    public static boolean writeFile(String filePath, String content, boolean append) {
        return writeFile(filePath, content, DEFAULT_CHARSET, append);
    }

    /**
     * 写入文件内容
     * @param filePath 文件路径
     * @param content 文件内容
     * @param charset 字符集
     * @param append 是否追加模式
     * @return 是否写入成功
     */
    public static boolean writeFile(String filePath, String content, Charset charset, boolean append) {
        if (StringUtils.isEmpty(filePath) || content == null) {
            return false;
        }
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(filePath, append), charset))) {
            writer.write(content);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 读取文件内容
     * @param filePath 文件路径
     * @return 文件内容
     */
    public static String readFile(String filePath) {
        return readFile(filePath, DEFAULT_CHARSET);
    }

    /**
     * 读取文件内容
     * @param filePath 文件路径
     * @param charset 字符集
     * @return 文件内容
     */
    public static String readFile(String filePath, Charset charset) {
        if (StringUtils.isEmpty(filePath)) {
            return null;
        }
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(filePath), charset))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
            // 移除最后一个换行符
            if (content.length() > 0) {
                content.delete(content.length() - System.lineSeparator().length(), content.length());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return content.toString();
    }

    /**
     * 读取文件行列表
     * @param filePath 文件路径
     * @return 文件行列表
     */
    public static List<String> readFileLines(String filePath) {
        return readFileLines(filePath, DEFAULT_CHARSET);
    }

    /**
     * 读取文件行列表
     * @param filePath 文件路径
     * @param charset 字符集
     * @return 文件行列表
     */
    public static List<String> readFileLines(String filePath, Charset charset) {
        if (StringUtils.isEmpty(filePath)) {
            return new ArrayList<>();
        }
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(filePath), charset))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    /**
     * 复制文件
     * @param sourceFilePath 源文件路径
     * @param targetFilePath 目标文件路径
     * @return 是否复制成功
     */
    public static boolean copyFile(String sourceFilePath, String targetFilePath) {
        if (StringUtils.isEmpty(sourceFilePath) || StringUtils.isEmpty(targetFilePath)) {
            return false;
        }
        try {
            Path sourcePath = Paths.get(sourceFilePath);
            Path targetPath = Paths.get(targetFilePath);
            // 创建目标文件的父目录（如果存在）
            Path parentPath = targetPath.getParent();
            if (parentPath != null) {
                Files.createDirectories(parentPath);
            }
            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移动文件
     * @param sourceFilePath 源文件路径
     * @param targetFilePath 目标文件路径
     * @return 是否移动成功
     */
    public static boolean moveFile(String sourceFilePath, String targetFilePath) {
        if (StringUtils.isEmpty(sourceFilePath) || StringUtils.isEmpty(targetFilePath)) {
            return false;
        }
        try {
            Path sourcePath = Paths.get(sourceFilePath);
            Path targetPath = Paths.get(targetFilePath);
            // 创建目标文件的父目录（如果存在）
            Path parentPath = targetPath.getParent();
            if (parentPath != null) {
                Files.createDirectories(parentPath);
            }
            Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 复制目录
     * @param sourceDirPath 源目录路径
     * @param targetDirPath 目标目录路径
     * @return 是否复制成功
     */
    public static boolean copyDirectory(String sourceDirPath, String targetDirPath) {
        if (StringUtils.isEmpty(sourceDirPath) || StringUtils.isEmpty(targetDirPath)) {
            return false;
        }
        try {
            Path sourcePath = Paths.get(sourceDirPath);
            Path targetPath = Paths.get(targetDirPath);
            
            Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Path targetDir = targetPath.resolve(sourcePath.relativize(dir));
                    Files.createDirectories(targetDir);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path targetFile = targetPath.resolve(sourcePath.relativize(file));
                    Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                    return FileVisitResult.CONTINUE;
                }
            });
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取文件大小
     * @param filePath 文件路径
     * @return 文件大小（字节），如果文件不存在则返回 -1
     */
    public static long getFileSize(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return -1;
        }
        File file = new File(filePath);
        return file.exists() && file.isFile() ? file.length() : -1;
    }

    /**
     * 获取文件扩展名
     * @param fileName 文件名
     * @return 文件扩展名（不含点），如果没有扩展名则返回空字符串
     */
    public static String getFileExtension(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf('.');
        return lastDotIndex > 0 && lastDotIndex < fileName.length() - 1 ? 
                fileName.substring(lastDotIndex + 1) : "";
    }

    /**
     * 获取文件名（不含扩展名）
     * @param fileName 文件名
     * @return 文件名（不含扩展名）
     */
    public static String getFileNameWithoutExtension(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf('.');
        return lastDotIndex > 0 ? fileName.substring(0, lastDotIndex) : fileName;
    }

    /**
     * 获取文件名（不含路径）
     * @param filePath 文件路径
     * @return 文件名
     */
    public static String getFileName(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return "";
        }
        File file = new File(filePath);
        return file.getName();
    }

    /**
     * 获取文件父目录路径
     * @param filePath 文件路径
     * @return 父目录路径
     */
    public static String getParentDirectory(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return "";
        }
        File file = new File(filePath);
        File parent = file.getParentFile();
        return parent != null ? parent.getAbsolutePath() : "";
    }

    /**
     * 检查文件是否存在
     * @param filePath 文件路径
     * @return 是否存在
     */
    public static boolean exists(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return false;
        }
        return new File(filePath).exists();
    }

    /**
     * 检查是否为文件
     * @param filePath 文件路径
     * @return 是否为文件
     */
    public static boolean isFile(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        return file.exists() && file.isFile();
    }

    /**
     * 检查是否为目录
     * @param filePath 文件路径
     * @return 是否为目录
     */
    public static boolean isDirectory(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        return file.exists() && file.isDirectory();
    }

    /**
     * 列出目录下的文件
     * @param directoryPath 目录路径
     * @return 文件列表
     */
    public static List<String> listFiles(String directoryPath) {
        return listFiles(directoryPath, false);
    }

    /**
     * 列出目录下的文件
     * @param directoryPath 目录路径
     * @param recursive 是否递归列出子目录
     * @return 文件列表
     */
    public static List<String> listFiles(String directoryPath, boolean recursive) {
        List<String> files = new ArrayList<>();
        if (StringUtils.isEmpty(directoryPath) || !isDirectory(directoryPath)) {
            return files;
        }

        File directory = new File(directoryPath);
        File[] fileArray = directory.listFiles();
        if (fileArray == null) {
            return files;
        }

        for (File file : fileArray) {
            if (file.isFile()) {
                files.add(file.getAbsolutePath());
            } else if (file.isDirectory() && recursive) {
                files.addAll(listFiles(file.getAbsolutePath(), true));
            }
        }
        return files;
    }

    /**
     * 列出目录下的文件（按扩展名过滤）
     * @param directoryPath 目录路径
     * @param extension 文件扩展名（不含点）
     * @param recursive 是否递归列出子目录
     * @return 文件列表
     */
    public static List<String> listFilesByExtension(String directoryPath, String extension, boolean recursive) {
        List<String> files = listFiles(directoryPath, recursive);
        List<String> filteredFiles = new ArrayList<>();
        if (StringUtils.isEmpty(extension)) {
            return files;
        }

        for (String file : files) {
            String fileExt = getFileExtension(file);
            if (extension.equalsIgnoreCase(fileExt)) {
                filteredFiles.add(file);
            }
        }
        return filteredFiles;
    }

    /**
     * 重命名文件
     * @param oldFilePath 旧文件路径
     * @param newFilePath 新文件路径
     * @return 是否重命名成功
     */
    public static boolean renameFile(String oldFilePath, String newFilePath) {
        if (StringUtils.isEmpty(oldFilePath) || StringUtils.isEmpty(newFilePath)) {
            return false;
        }
        File oldFile = new File(oldFilePath);
        File newFile = new File(newFilePath);
        return oldFile.exists() && oldFile.renameTo(newFile);
    }

    /**
     * 获取文件最后修改时间
     * @param filePath 文件路径
     * @return 最后修改时间（毫秒时间戳），如果文件不存在则返回 -1
     */
    public static long getLastModifiedTime(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return -1;
        }
        File file = new File(filePath);
        return file.exists() ? file.lastModified() : -1;
    }

    /**
     * 清空文件内容
     * @param filePath 文件路径
     * @return 是否清空成功
     */
    public static boolean clearFile(String filePath) {
        return writeFile(filePath, "");
    }
}
