package io.github.jukejuke.tool.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel 列注解
 * 用于配置实体类字段在 Excel 中的展示属性
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {
    
    /**
     * Excel 列名
     */
    String name();
    
    /**
     * 列展示顺序（数值越小越靠前，默认按字段定义顺序）
     */
    int order() default 0;
    
    /**
     * 数据格式（如日期格式 "yyyy-MM-dd"、数字格式 "#,##0.00"）
     */
    String format() default "";
    
    /**
     * 列宽（单位：字符，默认自适应）
     */
    int width() default 0;
    
    /**
     * 是否必填（导出时为空则填充默认值，默认值可配置）
     */
    boolean required() default false;
    
    /**
     * 字段为空时的默认填充值（默认空字符串）
     */
    String defaultValue() default "";
    
    /**
     * 水平对齐方式
     */
    ExcelAlignment alignment() default ExcelAlignment.LEFT;
}
