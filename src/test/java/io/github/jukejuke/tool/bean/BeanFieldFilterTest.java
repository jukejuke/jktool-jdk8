package io.github.jukejuke.tool.bean;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class BeanFieldFilterTest {

    // 测试用Bean类
    static class TestBean {
        private String name;
        private int age;
        private boolean active;
        private transient String tempField;

        public TestBean(String name, int age, boolean active, String tempField) {
            this.name = name;
            this.age = age;
            this.active = active;
            this.tempField = tempField;
        }

        // getters for testing field access
        public String getName() { return name; }
        public int getAge() { return age; }
        public boolean isActive() { return active; }
        public String getTempField() { return tempField; }
    }

    @Test
    void getFilteredFields_ExcludeFields_ReturnsFilteredMap() throws IllegalAccessException {
        // Arrange
        TestBean testBean = new TestBean("Test", 25, true, "temporary");
        Set<String> excludeFields = new HashSet<>();
        excludeFields.add("tempField");
        excludeFields.add("active");

        // Act
        Map<String, Object> result = BeanFieldFilter.getFilteredFields(testBean, excludeFields);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.containsKey("name"));
        assertTrue(result.containsKey("age"));
        assertEquals("Test", result.get("name"));
        assertEquals(25, result.get("age"));
        assertFalse(result.containsKey("tempField"));
        assertFalse(result.containsKey("active"));
    }

    @Test
    void getFilteredFields_NullExcludeFields_ReturnsAllFields() throws IllegalAccessException {
        // Arrange
        TestBean testBean = new TestBean("Test", 25, true, "temporary");

        // Act
        Map<String, Object> result = BeanFieldFilter.getFilteredFields(testBean, null);

        // Assert
        assertEquals(4, result.size());
        assertTrue(result.containsKey("name"));
        assertTrue(result.containsKey("age"));
        assertTrue(result.containsKey("active"));
        assertTrue(result.containsKey("tempField"));
    }

    @Test
    void getFilteredFields_ExcludeNonExistentFields_ReturnsAllFields() throws IllegalAccessException {
        // Arrange
        TestBean testBean = new TestBean("Test", 25, true, "temporary");
        Set<String> excludeFields = new HashSet<>();
        excludeFields.add("nonexistent");
        excludeFields.add("field");

        // Act
        Map<String, Object> result = BeanFieldFilter.getFilteredFields(testBean, excludeFields);

        // Assert
        assertEquals(4, result.size());
    }

    @Test
    void getFilteredFields_EmptyExcludeFields_ReturnsAllFields() throws IllegalAccessException {
        // Arrange
        TestBean testBean = new TestBean("Test", 25, true, "temporary");
        Set<String> excludeFields = new HashSet<>();

        // Act
        Map<String, Object> result = BeanFieldFilter.getFilteredFields(testBean, excludeFields);

        // Assert
        assertEquals(4, result.size());
    }

    @Test
    void getFilteredFields_NullBean_ThrowsException() {
        // Arrange
        Set<String> excludeFields = new HashSet<>();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            BeanFieldFilter.getFilteredFields(null, excludeFields);
        });
    }
}