package io.github.jukejuke.tool.response;

import java.util.ArrayList;
import java.util.List;

/**
 * API响应List支持示例
 * 展示如何使用ApiResponse和ResponseUtils处理List类型
 */
public class ApiResponseListExample {
    
    public static void main(String[] args) {
        // 示例1：返回基本类型List
        List<Integer> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(4);
        numbers.add(5);
        
        ApiResponse<List<Integer>> numberResponse = ResponseUtils.success(numbers);
        System.out.println("基本类型List响应: " + numberResponse.getCode() + " - " + numberResponse.getMessage());
        System.out.println("List大小: " + numberResponse.getData().size());
        System.out.println("第一个元素: " + numberResponse.getData().get(0));
        
        // 示例2：返回字符串List
        List<String> fruits = new ArrayList<>();
        fruits.add("apple");
        fruits.add("banana");
        fruits.add("orange");
        
        ApiResponse<List<String>> fruitResponse = ResponseUtils.success(fruits);
        System.out.println("字符串List响应: " + fruitResponse.getCode() + " - " + fruitResponse.getMessage());
        System.out.println("List大小: " + fruitResponse.getData().size());
        System.out.println("第一个元素: " + fruitResponse.getData().get(0));
        
        // 示例3：返回对象List
        List<User> users = new ArrayList<>();
        users.add(new User(1, "Alice"));
        users.add(new User(2, "Bob"));
        users.add(new User(3, "Charlie"));
        
        ApiResponse<List<User>> userResponse = ResponseUtils.success(users);
        System.out.println("对象List响应: " + userResponse.getCode() + " - " + userResponse.getMessage());
        System.out.println("List大小: " + userResponse.getData().size());
        System.out.println("第一个用户: " + userResponse.getData().get(0));
        
        // 示例4：返回空List
        List<String> emptyList = new ArrayList<>();
        ApiResponse<List<String>> emptyResponse = ResponseUtils.success(emptyList);
        System.out.println("空List响应: " + emptyResponse.getCode() + " - " + emptyResponse.getMessage());
        System.out.println("List大小: " + emptyResponse.getData().size());
        
        System.out.println("\n所有示例执行成功，ApiResponse和ResponseUtils完全支持List类型！");
    }
    
    /**
     * 示例用户类
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