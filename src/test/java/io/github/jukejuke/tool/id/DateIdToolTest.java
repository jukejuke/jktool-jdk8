package io.github.jukejuke.tool.id;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DateIdTool测试类
 */
class DateIdToolTest {

    @BeforeEach
    void setUp() {
        // 重置计数器，确保每次测试从0开始
        DateIdTool.resetCounter();
    }

    @Test
    void testGenerateId() {
        String id1 = DateIdTool.generateId();
        String id2 = DateIdTool.generateId();
        String id3 = DateIdTool.generateId();
        
        assertNotNull(id1);
        assertNotNull(id2);
        assertNotNull(id3);
        
        // 检查ID长度：8位日期 + 8位数字 = 16位
        assertEquals(16, id1.length());
        assertEquals(16, id2.length());
        assertEquals(16, id3.length());
        
        // 检查日期部分是否相同（同一测试运行中）
        String datePart1 = id1.substring(0, 8);
        String datePart2 = id2.substring(0, 8);
        String datePart3 = id3.substring(0, 8);
        assertEquals(datePart1, datePart2);
        assertEquals(datePart1, datePart3);
        
        // 检查数字部分是否递增
        long num1 = Long.parseLong(id1.substring(8));
        long num2 = Long.parseLong(id2.substring(8));
        long num3 = Long.parseLong(id3.substring(8));
        assertEquals(1, num1);
        assertEquals(2, num2);
        assertEquals(3, num3);
        
        System.out.println("日期趋势ID 1: " + id1);
        System.out.println("日期趋势ID 2: " + id2);
        System.out.println("日期趋势ID 3: " + id3);
    }

    @Test
    void testGenerateIdWithPrefix() {
        String prefix = "USER_";
        String id1 = DateIdTool.generateIdWithPrefix(prefix);
        String id2 = DateIdTool.generateIdWithPrefix(prefix);
        
        assertNotNull(id1);
        assertNotNull(id2);
        
        // 检查是否带有前缀
        assertTrue(id1.startsWith(prefix));
        assertTrue(id2.startsWith(prefix));
        
        // 检查ID长度：前缀长度 + 8位日期 + 8位数字
        assertEquals(prefix.length() + 16, id1.length());
        assertEquals(prefix.length() + 16, id2.length());
        
        // 检查日期部分是否相同（同一测试运行中）
        String datePart1 = id1.substring(prefix.length(), prefix.length() + 8);
        String datePart2 = id2.substring(prefix.length(), prefix.length() + 8);
        assertEquals(datePart1, datePart2);
        
        // 检查数字部分是否递增
        long num1 = Long.parseLong(id1.substring(prefix.length() + 8));
        long num2 = Long.parseLong(id2.substring(prefix.length() + 8));
        assertEquals(1, num1);
        assertEquals(2, num2);
        
        System.out.println("带前缀日期趋势ID 1: " + id1);
        System.out.println("带前缀日期趋势ID 2: " + id2);
    }

    @Test
    void testGenerateIdWithBusinessType() {
        String businessType = "ORDER";
        String id1 = DateIdTool.generateIdWithBusinessType(businessType);
        String id2 = DateIdTool.generateIdWithBusinessType(businessType);
        
        assertNotNull(id1);
        assertNotNull(id2);
        
        // 检查是否带有业务类型前缀
        assertTrue(id1.startsWith(businessType));
        assertTrue(id2.startsWith(businessType));
        
        System.out.println("带业务类型日期趋势ID 1: " + id1);
        System.out.println("带业务类型日期趋势ID 2: " + id2);
    }

    @Test
    void testDifferentPrefixesHaveSeparateCounters() {
        // 生成不同前缀的ID
        String prefix1 = "USER_";
        String prefix2 = "ORDER_";
        
        String id1 = DateIdTool.generateIdWithPrefix(prefix1);
        String id2 = DateIdTool.generateIdWithPrefix(prefix2);
        String id3 = DateIdTool.generateIdWithPrefix(prefix1);
        String id4 = DateIdTool.generateIdWithPrefix(prefix2);
        
        assertNotNull(id1);
        assertNotNull(id2);
        assertNotNull(id3);
        assertNotNull(id4);
        
        // 检查前缀
        assertTrue(id1.startsWith(prefix1));
        assertTrue(id2.startsWith(prefix2));
        assertTrue(id3.startsWith(prefix1));
        assertTrue(id4.startsWith(prefix2));
        
        // 检查不同前缀的计数器是否独立
        // 提取数字部分
        String num1 = id1.substring(prefix1.length() + 8); // 前缀长度 + 8位日期
        String num2 = id2.substring(prefix2.length() + 8);
        String num3 = id3.substring(prefix1.length() + 8);
        String num4 = id4.substring(prefix2.length() + 8);
        
        // 验证每个前缀的计数器都是从1开始递增
        assertEquals("00000001", num1);
        assertEquals("00000001", num2);
        assertEquals("00000002", num3);
        assertEquals("00000002", num4);
        
        System.out.println("前缀1 ID 1: " + id1);
        System.out.println("前缀2 ID 1: " + id2);
        System.out.println("前缀1 ID 2: " + id3);
        System.out.println("前缀2 ID 2: " + id4);
    }

    @Test
    void testGetCurrentDate() {
        String currentDate = DateIdTool.getCurrentDate();
        assertNotNull(currentDate);
        assertEquals(8, currentDate.length());
        // 检查是否为数字
        assertTrue(currentDate.matches("\\d{8}"));
        System.out.println("当前日期: " + currentDate);
    }

    @Test
    void testGenerateIdWithEmptyPrefix() {
        assertThrows(IllegalArgumentException.class, () -> {
            DateIdTool.generateIdWithPrefix("");
        });
    }

    @Test
    void testGenerateIdWithNullPrefix() {
        assertThrows(IllegalArgumentException.class, () -> {
            DateIdTool.generateIdWithPrefix(null);
        });
    }
}
