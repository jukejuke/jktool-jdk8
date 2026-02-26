package io.github.jukejuke.tool.response;

/**
 * 响应状态枚举
 * 定义API响应的状态码和消息
 */
public enum ResponseStatus {
    // 成功状态
    SUCCESS(200, "操作成功"),
    
    // 客户端错误
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),
    
    // 服务器错误
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),
    
    // 业务错误
    BUSINESS_ERROR(600, "业务逻辑错误"),
    VALIDATION_ERROR(601, "数据校验错误"),
    DATABASE_ERROR(602, "数据库操作错误");
    
    private final int code;
    private final String message;
    
    ResponseStatus(int code, String message) {
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