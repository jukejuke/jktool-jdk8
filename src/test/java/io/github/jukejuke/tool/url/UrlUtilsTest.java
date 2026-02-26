package io.github.jukejuke.tool.url;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UrlUtils 测试类
 * 测试 URL 编码和解码功能
 */
public class UrlUtilsTest {

    /**
     * 测试 URL 编码（默认 UTF-8 编码）
     */
    @Test
    void testEncode() {
        // 测试普通字符串
        assertEquals("hello", UrlUtils.encode("hello"));
        
        // 测试包含特殊字符的字符串
        assertEquals("hello%20world", UrlUtils.encode("hello world"));
        assertEquals("hello%2Bworld", UrlUtils.encode("hello+world"));
        assertEquals("hello%26world", UrlUtils.encode("hello&world"));
        
        // 测试中文
        assertEquals("%E4%B8%AD%E6%96%87", UrlUtils.encode("中文"));
        
        // 测试空值
        assertNull(UrlUtils.encode(null));
    }

    /**
     * 测试 URL 编码（指定编码）
     */
    @Test
    void testEncodeWithCharset() {
        // 测试 UTF-8 编码
        assertEquals("hello%20world", UrlUtils.encode("hello world", "UTF-8"));
        
        // 测试中文
        assertEquals("%E4%B8%AD%E6%96%87", UrlUtils.encode("中文", "UTF-8"));
        
        // 测试空值
        assertNull(UrlUtils.encode(null, "UTF-8"));
    }

    /**
     * 测试 URL 解码（默认 UTF-8 编码）
     */
    @Test
    void testDecode() {
        // 测试普通字符串
        assertEquals("hello", UrlUtils.decode("hello"));
        
        // 测试包含特殊字符的字符串
        assertEquals("hello world", UrlUtils.decode("hello%20world"));
        assertEquals("hello+world", UrlUtils.decode("hello%2Bworld"));
        assertEquals("hello&world", UrlUtils.decode("hello%26world"));
        
        // 测试中文
        assertEquals("中文", UrlUtils.decode("%E4%B8%AD%E6%96%87"));
        
        // 测试空值
        assertNull(UrlUtils.decode(null));
    }

    /**
     * 测试 URL 解码（指定编码）
     */
    @Test
    void testDecodeWithCharset() {
        // 测试 UTF-8 编码
        assertEquals("hello world", UrlUtils.decode("hello%20world", "UTF-8"));
        
        // 测试中文
        assertEquals("中文", UrlUtils.decode("%E4%B8%AD%E6%96%87", "UTF-8"));
        
        // 测试空值
        assertNull(UrlUtils.decode(null, "UTF-8"));
    }

    /**
     * 测试异常处理（无效编码）
     */
    @Test
    void testInvalidCharset() {
        // 测试无效编码时的异常
        assertThrows(RuntimeException.class, () -> {
            UrlUtils.encode("test", "INVALID_CHARSET");
        });
        
        assertThrows(RuntimeException.class, () -> {
            UrlUtils.decode("test", "INVALID_CHARSET");
        });
    }
}
