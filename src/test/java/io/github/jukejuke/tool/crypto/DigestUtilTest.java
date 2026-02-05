package io.github.jukejuke.tool.crypto;

import io.github.jukejuke.tool.crypto.digest.DigestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class DigestUtilTest {

    private static final String TEST_DATA = "测试数据TestData123456";
    private static final String TEST_DATA_GBK = "中文测试数据";
    private static final byte[] TEST_DATA_BYTES = "二进制测试数据".getBytes();
    private static final String TEST_KEY = "secretKey123";

    @Test
    void testMd5String() throws Exception {
        String hash = DigestUtil.md5(TEST_DATA);
        assertNotNull(hash);
        assertEquals(32, hash.length());
        assertFalse(hash.isEmpty());
    }

    @Test
    void testMd5Bytes() throws Exception {
        String hash = DigestUtil.md5(TEST_DATA_BYTES);
        assertNotNull(hash);
        assertEquals(32, hash.length());
    }

    @Test
    void testMd5EmptyString() throws Exception {
        String hash = DigestUtil.md5("");
        assertNotNull(hash);
        assertEquals(32, hash.length());
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", hash);
    }

    @Test
    void testMd5EmptyBytes() throws Exception {
        String hash = DigestUtil.md5(new byte[0]);
        assertNotNull(hash);
        assertEquals(32, hash.length());
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", hash);
    }

    @Test
    void testMd5Consistency() throws Exception {
        String hash1 = DigestUtil.md5(TEST_DATA);
        String hash2 = DigestUtil.md5(TEST_DATA);
        assertEquals(hash1, hash2);
    }

    @Test
    void testMd5DifferentData() throws Exception {
        String hash1 = DigestUtil.md5("data1");
        String hash2 = DigestUtil.md5("data2");
        assertNotEquals(hash1, hash2);
    }

    @Test
    void testMd5File(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("test.txt");
        Files.write(testFile, TEST_DATA.getBytes());

        String hash = DigestUtil.md5File(testFile.toString());
        assertNotNull(hash);
        assertEquals(32, hash.length());
    }

    @Test
    void testMd5FileEmpty(@TempDir Path tempDir) throws Exception {
        Path emptyFile = tempDir.resolve("empty.txt");
        Files.write(emptyFile, new byte[0]);

        String hash = DigestUtil.md5File(emptyFile.toString());
        assertNotNull(hash);
        assertEquals(32, hash.length());
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", hash);
    }

    @Test
    void testSha1String() throws Exception {
        String hash = DigestUtil.sha1(TEST_DATA);
        assertNotNull(hash);
        assertEquals(40, hash.length());
    }

    @Test
    void testSha1Bytes() throws Exception {
        String hash = DigestUtil.sha1(TEST_DATA_BYTES);
        assertNotNull(hash);
        assertEquals(40, hash.length());
    }

    @Test
    void testSha1Empty() throws Exception {
        String hash = DigestUtil.sha1("");
        assertNotNull(hash);
        assertEquals(40, hash.length());
        assertEquals("da39a3ee5e6b4b0d3255bfef95601890afd80709", hash);
    }

    @Test
    void testSha1Consistency() throws Exception {
        String hash1 = DigestUtil.sha1(TEST_DATA);
        String hash2 = DigestUtil.sha1(TEST_DATA);
        assertEquals(hash1, hash2);
    }

    @Test
    void testSha1File(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("test.txt");
        Files.write(testFile, TEST_DATA.getBytes());

        String hash = DigestUtil.sha1File(testFile.toString());
        assertNotNull(hash);
        assertEquals(40, hash.length());
    }

    @Test
    void testSha256String() throws Exception {
        String hash = DigestUtil.sha256(TEST_DATA);
        assertNotNull(hash);
        assertEquals(64, hash.length());
    }

    @Test
    void testSha256Bytes() throws Exception {
        String hash = DigestUtil.sha256(TEST_DATA_BYTES);
        assertNotNull(hash);
        assertEquals(64, hash.length());
    }

    @Test
    void testSha256Empty() throws Exception {
        String hash = DigestUtil.sha256("");
        assertNotNull(hash);
        assertEquals(64, hash.length());
        assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", hash);
    }

    @Test
    void testSha256Consistency() throws Exception {
        String hash1 = DigestUtil.sha256(TEST_DATA);
        String hash2 = DigestUtil.sha256(TEST_DATA);
        assertEquals(hash1, hash2);
    }

    @Test
    void testSha256File(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("test.txt");
        Files.write(testFile, TEST_DATA.getBytes());

        String hash = DigestUtil.sha256File(testFile.toString());
        assertNotNull(hash);
        assertEquals(64, hash.length());
    }

    @Test
    void testSha384String() throws Exception {
        String hash = DigestUtil.sha384(TEST_DATA);
        assertNotNull(hash);
        assertEquals(96, hash.length());
    }

    @Test
    void testSha384Bytes() throws Exception {
        String hash = DigestUtil.sha384(TEST_DATA_BYTES);
        assertNotNull(hash);
        assertEquals(96, hash.length());
    }

    @Test
    void testSha384Empty() throws Exception {
        String hash = DigestUtil.sha384("");
        assertNotNull(hash);
        assertEquals(96, hash.length());
    }

    @Test
    void testSha384Consistency() throws Exception {
        String hash1 = DigestUtil.sha384(TEST_DATA);
        String hash2 = DigestUtil.sha384(TEST_DATA);
        assertEquals(hash1, hash2);
    }

    @Test
    void testSha384File(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("test.txt");
        Files.write(testFile, TEST_DATA.getBytes());

        String hash = DigestUtil.sha384File(testFile.toString());
        assertNotNull(hash);
        assertEquals(96, hash.length());
    }

    @Test
    void testSha512String() throws Exception {
        String hash = DigestUtil.sha512(TEST_DATA);
        assertNotNull(hash);
        assertEquals(128, hash.length());
    }

    @Test
    void testSha512Bytes() throws Exception {
        String hash = DigestUtil.sha512(TEST_DATA_BYTES);
        assertNotNull(hash);
        assertEquals(128, hash.length());
    }

    @Test
    void testSha512Empty() throws Exception {
        String hash = DigestUtil.sha512("");
        assertNotNull(hash);
        assertEquals(128, hash.length());
    }

    @Test
    void testSha512Consistency() throws Exception {
        String hash1 = DigestUtil.sha512(TEST_DATA);
        String hash2 = DigestUtil.sha512(TEST_DATA);
        assertEquals(hash1, hash2);
    }

    @Test
    void testSha512File(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("test.txt");
        Files.write(testFile, TEST_DATA.getBytes());

        String hash = DigestUtil.sha512File(testFile.toString());
        assertNotNull(hash);
        assertEquals(128, hash.length());
    }

    @Test
    void testSha3_256String() throws Exception {
        String hash = DigestUtil.sha3_256(TEST_DATA);
        assertNotNull(hash);
        assertEquals(64, hash.length());
    }

    @Test
    void testSha3_256Bytes() throws Exception {
        String hash = DigestUtil.sha3_256(TEST_DATA_BYTES);
        assertNotNull(hash);
        assertEquals(64, hash.length());
    }

    @Test
    void testSha3_256Consistency() throws Exception {
        String hash1 = DigestUtil.sha3_256(TEST_DATA);
        String hash2 = DigestUtil.sha3_256(TEST_DATA);
        assertEquals(hash1, hash2);
    }

    @Test
    void testSha3_256File(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("test.txt");
        Files.write(testFile, TEST_DATA.getBytes());

        String hash = DigestUtil.sha3_256File(testFile.toString());
        assertNotNull(hash);
        assertEquals(64, hash.length());
    }

    @Test
    void testSha3_384String() throws Exception {
        String hash = DigestUtil.sha3_384(TEST_DATA);
        assertNotNull(hash);
        assertEquals(96, hash.length());
    }

    @Test
    void testSha3_384Bytes() throws Exception {
        String hash = DigestUtil.sha3_384(TEST_DATA_BYTES);
        assertNotNull(hash);
        assertEquals(96, hash.length());
    }

    @Test
    void testSha3_384Consistency() throws Exception {
        String hash1 = DigestUtil.sha3_384(TEST_DATA);
        String hash2 = DigestUtil.sha3_384(TEST_DATA);
        assertEquals(hash1, hash2);
    }

    @Test
    void testSha3_384File(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("test.txt");
        Files.write(testFile, TEST_DATA.getBytes());

        String hash = DigestUtil.sha3_384File(testFile.toString());
        assertNotNull(hash);
        assertEquals(96, hash.length());
    }

    @Test
    void testSha3_512String() throws Exception {
        String hash = DigestUtil.sha3_512(TEST_DATA);
        assertNotNull(hash);
        assertEquals(128, hash.length());
    }

    @Test
    void testSha3_512Bytes() throws Exception {
        String hash = DigestUtil.sha3_512(TEST_DATA_BYTES);
        assertNotNull(hash);
        assertEquals(128, hash.length());
    }

    @Test
    void testSha3_512Consistency() throws Exception {
        String hash1 = DigestUtil.sha3_512(TEST_DATA);
        String hash2 = DigestUtil.sha3_512(TEST_DATA);
        assertEquals(hash1, hash2);
    }

    @Test
    void testSha3_512File(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("test.txt");
        Files.write(testFile, TEST_DATA.getBytes());

        String hash = DigestUtil.sha3_512File(testFile.toString());
        assertNotNull(hash);
        assertEquals(128, hash.length());
    }

    @Test
    void testHmacMd5String() throws Exception {
        String hmac = DigestUtil.hmacMd5(TEST_DATA, TEST_KEY);
        assertNotNull(hmac);
        assertFalse(hmac.isEmpty());
    }

    @Test
    void testHmacMd5Bytes() throws Exception {
        String hmac = DigestUtil.hmacMd5(TEST_DATA_BYTES, TEST_KEY.getBytes());
        assertNotNull(hmac);
        assertFalse(hmac.isEmpty());
    }

    @Test
    void testHmacMd5EmptyData() throws Exception {
        String hmac = DigestUtil.hmacMd5("", TEST_KEY);
        assertNotNull(hmac);
        assertFalse(hmac.isEmpty());
    }

    @Test
    void testHmacMd5EmptyKey() throws Exception {
        String hmac = DigestUtil.hmacMd5(TEST_DATA, "");
        assertNotNull(hmac);
        assertFalse(hmac.isEmpty());
    }

    @Test
    void testHmacMd5Consistency() throws Exception {
        String hmac1 = DigestUtil.hmacMd5(TEST_DATA, TEST_KEY);
        String hmac2 = DigestUtil.hmacMd5(TEST_DATA, TEST_KEY);
        assertEquals(hmac1, hmac2);
    }

    @Test
    void testHmacMd5DifferentKeys() throws Exception {
        String hmac1 = DigestUtil.hmacMd5(TEST_DATA, "key1");
        String hmac2 = DigestUtil.hmacMd5(TEST_DATA, "key2");
        assertNotEquals(hmac1, hmac2);
    }

    @Test
    void testHmacSha1String() throws Exception {
        String hmac = DigestUtil.hmacSha1(TEST_DATA, TEST_KEY);
        assertNotNull(hmac);
        assertFalse(hmac.isEmpty());
    }

    @Test
    void testHmacSha1Bytes() throws Exception {
        String hmac = DigestUtil.hmacSha1(TEST_DATA_BYTES, TEST_KEY.getBytes());
        assertNotNull(hmac);
        assertFalse(hmac.isEmpty());
    }

    @Test
    void testHmacSha1Consistency() throws Exception {
        String hmac1 = DigestUtil.hmacSha1(TEST_DATA, TEST_KEY);
        String hmac2 = DigestUtil.hmacSha1(TEST_DATA, TEST_KEY);
        assertEquals(hmac1, hmac2);
    }

    @Test
    void testHmacSha256String() throws Exception {
        String hmac = DigestUtil.hmacSha256(TEST_DATA, TEST_KEY);
        assertNotNull(hmac);
        assertFalse(hmac.isEmpty());
    }

    @Test
    void testHmacSha256Bytes() throws Exception {
        String hmac = DigestUtil.hmacSha256(TEST_DATA_BYTES, TEST_KEY.getBytes());
        assertNotNull(hmac);
        assertFalse(hmac.isEmpty());
    }

    @Test
    void testHmacSha256Consistency() throws Exception {
        String hmac1 = DigestUtil.hmacSha256(TEST_DATA, TEST_KEY);
        String hmac2 = DigestUtil.hmacSha256(TEST_DATA, TEST_KEY);
        assertEquals(hmac1, hmac2);
    }

    @Test
    void testHmacSha512String() throws Exception {
        String hmac = DigestUtil.hmacSha512(TEST_DATA, TEST_KEY);
        assertNotNull(hmac);
        assertFalse(hmac.isEmpty());
    }

    @Test
    void testHmacSha512Bytes() throws Exception {
        String hmac = DigestUtil.hmacSha512(TEST_DATA_BYTES, TEST_KEY.getBytes());
        assertNotNull(hmac);
        assertFalse(hmac.isEmpty());
    }

    @Test
    void testHmacSha512Consistency() throws Exception {
        String hmac1 = DigestUtil.hmacSha512(TEST_DATA, TEST_KEY);
        String hmac2 = DigestUtil.hmacSha512(TEST_DATA, TEST_KEY);
        assertEquals(hmac1, hmac2);
    }

    @Test
    void testHmacGeneric() throws Exception {
        String hmac = DigestUtil.hmac(TEST_DATA, TEST_KEY, "HmacSHA256");
        assertNotNull(hmac);
        assertFalse(hmac.isEmpty());
    }

    @Test
    void testHmacGenericBytes() throws Exception {
        String hmac = DigestUtil.hmac(TEST_DATA_BYTES, TEST_KEY.getBytes(), "HmacSHA256");
        assertNotNull(hmac);
        assertFalse(hmac.isEmpty());
    }

    @Test
    void testVerifyDigestSame() throws Exception {
        String hash1 = DigestUtil.md5(TEST_DATA);
        String hash2 = DigestUtil.md5(TEST_DATA);
        assertTrue(DigestUtil.verifyDigest(hash1, hash2));
    }

    @Test
    void testVerifyDigestDifferent() throws Exception {
        String hash1 = DigestUtil.md5("data1");
        String hash2 = DigestUtil.md5("data2");
        assertFalse(DigestUtil.verifyDigest(hash1, hash2));
    }

    @Test
    void testVerifyDigestCaseInsensitive() throws Exception {
        String hash1 = DigestUtil.md5(TEST_DATA);
        String hash2 = hash1.toUpperCase();
        assertTrue(DigestUtil.verifyDigest(hash1, hash2));
    }

    @Test
    void testVerifyDigestNull() {
        assertFalse(DigestUtil.verifyDigest(null, "hash"));
        assertFalse(DigestUtil.verifyDigest("hash", null));
        assertFalse(DigestUtil.verifyDigest(null, null));
    }

    @Test
    void testDigestFileGeneric(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("test.txt");
        Files.write(testFile, TEST_DATA.getBytes());

        String hash = DigestUtil.digestFile(testFile.toString(), "SHA-256");
        assertNotNull(hash);
        assertEquals(64, hash.length());
    }

    @Test
    void testDigestFileMd5(@TempDir Path tempDir) throws Exception {
        Path testFile = tempDir.resolve("test.txt");
        Files.write(testFile, TEST_DATA.getBytes());

        String hash = DigestUtil.digestFile(testFile.toString(), "MD5");
        assertNotNull(hash);
        assertEquals(32, hash.length());
    }

    @Test
    void testBytesToHex() throws Exception {
        byte[] bytes = {0x01, 0x02, (byte) 0xab, (byte) 0xcd, 0x10, (byte) 0xff};
        String hex = DigestUtil.bytesToHex(bytes);
        assertEquals("0102abcd10ff", hex);
    }

    @Test
    void testBytesToHexEmpty() throws Exception {
        String hex = DigestUtil.bytesToHex(new byte[0]);
        assertEquals("", hex);
    }

    @Test
    void testHexToBytes() throws Exception {
        String hex = "0102abcd10ff";
        byte[] bytes = DigestUtil.hexToBytes(hex);
        assertArrayEquals(new byte[]{0x01, 0x02, (byte) 0xab, (byte) 0xcd, 0x10, (byte) 0xff}, bytes);
    }

    @Test
    void testHexToBytesEmpty() throws Exception {
        byte[] bytes = DigestUtil.hexToBytes("");
        assertArrayEquals(new byte[0], bytes);
    }

    @Test
    void testBytesToHexAndHexToBytesRoundTrip() throws Exception {
        byte[] originalBytes = "测试数据".getBytes();
        String hex = DigestUtil.bytesToHex(originalBytes);
        byte[] resultBytes = DigestUtil.hexToBytes(hex);
        assertArrayEquals(originalBytes, resultBytes);
    }

    @Test
    void testBytesToBase64() throws Exception {
        byte[] bytes = TEST_DATA_BYTES;
        String base64 = DigestUtil.bytesToBase64(bytes);
        assertNotNull(base64);
        assertFalse(base64.isEmpty());
    }

    @Test
    void testBase64ToBytes() throws Exception {
        String original = DigestUtil.bytesToBase64(TEST_DATA_BYTES);
        byte[] bytes = DigestUtil.base64ToBytes(original);
        assertArrayEquals(TEST_DATA_BYTES, bytes);
    }

    @Test
    void testBytesToBase64AndBase64ToBytesRoundTrip() throws Exception {
        byte[] originalBytes = "测试数据".getBytes();
        String base64 = DigestUtil.bytesToBase64(originalBytes);
        byte[] resultBytes = DigestUtil.base64ToBytes(base64);
        assertArrayEquals(originalBytes, resultBytes);
    }

    @Test
    void testChineseData() throws Exception {
        String hash = DigestUtil.md5(TEST_DATA_GBK);
        assertNotNull(hash);
        assertEquals(32, hash.length());
    }

    @Test
    void testSpecialCharacters() throws Exception {
        String specialData = "!@#$%^&*()_+-=[]{}|;':\",./<>?`~\n\t\r";
        String hash = DigestUtil.sha256(specialData);
        assertNotNull(hash);
        assertEquals(64, hash.length());
    }

    @Test
    void testUnicodeData() throws Exception {
        String unicodeData = "中文日本語한국어العربيةעברית";
        String hash = DigestUtil.sha256(unicodeData);
        assertNotNull(hash);
        assertEquals(64, hash.length());
    }

    @Test
    void testLongData() throws Exception {
        StringBuilder longData = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longData.append("测试数据").append(i);
        }
        String data = longData.toString();

        String hash = DigestUtil.md5(data);
        assertNotNull(hash);
        assertEquals(32, hash.length());
    }

    @Test
    void testDifferentAlgorithmsProduceDifferentHashes() throws Exception {
        String data = "test";
        String md5Hash = DigestUtil.md5(data);
        String sha1Hash = DigestUtil.sha1(data);
        String sha256Hash = DigestUtil.sha256(data);

        assertNotEquals(md5Hash, sha1Hash);
        assertNotEquals(md5Hash, sha256Hash);
        assertNotEquals(sha1Hash, sha256Hash);
    }

    @Test
    void testHmacDifferentAlgorithmsProduceDifferentResults() throws Exception {
        String data = "test";
        String key = "key";

        String hmacMd5 = DigestUtil.hmacMd5(data, key);
        String hmacSha1 = DigestUtil.hmacSha1(data, key);
        String hmacSha256 = DigestUtil.hmacSha256(data, key);

        assertNotEquals(hmacMd5, hmacSha1);
        assertNotEquals(hmacMd5, hmacSha256);
        assertNotEquals(hmacSha1, hmacSha256);
    }
}
