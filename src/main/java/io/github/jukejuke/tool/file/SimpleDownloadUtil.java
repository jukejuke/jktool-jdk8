package io.github.jukejuke.tool.file;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 简单的文件下载工具类（不依赖SLF4J）
 */
public class SimpleDownloadUtil {

    /**
     * 下载文件从指定URL到指定目录
     * @param urlStr 文件URL地址
     * @param saveDir 保存目录路径
     * @return 下载的文件路径，如果下载失败返回null
     */
    public static String download(String urlStr, String saveDir) {
        return download(urlStr, saveDir, null);
    }

    /**
     * 下载文件从指定URL到指定目录，可指定保存文件名
     * @param urlStr 文件URL地址
     * @param saveDir 保存目录路径
     * @param fileName 保存的文件名，如果为null则从URL中提取
     * @return 下载的文件路径，如果下载失败返回null
     */
    public static String download(String urlStr, String saveDir, String fileName) {
        // 创建保存目录
        if (!FileUtils.mkdirs(saveDir)) {
            System.err.println("Failed to create save directory: " + saveDir);
            return null;
        }

        // 如果未指定文件名，从URL中提取
        if (fileName == null || fileName.isEmpty()) {
            fileName = extractFileNameFromUrl(urlStr);
        }

        // 构建完整的保存路径
        String savePath = saveDir + File.separator + fileName;

        HttpURLConnection connection = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            // 创建URL对象
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(60000);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");

            // 获取响应码
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.err.println("HTTP request failed with response code: " + responseCode);
                return null;
            }

            // 获取输入流
            inputStream = connection.getInputStream();
            
            // 创建输出流
            outputStream = new FileOutputStream(savePath);

            // 缓冲区大小
            byte[] buffer = new byte[4096];
            int bytesRead;
            long totalBytesRead = 0;
            long contentLength = connection.getContentLengthLong();

            // 读取数据并写入文件
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
                
                // 打印下载进度（可选）
                if (contentLength > 0) {
                    int progress = (int) ((totalBytesRead * 100) / contentLength);
                    if (progress % 10 == 0) {
                        System.out.println("Download progress: " + progress + "%");
                    }
                }
            }

            System.out.println("File downloaded successfully: " + savePath);
            return savePath;

        } catch (Exception e) {
            System.err.println("Failed to download file from URL: " + urlStr);
            e.printStackTrace();
            // 下载失败，删除部分文件
            FileUtils.deleteFile(savePath);
            return null;
        } finally {
            // 关闭资源
            try {
                if (outputStream != null) outputStream.close();
                if (inputStream != null) inputStream.close();
                if (connection != null) connection.disconnect();
            } catch (IOException e) {
                System.err.println("Error closing resources during download");
                e.printStackTrace();
            }
        }
    }

    /**
     * 从URL中提取文件名
     * @param urlStr URL地址
     * @return 提取的文件名
     */
    private static String extractFileNameFromUrl(String urlStr) {
        try {
            URL url = new URL(urlStr);
            String path = url.getPath();
            String fileName = FileUtils.getFileName(path);
            
            // 如果从路径中提取不到文件名，使用时间戳作为文件名
            if (fileName.isEmpty()) {
                fileName = System.currentTimeMillis() + ".tmp";
            }
            
            return fileName;
        } catch (Exception e) {
            System.err.println("Failed to extract file name from URL: " + urlStr);
            e.printStackTrace();
            return System.currentTimeMillis() + ".tmp";
        }
    }

    /**
     * 下载文件为byte数组
     * @param urlStr 文件URL地址
     * @return 下载的文件内容作为byte数组，如果下载失败返回null
     */
    public static byte[] downloadAsByteArray(String urlStr) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;

        try {
            // 创建URL对象
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(60000);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");

            // 获取响应码
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.err.println("HTTP request failed with response code: " + responseCode);
                return null;
            }

            // 获取输入流
            inputStream = connection.getInputStream();
            
            // 创建字节数组输出流
            outputStream = new ByteArrayOutputStream();

            // 缓冲区大小
            byte[] buffer = new byte[4096];
            int bytesRead;
            long totalBytesRead = 0;
            long contentLength = connection.getContentLengthLong();

            // 读取数据并写入字节数组
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
                
                // 打印下载进度（可选）
                if (contentLength > 0) {
                    int progress = (int) ((totalBytesRead * 100) / contentLength);
                    if (progress % 10 == 0) {
                        System.out.println("Download progress: " + progress + "%");
                    }
                }
            }

            byte[] result = outputStream.toByteArray();
            System.out.println("File downloaded successfully as byte array. Size: " + result.length + " bytes");
            return result;

        } catch (Exception e) {
            System.err.println("Failed to download file as byte array from URL: " + urlStr);
            e.printStackTrace();
            return null;
        } finally {
            // 关闭资源
            try {
                if (outputStream != null) outputStream.close();
                if (inputStream != null) inputStream.close();
                if (connection != null) connection.disconnect();
            } catch (IOException e) {
                System.err.println("Error closing resources during download");
                e.printStackTrace();
            }
        }
    }
}
