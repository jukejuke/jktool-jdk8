package io.github.jukejuke.tool.url;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * URL 编解码工具类
 * 提供 URL 编码和解码的静态方法
 */
public class UrlUtils {

    /**
     * 默认字符编码
     */
    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * URL 编码
     * 
     * @param value 需要编码的字符串
     * @return 编码后的字符串
     */
    public static String encode(String value) {
        return encode(value, DEFAULT_CHARSET);
    }

    /**
     * URL 编码
     * 
     * @param value   需要编码的字符串
     * @param charset 字符编码
     * @return 编码后的字符串
     */
    public static String encode(String value, String charset) {
        if (value == null) {
            return null;
        }
        try {
            return URLEncoder.encode(value, charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("URL encode failed: " + e.getMessage(), e);
        }
    }

    /**
     * URL 解码
     * 
     * @param value 需要解码的字符串
     * @return 解码后的字符串
     */
    public static String decode(String value) {
        return decode(value, DEFAULT_CHARSET);
    }

    /**
     * URL 解码
     * 
     * @param value   需要解码的字符串
     * @param charset 字符编码
     * @return 解码后的字符串
     */
    public static String decode(String value, String charset) {
        if (value == null) {
            return null;
        }
        try {
            return URLDecoder.decode(value, charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("URL decode failed: " + e.getMessage(), e);
        }
    }
}
