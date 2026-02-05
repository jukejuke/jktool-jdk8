package io.github.jukejuke.api;

/**
 * API响应状态码枚举
 * 定义系统中所有API响应的状态码和对应的消息
 */
public enum ApiCode {
    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),
    
    /**
     * 请求参数错误
     */
    BAD_REQUEST(400, "请求参数错误"),
    
    /**
     * 未授权
     */
    UNAUTHORIZED(401, "未授权，请登录"),
    
    /**
     * 拒绝访问
     */
    FORBIDDEN(403, "拒绝访问"),
    
    /**
     * 请求资源不存在
     */
    NOT_FOUND(404, "请求资源不存在"),
    
    /**
     * 方法不允许
     */
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),
    
    /**
     * 请求冲突
     */
    CONFLICT(409, "请求冲突"),
    
    /**
     * 请求实体过大
     */
    PAYLOAD_TOO_LARGE(413, "请求实体过大"),
    
    /**
     * 请求URI过长
     */
    URI_TOO_LONG(414, "请求URI过长"),
    
    /**
     * 不支持的媒体类型
     */
    UNSUPPORTED_MEDIA_TYPE(415, "不支持的媒体类型"),
    
    /**
     * 服务器内部错误
     */
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    
    /**
     * 服务不可用
     */
    SERVICE_UNAVAILABLE(503, "服务不可用"),
    
    /**
     * 数据库操作错误
     */
    DATABASE_ERROR(5001, "数据库操作错误"),
    
    /**
     * 业务逻辑错误
     */
    BUSINESS_ERROR(5002, "业务逻辑错误"),
    
    /**
     * 网络请求错误
     */
    NETWORK_ERROR(5003, "网络请求错误"),
    
    /**
     * 数据格式错误
     */
    DATA_FORMAT_ERROR(5004, "数据格式错误"),
    
    /**
     * 配置错误
     */
    CONFIG_ERROR(5005, "配置错误"),
    
    /**
     * 权限不足
     */
    PERMISSION_DENIED(5006, "权限不足"),
    
    /**
     * 参数验证失败
     */
    VALIDATION_ERROR(5007, "参数验证失败");
    
    /**
     * 状态码
     */
    private final int code;
    
    /**
     * 状态消息
     */
    private final String message;
    
    /**
     * 构造方法
     * @param code 状态码
     * @param message 状态消息
     */
    ApiCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    
    /**
     * 获取状态码
     * @return 状态码
     */
    public int getCode() {
        return code;
    }
    
    /**
     * 获取状态消息
     * @return 状态消息
     */
    public String getMessage() {
        return message;
    }
}