package io.github.jukejuke.tool.response;

import lombok.Data;

import java.io.Serializable;

/**
 * API响应基础类
 * 统一API响应格式
 * @param <T> 响应数据类型
 */
@Data
public class ApiResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 响应状态码
     */
    private int code;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private T data;
    
    /**
     * 响应时间戳
     */
    private long timestamp;
    
    /**
     * 无参构造方法
     */
    public ApiResponse() {
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * 全参构造方法
     * @param code 状态码
     * @param message 消息
     * @param data 数据
     */
    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * 构造方法（无数据）
     * @param code 状态码
     * @param message 消息
     */
    public ApiResponse(int code, String message) {
        this(code, message, null);
    }
    
    /**
     * 构造方法（使用响应状态枚举）
     * @param status 响应状态枚举
     * @param data 数据
     */
    public ApiResponse(ResponseStatus status, T data) {
        this(status.getCode(), status.getMessage(), data);
    }
    
    /**
     * 构造方法（使用响应状态枚举，无数据）
     * @param status 响应状态枚举
     */
    public ApiResponse(ResponseStatus status) {
        this(status, null);
    }
}