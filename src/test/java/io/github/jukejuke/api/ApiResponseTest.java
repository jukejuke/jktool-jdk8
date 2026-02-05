package io.github.jukejuke.api;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * ApiResponse测试类
 */
public class ApiResponseTest {

    /**
     * 测试成功响应（带数据）
     */
    @Test
    public void testSuccessWithData() {
        String testData = "test data";
        ApiResponse<String> response = ApiResponse.success(testData);
        
        assertEquals(ApiCode.SUCCESS.getCode(), response.getCode());
        assertEquals(ApiCode.SUCCESS.getMessage(), response.getMessage());
        assertEquals(testData, response.getData());
    }

    /**
     * 测试成功响应（无数据）
     */
    @Test
    public void testSuccessWithoutData() {
        ApiResponse<String> response = ApiResponse.success();
        
        assertEquals(ApiCode.SUCCESS.getCode(), response.getCode());
        assertEquals(ApiCode.SUCCESS.getMessage(), response.getMessage());
        assertNull(response.getData());
    }

    /**
     * 测试失败响应（使用错误码和消息）
     */
    @Test
    public void testFailWithCodeAndMessage() {
        int errorCode = 500;
        String errorMessage = "Internal Server Error";
        ApiResponse<String> response = ApiResponse.fail(errorCode, errorMessage);
        
        assertEquals(errorCode, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
    }

    /**
     * 测试失败响应（使用ApiCode枚举）
     */
    @Test
    public void testFailWithApiCode() {
        ApiResponse<String> response = ApiResponse.fail(ApiCode.BAD_REQUEST);
        
        assertEquals(ApiCode.BAD_REQUEST.getCode(), response.getCode());
        assertEquals(ApiCode.BAD_REQUEST.getMessage(), response.getMessage());
        assertNull(response.getData());
    }

    /**
     * 测试失败响应（使用ApiCode枚举和自定义消息）
     */
    @Test
    public void testFailWithApiCodeAndCustomMessage() {
        String customMessage = "Custom Bad Request Message";
        ApiResponse<String> response = ApiResponse.fail(ApiCode.BAD_REQUEST, customMessage);
        
        assertEquals(ApiCode.BAD_REQUEST.getCode(), response.getCode());
        assertEquals(customMessage, response.getMessage());
        assertNull(response.getData());
    }

    /**
     * 测试构造方法（带数据）
     */
    @Test
    public void testConstructorWithData() {
        String testData = "test data";
        ApiResponse<String> response = new ApiResponse<>(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getMessage(), testData);
        
        assertEquals(ApiCode.SUCCESS.getCode(), response.getCode());
        assertEquals(ApiCode.SUCCESS.getMessage(), response.getMessage());
        assertEquals(testData, response.getData());
    }

    /**
     * 测试构造方法（无数据）
     */
    @Test
    public void testConstructorWithoutData() {
        ApiResponse<String> response = new ApiResponse<>(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getMessage(), null);
        
        assertEquals(ApiCode.SUCCESS.getCode(), response.getCode());
        assertEquals(ApiCode.SUCCESS.getMessage(), response.getMessage());
        assertNull(response.getData());
    }

    /**
     * 测试构造方法（仅数据）
     */
    @Test
    public void testConstructorWithOnlyData() {
        String testData = "test data";
        ApiResponse<String> response = new ApiResponse<>(testData);
        
        assertEquals(ApiCode.SUCCESS.getCode(), response.getCode());
        assertEquals(ApiCode.SUCCESS.getMessage(), response.getMessage());
        assertEquals(testData, response.getData());
    }

    /**
     * 测试toString方法
     */
    @Test
    public void testToString() {
        String testData = "test data";
        ApiResponse<String> response = ApiResponse.success(testData);
        String toString = response.toString();
        
        assertNotNull(toString);
        assertTrue(toString.contains(String.valueOf(ApiCode.SUCCESS.getCode())));
        assertTrue(toString.contains(ApiCode.SUCCESS.getMessage()));
        assertTrue(toString.contains(testData));
    }

    /**
     * 测试getter和setter方法
     */
    @Test
    public void testGettersAndSetters() {
        ApiResponse<String> response = new ApiResponse<>("initial data");
        
        int testCode = 200;
        String testMessage = "test message";
        String testData = "test data";
        
        response.setCode(testCode);
        response.setMessage(testMessage);
        response.setData(testData);
        
        assertEquals(testCode, response.getCode());
        assertEquals(testMessage, response.getMessage());
        assertEquals(testData, response.getData());
    }

    /**
     * 测试泛型数据类型
     */
    @Test
    public void testGenericDataType() {
        // 测试字符串类型
        ApiResponse<String> stringResponse = ApiResponse.success("string data");
        assertEquals("string data", stringResponse.getData());
        
        // 测试整数类型
        ApiResponse<Integer> intResponse = ApiResponse.success(123);
        assertEquals((Integer)123, intResponse.getData());
        
        // 测试布尔类型
        ApiResponse<Boolean> boolResponse = ApiResponse.success(true);
        assertEquals(Boolean.TRUE, boolResponse.getData());
    }
}