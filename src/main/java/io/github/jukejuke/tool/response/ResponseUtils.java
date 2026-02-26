package io.github.jukejuke.tool.response;

/**
 * API响应工具类
 * 提供静态方法快速构建各种类型的响应
 */
public class ResponseUtils {
    
    /**
     * 构建成功响应
     * @param <T> 数据类型
     * @param data 响应数据
     * @return 成功响应对象
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ResponseStatus.SUCCESS, data);
    }
    
    /**
     * 构建成功响应（无数据）
     * @return 成功响应对象
     */
    public static ApiResponse<?> success() {
        return success(null);
    }
    
    /**
     * 构建失败响应
     * @param status 响应状态
     * @param data 响应数据
     * @param <T> 数据类型
     * @return 失败响应对象
     */
    public static <T> ApiResponse<T> fail(ResponseStatus status, T data) {
        return new ApiResponse<>(status, data);
    }
    
    /**
     * 构建失败响应（无数据）
     * @param status 响应状态
     * @return 失败响应对象
     */
    public static ApiResponse<?> fail(ResponseStatus status) {
        return fail(status, null);
    }
    
    /**
     * 构建自定义失败响应
     * @param code 状态码
     * @param message 消息
     * @param data 响应数据
     * @param <T> 数据类型
     * @return 失败响应对象
     */
    public static <T> ApiResponse<T> fail(int code, String message, T data) {
        return new ApiResponse<>(code, message, data);
    }
    
    /**
     * 构建自定义失败响应（无数据）
     * @param code 状态码
     * @param message 消息
     * @return 失败响应对象
     */
    public static ApiResponse<?> fail(int code, String message) {
        return fail(code, message, null);
    }
    
    /**
     * 构建业务错误响应
     * @param message 错误消息
     * @param data 响应数据
     * @param <T> 数据类型
     * @return 业务错误响应对象
     */
    public static <T> ApiResponse<T> businessError(String message, T data) {
        return new ApiResponse<>(ResponseStatus.BUSINESS_ERROR.getCode(), message, data);
    }
    
    /**
     * 构建业务错误响应（无数据）
     * @param message 错误消息
     * @return 业务错误响应对象
     */
    public static ApiResponse<?> businessError(String message) {
        return businessError(message, null);
    }
    
    /**
     * 构建参数错误响应
     * @param message 错误消息
     * @param data 响应数据
     * @param <T> 数据类型
     * @return 参数错误响应对象
     */
    public static <T> ApiResponse<T> badRequest(String message, T data) {
        return new ApiResponse<>(ResponseStatus.BAD_REQUEST.getCode(), message, data);
    }
    
    /**
     * 构建参数错误响应（无数据）
     * @param message 错误消息
     * @return 参数错误响应对象
     */
    public static ApiResponse<?> badRequest(String message) {
        return badRequest(message, null);
    }
    
    /**
     * 构建服务器错误响应
     * @param message 错误消息
     * @param data 响应数据
     * @param <T> 数据类型
     * @return 服务器错误响应对象
     */
    public static <T> ApiResponse<T> serverError(String message, T data) {
        return new ApiResponse<>(ResponseStatus.INTERNAL_SERVER_ERROR.getCode(), message, data);
    }
    
    /**
     * 构建服务器错误响应（无数据）
     * @param message 错误消息
     * @return 服务器错误响应对象
     */
    public static ApiResponse<?> serverError(String message) {
        return serverError(message, null);
    }
}