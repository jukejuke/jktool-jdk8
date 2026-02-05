package io.github.jukejuke.tool.freemarker;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FreemarkerUtils测试类
 * 测试Freemarker模板处理工具的各种功能
 */
class FreemarkerUtilsTest {

    private File testTemplateFile;
    private Map<String, Object> testDataModel;

    @BeforeEach
    void setUp() throws IOException {
        // 创建测试数据模型
        testDataModel = new HashMap<>();
        testDataModel.put("name", "张三");
        testDataModel.put("age", 30);
        testDataModel.put("city", "北京");
        
        // 创建测试模板文件
        testTemplateFile = File.createTempFile("test_template", ".ftl");
        testTemplateFile.deleteOnExit();
        
        try (Writer writer = new FileWriter(testTemplateFile)) {
            writer.write("您好，${name}！您今年${age}岁，来自${city}。");
        }
    }

    @Test
    void testGetDefaultConfiguration() {
        Configuration configuration = FreemarkerUtils.getDefaultConfiguration();
        assertNotNull(configuration);
        assertEquals("UTF-8", configuration.getDefaultEncoding());
    }

    @Test
    void testCreateConfiguration() {
        Properties properties = new Properties();
        properties.setProperty("template_update_delay", "1000");
        properties.setProperty("default_encoding", "GBK");
        
        Configuration configuration = FreemarkerUtils.createConfiguration(properties);
        assertNotNull(configuration);
        assertEquals("GBK", configuration.getDefaultEncoding());
    }

    @Test
    void testRenderTemplate() throws IOException, TemplateException {
        String templateContent = "您好，${name}！您今年${age}岁，来自${city}。";
        
        String result = FreemarkerUtils.renderTemplate(templateContent, testDataModel);
        assertNotNull(result);
        assertEquals("您好，张三！您今年30岁，来自北京。", result);
    }

    @Test
    void testRenderTemplateWithCustomConfiguration() throws IOException, TemplateException {
        String templateContent = "您好，${name}！您今年${age}岁，来自${city}。";
        
        Properties properties = new Properties();
        properties.setProperty("default_encoding", "UTF-8");
        Configuration configuration = FreemarkerUtils.createConfiguration(properties);
        
        String result = FreemarkerUtils.renderTemplate(templateContent, testDataModel, configuration);
        assertNotNull(result);
        assertEquals("您好，张三！您今年30岁，来自北京。", result);
    }

    @Test
    void testRenderTemplateWithNullContent() throws IOException, TemplateException {
        String result = FreemarkerUtils.renderTemplate(null, testDataModel);
        assertNull(result);
        
        result = FreemarkerUtils.renderTemplate("", testDataModel);
        assertNull(result);
    }

    @Test
    void testRenderFileTemplate() throws IOException, TemplateException {
        String result = FreemarkerUtils.renderFileTemplate(testTemplateFile.getAbsolutePath(), testDataModel);
        assertNotNull(result);
        assertEquals("您好，张三！您今年30岁，来自北京。", result);
    }

    @Test
    void testRenderFileTemplateWithCustomConfiguration() throws IOException, TemplateException {
        Properties properties = new Properties();
        properties.setProperty("default_encoding", "UTF-8");
        Configuration configuration = FreemarkerUtils.createConfiguration(properties);
        
        String result = FreemarkerUtils.renderFileTemplate(testTemplateFile.getAbsolutePath(), testDataModel, configuration);
        assertNotNull(result);
        assertEquals("您好，张三！您今年30岁，来自北京。", result);
    }

    @Test
    void testRenderFileTemplateWithInvalidPath() {
        assertThrows(FileNotFoundException.class, () -> {
            FreemarkerUtils.renderFileTemplate("non_existent_template.ftl", testDataModel);
        });
    }

    @Test
    void testRenderFileTemplateWithNullPath() throws IOException, TemplateException {
        String result = FreemarkerUtils.renderFileTemplate(null, testDataModel);
        assertNull(result);
        
        result = FreemarkerUtils.renderFileTemplate("", testDataModel);
        assertNull(result);
    }

    @Test
    void testRenderTemplateToFile() throws IOException, TemplateException {
        String templateContent = "您好，${name}！您今年${age}岁，来自${city}。";
        File outputFile = File.createTempFile("test_output", ".txt");
        outputFile.deleteOnExit();
        
        FreemarkerUtils.renderTemplateToFile(templateContent, testDataModel, outputFile.getAbsolutePath());
        
        // 验证文件内容
        String fileContent = readFileContent(outputFile);
        assertEquals("您好，张三！您今年30岁，来自北京。", fileContent);
    }

    @Test
    void testRenderFileTemplateToFile() throws IOException, TemplateException {
        File outputFile = File.createTempFile("test_output", ".txt");
        outputFile.deleteOnExit();
        
        FreemarkerUtils.renderFileTemplateToFile(testTemplateFile.getAbsolutePath(), testDataModel, outputFile.getAbsolutePath());
        
        // 验证文件内容
        String fileContent = readFileContent(outputFile);
        assertEquals("您好，张三！您今年30岁，来自北京。", fileContent);
    }

    @Test
    void testRenderFileTemplateToStream() throws IOException, TemplateException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        FreemarkerUtils.renderFileTemplateToStream(testTemplateFile.getAbsolutePath(), testDataModel, outputStream);
        
        String result = outputStream.toString(FreemarkerUtils.DEFAULT_ENCODING);
        assertEquals("您好，张三！您今年30岁，来自北京。", result);
    }

    @Test
    void testRenderFileTemplateToStreamWithNullOutputStream() throws IOException, TemplateException {
        // 测试空输出流不会抛出异常
        FreemarkerUtils.renderFileTemplateToStream(testTemplateFile.getAbsolutePath(), testDataModel, null);
    }

    @Test
    void testRenderTemplateToFileWithNullOutputPath() throws IOException, TemplateException {
        String templateContent = "您好，${name}！";
        
        // 测试空输出路径不会抛出异常
        FreemarkerUtils.renderTemplateToFile(templateContent, testDataModel, null);
        FreemarkerUtils.renderTemplateToFile(templateContent, testDataModel, "");
    }

    @Test
    void testRenderFileTemplateToFileWithNullOutputPath() throws IOException, TemplateException {
        // 测试空输出路径不会抛出异常
        FreemarkerUtils.renderFileTemplateToFile(testTemplateFile.getAbsolutePath(), testDataModel, null);
        FreemarkerUtils.renderFileTemplateToFile(testTemplateFile.getAbsolutePath(), testDataModel, "");
    }

    /**
     * 读取文件内容
     * @param file 文件对象
     * @return 文件内容字符串
     * @throws IOException IO异常
     */
    private String readFileContent(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        }
        return content.toString();
    }
}
