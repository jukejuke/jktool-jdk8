package io.github.jukejuke.tool.crypto;

import io.github.jukejuke.tool.log.LogUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class SecureUtilTest {

    private static final String TEST_DATA = "testData123456";
    private static final String TEST_KEY = "testSecretKey";
    private SecretKey aesKey;
    private SecretKey desKey;
    private KeyPair rsaKeyPair;
    private KeyPair dsaKeyPair;

    @BeforeEach
    void setUp() throws Exception {
        // 初始化测试密钥
        aesKey = SecureUtil.generateKey("AES");
        desKey = SecureUtil.generateKey("DES");
        rsaKeyPair = SecureUtil.generateKeyPair("RSA");
        dsaKeyPair = SecureUtil.generateKeyPair("DSA");
    }

    @Test
    void testAesEncryptionDecryption() throws Exception {
        // 加密
        String encrypted = SecureUtil.aes(TEST_DATA, aesKey.getEncoded(), Cipher.ENCRYPT_MODE);
        // 解密
        String decrypted = SecureUtil.aes(encrypted, aesKey.getEncoded(), Cipher.DECRYPT_MODE);
        assertEquals(TEST_DATA, decrypted);
    }

    @Test
    void testDesEncryptionDecryption() throws Exception {
        String encrypted = SecureUtil.des(TEST_DATA, desKey.getEncoded(), Cipher.ENCRYPT_MODE);
        String decrypted = SecureUtil.des(encrypted, desKey.getEncoded(), Cipher.DECRYPT_MODE);
        assertEquals(TEST_DATA, decrypted);
    }

    @Test
    void testMd5() throws Exception {
        // 已知MD5测试值："testData123456"的MD5哈希为"1189fb7f0fe0380b601966dca119ecf6"
        String result = SecureUtil.md5(TEST_DATA);
        assertEquals("1189fb7f0fe0380b601966dca119ecf6", result);
    }

    @Test
    void testSha1() throws Exception {
        // 已知SHA-1测试值："testData123456"的SHA-1哈希为"288f50fa6f1487e9aef99e25bf4d92c3b369c3af"
        String result = SecureUtil.sha1(TEST_DATA);
        assertEquals("288f50fa6f1487e9aef99e25bf4d92c3b369c3af", result);
    }

    @Test
    void testHmacMd5() throws Exception {
        String result = SecureUtil.hmacMd5(TEST_DATA, TEST_KEY);
        LogUtil.info(result);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testHmacSha1() throws Exception {
        String result = SecureUtil.hmacSha1(TEST_DATA, TEST_KEY);
        LogUtil.info(result);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testRsaEncryptionDecryption() throws Exception {
        PublicKey publicKey = rsaKeyPair.getPublic();
        PrivateKey privateKey = rsaKeyPair.getPrivate();

        String encrypted = SecureUtil.rsa(TEST_DATA, publicKey, Cipher.ENCRYPT_MODE);
        LogUtil.info(encrypted);
        String decrypted = SecureUtil.rsa(encrypted, privateKey, Cipher.DECRYPT_MODE);
        LogUtil.info(decrypted);
        assertEquals(TEST_DATA, decrypted);
    }

    @Test
    void testDsaSignature() throws Exception {
        PrivateKey privateKey = dsaKeyPair.getPrivate();
        PublicKey publicKey = dsaKeyPair.getPublic();

        String signature = SecureUtil.dsa(TEST_DATA, privateKey);
        LogUtil.info(signature);
        // 这里需要实现DSA签名验证逻辑，假设SecureUtil有verifyDsa方法
        // 由于原类中没有验证方法，此测试仅验证签名生成不抛出异常
        assertNotNull(signature);
        assertFalse(signature.isEmpty());
    }

    @Test
    void testSimpleUUID() {
        String uuid = SecureUtil.simpleUUID();
        LogUtil.info(uuid);
        assertNotNull(uuid);
        assertEquals(32, uuid.length()); // UUID去掉连字符后长度为32
        assertTrue(uuid.matches("^[0-9a-fA-F]{32}$"));
    }

    @Test
    void testGenerateKey() throws Exception {
        SecretKey key = SecureUtil.generateKey("AES");
        assertNotNull(key);
        assertEquals("AES", key.getAlgorithm());
    }

    @Test
    void testGenerateKeyPair() throws Exception {
        KeyPair keyPair = SecureUtil.generateKeyPair("RSA");
        assertNotNull(keyPair);
        assertNotNull(keyPair.getPublic());
        assertNotNull(keyPair.getPrivate());
        assertEquals("RSA", keyPair.getPublic().getAlgorithm());
    }

    @Test
    void testGenerateSignature() throws Exception {
        PrivateKey privateKey = rsaKeyPair.getPrivate();
        String signature = SecureUtil.generateSignature(TEST_DATA, privateKey, "SHA256withRSA");
        assertNotNull(signature);
        assertFalse(signature.isEmpty());
    }

    @Test
    void testBytesToHex() throws Exception {
        // 测试字节转十六进制方法
        byte[] bytes = {0x01, 0x02, (byte) 0xab, (byte) 0xcd};
        // 使用反射调用私有方法
        java.lang.reflect.Method method = SecureUtil.class.getDeclaredMethod("bytesToHex", byte[].class);
        method.setAccessible(true);
        String result = (String) method.invoke(null, bytes);
        assertEquals("0102abcd", result);
    }

    @Test
    void testAesWithInvalidKey() {
        assertThrows(Exception.class, () -> {
            SecureUtil.aes(TEST_DATA, new byte[15], Cipher.ENCRYPT_MODE); // AES密钥长度不支持15字节
        });
    }
}