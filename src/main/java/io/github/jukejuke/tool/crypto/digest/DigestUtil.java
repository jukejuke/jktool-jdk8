package io.github.jukejuke.tool.crypto.digest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * 摘要工具类，提供各种哈希算法和消息认证码的实现
 * 支持MD5、SHA系列（SHA-1, SHA-256, SHA-384, SHA-512, SHA3）、HMAC
 * 摘要算法：将任意长度的数据转换为固定长度的哈希值，不可逆
 */
public class DigestUtil {

    /**
     * 计算字符串的MD5摘要
     * @param data 输入字符串
     * @return MD5哈希值的十六进制字符串（32位小写）
     * @throws Exception 当计算过程中发生错误时抛出
     */
    public static String md5(String data) throws Exception {
        return md5(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 计算字节数组的MD5摘要
     * @param data 输入字节数组
     * @return MD5哈希值的十六进制字符串（32位小写）
     * @throws Exception 当计算过程中发生错误时抛出
     */
    public static String md5(byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(data);
        return bytesToHex(digest);
    }

    /**
     * 计算文件的MD5摘要
     * @param filePath 文件路径
     * @return MD5哈希值的十六进制字符串（32位小写）
     * @throws Exception 当文件读取或计算过程中发生错误时抛出
     */
    public static String md5File(String filePath) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                md.update(buffer, 0, read);
            }
        }
        return bytesToHex(md.digest());
    }

    /**
     * 计算字符串的SHA-1摘要
     * @param data 输入字符串
     * @return SHA-1哈希值的十六进制字符串（40位小写）
     * @throws Exception 当计算过程中发生错误时抛出
     */
    public static String sha1(String data) throws Exception {
        return sha1(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 计算字节数组的SHA-1摘要
     * @param data 输入字节数组
     * @return SHA-1哈希值的十六进制字符串（40位小写）
     * @throws Exception 当计算过程中发生错误时抛出
     */
    public static String sha1(byte[] data) throws Exception {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        byte[] digest = sha1.digest(data);
        return bytesToHex(digest);
    }

    /**
     * 计算文件的SHA-1摘要
     * @param filePath 文件路径
     * @return SHA-1哈希值的十六进制字符串（40位小写）
     * @throws Exception 当文件读取或计算过程中发生错误时抛出
     */
    public static String sha1File(String filePath) throws Exception {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                sha1.update(buffer, 0, read);
            }
        }
        return bytesToHex(sha1.digest());
    }

    /**
     * 计算字符串的SHA-256摘要
     * @param data 输入字符串
     * @return SHA-256哈希值的十六进制字符串（64位小写）
     * @throws Exception 当计算过程中发生错误时抛出
     */
    public static String sha256(String data) throws Exception {
        return sha256(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 计算字节数组的SHA-256摘要
     * @param data 输入字节数组
     * @return SHA-256哈希值的十六进制字符串（64位小写）
     * @throws Exception 当计算过程中发生错误时抛出
     */
    public static String sha256(byte[] data) throws Exception {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] digest = sha256.digest(data);
        return bytesToHex(digest);
    }

    /**
     * 计算文件的SHA-256摘要
     * @param filePath 文件路径
     * @return SHA-256哈希值的十六进制字符串（64位小写）
     * @throws Exception 当文件读取或计算过程中发生错误时抛出
     */
    public static String sha256File(String filePath) throws Exception {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                sha256.update(buffer, 0, read);
            }
        }
        return bytesToHex(sha256.digest());
    }

    /**
     * 计算字符串的SHA-384摘要
     * @param data 输入字符串
     * @return SHA-384哈希值的十六进制字符串（96位小写）
     * @throws Exception 当计算过程中发生错误时抛出
     */
    public static String sha384(String data) throws Exception {
        return sha384(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 计算字节数组的SHA-384摘要
     * @param data 输入字节数组
     * @return SHA-384哈希值的十六进制字符串（96位小写）
     * @throws Exception 当计算过程中发生错误时抛出
     */
    public static String sha384(byte[] data) throws Exception {
        MessageDigest sha384 = MessageDigest.getInstance("SHA-384");
        byte[] digest = sha384.digest(data);
        return bytesToHex(digest);
    }

    /**
     * 计算文件的SHA-384摘要
     * @param filePath 文件路径
     * @return SHA-384哈希值的十六进制字符串（96位小写）
     * @throws Exception 当文件读取或计算过程中发生错误时抛出
     */
    public static String sha384File(String filePath) throws Exception {
        MessageDigest sha384 = MessageDigest.getInstance("SHA-384");
        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                sha384.update(buffer, 0, read);
            }
        }
        return bytesToHex(sha384.digest());
    }

    /**
     * 计算字符串的SHA-512摘要
     * @param data 输入字符串
     * @return SHA-512哈希值的十六进制字符串（128位小写）
     * @throws Exception 当计算过程中发生错误时抛出
     */
    public static String sha512(String data) throws Exception {
        return sha512(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 计算字节数组的SHA-512摘要
     * @param data 输入字节数组
     * @return SHA-512哈希值的十六进制字符串（128位小写）
     * @throws Exception 当计算过程中发生错误时抛出
     */
    public static String sha512(byte[] data) throws Exception {
        MessageDigest sha512 = MessageDigest.getInstance("SHA-512");
        byte[] digest = sha512.digest(data);
        return bytesToHex(digest);
    }

    /**
     * 计算文件的SHA-512摘要
     * @param filePath 文件路径
     * @return SHA-512哈希值的十六进制字符串（128位小写）
     * @throws Exception 当文件读取或计算过程中发生错误时抛出
     */
    public static String sha512File(String filePath) throws Exception {
        MessageDigest sha512 = MessageDigest.getInstance("SHA-512");
        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                sha512.update(buffer, 0, read);
            }
        }
        return bytesToHex(sha512.digest());
    }

    /**
     * 计算字符串的SHA3-256摘要
     * @param data 输入字符串
     * @return SHA3-256哈希值的十六进制字符串（64位小写）
     * @throws Exception 当计算过程中发生错误时抛出
     */
    public static String sha3_256(String data) throws Exception {
        return sha3_256(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 计算字节数组的SHA3-256摘要
     * @param data 输入字节数组
     * @return SHA3-256哈希值的十六进制字符串（64位小写）
     * @throws Exception 当计算过程中发生错误时抛出
     */
    public static String sha3_256(byte[] data) throws Exception {
        MessageDigest sha3_256 = MessageDigest.getInstance("SHA3-256");
        byte[] digest = sha3_256.digest(data);
        return bytesToHex(digest);
    }

    /**
     * 计算文件的SHA3-256摘要
     * @param filePath 文件路径
     * @return SHA3-256哈希值的十六进制字符串（64位小写）
     * @throws Exception 当文件读取或计算过程中发生错误时抛出
     */
    public static String sha3_256File(String filePath) throws Exception {
        MessageDigest sha3_256 = MessageDigest.getInstance("SHA3-256");
        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                sha3_256.update(buffer, 0, read);
            }
        }
        return bytesToHex(sha3_256.digest());
    }

    /**
     * 计算字符串的SHA3-384摘要
     * @param data 输入字符串
     * @return SHA3-384哈希值的十六进制字符串（96位小写）
     * @throws Exception 当计算过程中发生错误时抛出
     */
    public static String sha3_384(String data) throws Exception {
        return sha3_384(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 计算字节数组的SHA3-384摘要
     * @param data 输入字节数组
     * @return SHA3-384哈希值的十六进制字符串（96位小写）
     * @throws Exception 当计算过程中发生错误时抛出
     */
    public static String sha3_384(byte[] data) throws Exception {
        MessageDigest sha3_384 = MessageDigest.getInstance("SHA3-384");
        byte[] digest = sha3_384.digest(data);
        return bytesToHex(digest);
    }

    /**
     * 计算文件的SHA3-384摘要
     * @param filePath 文件路径
     * @return SHA3-384哈希值的十六进制字符串（96位小写）
     * @throws Exception 当文件读取或计算过程中发生错误时抛出
     */
    public static String sha3_384File(String filePath) throws Exception {
        MessageDigest sha3_384 = MessageDigest.getInstance("SHA3-384");
        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                sha3_384.update(buffer, 0, read);
            }
        }
        return bytesToHex(sha3_384.digest());
    }

    /**
     * 计算字符串的SHA3-512摘要
     * @param data 输入字符串
     * @return SHA3-512哈希值的十六进制字符串（128位小写）
     * @throws Exception 当计算过程中发生错误时抛出
     */
    public static String sha3_512(String data) throws Exception {
        return sha3_512(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 计算字节数组的SHA3-512摘要
     * @param data 输入字节数组
     * @return SHA3-512哈希值的十六进制字符串（128位小写）
     * @throws Exception 当计算过程中发生错误时抛出
     */
    public static String sha3_512(byte[] data) throws Exception {
        MessageDigest sha3_512 = MessageDigest.getInstance("SHA3-512");
        byte[] digest = sha3_512.digest(data);
        return bytesToHex(digest);
    }

    /**
     * 计算文件的SHA3-512摘要
     * @param filePath 文件路径
     * @return SHA3-512哈希值的十六进制字符串（128位小写）
     * @throws Exception 当文件读取或计算过程中发生错误时抛出
     */
    public static String sha3_512File(String filePath) throws Exception {
        MessageDigest sha3_512 = MessageDigest.getInstance("SHA3-512");
        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                sha3_512.update(buffer, 0, read);
            }
        }
        return bytesToHex(sha3_512.digest());
    }

    /**
     * 使用指定算法计算HMAC
     * @param data 输入数据
     * @param key 密钥字符串
     * @param algorithm HMAC算法（如HmacMD5, HmacSHA1, HmacSHA256）
     * @return HMAC结果的Base64编码字符串
     * @throws Exception 当计算过程中发生错误时抛出
     */
    public static String hmac(String data, String key, String algorithm) throws Exception {
        Mac mac = Mac.getInstance(algorithm);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), algorithm);
        mac.init(secretKeySpec);
        byte[] hmacBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hmacBytes);
    }

    /**
     * 使用指定算法计算HMAC（字节数组密钥）
     * @param data 输入数据
     * @param key 密钥字节数组
     * @param algorithm HMAC算法（如HmacMD5, HmacSHA1, HmacSHA256）
     * @return HMAC结果的Base64编码字符串
     * @throws Exception 当计算过程中发生错误时抛出
     */
    public static String hmac(byte[] data, byte[] key, String algorithm) throws Exception {
        Mac mac = Mac.getInstance(algorithm);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, algorithm);
        mac.init(secretKeySpec);
        byte[] hmacBytes = mac.doFinal(data);
        return Base64.getEncoder().encodeToString(hmacBytes);
    }

    /**
     * 使用HMAC-MD5算法计算消息认证码
     * @param data 输入数据
     * @param key 密钥字符串
     * @return HMAC-MD5结果的Base64编码字符串
     * @throws Exception 当计算过程中发生错误时抛出
     */
    public static String hmacMd5(String data, String key) throws Exception {
        return hmac(data, key, "HmacMD5");
    }

    /**
     * 使用HMAC-MD5算法计算消息认证码（字节数组密钥）
     * @param data 输入数据
     * @param key 密钥字节数组
     * @return HMAC-MD5结果的Base64编码字符串
     * @throws Exception 当计算过程中发生错误时抛出
     */
    public static String hmacMd5(byte[] data, byte[] key) throws Exception {
        return hmac(data, key, "HmacMD5");
    }

    /**
     * 使用HMAC-SHA1算法计算消息认证码
     * @param data 输入数据
     * @param key 密钥字符串
     * @return HMAC-SHA1结果的Base64编码字符串
     * @throws Exception 当计算过程中发生错误时抛出
     */
    public static String hmacSha1(String data, String key) throws Exception {
        return hmac(data, key, "HmacSHA1");
    }

    /**
     * 使用HMAC-SHA1算法计算消息认证码（字节数组密钥）
     * @param data 输入数据
     * @param key 密钥字节数组
     * @return HMAC-SHA1结果的Base64编码字符串
     * @throws Exception 当计算过程中发生错误时抛出
     */
    public static String hmacSha1(byte[] data, byte[] key) throws Exception {
        return hmac(data, key, "HmacSHA1");
    }

    /**
     * 使用HMAC-SHA256算法计算消息认证码
     * @param data 输入数据
     * @param key 密钥字符串
     * @return HMAC-SHA256结果的Base64编码字符串
     * @throws Exception 当计算过程中发生错误时抛出
     */
    public static String hmacSha256(String data, String key) throws Exception {
        return hmac(data, key, "HmacSHA256");
    }

    /**
     * 使用HMAC-SHA256算法计算消息认证码（字节数组密钥）
     * @param data 输入数据
     * @param key 密钥字节数组
     * @return HMAC-SHA256结果的Base64编码字符串
     * @throws Exception 当计算过程中发生错误时抛出
     */
    public static String hmacSha256(byte[] data, byte[] key) throws Exception {
        return hmac(data, key, "HmacSHA256");
    }

    /**
     * 使用HMAC-SHA512算法计算消息认证码
     * @param data 输入数据
     * @param key 密钥字符串
     * @return HMAC-SHA512结果的Base64编码字符串
     * @throws Exception 当计算过程中发生错误时抛出
     */
    public static String hmacSha512(String data, String key) throws Exception {
        return hmac(data, key, "HmacSHA512");
    }

    /**
     * 使用HMAC-SHA512算法计算消息认证码（字节数组密钥）
     * @param data 输入数据
     * @param key 密钥字节数组
     * @return HMAC-SHA512结果的Base64编码字符串
     * @throws Exception 当计算过程中发生错误时抛出
     */
    public static String hmacSha512(byte[] data, byte[] key) throws Exception {
        return hmac(data, key, "HmacSHA512");
    }

    /**
     * 验证数据的完整性（比较两个摘要是否相同）
     * @param digest1 第一个摘要
     * @param digest2 第二个摘要
     * @return 如果两个摘要相同返回true，否则返回false
     */
    public static boolean verifyDigest(String digest1, String digest2) {
        if (digest1 == null || digest2 == null) {
            return false;
        }
        return digest1.toLowerCase().equals(digest2.toLowerCase());
    }

    /**
     * 使用指定算法计算文件的摘要
     * @param filePath 文件路径
     * @param algorithm 摘要算法（如MD5, SHA-1, SHA-256）
     * @return 摘要的十六进制字符串
     * @throws Exception 当文件读取或计算过程中发生错误时抛出
     */
    public static String digestFile(String filePath, String algorithm) throws Exception {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, read);
            }
        }
        return bytesToHex(digest.digest());
    }

    /**
     * 将字节数组转换为十六进制字符串
     * @param bytes 字节数组
     * @return 十六进制字符串（小写）
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * 将十六进制字符串转换为字节数组
     * @param hex 十六进制字符串
     * @return 字节数组
     */
    public static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * 将字节数组转换为Base64字符串
     * @param bytes 字节数组
     * @return Base64编码字符串
     */
    public static String bytesToBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 将Base64字符串转换为字节数组
     * @param base64 Base64编码字符串
     * @return 字节数组
     */
    public static byte[] base64ToBytes(String base64) {
        return Base64.getDecoder().decode(base64);
    }
}
