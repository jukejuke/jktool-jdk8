package io.github.jukejuke.tool.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class PropertiesUtilsTest {

    private static final String TEST_FILE_NAME = "test.properties";
    private static final String TEST_FILE_CONTENT = "key1=value1\nkey2=123\nkey3=3.14\nkey4=true";
    private static final String TEST_FILE_PATH;

    static {
        try {
            // 创建临时文件用于测试
            File tempFile = File.createTempFile("test", ".properties");
            tempFile.deleteOnExit();
            Files.write(Paths.get(tempFile.getAbsolutePath()), TEST_FILE_CONTENT.getBytes());
            TEST_FILE_PATH = tempFile.getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create test file", e);
        }
    }

    @BeforeEach
    void setUp() {
        // 在每个测试方法前执行
    }

    @Test
    void testLoadFromFileSystem() throws IOException {
        Properties properties = PropertiesUtils.loadFromFileSystem(TEST_FILE_PATH);
        assertNotNull(properties);
        assertEquals("value1", properties.getProperty("key1"));
        assertEquals("123", properties.getProperty("key2"));
        assertEquals("3.14", properties.getProperty("key3"));
        assertEquals("true", properties.getProperty("key4"));
    }

    @Test
    void testGetString() throws IOException {
        Properties properties = PropertiesUtils.loadFromFileSystem(TEST_FILE_PATH);
        assertEquals("value1", PropertiesUtils.getString(properties, "key1"));
        assertEquals("default", PropertiesUtils.getString(properties, "nonexistent", "default"));
        assertNull(PropertiesUtils.getString(null, "key1"));
        assertNull(PropertiesUtils.getString(properties, "nonexistent"));
    }

    @Test
    void testGetInteger() throws IOException {
        Properties properties = PropertiesUtils.loadFromFileSystem(TEST_FILE_PATH);
        assertEquals(123, PropertiesUtils.getInteger(properties, "key2"));
        assertEquals(456, PropertiesUtils.getInteger(properties, "nonexistent", 456));
        assertNull(PropertiesUtils.getInteger(null, "key2"));
        assertNull(PropertiesUtils.getInteger(properties, "nonexistent"));
        assertEquals(456, PropertiesUtils.getInteger(properties, "invalidNumber", 456));
    }

    @Test
    void testGetLong() throws IOException {
        Properties properties = PropertiesUtils.loadFromFileSystem(TEST_FILE_PATH);
        properties.setProperty("longKey", "1234567890123");
        assertEquals(1234567890123L, PropertiesUtils.getLong(properties, "longKey"));
        assertEquals(4567890123456L, PropertiesUtils.getLong(properties, "nonexistent", 4567890123456L));
        assertNull(PropertiesUtils.getLong(null, "longKey"));
        assertNull(PropertiesUtils.getLong(properties, "nonexistent"));
        assertEquals(456L, PropertiesUtils.getLong(properties, "invalidNumber", 456L));
    }

    @Test
    void testGetBoolean() throws IOException {
        Properties properties = PropertiesUtils.loadFromFileSystem(TEST_FILE_PATH);
        assertTrue(PropertiesUtils.getBoolean(properties, "key4"));
        assertFalse(PropertiesUtils.getBoolean(properties, "nonexistent", false));
        assertNull(PropertiesUtils.getBoolean(null, "key4"));
        assertNull(PropertiesUtils.getBoolean(properties, "nonexistent"));
        assertFalse(PropertiesUtils.getBoolean(properties, "invalidBoolean", false));
    }

    @Test
    void testGetDouble() throws IOException {
        Properties properties = PropertiesUtils.loadFromFileSystem(TEST_FILE_PATH);
        assertEquals(3.14, PropertiesUtils.getDouble(properties, "key3"));
        assertEquals(6.28, PropertiesUtils.getDouble(properties, "nonexistent", 6.28));
        assertNull(PropertiesUtils.getDouble(null, "key3"));
        assertNull(PropertiesUtils.getDouble(properties, "nonexistent"));
        assertEquals(6.28, PropertiesUtils.getDouble(properties, "invalidNumber", 6.28));
    }

    @Test
    void testSaveToFileSystem() throws IOException {
        // 创建一个新的Properties对象并保存
        Properties properties = new Properties();
        properties.setProperty("newKey1", "newValue1");
        properties.setProperty("newKey2", "789");

        // 使用临时文件路径保存
        String savePath = TEST_FILE_PATH + ".save";
        File saveFile = new File(savePath);
        saveFile.deleteOnExit();

        PropertiesUtils.saveToFileSystem(properties, savePath);

        // 验证保存是否成功
        Properties savedProperties = PropertiesUtils.loadFromFileSystem(savePath);
        assertNotNull(savedProperties);
        assertEquals("newValue1", savedProperties.getProperty("newKey1"));
        assertEquals("789", savedProperties.getProperty("newKey2"));
    }

    @Test
    void testSaveToFileSystemWithNullProperties() {
        assertThrows(IllegalArgumentException.class, () -> {
            PropertiesUtils.saveToFileSystem(null, TEST_FILE_PATH);
        });
    }

    @Test
    void testSaveToClassPath() throws IOException {
        // 这个测试需要在类路径中有一个可写的文件
        // 由于在CI环境或JAR文件中可能无法写入类路径，这里仅测试异常情况
        Properties properties = new Properties();
        properties.setProperty("testKey", "testValue");

        // 测试不存在的文件路径
        assertThrows(IOException.class, () -> {
            PropertiesUtils.saveToClassPath(properties, "nonexistent/config.properties");
        });
    }

    @Test
    void testSaveToClassPathWithNullProperties() {
        assertThrows(IllegalArgumentException.class, () -> {
            PropertiesUtils.saveToClassPath(null, "test.properties");
        });
    }

    @Test
    void testLoadFromClassPath() {
        // 这个测试需要类路径中有一个测试文件
        // 由于我们没有在类路径中放置测试文件，这里仅测试异常情况
        assertThrows(IOException.class, () -> {
            PropertiesUtils.loadFromClassPath("nonexistent/config.properties");
        });
    }
}
