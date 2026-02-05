package io.github.jukejuke.tool.config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Properties配置文件读写工具类
 * 提供从类路径或文件系统读取、修改和保存properties配置文件的功能
 */
public class PropertiesUtils {

    /**
     * 从类路径保存properties文件
     * <p>注意：此方法仅适用于开发环境，当应用程序以JAR文件形式运行时，无法修改JAR内部的配置文件</p>
     * @param properties Properties对象
     * @param filePath 类路径下的文件路径，如 "config.properties"
     * @throws IOException 保存文件时发生异常
     */
    public static void saveToClassPath(Properties properties, String filePath) throws IOException {
        if (properties == null) {
            throw new IllegalArgumentException("Properties对象不能为空");
        }
        
        java.net.URL url = PropertiesUtils.class.getClassLoader().getResource(filePath);
        if (url == null) {
            throw new IOException("配置文件不存在: " + filePath);
        }
        
        String protocol = url.getProtocol();
        if ("jar".equals(protocol)) {
            throw new IOException("无法修改JAR文件中的配置文件: " + filePath + ", 请使用saveToFileSystem方法保存到文件系统");
        }
        
        try (OutputStream outputStream = new FileOutputStream(url.getPath())) {
            properties.store(outputStream, null);
        }
    }
    
    /**
     * 从类路径加载properties文件
     * @param filePath 类路径下的文件路径，如 "config.properties"
     * @return Properties对象
     * @throws IOException 读取文件时发生异常
     */
    public static Properties loadFromClassPath(String filePath) throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = PropertiesUtils.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new IOException("配置文件不存在: " + filePath);
            }
            properties.load(inputStream);
        }
        return properties;
    }

    /**
     * 从文件系统加载properties文件
     * @param filePath 文件系统的绝对路径，如 "D:/config.properties"
     * @return Properties对象
     * @throws IOException 读取文件时发生异常
     */
    public static Properties loadFromFileSystem(String filePath) throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = new FileInputStream(filePath)) {
            properties.load(inputStream);
        }
        return properties;
    }

    /**
     * 获取字符串类型的属性值
     * @param properties Properties对象
     * @param key 属性键
     * @param defaultValue 默认值
     * @return 属性值或默认值
     */
    public static String getString(Properties properties, String key, String defaultValue) {
        if (properties == null) {
            return defaultValue;
        }
        String value = properties.getProperty(key);
        return value != null ? value : defaultValue;
    }

    /**
     * 获取字符串类型的属性值
     * @param properties Properties对象
     * @param key 属性键
     * @return 属性值或null
     */
    public static String getString(Properties properties, String key) {
        return getString(properties, key, null);
    }

    /**
     * 获取整数类型的属性值
     * @param properties Properties对象
     * @param key 属性键
     * @param defaultValue 默认值
     * @return 属性值或默认值
     */
    public static Integer getInteger(Properties properties, String key, Integer defaultValue) {
        String value = getString(properties, key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 获取整数类型的属性值
     * @param properties Properties对象
     * @param key 属性键
     * @return 属性值或null
     */
    public static Integer getInteger(Properties properties, String key) {
        return getInteger(properties, key, null);
    }

    /**
     * 获取长整数类型的属性值
     * @param properties Properties对象
     * @param key 属性键
     * @param defaultValue 默认值
     * @return 属性值或默认值
     */
    public static Long getLong(Properties properties, String key, Long defaultValue) {
        String value = getString(properties, key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 获取长整数类型的属性值
     * @param properties Properties对象
     * @param key 属性键
     * @return 属性值或null
     */
    public static Long getLong(Properties properties, String key) {
        return getLong(properties, key, null);
    }

    /**
     * 获取布尔类型的属性值
     * @param properties Properties对象
     * @param key 属性键
     * @param defaultValue 默认值
     * @return 属性值或默认值
     */
    public static Boolean getBoolean(Properties properties, String key, Boolean defaultValue) {
        String value = getString(properties, key);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    /**
     * 获取布尔类型的属性值
     * @param properties Properties对象
     * @param key 属性键
     * @return 属性值或null
     */
    public static Boolean getBoolean(Properties properties, String key) {
        return getBoolean(properties, key, null);
    }

    /**
     * 获取双精度浮点数类型的属性值
     * @param properties Properties对象
     * @param key 属性键
     * @param defaultValue 默认值
     * @return 属性值或默认值
     */
    public static Double getDouble(Properties properties, String key, Double defaultValue) {
        String value = getString(properties, key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 获取双精度浮点数类型的属性值
     * @param properties Properties对象
     * @param key 属性键
     * @return 属性值或null
     */
    public static Double getDouble(Properties properties, String key) {
        return getDouble(properties, key, null);
    }
    
    /**
     * 从文件系统保存properties文件
     * @param properties Properties对象
     * @param filePath 文件系统的绝对路径，如 "D:/config.properties"
     * @throws IOException 保存文件时发生异常
     */
    public static void saveToFileSystem(Properties properties, String filePath) throws IOException {
        if (properties == null) {
            throw new IllegalArgumentException("Properties对象不能为空");
        }
        
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            properties.store(outputStream, null);
        }
    }
}
