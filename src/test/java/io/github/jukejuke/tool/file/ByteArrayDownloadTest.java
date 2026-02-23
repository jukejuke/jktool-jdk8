package io.github.jukejuke.tool.file;

/**
 * 测试下载文件为byte数组的功能
 */
public class ByteArrayDownloadTest {

    public static void main(String[] args) {
        // 测试URL
        String testUrl = "https://icon2.xinrui-cn.com/private/icon1757908622481.jpg?imageView2/2/w/200&e=1789021560&token=FlOC6ljJCmETSQOmc9-7jo8eVzY3Z-xM50oa6Ql_:S7B7Mc_-yKhCK4AEpTk9hG94VjQ=";
        
        System.out.println("=== Testing downloadAsByteArray ===");
        System.out.println("Test URL: " + testUrl);
        
        // 执行下载为byte数组
        byte[] result = SimpleDownloadUtil.downloadAsByteArray(testUrl);
        
        if (result != null) {
            System.out.println("\n✅ Download successful!");
            System.out.println("File size: " + result.length + " bytes");
            System.out.println("First 10 bytes: " + bytesToHex(result, 10));
        } else {
            System.out.println("\n❌ Download failed");
        }
        
        System.out.println("\n=== Test completed ===");
    }

    /**
     * 将字节数组转换为十六进制字符串（用于调试）
     * @param bytes 字节数组
     * @param length 要转换的长度
     * @return 十六进制字符串
     */
    private static String bytesToHex(byte[] bytes, int length) {
        if (bytes == null || bytes.length == 0) {
            return "";
        }
        
        int len = Math.min(bytes.length, length);
        StringBuilder sb = new StringBuilder(len * 2);
        
        for (int i = 0; i < len; i++) {
            sb.append(String.format("%02X", bytes[i]));
        }
        
        return sb.toString();
    }
}
