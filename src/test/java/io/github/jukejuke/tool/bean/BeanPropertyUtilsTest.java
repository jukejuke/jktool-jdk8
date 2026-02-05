package io.github.jukejuke.tool.bean;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * BeanPropertyUtils工具类的单元测试
 */
class BeanPropertyUtilsTest {

    /**
     * 测试用例Bean类
     */
    static class TestBean {
        private String name;
        private int age;
        private boolean active;
        private Object data;

        public TestBean(String name, int age, boolean active, Object data) {
            this.name = name;
            this.age = age;
            this.active = active;
            this.data = data;
        }

        // Getters for testing verification
        public String getName() { return name; }
        public int getAge() { return age; }
        public boolean isActive() { return active; }
        public Object getData() { return data; }
    }

    /**
     * 测试removeProperties方法正常移除属性功能
     */
    @Test
    void removeProperties_ShouldSetSpecifiedPropertiesToNull() throws IllegalAccessException {
        // 准备测试数据
        TestBean testBean = new TestBean("Test", 25, true, new Object());
        String[] propertiesToRemove = {"name", "data"};

        // 执行测试方法
        BeanPropertyUtils.removeProperties(testBean, propertiesToRemove);

        // 验证结果
        assertNull(testBean.getName(), "name属性应被设为null");
        assertNull(testBean.getData(), "data属性应被设为null");
        assertEquals(25, testBean.getAge(), "age属性不应被修改");
        assertTrue(testBean.isActive(), "active属性不应被修改");
    }

    /**
     * 测试removeProperties方法处理空属性数组的情况
     */
    @Test
    void removeProperties_WithEmptyPropertyArray_ShouldDoNothing() throws IllegalAccessException {
        TestBean testBean = new TestBean("Test", 25, true, new Object());
        String[] propertiesToRemove = {};

        BeanPropertyUtils.removeProperties(testBean, propertiesToRemove);

        // 验证所有属性都未被修改
        assertEquals("Test", testBean.getName());
        assertEquals(25, testBean.getAge());
        assertTrue(testBean.isActive());
        assertNotNull(testBean.getData());
    }

    /**
     * 测试removeProperties方法处理null属性数组的情况
     */
    @Test
    void removeProperties_WithNullPropertyArray_ShouldDoNothing() throws IllegalAccessException {
        TestBean testBean = new TestBean("Test", 25, true, new Object());

        BeanPropertyUtils.removeProperties(testBean, null);

        // 验证所有属性都未被修改
        assertEquals("Test", testBean.getName());
        assertEquals(25, testBean.getAge());
        assertTrue(testBean.isActive());
        assertNotNull(testBean.getData());
    }

    /**
     * 测试removeProperties方法处理不存在的属性
     */
    @Test
    void removeProperties_WithNonExistentProperties_ShouldIgnoreThem() throws IllegalAccessException {
        TestBean testBean = new TestBean("Test", 25, true, new Object());
        String[] propertiesToRemove = {"nonexistent", "name"};

        BeanPropertyUtils.removeProperties(testBean, propertiesToRemove);

        // 验证存在的属性被修改，不存在的属性被忽略
        assertNull(testBean.getName());
        assertEquals(25, testBean.getAge());
        assertTrue(testBean.isActive());
        assertNotNull(testBean.getData());
    }

    /**
     * 测试removePropertiesSafe方法在发生异常时的处理
     */
    @Test
    void removePropertiesSafe_ShouldHandleExceptionAndReturnOriginalBean() {
        // 使用一个特殊的Bean来触发异常（例如没有无参构造函数的类）
        final Object testBean = new Object() {};
        String[] propertiesToRemove = {"toString"}; // 尝试修改Object类的toString方法（实际是方法不是字段）

        Object result = BeanPropertyUtils.removePropertiesSafe(testBean, propertiesToRemove);

        // 验证返回的是原对象
        assertSame(testBean, result);
    }
}