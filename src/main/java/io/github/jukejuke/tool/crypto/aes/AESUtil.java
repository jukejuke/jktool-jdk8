package io.github.jukejuke.tool.crypto.aes;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES工具类，提供AES加密/解密、密钥生成和文件加解密功能
 * 支持多种加密模式：GCM（推荐）、ECB、CBC
 * AES：对称加密算法，加密解密使用相同密钥
 * AES（高级加密标准） 取代了旧的 DES（数据加密标准）
 */
public class AESUtil {

    private static final int GCM_IV_LENGTH = 12;

    private static final int GCM_TAG_LENGTH = 128;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * 生成指定密钥长度的AES密钥
     * @param keySize 密钥长度，支持128、192、256位
     * @return SecretKey 密钥对象
     * @throws Exception 当密钥生成过程中发生错误时抛出
     */
    public static SecretKey generateKey(int keySize) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(keySize);
        return keyGenerator.generateKey();
    }

    /**
     * 生成默认长度（256位）的AES密钥
     * @return SecretKey 密钥对象
     * @throws Exception 当密钥生成过程中发生错误时抛出
     */
    public static SecretKey generateKey() throws Exception {
        return generateKey(256);
    }

    /**
     * 生成指定密钥长度的AES密钥并返回字节数组
     * @param keySize 密钥长度，支持128、192、256位
     * @return 密钥的字节数组表示
     * @throws Exception 当密钥生成过程中发生错误时抛出
     */
    public static byte[] generateKeyBytes(int keySize) throws Exception {
        SecretKey key = generateKey(keySize);
        return key.getEncoded();
    }

    /**
     * 生成默认长度（256位）的AES密钥并返回字节数组
     * @return 密钥的字节数组表示
     * @throws Exception 当密钥生成过程中发生错误时抛出
     */
    public static byte[] generateKeyBytes() throws Exception {
        return generateKeyBytes(256);
    }

    /**
     * 使用GCM模式加密字符串（推荐使用此方法）
     * GCM模式提供认证加密，自动生成随机IV
     * @param data 待加密的明文
     * @param key AES密钥
     * @return 加密后的Base64编码字符串（包含IV）
     * @throws Exception 加密过程中发生错误时抛出
     */
    public static String encrypt(String data, SecretKey key) throws Exception {
        byte[] iv = generateIV();
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
        byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(concat(iv, encryptedData));
    }

    /**
     * 使用GCM模式解密字符串
     * @param encryptedData 加密后的Base64编码字符串
     * @param key AES密钥
     * @return 解密后的明文
     * @throws Exception 解密过程中发生错误时抛出
     */
    public static String decrypt(String encryptedData, SecretKey key) throws Exception {
        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
        byte[] iv = extractIV(decodedData);
        byte[] cipherText = extractCipherText(decodedData);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
        byte[] decryptedData = cipher.doFinal(cipherText);
        return new String(decryptedData, StandardCharsets.UTF_8);
    }

    /**
     * 使用ECB模式加密字符串（简单但不推荐用于长文本）
     * ECB模式不使用IV，相同明文块产生相同密文块
     * @param data 待加密的明文
     * @param key 密钥字节数组（16、24或32字节）
     * @return 加密后的Base64编码字符串
     * @throws Exception 加密过程中发生错误时抛出
     */
    public static String encryptWithECB(String data, byte[] key) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    /**
     * 使用ECB模式解密字符串
     * @param encryptedData 加密后的Base64编码字符串
     * @param key 密钥字节数组
     * @return 解密后的明文
     * @throws Exception 解密过程中发生错误时抛出
     */
    public static String decryptWithECB(String encryptedData, byte[] key) throws Exception {
        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decryptedData = cipher.doFinal(decodedData);
        return new String(decryptedData, StandardCharsets.UTF_8);
    }

    /**
     * 使用CBC模式加密字符串（需要手动指定IV）
     * CBC模式使用前一个密文块影响当前块，安全性高于ECB
     * @param data 待加密的明文
     * @param key 密钥字节数组
     * @param iv 初始化向量（16字节）
     * @return 加密后的Base64编码字符串
     * @throws Exception 加密过程中发生错误时抛出
     */
    public static String encryptWithCBC(String data, byte[] key, byte[] iv) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
        byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    /**
     * 使用CBC模式解密字符串
     * @param encryptedData 加密后的Base64编码字符串
     * @param key 密钥字节数组
     * @param iv 初始化向量（需与加密时使用相同的IV）
     * @return 解密后的明文
     * @throws Exception 解密过程中发生错误时抛出
     */
    public static String decryptWithCBC(String encryptedData, byte[] key, byte[] iv) throws Exception {
        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
        byte[] decryptedData = cipher.doFinal(decodedData);
        return new String(decryptedData, StandardCharsets.UTF_8);
    }

    /**
     * 使用指定IV的GCM模式加密字符串
     * @param data 待加密的明文
     * @param key 密钥字节数组
     * @param iv 初始化向量（12字节）
     * @return 加密后的Base64编码字符串（包含认证标签）
     * @throws Exception 加密过程中发生错误时抛出
     */
    public static String encryptWithIV(String data, byte[] key, byte[] iv) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, parameterSpec);
        byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(concat(iv, encryptedData));
    }

    /**
     * 使用指定密钥的GCM模式解密字符串（从密文中提取IV）
     * @param encryptedData 加密后的Base64编码字符串
     * @param key 密钥字节数组
     * @return 解密后的明文
     * @throws Exception 解密过程中发生错误时抛出
     */
    public static String decryptWithIV(String encryptedData, byte[] key) throws Exception {
        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
        byte[] iv = extractIV(decodedData);
        byte[] cipherText = extractCipherText(decodedData);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, parameterSpec);
        byte[] decryptedData = cipher.doFinal(cipherText);
        return new String(decryptedData, StandardCharsets.UTF_8);
    }

    /**
     * 使用GCM模式加密字节数组
     * @param data 待加密的字节数组
     * @param key AES密钥
     * @return 加密后的字节数组（IV + 密文）
     * @throws Exception 加密过程中发生错误时抛出
     */
    public static byte[] encryptBytes(byte[] data, SecretKey key) throws Exception {
        byte[] iv = generateIV();
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
        byte[] encryptedData = cipher.doFinal(data);
        return concat(iv, encryptedData);
    }

    /**
     * 使用GCM模式解密字节数组
     * @param encryptedData 加密后的字节数组
     * @param key AES密钥
     * @return 解密后的原始字节数组
     * @throws Exception 解密过程中发生错误时抛出
     */
    public static byte[] decryptBytes(byte[] encryptedData, SecretKey key) throws Exception {
        byte[] iv = extractIV(encryptedData);
        byte[] cipherText = extractCipherText(encryptedData);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
        return cipher.doFinal(cipherText);
    }

    /**
     * 加密文件（使用GCM模式）
     * @param inputFile 输入文件路径
     * @param outputFile 输出文件路径（加密后）
     * @param key AES密钥
     * @throws Exception 文件处理或加密过程中发生错误时抛出
     */
    public static void encryptFile(String inputFile, String outputFile, SecretKey key) throws Exception {
        byte[] fileContent = Files.readAllBytes(Paths.get(inputFile));
        byte[] encryptedData = encryptBytes(fileContent, key);
        Files.write(Paths.get(outputFile), encryptedData);
    }

    /**
     * 解密文件（使用GCM模式）
     * @param inputFile 输入文件路径（加密后）
     * @param outputFile 输出文件路径（解密后）
     * @param key AES密钥
     * @throws Exception 文件处理或解密过程中发生错误时抛出
     */
    public static void decryptFile(String inputFile, String outputFile, SecretKey key) throws Exception {
        byte[] fileContent = Files.readAllBytes(Paths.get(inputFile));
        byte[] decryptedData = decryptBytes(fileContent, key);
        Files.write(Paths.get(outputFile), decryptedData);
    }

    /**
     * 加密文件（使用字节数组密钥）
     * @param inputFile 输入文件路径
     * @param outputFile 输出文件路径
     * @param key 密钥字节数组
     * @throws Exception 文件处理或加密过程中发生错误时抛出
     */
    public static void encryptFile(String inputFile, String outputFile, byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, "AES");
        encryptFile(inputFile, outputFile, secretKey);
    }

    /**
     * 解密文件（使用字节数组密钥）
     * @param inputFile 输入文件路径
     * @param outputFile 输出文件路径
     * @param key 密钥字节数组
     * @throws Exception 文件处理或解密过程中发生错误时抛出
     */
    public static void decryptFile(String inputFile, String outputFile, byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, "AES");
        decryptFile(inputFile, outputFile, secretKey);
    }

    /**
     * 从字节数组导入AES密钥
     * @param keyBytes 密钥字节数组
     * @return SecretKey 密钥对象
     */
    public static SecretKey importKey(byte[] keyBytes) {
        return new SecretKeySpec(keyBytes, "AES");
    }

    /**
     * 导出密钥为Base64字符串
     * @param key AES密钥
     * @return Base64编码的密钥字符串
     */
    public static String exportKey(SecretKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    /**
     * 从Base64字符串导入AES密钥
     * @param keyStr Base64编码的密钥字符串
     * @return SecretKey 密钥对象
     */
    public static SecretKey importKeyFromString(String keyStr) {
        byte[] keyBytes = Base64.getDecoder().decode(keyStr);
        return new SecretKeySpec(keyBytes, "AES");
    }

    /**
     * 将密钥导出到文件（Base64编码）
     * @param key AES密钥
     * @param filePath 文件路径
     * @throws Exception 文件写入过程中发生错误时抛出
     */
    public static void exportKeyToFile(SecretKey key, String filePath) throws Exception {
        String keyStr = exportKey(key);
        Files.write(Paths.get(filePath), keyStr.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 从文件导入AES密钥（Base64编码）
     * @param filePath 文件路径
     * @return SecretKey 密钥对象
     * @throws Exception 文件读取或密钥解析过程中发生错误时抛出
     */
    public static SecretKey importKeyFromFile(String filePath) throws Exception {
        String keyStr = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
        return importKeyFromString(keyStr);
    }

    /**
     * 生成随机初始化向量（IV）
     * @return 12字节的随机IV数组
     */
    public static byte[] generateIV() {
        byte[] iv = new byte[GCM_IV_LENGTH];
        SECURE_RANDOM.nextBytes(iv);
        return iv;
    }

    public static byte[] generateIV16() {
        byte[] iv = new byte[16];
        SECURE_RANDOM.nextBytes(iv);
        return iv;
    }

    /**
     * 将字节数组转换为十六进制字符串
     * @param bytes 待转换的字节数组
     * @return 十六进制字符串
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

    private static byte[] concat(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    private static byte[] extractIV(byte[] data) {
        byte[] iv = new byte[GCM_IV_LENGTH];
        System.arraycopy(data, 0, iv, 0, GCM_IV_LENGTH);
        return iv;
    }

    private static byte[] extractCipherText(byte[] data) {
        byte[] cipherText = new byte[data.length - GCM_IV_LENGTH];
        System.arraycopy(data, GCM_IV_LENGTH, cipherText, 0, cipherText.length);
        return cipherText;
    }
}
