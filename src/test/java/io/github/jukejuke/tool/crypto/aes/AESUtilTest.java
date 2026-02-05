package io.github.jukejuke.tool.crypto.aes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.crypto.SecretKey;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class AESUtilTest {

    private static final String TEST_DATA = "测试数据TestData123456";
    private static final String TEST_DATA_GBK = "中文测试数据";
    private static final byte[] TEST_DATA_BYTES = "二进制测试数据".getBytes();

    private SecretKey key256;
    private SecretKey key192;
    private SecretKey key128;
    private byte[] keyBytes256;

    @BeforeEach
    void setUp() throws Exception {
        key256 = AESUtil.generateKey(256);
        key192 = AESUtil.generateKey(192);
        key128 = AESUtil.generateKey(128);
        keyBytes256 = AESUtil.generateKeyBytes(256);
    }

    @Test
    void testGenerateKey256() throws Exception {
        SecretKey key = AESUtil.generateKey(256);
        assertNotNull(key);
        assertEquals("AES", key.getAlgorithm());
        assertEquals(256, key.getEncoded().length * 8);
    }

    @Test
    void testGenerateKey192() throws Exception {
        SecretKey key = AESUtil.generateKey(192);
        assertNotNull(key);
        assertEquals("AES", key.getAlgorithm());
        assertEquals(192, key.getEncoded().length * 8);
    }

    @Test
    void testGenerateKey128() throws Exception {
        SecretKey key = AESUtil.generateKey(128);
        assertNotNull(key);
        assertEquals("AES", key.getAlgorithm());
        assertEquals(128, key.getEncoded().length * 8);
    }

    @Test
    void testGenerateKeyDefault() throws Exception {
        SecretKey key = AESUtil.generateKey();
        assertNotNull(key);
        assertEquals(256, key.getEncoded().length * 8);
    }

    @Test
    void testGenerateKeyBytes() throws Exception {
        byte[] keyBytes = AESUtil.generateKeyBytes(256);
        assertNotNull(keyBytes);
        assertEquals(32, keyBytes.length);
    }

    @Test
    void testGenerateKeyBytesDefault() throws Exception {
        byte[] keyBytes = AESUtil.generateKeyBytes();
        assertNotNull(keyBytes);
        assertEquals(32, keyBytes.length);
    }

    @Test
    void testEncryptDecryptGCM() throws Exception {
        String encrypted = AESUtil.encrypt(TEST_DATA, key256);
        assertNotNull(encrypted);
        assertFalse(encrypted.isEmpty());

        String decrypted = AESUtil.decrypt(encrypted, key256);
        assertEquals(TEST_DATA, decrypted);
    }

    @Test
    void testEncryptDecryptGCMWithDifferentKeys() throws Exception {
        String encrypted = AESUtil.encrypt(TEST_DATA, key256);
        String decrypted = AESUtil.decrypt(encrypted, key256);
        assertEquals(TEST_DATA, decrypted);
    }

    @Test
    void testEncryptDecryptGBK() throws Exception {
        String encrypted = AESUtil.encrypt(TEST_DATA_GBK, key256);
        assertNotNull(encrypted);

        String decrypted = AESUtil.decrypt(encrypted, key256);
        assertEquals(TEST_DATA_GBK, decrypted);
    }

    @Test
    void testEncryptDecryptEmptyString() throws Exception {
        String encrypted = AESUtil.encrypt("", key256);
        assertNotNull(encrypted);

        String decrypted = AESUtil.decrypt(encrypted, key256);
        assertEquals("", decrypted);
    }

    @Test
    void testEncryptDecryptLongData() throws Exception {
        StringBuilder longData = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longData.append("测试数据").append(i);
        }
        String data = longData.toString();

        String encrypted = AESUtil.encrypt(data, key256);
        String decrypted = AESUtil.decrypt(encrypted, key256);
        assertEquals(data, decrypted);
    }

    @Test
    void testEncryptDecryptWithDifferentKeySizes() throws Exception {
        String encrypted256 = AESUtil.encrypt(TEST_DATA, key256);
        String decrypted256 = AESUtil.decrypt(encrypted256, key256);
        assertEquals(TEST_DATA, decrypted256);

        String encrypted192 = AESUtil.encrypt(TEST_DATA, key192);
        String decrypted192 = AESUtil.decrypt(encrypted192, key192);
        assertEquals(TEST_DATA, decrypted192);

        String encrypted128 = AESUtil.encrypt(TEST_DATA, key128);
        String decrypted128 = AESUtil.decrypt(encrypted128, key128);
        assertEquals(TEST_DATA, decrypted128);
    }

    @Test
    void testEncryptDecryptECB() throws Exception {
        String encrypted = AESUtil.encryptWithECB(TEST_DATA, keyBytes256);
        assertNotNull(encrypted);
        assertFalse(encrypted.isEmpty());

        String decrypted = AESUtil.decryptWithECB(encrypted, keyBytes256);
        assertEquals(TEST_DATA, decrypted);
    }

    @Test
    void testEncryptDecryptECBWithDifferentKeySizes() throws Exception {
        byte[] keyBytes128 = AESUtil.generateKeyBytes(128);
        byte[] keyBytes192 = AESUtil.generateKeyBytes(192);

        String encrypted128 = AESUtil.encryptWithECB(TEST_DATA, keyBytes128);
        String decrypted128 = AESUtil.decryptWithECB(encrypted128, keyBytes128);
        assertEquals(TEST_DATA, decrypted128);

        String encrypted192 = AESUtil.encryptWithECB(TEST_DATA, keyBytes192);
        String decrypted192 = AESUtil.decryptWithECB(encrypted192, keyBytes192);
        assertEquals(TEST_DATA, decrypted192);
    }

    @Test
    void testEncryptDecryptCBC() throws Exception {
        byte[] iv = AESUtil.generateIV16();
        String encrypted = AESUtil.encryptWithCBC(TEST_DATA, keyBytes256, iv);
        assertNotNull(encrypted);
        assertFalse(encrypted.isEmpty());

        String decrypted = AESUtil.decryptWithCBC(encrypted, keyBytes256, iv);
        assertEquals(TEST_DATA, decrypted);
    }

    @Test
    void testEncryptDecryptCBCWithSameIV() throws Exception {
        byte[] iv = AESUtil.generateIV16();

        String encrypted = AESUtil.encryptWithCBC(TEST_DATA, keyBytes256, iv);
        String decrypted = AESUtil.decryptWithCBC(encrypted, keyBytes256, iv);
        assertEquals(TEST_DATA, decrypted);
    }

    @Test
    void testEncryptDecryptWithIV() throws Exception {
        byte[] iv = AESUtil.generateIV();
        String encrypted = AESUtil.encryptWithIV(TEST_DATA, keyBytes256, iv);
        assertNotNull(encrypted);

        String decrypted = AESUtil.decryptWithIV(encrypted, keyBytes256);
        assertEquals(TEST_DATA, decrypted);
    }

    @Test
    void testEncryptBytesDecryptBytes() throws Exception {
        byte[] encrypted = AESUtil.encryptBytes(TEST_DATA_BYTES, key256);
        assertNotNull(encrypted);
        assertTrue(encrypted.length > TEST_DATA_BYTES.length);

        byte[] decrypted = AESUtil.decryptBytes(encrypted, key256);
        assertArrayEquals(TEST_DATA_BYTES, decrypted);
    }

    @Test
    void testEncryptBytesDecryptBytesEmpty() throws Exception {
        byte[] encrypted = AESUtil.encryptBytes(new byte[0], key256);
        byte[] decrypted = AESUtil.decryptBytes(encrypted, key256);
        assertArrayEquals(new byte[0], decrypted);
    }

    @Test
    void testEncryptFileDecryptFile(@TempDir Path tempDir) throws Exception {
        Path inputFile = tempDir.resolve("test.txt");
        Path encryptedFile = tempDir.resolve("test.enc");
        Path outputFile = tempDir.resolve("output.txt");

        Files.write(inputFile, TEST_DATA.getBytes());

        AESUtil.encryptFile(inputFile.toString(), encryptedFile.toString(), key256);
        assertTrue(Files.exists(encryptedFile));
        assertTrue(Files.size(encryptedFile) > 0);

        AESUtil.decryptFile(encryptedFile.toString(), outputFile.toString(), key256);
        assertTrue(Files.exists(outputFile));
        assertEquals(TEST_DATA, new String(Files.readAllBytes(outputFile)));
    }

    @Test
    void testEncryptFileDecryptFileWithBytesKey(@TempDir Path tempDir) throws Exception {
        Path inputFile = tempDir.resolve("test.txt");
        Path encryptedFile = tempDir.resolve("test.enc");
        Path outputFile = tempDir.resolve("output.txt");

        Files.write(inputFile, TEST_DATA.getBytes());

        AESUtil.encryptFile(inputFile.toString(), encryptedFile.toString(), keyBytes256);
        AESUtil.decryptFile(encryptedFile.toString(), outputFile.toString(), keyBytes256);
        assertEquals(TEST_DATA, new String(Files.readAllBytes(outputFile)));
    }

    @Test
    void testImportKey() throws Exception {
        SecretKey importedKey = AESUtil.importKey(keyBytes256);
        assertNotNull(importedKey);
        assertEquals("AES", importedKey.getAlgorithm());
        assertArrayEquals(keyBytes256, importedKey.getEncoded());
    }

    @Test
    void testExportKey() throws Exception {
        String exportedKey = AESUtil.exportKey(key256);
        assertNotNull(exportedKey);
        assertFalse(exportedKey.isEmpty());

        byte[] keyBytes = java.util.Base64.getDecoder().decode(exportedKey);
        assertArrayEquals(key256.getEncoded(), keyBytes);
    }

    @Test
    void testImportKeyFromString() throws Exception {
        String keyStr = AESUtil.exportKey(key256);
        SecretKey importedKey = AESUtil.importKeyFromString(keyStr);
        assertNotNull(importedKey);
        assertArrayEquals(key256.getEncoded(), importedKey.getEncoded());
    }

    @Test
    void testExportKeyToFileImportKeyFromFile(@TempDir Path tempDir) throws Exception {
        Path keyFile = tempDir.resolve("key.txt");

        AESUtil.exportKeyToFile(key256, keyFile.toString());
        assertTrue(Files.exists(keyFile));

        SecretKey importedKey = AESUtil.importKeyFromFile(keyFile.toString());
        assertNotNull(importedKey);
        assertArrayEquals(key256.getEncoded(), importedKey.getEncoded());
    }

    @Test
    void testGenerateIV() throws Exception {
        byte[] iv1 = AESUtil.generateIV();
        byte[] iv2 = AESUtil.generateIV();

        assertNotNull(iv1);
        assertEquals(12, iv1.length);

        assertNotNull(iv2);
        assertEquals(12, iv2.length);

        assertFalse(java.util.Arrays.equals(iv1, iv2));
    }

    @Test
    void testBytesToHex() throws Exception {
        byte[] bytes = {0x01, 0x02, (byte) 0xab, (byte) 0xcd, 0x10, (byte) 0xff};
        String hex = AESUtil.bytesToHex(bytes);
        assertEquals("0102abcd10ff", hex);
    }

    @Test
    void testBytesToHexEmpty() throws Exception {
        String hex = AESUtil.bytesToHex(new byte[0]);
        assertEquals("", hex);
    }

    @Test
    void testHexToBytes() throws Exception {
        String hex = "0102abcd10ff";
        byte[] bytes = AESUtil.hexToBytes(hex);
        assertArrayEquals(new byte[]{0x01, 0x02, (byte) 0xab, (byte) 0xcd, 0x10, (byte) 0xff}, bytes);
    }

    @Test
    void testHexToBytesEmpty() throws Exception {
        byte[] bytes = AESUtil.hexToBytes("");
        assertArrayEquals(new byte[0], bytes);
    }

    @Test
    void testBytesToHexAndHexToBytesRoundTrip() throws Exception {
        byte[] originalBytes = "测试数据".getBytes();
        String hex = AESUtil.bytesToHex(originalBytes);
        byte[] resultBytes = AESUtil.hexToBytes(hex);
        assertArrayEquals(originalBytes, resultBytes);
    }

    @Test
    void testDecryptWithWrongKey() throws Exception {
        String encrypted = AESUtil.encrypt(TEST_DATA, key256);

        byte[] wrongKeyBytes = AESUtil.generateKeyBytes(256);
        assertThrows(Exception.class, () -> {
            AESUtil.decrypt(encrypted, AESUtil.importKey(wrongKeyBytes));
        });
    }

    @Test
    void testDecryptWithInvalidData() {
        String invalidData = "这不是有效的加密数据!!!";
        assertThrows(Exception.class, () -> {
            AESUtil.decrypt(invalidData, key256);
        });
    }

    @Test
    void testDecryptECBWithWrongKey() throws Exception {
        String encrypted = AESUtil.encryptWithECB(TEST_DATA, keyBytes256);

        byte[] wrongKeyBytes = AESUtil.generateKeyBytes(256);
        assertThrows(Exception.class, () -> {
            AESUtil.decryptWithECB(encrypted, wrongKeyBytes);
        });
    }

    @Test
    void testDecryptCBCWithWrongIV() throws Exception {
        byte[] iv = AESUtil.generateIV16();
        String encrypted = AESUtil.encryptWithCBC(TEST_DATA, keyBytes256, iv);

        byte[] wrongIV = AESUtil.generateIV16();
        assertThrows(Exception.class, () -> {
            AESUtil.decryptWithCBC(encrypted, keyBytes256, wrongIV);
        });
    }

    @Test
    void testEncryptDecryptSpecialCharacters() throws Exception {
        String specialData = "!@#$%^&*()_+-=[]{}|;':\",./<>?`~\n\t\r";
        String encrypted = AESUtil.encrypt(specialData, key256);
        String decrypted = AESUtil.decrypt(encrypted, key256);
        assertEquals(specialData, decrypted);
    }

    @Test
    void testEncryptDecryptUnicode() throws Exception {
        String unicodeData = "中文日本語한국어العربيةעברית";
        String encrypted = AESUtil.encrypt(unicodeData, key256);
        String decrypted = AESUtil.decrypt(encrypted, key256);
        assertEquals(unicodeData, decrypted);
    }
}
