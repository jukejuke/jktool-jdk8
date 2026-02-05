package io.github.jukejuke.tool.http;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * HTTP 工具类 - 封装常用 HTTP 请求静态方法
 * 
 * @author jukejuke
 */
@Slf4j
public class HttpUtil {
    
    /**
     * 发送 GET 请求（无参数）
     * 
     * @param url 请求地址
     * @return 响应内容
     */
    public static String get(String url) {
        return get(url, null);
    }
    
    /**
     * 发送 GET 请求（带参数）
     * 
     * @param url 请求地址
     * @param params 参数 map，会自动拼接到 URL 后面
     * @return 响应内容
     */
    public static String get(String url, Map<String, Object> params) {
        return get(url, params, StandardCharsets.UTF_8.name());
    }
    
    /**
     * 发送 GET 请求（带参数，自定义字符集）
     * 
     * @param url 请求地址
     * @param params 参数 map，会自动拼接到 URL 后面
     * @param charset 字符集名称，如 UTF-8、GBK、ISO-8859-1 等
     * @return 响应内容
     */
    public static String get(String url, Map<String, Object> params, String charset) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        StringBuilder result = new StringBuilder();
        
        try {
            // 如果有参数，拼接到 URL 后面
            if (params != null && !params.isEmpty()) {
                StringBuilder paramBuilder = new StringBuilder();
                boolean first = true;
                for (Map.Entry<String, Object> param : params.entrySet()) {
                    if (!first) {
                        paramBuilder.append("&");
                    }
                    paramBuilder.append(URLEncoder.encode(param.getKey(), charset))
                               .append("=")
                               .append(URLEncoder.encode(String.valueOf(param.getValue()), charset));
                    first = false;
                }
                url += "?" + paramBuilder.toString();
            }
            
            // 创建 URL 连接
            URL requestUrl = new URL(url);
            connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
            
            // 读取响应
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
            } else {
                // 读取错误流
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), charset));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
            }
            
        } catch (Exception e) {
            log.error("HTTP GET request failed for URL: {}", url, e);
            result.setLength(0);
            result.append("HTTP Error: ").append(e.getMessage());
        } finally {
            // 关闭资源
            try {
                if (reader != null) reader.close();
                if (connection != null) connection.disconnect();
            } catch (IOException e) {
                log.error("Error closing resources for GET request: {}", url, e);
            }
        }
        
        return result.toString();
    }
    
    /**
     * 发送 POST 请求（带参数）
     * 
     * @param url 请求地址
     * @param params 参数 map，会以 application/x-www-form-urlencoded 方式发送
     * @return 响应内容
     */
    public static String post(String url, Map<String, Object> params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        OutputStream outputStream = null;
        StringBuilder result = new StringBuilder();
        
        try {
            // 创建 URL 连接
            URL requestUrl = new URL(url);
            connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            
            // 写入请求参数
            if (params != null && !params.isEmpty()) {
                StringBuilder paramBuilder = new StringBuilder();
                boolean first = true;
                for (Map.Entry<String, Object> param : params.entrySet()) {
                    if (!first) {
                        paramBuilder.append("&");
                    }
                    paramBuilder.append(URLEncoder.encode(param.getKey(), StandardCharsets.UTF_8.toString()))
                               .append("=")
                               .append(URLEncoder.encode(String.valueOf(param.getValue()), StandardCharsets.UTF_8.toString()));
                    first = false;
                }
                
                outputStream = connection.getOutputStream();
                outputStream.write(paramBuilder.toString().getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
            }
            
            // 读取响应
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
            } else {
                // 读取错误流
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
            }
            
        } catch (Exception e) {
            log.error("HTTP POST request failed for URL: {}", url, e);
            result.setLength(0);
            result.append("HTTP Error: ").append(e.getMessage());
        } finally {
            // 关闭资源
            try {
                if (outputStream != null) outputStream.close();
                if (reader != null) reader.close();
                if (connection != null) connection.disconnect();
            } catch (IOException e) {
                log.error("Error closing resources for POST request: {}", url, e);
            }
        }
        
        return result.toString();
    }
    
    /**
     * 发送 POST JSON 请求
     * 
     * @param url 请求地址
     * @param json JSON 字符串
     * @return 响应内容
     */
    public static String postJson(String url, String json) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        OutputStream outputStream = null;
        StringBuilder result = new StringBuilder();
        
        try {
            // 创建 URL 连接
            URL requestUrl = new URL(url);
            connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            
            // 写入 JSON 数据
            if (json != null && !json.isEmpty()) {
                outputStream = connection.getOutputStream();
                outputStream.write(json.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
            }
            
            // 读取响应
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
            }
            
        } catch (Exception e) {
            log.error("HTTP POST JSON request failed for URL: {}", url, e);
            result.setLength(0);
            result.append("HTTP Error: ").append(e.getMessage());
        } finally {
            try {
                if (outputStream != null) outputStream.close();
                if (reader != null) reader.close();
                if (connection != null) connection.disconnect();
            } catch (IOException e) {
                log.error("Error closing resources for POST JSON request: {}", url, e);
            }
        }
        
        return result.toString();
    }
}

