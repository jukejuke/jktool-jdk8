package io.github.jukejuke.tool.response;

import com.alibaba.fastjson2.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * API响应测试类
 * 验证响应类对List类型的支持
 */
public class ApiResponseTest {
    
    public static void main(String[] args) {
        // 测试基本类型List
        testPrimitiveList();
        
        // 测试对象类型List
        testObjectList();
        
        // 测试使用工具类构建List响应
        testListWithUtils();
        
        System.out.println("\n测试完成，ApiResponse和ResponseUtils支持List类型！");
    }
    
    /**
     * 测试基本类型List
     */
    private static void testPrimitiveList() {
        System.out.println("=== 测试基本类型List ===");
        
        // Integer List
        List<Integer> intList = new ArrayList<>();
        intList.add(1);
        intList.add(2);
        intList.add(3);
        intList.add(4);
        intList.add(5);
        
        ApiResponse<List<Integer>> intListResponse = new ApiResponse<>(ResponseStatus.SUCCESS, intList);
        System.out.println("Integer List响应JSON: " + JSON.toJSONString(intListResponse));
        System.out.println("Integer List响应状态码: " + intListResponse.getCode());
        System.out.println("Integer List响应消息: " + intListResponse.getMessage());
        System.out.println("Integer List响应数据大小: " + intListResponse.getData().size());
        System.out.println("Integer List响应数据[0]: " + intListResponse.getData().get(0));
        
        // String List
        List<String> stringList = new ArrayList<>();
        stringList.add("apple");
        stringList.add("banana");
        stringList.add("orange");
        
        ApiResponse<List<String>> stringListResponse = new ApiResponse<>(ResponseStatus.SUCCESS, stringList);
        System.out.println("String List响应JSON: " + JSON.toJSONString(stringListResponse));
        System.out.println("String List响应状态码: " + stringListResponse.getCode());
        System.out.println("String List响应消息: " + stringListResponse.getMessage());
        System.out.println("String List响应数据大小: " + stringListResponse.getData().size());
        System.out.println("String List响应数据[0]: " + stringListResponse.getData().get(0));
    }
    
    /**
     * 测试对象类型List
     */
    private static void testObjectList() {
        System.out.println("\n=== 测试对象类型List ===");
        
        // 创建测试对象List
        List<User> userList = new ArrayList<>();
        userList.add(new User(1, "Alice"));
        userList.add(new User(2, "Bob"));
        userList.add(new User(3, "Charlie"));
        
        ApiResponse<List<User>> userListResponse = new ApiResponse<>(ResponseStatus.SUCCESS, userList);
        System.out.println("User List响应JSON: " + JSON.toJSONString(userListResponse));
        System.out.println("User List响应状态码: " + userListResponse.getCode());
        System.out.println("User List响应消息: " + userListResponse.getMessage());
        System.out.println("User List响应数据大小: " + userListResponse.getData().size());
        System.out.println("User List响应数据[0]: " + userListResponse.getData().get(0));
    }
    
    /**
     * 测试使用工具类构建List响应
     */
    private static void testListWithUtils() {
        System.out.println("\n=== 测试使用工具类构建List响应 ===");
        
        // 使用工具类构建List响应
        List<Double> doubleList = new ArrayList<>();
        doubleList.add(1.1);
        doubleList.add(2.2);
        doubleList.add(3.3);
        
        ApiResponse<List<Double>> response = ResponseUtils.success(doubleList);
        System.out.println("工具类构建的List响应状态码: " + response.getCode());
        System.out.println("工具类构建的List响应消息: " + response.getMessage());
        System.out.println("工具类构建的List响应数据大小: " + response.getData().size());
        System.out.println("工具类构建的List响应数据[0]: " + response.getData().get(0));
    }
    
    /**
     * 测试用户类
     */
    static class User {
        private int id;
        private String name;
        
        public User(int id, String name) {
            this.id = id;
            this.name = name;
        }
        
        public int getId() {
            return id;
        }
        
        public void setId(int id) {
            this.id = id;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return "User{id=" + id + ", name='" + name + "'}";
        }
    }
}