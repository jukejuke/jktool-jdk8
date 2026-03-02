package io.github.jukejuke.tool.excel;

import org.apache.poi.ss.usermodel.HorizontalAlignment;

/**
 * Excel 对齐方式枚举
 * 提供更方便的对齐方式配置
 */
public enum ExcelAlignment {
    /**
     * 左对齐
     */
    LEFT(HorizontalAlignment.LEFT),
    
    /**
     * 居中对齐
     */
    CENTER(HorizontalAlignment.CENTER),
    
    /**
     * 右对齐
     */
    RIGHT(HorizontalAlignment.RIGHT),
    
    /**
     * 填充
     */
    FILL(HorizontalAlignment.FILL),
    
    /**
     * 两端对齐
     */
    JUSTIFY(HorizontalAlignment.JUSTIFY),
    
    /**
     * 选择居中
     */
    CENTER_SELECTION(HorizontalAlignment.CENTER_SELECTION),
    
    /**
     * 分布式
     */
    DISTRIBUTED(HorizontalAlignment.DISTRIBUTED);
    
    private final HorizontalAlignment poiAlignment;
    
    ExcelAlignment(HorizontalAlignment poiAlignment) {
        this.poiAlignment = poiAlignment;
    }
    
    /**
     * 获取对应的 POI 对齐方式
     * @return POI 对齐方式
     */
    public HorizontalAlignment getPoiAlignment() {
        return poiAlignment;
    }
}