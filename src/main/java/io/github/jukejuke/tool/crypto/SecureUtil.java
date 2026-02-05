
package io.github.jukejuke.tool.crypto;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Base64;
import java.util.UUID;
import java.nio.charset.StandardCharsets;

/**
 * 安全工具类，提供各种加密、解密、哈希和签名算法的实现
 * 支持对称加密(AES,DES)、摘要算法(MD5,SHA-1)、HMAC、非对称加密(RSA)和数字签名(DSA)
 */
@Deprecated
public class SecureUtil {

    /**
     * AES加密/解密方法 (对称加密)
     * @param data 待处理的数据
     * @param key 密钥字节数组
     * @param mode 加密模式:Cipher.ENCRYPT_MODE 或 Cipher.DECRYPT_MODE
     * @return 加密/解密后的Base64编码字符串
     * @throws Exception 当加密/解密过程中发生错误时抛出
     */
    public static String aes(String data, byte[] key, int mode) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        // 使用带填充的AES算法模式，确保数据块大小正确
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(mode, secretKeySpec);
        byte[] result;
        if (mode == Cipher.ENCRYPT_MODE) {
            // 加密：明文→加密→Base64编码
            result = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(result);
        } else {
            // 解密：Base64解码→解密→明文
            byte[] decodedData = Base64.getDecoder().decode(data);
            result = cipher.doFinal(decodedData);
            return new String(result);
        }
    }

    /**
     * DES加密/解密方法 (对称加密)
     * @param data 待处理的数据
     * @param key 密钥字节数组
     * @param mode 加密模式:Cipher.ENCRYPT_MODE 或 Cipher.DECRYPT_MODE
     * @return 加密/解密后的Base64编码字符串
     * @throws Exception 当加密/解密过程中发生错误时抛出
     */
    public static String des(String data, byte[] key, int mode) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "DES");
        // 使用带填充的DES算法模式，确保数据块大小正确
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(mode, secretKeySpec);
        byte[] result;
        if (mode == Cipher.ENCRYPT_MODE) {
            // 加密：明文→加密→Base64编码
            result = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(result);
        } else {
            // 解密：Base64解码→解密→明文
            byte[] decodedData = Base64.getDecoder().decode(data);
            result = cipher.doFinal(decodedData);
            return new String(result);
        }
    }

    /**
     * MD5哈希算法 (摘要算法)
     * @param data 待哈希的数据
     * @return 哈希结果的十六进制字符串
     * @throws Exception 当哈希过程中发生错误时抛出
     */
    public static String md5(String data) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        // 显式指定UTF-8编码，确保跨平台一致性
        byte[] result = md5.digest(data.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(result);
    }

    /**
     * SHA-1哈希算法 (摘要算法)
     * @param data 待哈希的数据
     * @return 哈希结果的十六进制字符串
     * @throws Exception 当哈希过程中发生错误时抛出
     */
    public static String sha1(String data) throws Exception {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        byte[] result = sha1.digest(data.getBytes());
        return bytesToHex(result);
    }

    /**
     * 通用HMAC方法 (摘要算法)
     * @param data 待处理的数据
     * @param key 密钥字符串
     * @param algorithm HMAC算法名称(如HmacMD5, HmacSHA1)
     * @return HMAC结果的Base64编码字符串
     * @throws Exception 当HMAC计算过程中发生错误时抛出
     */
    public static String hmac(String data, String key, String algorithm) throws Exception {
        Mac mac = Mac.getInstance(algorithm);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), algorithm);
        mac.init(secretKeySpec);
        byte[] result = mac.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(result);
    }

    /**
     * HMAC-MD5算法 (摘要算法)
     * @param data 待处理的数据
     * @param key 密钥字符串
     * @return HMAC-MD5结果的Base64编码字符串
     * @throws Exception 当计算过程中发生错误时抛出
     */
    public static String hmacMd5(String data, String key) throws Exception {
        return hmac(data, key, "HmacMD5");
    }

    /**
     * HMAC-SHA1算法 (摘要算法)
     * @param data 待处理的数据
     * @param key 密钥字符串
     * @return HMAC-SHA1结果的Base64编码字符串
     * @throws Exception 当计算过程中发生错误时抛出
     */
    public static String hmacSha1(String data, String key) throws Exception {
        return hmac(data, key, "HmacSHA1");
    }

    /**
     * RSA加密/解密方法 (非对称加密)
     * @param data 待处理的数据
     * @param key RSA公钥或私钥
     * @param mode 加密模式:Cipher.ENCRYPT_MODE 或 Cipher.DECRYPT_MODE
     * @return 加密/解密结果的Base64编码字符串
     * @throws Exception 当加密/解密过程中发生错误时抛出
     */
    public static String rsa(String data, Key key, int mode) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(mode, key);
        byte[] result;
        if (mode == Cipher.ENCRYPT_MODE) {
            // 加密：明文→加密→Base64编码
            result = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(result);
        } else {
            // 解密：Base64解码→解密→明文
            byte[] decodedData = Base64.getDecoder().decode(data);
            result = cipher.doFinal(decodedData);
            return new String(result);
        }
    }

    /**
     * DSA数字签名 (非对称加密)
     * @param data 待签名的数据
     * @param privateKey DSA私钥
     * @return 签名结果的Base64编码字符串
     * @throws Exception 当签名过程中发生错误时抛出
     */
    public static String dsa(String data, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withDSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes());
        byte[] result = signature.sign();
        return Base64.getEncoder().encodeToString(result);
    }

    /**
     * 生成简化的UUID(不带连字符)
     * @return 简化的UUID字符串
     */
    public static String simpleUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成对称密钥
     * @param algorithm 密钥算法(如AES, DES)
     * @return 生成的密钥对象
     * @throws Exception 当密钥生成过程中发生错误时抛出
     */
    public static SecretKey generateKey(String algorithm) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
        return keyGenerator.generateKey();
    }

    /**
     * 生成非对称密钥对
     * @param algorithm 密钥算法(如RSA, DSA)
     * @return 生成的密钥对对象
     * @throws Exception 当密钥对生成过程中发生错误时抛出
     */
    public static KeyPair generateKeyPair(String algorithm) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 生成数字签名
     * @param data 待签名的数据
     * @param privateKey 私钥
     * @param algorithm 签名算法
     * @return 签名结果的Base64编码字符串
     * @throws Exception 当签名过程中发生错误时抛出
     */
    public static String generateSignature(String data, PrivateKey privateKey, String algorithm) throws Exception {
        Signature signature = Signature.getInstance(algorithm);
        signature.initSign(privateKey);
        signature.update(data.getBytes());
        byte[] result = signature.sign();
        return Base64.getEncoder().encodeToString(result);
    }

    /**
     * 将字节数组转换为十六进制字符串
     * @param bytes 待转换的字节数组
     * @return 十六进制字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}