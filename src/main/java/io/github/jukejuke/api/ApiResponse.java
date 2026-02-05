package io.github.jukejuke.api;

import java.io.Serializable;

/**
 * API响应结果封装类
 * 统一封装API返回的数据格式，包含状态码、消息和数据
 * @param <T> 响应数据类型
 */
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
     * 构造方法
     * @param code 状态码
     * @param message 消息
     * @param data 数据
     */
    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    /**
     * 成功响应构造方法
     * @param data 响应数据
     */
    public ApiResponse(T data) {
        this(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getMessage(), data);
    }
    
    /**
     * 成功响应静态方法
     * @param <T> 数据类型
     * @param data 响应数据
     * @return ApiResponse实例
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getMessage(), data);
    }
    
    /**
     * 成功响应静态方法（无数据）
     * @param <T> 数据类型
     * @return ApiResponse实例
     */
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getMessage(), null);
    }
    
    /**
     * 失败响应静态方法
     * @param <T> 数据类型
     * @param code 错误码
     * @param message 错误消息
     * @return ApiResponse实例
     */
    public static <T> ApiResponse<T> fail(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }
    
    /**
     * 失败响应静态方法（使用ApiCode）
     * @param <T> 数据类型
     * @param apiCode 错误码枚举
     * @return ApiResponse实例
     */
    public static <T> ApiResponse<T> fail(ApiCode apiCode) {
        return new ApiResponse<>(apiCode.getCode(), apiCode.getMessage(), null);
    }
    
    /**
     * 失败响应静态方法（使用ApiCode和自定义消息）
     * @param <T> 数据类型
     * @param apiCode 错误码枚举
     * @param message 自定义错误消息
     * @return ApiResponse实例
     */
    public static <T> ApiResponse<T> fail(ApiCode apiCode, String message) {
        return new ApiResponse<>(apiCode.getCode(), message, null);
    }
    
    // Getters and Setters
    public int getCode() {
        return code;
    }
    
    public void setCode(int code) {
        this.code = code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    @Override
    public String toString() {
        return "ApiResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}