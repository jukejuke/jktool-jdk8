package io.github.jukejuke.tool.http;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * HttpUtil 测试用例
 */
public class HttpUtilTest {
    
    @BeforeEach
    void setUp() {
        System.out.println("=== 开始测试 ===");
    }
    
    @AfterEach
    void tearDown() {
        System.out.println("=== 测试结束 ===\n");
    }
    
    @Test
    @DisplayName("GET 请求 - 无参数测试")
    void testGetWithoutParams() {
        String result = HttpUtil.get("https://www.baidu.com");
        
        assertNotNull(result, "响应结果不应为空");
        assertFalse(result.isEmpty(), "响应结果不应为空字符串");
        // 百度首页应该包含特定内容
        assertTrue(result.contains("百度") || result.contains("baidu"), "响应应该包含百度相关内容");
        System.out.println("GET 无参数测试通过，响应长度: " + result.length());
    }
    
    @Test
    @DisplayName("GET 请求 - 带参数测试")
    void testGetWithParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("wd", "测试");
        params.put("oq", "测试");
        
        String result = HttpUtil.get("https://www.baidu.com/s", params);
        
        assertNotNull(result, "响应结果不应为空");
        assertFalse(result.isEmpty(), "响应结果不应为空字符串");
        System.out.println("GET 带参数测试通过，响应长度: " + result.length());
    }
    
    @Test
    @DisplayName("GET 请求 - 空参数测试")
    void testGetWithEmptyParams() {
        Map<String, Object> params = new HashMap<>();
        
        String result = HttpUtil.get("https://www.baidu.com", params);
        
        assertNotNull(result, "响应结果不应为空");
        assertFalse(result.isEmpty(), "响应结果不应为空字符串");
        System.out.println("GET 空参数测试通过");
    }
    
    @Test
    @DisplayName("GET 请求 - null 参数测试")
    void testGetWithNullParams() {
        String result = HttpUtil.get("https://www.baidu.com", null);
        
        assertNotNull(result, "响应结果不应为空");
        assertFalse(result.isEmpty(), "响应结果不应为空字符串");
        System.out.println("GET null 参数测试通过");
    }
    
    @Test
    @DisplayName("POST 请求 - 表单参数测试")
    void testPostWithParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("city", "北京");
        params.put("district", "海淀区");
        
        String result = HttpUtil.post("https://www.baidu.com", params);
        
        assertNotNull(result, "响应结果不应为空");
        // POST 请求应该能正常发送并收到响应
        assertTrue(result.length() > 0, "响应应该有内容");
        System.out.println("POST 表单参数测试通过，响应长度: " + result.length());
    }
    
    @Test
    @DisplayName("POST 请求 - 空参数测试")
    void testPostWithEmptyParams() {
        Map<String, Object> params = new HashMap<>();
        
        String result = HttpUtil.post("https://www.baidu.com", params);
        
        assertNotNull(result, "响应结果不应为空");
        System.out.println("POST 空参数测试通过");
    }
    
    @Test
    @DisplayName("POST 请求 - null 参数测试")
    void testPostWithNullParams() {
        String result = HttpUtil.post("https://www.baidu.com", null);
        
        assertNotNull(result, "响应结果不应为空");
        assertFalse(result.isEmpty(), "响应结果不应为空字符串");
        System.out.println("POST null 参数测试通过");
    }
    
    @Test
    @DisplayName("POST JSON 请求测试")
    void testPostJson() {
        String json = "{\"name\":\"测试\",\"age\":25}";
        
        String result = HttpUtil.postJson("https://www.baidu.com", json);
        
        assertNotNull(result, "响应结果不应为空");
        System.out.println("POST JSON 测试通过，响应长度: " + result.length());
    }
    
    @Test
    @DisplayName("POST JSON 请求 - 空 JSON 测试")
    void testPostJsonWithEmptyJson() {
        String result = HttpUtil.postJson("https://www.baidu.com", "");
        
        assertNotNull(result, "响应结果不应为空");
        System.out.println("POST 空 JSON 测试通过");
    }
    
    @Test
    @DisplayName("POST JSON 请求 - null JSON 测试")
    void testPostJsonWithNullJson() {
        String result = HttpUtil.postJson("https://www.baidu.com", null);
        
        assertNotNull(result, "响应结果不应为空");
        System.out.println("POST null JSON 测试通过");
    }
    
    @Test
    @DisplayName("GET 请求 - URL 编码测试（中文参数）")
    void testGetWithChineseParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", "中国");
        params.put("city", "北京");
        
        String result = HttpUtil.get("https://www.baidu.com/s", params);
        
        assertNotNull(result, "响应结果不应为空");
        assertFalse(result.isEmpty(), "响应结果不应为空字符串");
        System.out.println("GET 中文参数测试通过");
    }
    
    @Test
    @DisplayName("POST 请求 - URL 编码测试（中文参数）")
    void testPostWithChineseParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "张三");
        params.put("address", "北京市朝阳区");
        
        String result = HttpUtil.post("https://www.baidu.com", params);
        
        assertNotNull(result, "响应结果不应为空");
        System.out.println("POST 中文参数测试通过");
    }
    
    @Test
    @DisplayName("GET 请求 - 特殊字符参数测试")
    void testGetWithSpecialChars() {
        Map<String, Object> params = new HashMap<>();
        params.put("query", "hello world");
        params.put("symbol", "a+b=c");
        
        String result = HttpUtil.get("https://www.baidu.com/s", params);
        
        assertNotNull(result, "响应结果不应为空");
        System.out.println("GET 特殊字符参数测试通过");
    }
    
    @Test
    @DisplayName("HTTP 错误处理测试（无效 URL）")
    void testErrorHandling() {
        // 使用一个不存在的域名来测试错误处理
        String result = HttpUtil.get("https://this-domain-does-not-exist-12345.xyz");
        
        // 应该返回错误信息而不是抛出异常
        assertNotNull(result, "错误处理结果不应为空");
        assertTrue(result.contains("HTTP Error") || result.contains("Error") || result.length() > 0, 
                   "应该返回错误信息或空结果");
        System.out.println("错误处理测试通过: " + result);
    }
    
    // ==================== 自定义字符集测试 ====================
    
    @Test
    @DisplayName("GET 请求 - 自定义 UTF-8 字符集测试")
    void testGetWithUTF8Charset() {
        Map<String, Object> params = new HashMap<>();
        params.put("wd", "测试");
        params.put("city", "北京");
        
        String result = HttpUtil.get("https://www.baidu.com/s", params, "UTF-8");
        
        assertNotNull(result, "响应结果不应为空");
        assertFalse(result.isEmpty(), "响应结果不应为空字符串");
        System.out.println("GET UTF-8 字符集测试通过，响应长度: " + result.length());
    }
    
    @Test
    @DisplayName("GET 请求 - GBK 字符集测试")
    void testGetWithGBKCharset() {
        Map<String, Object> params = new HashMap<>();
        params.put("wd", "测试");
        params.put("city", "北京");
        
        String result = HttpUtil.get("https://www.baidu.com/s", params, "GBK");
        
        assertNotNull(result, "响应结果不应为空");
        assertFalse(result.isEmpty(), "响应结果不应为空字符串");
        System.out.println("GET GBK 字符集测试通过，响应长度: " + result.length());
    }
    
    @Test
    @DisplayName("GET 请求 - ISO-8859-1 字符集测试")
    void testGetWithISO88591Charset() {
        Map<String, Object> params = new HashMap<>();
        params.put("wd", "test");
        params.put("query", "hello");
        
        String result = HttpUtil.get("https://www.baidu.com/s", params, "ISO-8859-1");
        
        assertNotNull(result, "响应结果不应为空");
        assertFalse(result.isEmpty(), "响应结果不应为空字符串");
        System.out.println("GET ISO-8859-1 字符集测试通过，响应长度: " + result.length());
    }
    
    @Test
    @DisplayName("GET 请求 - GB2312 字符集测试")
    void testGetWithGB2312Charset() {
        Map<String, Object> params = new HashMap<>();
        params.put("wd", "测试");
        params.put("city", "上海");
        
        String result = HttpUtil.get("https://www.baidu.com/s", params, "GB2312");
        
        assertNotNull(result, "响应结果不应为空");
        assertFalse(result.isEmpty(), "响应结果不应为空字符串");
        System.out.println("GET GB2312 字符集测试通过，响应长度: " + result.length());
    }
    
    @Test
    @DisplayName("GET 请求 - 空参数 + 自定义字符集测试")
    void testGetEmptyParamsWithCharset() {
        Map<String, Object> params = new HashMap<>();
        
        String result = HttpUtil.get("https://www.baidu.com", params, "UTF-8");
        
        assertNotNull(result, "响应结果不应为空");
        assertFalse(result.isEmpty(), "响应结果不应为空字符串");
        System.out.println("GET 空参数 + 自定义字符集测试通过");
    }
    
    @Test
    @DisplayName("GET 请求 - null 参数 + 自定义字符集测试")
    void testGetNullParamsWithCharset() {
        String result = HttpUtil.get("https://www.baidu.com", null, "UTF-8");
        
        assertNotNull(result, "响应结果不应为空");
        assertFalse(result.isEmpty(), "响应结果不应为空字符串");
        System.out.println("GET null 参数 + 自定义字符集测试通过");
    }
    
    @Test
    @DisplayName("GET 请求 - 无参数 + 自定义字符集测试")
    void testGetNoParamsWithCharset() {
        // 调用无参数方法验证默认行为
        String result1 = HttpUtil.get("https://www.baidu.com");
        
        assertNotNull(result1, "响应结果不应为空");
        assertFalse(result1.isEmpty(), "响应结果不应为空字符串");
        System.out.println("GET 无参数方法测试通过，响应长度: " + result1.length());
    }
}