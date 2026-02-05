/**
 * RSA工具类，提供RSA密钥对生成、加密解密和签名验证功能
 */
package io.github.jukejuke.tool.crypto.rsa;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * RSA工具类，提供RSA密钥对生成、加密解密和签名验证功能
 * RSA： 既能用于加密/解密，也能用于数字签名。
 * 密钥特点： 公钥和私钥可互换使用
 * 性能： 签名生成快，验证慢
 */
public class RSAUtil {
    
    /**
     * 生成RSA密钥对
     * @param keySize 密钥长度，推荐2048或4096
     * @return 生成的密钥对
     * @throws NoSuchAlgorithmException 如果JVM不支持RSA算法
     */
    public static KeyPair generateKeyPair(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.generateKeyPair();
    }
    
    /**
     * 生成默认长度(2048位)的RSA密钥对
     * @return 生成的密钥对
     * @throws NoSuchAlgorithmException 如果JVM不支持RSA算法
     */
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        return generateKeyPair(2048);
    }
    
    /**
     * 使用公钥加密数据
     * @param data 待加密的数据
     * @param publicKey RSA公钥
     * @return 加密后的数据（Base64编码）
     * @throws Exception 加密过程中发生错误时抛出
     */
    public static String encryptByPublicKey(String data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }
    
    /**
     * 使用私钥解密数据
     * @param encryptedData 加密后的数据（Base64编码）
     * @param privateKey RSA私钥
     * @return 解密后的原始数据
     * @throws Exception 解密过程中发生错误时抛出
     */
    public static String decryptByPrivateKey(String encryptedData, PrivateKey privateKey) throws Exception {
        byte[] dataBytes = Base64.getDecoder().decode(encryptedData);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedData = cipher.doFinal(dataBytes);
        return new String(decryptedData);
    }
    
    /**
     * 使用私钥对数据进行签名
     * @param data 待签名的数据
     * @param privateKey RSA私钥
     * @return 签名结果（Base64编码）
     * @throws Exception 签名过程中发生错误时抛出
     */
    public static String sign(String data, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes());
        byte[] signBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signBytes);
    }
    
    /**
     * 使用公钥验证签名
     * @param data 原始数据
     * @param publicKey RSA公钥
     * @param sign 待验证的签名（Base64编码）
     * @return 验证结果：true表示验证通过，false表示验证失败
     * @throws Exception 验证过程中发生错误时抛出
     */
    public static boolean verify(String data, PublicKey publicKey, String sign) throws Exception {
        byte[] signBytes = Base64.getDecoder().decode(sign);
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(data.getBytes());
        return signature.verify(signBytes);
    }
    
    /**
     * 从Base64字符串导入公钥
     * @param publicKeyStr Base64编码的公钥字符串
     * @return 公钥对象
     * @throws Exception 导入过程中发生错误时抛出
     */
    public static PublicKey importPublicKey(String publicKeyStr) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyStr);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }
    
    /**
     * 从Base64字符串导入私钥
     * @param privateKeyStr Base64编码的私钥字符串
     * @return 私钥对象
     * @throws Exception 导入过程中发生错误时抛出
     */
    public static PrivateKey importPrivateKey(String privateKeyStr) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyStr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }
    
    /**
     * 导出公钥为Base64字符串
     * @param publicKey 公钥对象
     * @return Base64编码的公钥字符串
     */
    public static String exportPublicKey(PublicKey publicKey) {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }
    
    /**
     * 导出私钥为Base64字符串
     * @param privateKey 私钥对象
     * @return Base64编码的私钥字符串
     */
    public static String exportPrivateKey(PrivateKey privateKey) {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

    /**
     * 导出公钥到文件（Base64编码）
     * @param publicKey 公钥对象
     * @param filePath 文件路径
     * @throws Exception 导出过程中发生错误时抛出
     */
    public static void exportPublicKeyToFile(PublicKey publicKey, String filePath) throws Exception {
        String publicKeyStr = exportPublicKey(publicKey);
        Files.write(Paths.get(filePath), publicKeyStr.getBytes(StandardCharsets.UTF_8));

        System.out.println("密钥已导出到文件:");
        System.out.println("公钥: " + filePath);
    }

    /**
     * 导出私钥到文件（Base64编码）
     * @param privateKey 私钥对象
     * @param filePath 文件路径
     * @throws Exception 导出过程中发生错误时抛出
     */
    public static void exportPrivateKeyToFile(PrivateKey privateKey, String filePath) throws Exception {
        String privateKeyStr = exportPrivateKey(privateKey);
        Files.write(Paths.get(filePath), privateKeyStr.getBytes(StandardCharsets.UTF_8));

        System.out.println("密钥已导出到文件:");
        System.out.println("私钥: " + filePath);
    }

    /**
     * 从文件导入公钥（Base64编码）
     * @param filePath 文件路径
     * @return 公钥对象
     * @throws Exception 导入过程中发生错误时抛出
     */
    public static PublicKey importPublicKeyFromFile(String filePath) throws Exception {
        String publicKeyStr = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
        return importPublicKey(publicKeyStr);
    }

    /**
     * 从文件导入私钥（Base64编码）
     * @param filePath 文件路径
     * @return 私钥对象
     * @throws Exception 导入过程中发生错误时抛出
     */
    public static PrivateKey importPrivateKeyFromFile(String filePath) throws Exception {
        String privateKeyStr = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
        return importPrivateKey(privateKeyStr);
    }
}