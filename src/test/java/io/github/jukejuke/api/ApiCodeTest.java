package io.github.jukejuke.api;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * ApiCode测试类
 */
public class ApiCodeTest {

    /**
     * 测试SUCCESS枚举值
     */
    @Test
    public void testSuccess() {
        assertEquals(200, ApiCode.SUCCESS.getCode());
        assertEquals("操作成功", ApiCode.SUCCESS.getMessage());
    }

    /**
     * 测试BAD_REQUEST枚举值
     */
    @Test
    public void testBadRequest() {
        assertEquals(400, ApiCode.BAD_REQUEST.getCode());
        assertEquals("请求参数错误", ApiCode.BAD_REQUEST.getMessage());
    }

    /**
     * 测试UNAUTHORIZED枚举值
     */
    @Test
    public void testUnauthorized() {
        assertEquals(401, ApiCode.UNAUTHORIZED.getCode());
        assertEquals("未授权，请登录", ApiCode.UNAUTHORIZED.getMessage());
    }

    /**
     * 测试FORBIDDEN枚举值
     */
    @Test
    public void testForbidden() {
        assertEquals(403, ApiCode.FORBIDDEN.getCode());
        assertEquals("拒绝访问", ApiCode.FORBIDDEN.getMessage());
    }

    /**
     * 测试NOT_FOUND枚举值
     */
    @Test
    public void testNotFound() {
        assertEquals(404, ApiCode.NOT_FOUND.getCode());
        assertEquals("请求资源不存在", ApiCode.NOT_FOUND.getMessage());
    }

    /**
     * 测试INTERNAL_SERVER_ERROR枚举值
     */
    @Test
    public void testInternalServerError() {
        assertEquals(500, ApiCode.INTERNAL_SERVER_ERROR.getCode());
        assertEquals("服务器内部错误", ApiCode.INTERNAL_SERVER_ERROR.getMessage());
    }

    /**
     * 测试数据库相关枚举值
     */
    @Test
    public void testDatabaseError() {
        assertEquals(5001, ApiCode.DATABASE_ERROR.getCode());
        assertEquals("数据库操作错误", ApiCode.DATABASE_ERROR.getMessage());
    }

    /**
     * 测试业务逻辑错误枚举值
     */
    @Test
    public void testBusinessError() {
        assertEquals(5002, ApiCode.BUSINESS_ERROR.getCode());
        assertEquals("业务逻辑错误", ApiCode.BUSINESS_ERROR.getMessage());
    }

    /**
     * 测试网络请求错误枚举值
     */
    @Test
    public void testNetworkError() {
        assertEquals(5003, ApiCode.NETWORK_ERROR.getCode());
        assertEquals("网络请求错误", ApiCode.NETWORK_ERROR.getMessage());
    }

    /**
     * 测试数据格式错误枚举值
     */
    @Test
    public void testDataFormatError() {
        assertEquals(5004, ApiCode.DATA_FORMAT_ERROR.getCode());
        assertEquals("数据格式错误", ApiCode.DATA_FORMAT_ERROR.getMessage());
    }

    /**
     * 测试配置错误枚举值
     */
    @Test
    public void testConfigError() {
        assertEquals(5005, ApiCode.CONFIG_ERROR.getCode());
        assertEquals("配置错误", ApiCode.CONFIG_ERROR.getMessage());
    }

    /**
     * 测试权限不足枚举值
     */
    @Test
    public void testPermissionDenied() {
        assertEquals(5006, ApiCode.PERMISSION_DENIED.getCode());
        assertEquals("权限不足", ApiCode.PERMISSION_DENIED.getMessage());
    }

    /**
     * 测试所有枚举值的数量
     */
    @Test
    public void testEnumCount() {
        ApiCode[] values = ApiCode.values();
        // 确保枚举值数量正确
        assertEquals("ApiCode枚举值数量不正确", 19, values.length);
    }

    /**
     * 测试枚举值的唯一性
     */
    @Test
    public void testEnumCodeUniqueness() {
        ApiCode[] values = ApiCode.values();
        for (int i = 0; i < values.length; i++) {
            for (int j = i + 1; j < values.length; j++) {
                assertNotEquals("ApiCode枚举值存在重复的code: " + values[i].getCode(), values[i].getCode(), values[j].getCode());
            }
        }
    }
}