package io.github.jukejuke.tool.url;

/**
 * UrlUtils 测试主类
 * 使用 main 方法测试 URL 编码和解码功能
 */
public class UrlUtilsTestMain {

    public static void main(String[] args) {
        // 测试 URL 编码功能
        System.out.println("=== 测试 URL 编码功能 ===");
        
        // 测试普通字符串
        testEncode("hello");
        
        // 测试包含特殊字符的字符串
        testEncode("hello world");
        testEncode("hello+world");
        testEncode("hello&world");
        
        // 测试中文
        testEncode("中文");
        
        // 测试空值
        testEncode(null);
        
        // 测试 URL 解码功能
        System.out.println("\n=== 测试 URL 解码功能 ===");
        
        // 测试普通字符串
        testDecode("hello");
        
        // 测试包含特殊字符的字符串
        testDecode("hello%20world");
        testDecode("hello+world");
        testDecode("hello%2Bworld");
        testDecode("hello%26world");
        
        // 测试中文
        testDecode("%E4%B8%AD%E6%96%87");
        
        // 测试空值
        testDecode(null);
        
        // 测试编码解码循环
        System.out.println("\n=== 测试编码解码循环 ===");
        String testString = "测试 URL 编码解码功能: hello world!";
        System.out.println("原始字符串: " + testString);
        String encoded = UrlUtils.encode(testString);
        System.out.println("编码后: " + encoded);
        String decoded = UrlUtils.decode(encoded);
        System.out.println("解码后: " + decoded);
        System.out.println("编码解码后是否相同: " + testString.equals(decoded));
        
        System.out.println("\n=== 测试完成 ===");
    }

    /**
     * 测试 URL 编码
     * 
     * @param input 输入字符串
     */
    private static void testEncode(String input) {
        String encoded = UrlUtils.encode(input);
        System.out.println("encode(" + input + "): " + encoded);
    }

    /**
     * 测试 URL 解码
     * 
     * @param input 输入字符串
     */
    private static void testDecode(String input) {
        String decoded = UrlUtils.decode(input);
        System.out.println("decode(" + input + "): " + decoded);
    }
}
