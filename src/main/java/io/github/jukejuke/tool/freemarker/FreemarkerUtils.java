package io.github.jukejuke.tool.freemarker;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.*;
import java.io.*;
import java.util.Map;
import java.util.Properties;

/**
 * Freemarker模板处理工具类
 * 提供模板渲染、文件模板渲染、模板配置等功能
 */
public class FreemarkerUtils {

    // 默认编码格式
    public static final String DEFAULT_ENCODING = "UTF-8";
    
    // 默认模板配置
    private static final Configuration DEFAULT_CONFIGURATION;
    
    // 静态初始化默认配置
    static {
        DEFAULT_CONFIGURATION = new Configuration(Configuration.VERSION_2_3_32);
        DEFAULT_CONFIGURATION.setDefaultEncoding(DEFAULT_ENCODING);
        DEFAULT_CONFIGURATION.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        DEFAULT_CONFIGURATION.setLogTemplateExceptions(false);
    }
    
    /**
     * 获取默认的Freemarker配置
     * @return 默认配置对象
     */
    public static Configuration getDefaultConfiguration() {
        return DEFAULT_CONFIGURATION;
    }
    
    /**
     * 创建自定义的Freemarker配置
     * @param properties 配置属性
     * @return 自定义配置对象
     */
    public static Configuration createConfiguration(Properties properties) {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);
        configuration.setDefaultEncoding(DEFAULT_ENCODING);
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        
        if (properties != null) {
            // 设置自定义属性
            properties.forEach((key, value) -> {
                if (key instanceof String && value instanceof String) {
                    try {
                        configuration.setSetting((String) key, (String) value);
                    } catch (TemplateException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        
        return configuration;
    }
    
    /**
     * 渲染字符串模板
     * @param templateContent 模板内容
     * @param dataModel 数据模型
     * @return 渲染后的字符串
     * @throws IOException IO异常
     * @throws TemplateException 模板异常
     */
    public static String renderTemplate(String templateContent, Map<String, Object> dataModel) 
            throws IOException, TemplateException {
        return renderTemplate(templateContent, dataModel, DEFAULT_CONFIGURATION);
    }
    
    /**
     * 渲染字符串模板
     * @param templateContent 模板内容
     * @param dataModel 数据模型
     * @param configuration 模板配置
     * @return 渲染后的字符串
     * @throws IOException IO异常
     * @throws TemplateException 模板异常
     */
    public static String renderTemplate(String templateContent, Map<String, Object> dataModel, 
                                        Configuration configuration) throws IOException, TemplateException {
        if (templateContent == null || templateContent.isEmpty()) {
            return null;
        }
        
        // 创建字符串模板加载器
        StringTemplateLoader templateLoader = new StringTemplateLoader();
        templateLoader.putTemplate("template", templateContent);
        
        // 设置模板加载器
        Configuration config = (Configuration) configuration.clone();
        config.setTemplateLoader(templateLoader);
        
        // 获取模板并渲染
        Template template = config.getTemplate("template");
        StringWriter writer = new StringWriter();
        template.process(dataModel, writer);
        
        return writer.toString();
    }
    
    /**
     * 渲染文件模板
     * @param templatePath 模板文件路径
     * @param dataModel 数据模型
     * @return 渲染后的字符串
     * @throws IOException IO异常
     * @throws TemplateException 模板异常
     */
    public static String renderFileTemplate(String templatePath, Map<String, Object> dataModel) 
            throws IOException, TemplateException {
        return renderFileTemplate(templatePath, dataModel, DEFAULT_CONFIGURATION);
    }
    
    /**
     * 渲染文件模板
     * @param templatePath 模板文件路径
     * @param dataModel 数据模型
     * @param configuration 模板配置
     * @return 渲染后的字符串
     * @throws IOException IO异常
     * @throws TemplateException 模板异常
     */
    public static String renderFileTemplate(String templatePath, Map<String, Object> dataModel, 
                                            Configuration configuration) throws IOException, TemplateException {
        if (templatePath == null || templatePath.isEmpty()) {
            return null;
        }
        
        File templateFile = new File(templatePath);
        if (!templateFile.exists() || !templateFile.isFile()) {
            throw new FileNotFoundException("Template file not found: " + templatePath);
        }
        
        // 设置文件模板加载器
        Configuration config = (Configuration) configuration.clone();
        config.setDirectoryForTemplateLoading(templateFile.getParentFile());
        
        // 获取模板并渲染
        Template template = config.getTemplate(templateFile.getName());
        StringWriter writer = new StringWriter();
        template.process(dataModel, writer);
        
        return writer.toString();
    }
    
    /**
     * 渲染文件模板到输出流
     * @param templatePath 模板文件路径
     * @param dataModel 数据模型
     * @param outputStream 输出流
     * @throws IOException IO异常
     * @throws TemplateException 模板异常
     */
    public static void renderFileTemplateToStream(String templatePath, Map<String, Object> dataModel, 
                                                  OutputStream outputStream) throws IOException, TemplateException {
        renderFileTemplateToStream(templatePath, dataModel, outputStream, DEFAULT_CONFIGURATION);
    }
    
    /**
     * 渲染文件模板到输出流
     * @param templatePath 模板文件路径
     * @param dataModel 数据模型
     * @param outputStream 输出流
     * @param configuration 模板配置
     * @throws IOException IO异常
     * @throws TemplateException 模板异常
     */
    public static void renderFileTemplateToStream(String templatePath, Map<String, Object> dataModel, 
                                                  OutputStream outputStream, Configuration configuration) 
            throws IOException, TemplateException {
        if (outputStream == null) {
            return;
        }
        
        String result = renderFileTemplate(templatePath, dataModel, configuration);
        if (result != null) {
            try (Writer writer = new OutputStreamWriter(outputStream, DEFAULT_ENCODING)) {
                writer.write(result);
            }
        }
    }
    
    /**
     * 渲染字符串模板到文件
     * @param templateContent 模板内容
     * @param dataModel 数据模型
     * @param outputPath 输出文件路径
     * @throws IOException IO异常
     * @throws TemplateException 模板异常
     */
    public static void renderTemplateToFile(String templateContent, Map<String, Object> dataModel, 
                                            String outputPath) throws IOException, TemplateException {
        renderTemplateToFile(templateContent, dataModel, outputPath, DEFAULT_CONFIGURATION);
    }
    
    /**
     * 渲染字符串模板到文件
     * @param templateContent 模板内容
     * @param dataModel 数据模型
     * @param outputPath 输出文件路径
     * @param configuration 模板配置
     * @throws IOException IO异常
     * @throws TemplateException 模板异常
     */
    public static void renderTemplateToFile(String templateContent, Map<String, Object> dataModel, 
                                            String outputPath, Configuration configuration) 
            throws IOException, TemplateException {
        if (outputPath == null || outputPath.isEmpty()) {
            return;
        }
        
        String result = renderTemplate(templateContent, dataModel, configuration);
        if (result != null) {
            // 创建输出文件及其父目录
            File outputFile = new File(outputPath);
            File parentDir = outputFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            
            try (Writer writer = new FileWriter(outputFile, false)) {
                writer.write(result);
            }
        }
    }
    
    /**
     * 渲染文件模板到文件
     * @param templatePath 模板文件路径
     * @param dataModel 数据模型
     * @param outputPath 输出文件路径
     * @throws IOException IO异常
     * @throws TemplateException 模板异常
     */
    public static void renderFileTemplateToFile(String templatePath, Map<String, Object> dataModel, 
                                               String outputPath) throws IOException, TemplateException {
        renderFileTemplateToFile(templatePath, dataModel, outputPath, DEFAULT_CONFIGURATION);
    }
    
    /**
     * 渲染文件模板到文件
     * @param templatePath 模板文件路径
     * @param dataModel 数据模型
     * @param outputPath 输出文件路径
     * @param configuration 模板配置
     * @throws IOException IO异常
     * @throws TemplateException 模板异常
     */
    public static void renderFileTemplateToFile(String templatePath, Map<String, Object> dataModel, 
                                               String outputPath, Configuration configuration) 
            throws IOException, TemplateException {
        if (outputPath == null || outputPath.isEmpty()) {
            return;
        }
        
        String result = renderFileTemplate(templatePath, dataModel, configuration);
        if (result != null) {
            // 创建输出文件及其父目录
            File outputFile = new File(outputPath);
            File parentDir = outputFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            
            try (Writer writer = new FileWriter(outputFile, false)) {
                writer.write(result);
            }
        }
    }
}