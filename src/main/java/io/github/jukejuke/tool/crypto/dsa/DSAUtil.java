/**
 * DSA工具类，提供DSA密钥对生成、签名和验证功能
 */
package io.github.jukejuke.tool.crypto.dsa;

import java.security.*;
import java.util.Base64;

import io.github.jukejuke.tool.log.LogUtil;

/**
 * DSA工具类，提供DSA密钥对生成、签名和验证功能
 * DSA： 专门为数字签名而设计，不能用于加密。
 * 密钥特点：公钥和私钥角色固定，不可互换
 * 性能：签名生成慢，验证快
 */
public class DSAUtil {

    /*
     * DSA 标准密钥长度
     * 在 Java 中，DSA 算法只支持特定的密钥长度：
     * 密钥长度	安全性	Java 支持	备注
     * 512 位	已弃用	支持	        不安全，不应使用
     * 768 位	已弃用	支持	        不安全，不应使用
     * 1024 位	弱安全	支持	        NIST 已不推荐
     * 2048 位	推荐	    支持	        当前标准
     * 3072 位	高安全	部分支持	    Java 11+
     * 4096 位	高安全	不支持	    DSA 不支持此长度
     */

    /**
     * 生成DSA密钥对
     * @param keySize 密钥长度，推荐2048或3072
     * @return 生成的密钥对
     * @throws NoSuchAlgorithmException 如果JVM不支持DSA算法
     */
    public static KeyPair generateKeyPair(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.generateKeyPair();
    }
    
    /**
     * 使用DSA私钥对数据进行签名
     * @param data 待签名的数据
     * @param privateKey DSA私钥
     * @return 签名结果的Base64编码字符串
     * @throws Exception 签名过程中发生错误时抛出
     */
    public static String sign(String data, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withDSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes());
        byte[] signBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signBytes);
    }
    
    /**
     * 使用DSA公钥验证签名
     * @param data 原始数据
     * @param publicKey DSA公钥
     * @param sign 待验证的签名（Base64编码）
     * @return 验证结果：true表示验证通过，false表示验证失败
     * @throws Exception 验证过程中发生错误时抛出
     */
    public static boolean verify(String data, PublicKey publicKey, String sign) throws Exception {
        Signature signature = Signature.getInstance("SHA256withDSA");
        signature.initVerify(publicKey);
        signature.update(data.getBytes());
        byte[] signBytes = Base64.getDecoder().decode(sign);
        return signature.verify(signBytes);
    }
    
    /**
     * 生成默认长度(2048位)的DSA密钥对
     * @return 生成的密钥对
     * @throws NoSuchAlgorithmException 如果JVM不支持DSA算法
     */
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        return generateKeyPair(2048);
    }
}