package io.github.jukejuke.tool.annotation;

import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * AnnotationUtils测试类
 */
public class AnnotationUtilsTest {

    // 测试用注解定义
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestClassAnnotation {
        String value() default "";
        int order() default 0;
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestMethodAnnotation {
        String name() default "";
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestFieldAnnotation {
        boolean required() default false;
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestInterfaceAnnotation {
        String description() default "";
    }

    // 测试用接口
    @TestInterfaceAnnotation(description = "Test Interface")
    public interface TestInterface {
    }

    // 测试用父类
    @TestClassAnnotation(value = "Super Class", order = 1)
    public static class TestSuperClass {
        @TestFieldAnnotation(required = true)
        private String superField;

        @TestMethodAnnotation(name = "superMethod")
        public void superMethod() {
        }
    }

    // 测试用实现类
    @TestClassAnnotation(value = "Test Class", order = 2)
    public static class TestClass extends TestSuperClass implements TestInterface {
        @TestFieldAnnotation
        private String testField1;

        @TestFieldAnnotation(required = true)
        private String testField2;

        private String testField3;

        @TestMethodAnnotation(name = "testMethod1")
        public void testMethod1() {
        }

        @TestMethodAnnotation(name = "testMethod2")
        public void testMethod2() {
        }

        public void testMethod3() {
        }
    }

    /**
     * 测试获取类上的指定注解
     */
    @Test
    public void testGetClassAnnotation() {
        TestClassAnnotation annotation = AnnotationUtils.getClassAnnotation(TestClass.class, TestClassAnnotation.class);
        Assert.assertNotNull(annotation);
        Assert.assertEquals("Test Class", annotation.value());
        Assert.assertEquals(2, annotation.order());
    }

    /**
     * 测试获取类上的所有注解
     */
    @Test
    public void testGetClassAnnotations() {
        Annotation[] annotations = AnnotationUtils.getClassAnnotations(TestClass.class);
        Assert.assertNotNull(annotations);
        Assert.assertTrue(annotations.length > 0);
    }

    /**
     * 测试检查类上是否存在指定注解
     */
    @Test
    public void testHasClassAnnotation() {
        boolean hasAnnotation = AnnotationUtils.hasClassAnnotation(TestClass.class, TestClassAnnotation.class);
        Assert.assertTrue(hasAnnotation);

        boolean hasNotAnnotation = AnnotationUtils.hasClassAnnotation(TestClass.class, TestMethodAnnotation.class);
        Assert.assertFalse(hasNotAnnotation);
    }

    /**
     * 测试获取方法上的指定注解
     */
    @Test
    public void testGetMethodAnnotation() throws NoSuchMethodException {
        Method method = TestClass.class.getMethod("testMethod1");
        TestMethodAnnotation annotation = AnnotationUtils.getMethodAnnotation(method, TestMethodAnnotation.class);
        Assert.assertNotNull(annotation);
        Assert.assertEquals("testMethod1", annotation.name());
    }

    /**
     * 测试获取方法上的所有注解
     */
    @Test
    public void testGetMethodAnnotations() throws NoSuchMethodException {
        Method method = TestClass.class.getMethod("testMethod1");
        Annotation[] annotations = AnnotationUtils.getMethodAnnotations(method);
        Assert.assertNotNull(annotations);
        Assert.assertTrue(annotations.length > 0);
    }

    /**
     * 测试检查方法上是否存在指定注解
     */
    @Test
    public void testHasMethodAnnotation() throws NoSuchMethodException {
        Method method1 = TestClass.class.getMethod("testMethod1");
        boolean hasAnnotation1 = AnnotationUtils.hasMethodAnnotation(method1, TestMethodAnnotation.class);
        Assert.assertTrue(hasAnnotation1);

        Method method3 = TestClass.class.getMethod("testMethod3");
        boolean hasAnnotation3 = AnnotationUtils.hasMethodAnnotation(method3, TestMethodAnnotation.class);
        Assert.assertFalse(hasAnnotation3);
    }

    /**
     * 测试获取字段上的指定注解
     */
    @Test
    public void testGetFieldAnnotation() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("testField1");
        TestFieldAnnotation annotation = AnnotationUtils.getFieldAnnotation(field, TestFieldAnnotation.class);
        Assert.assertNotNull(annotation);
        Assert.assertFalse(annotation.required());

        Field field2 = TestClass.class.getDeclaredField("testField2");
        TestFieldAnnotation annotation2 = AnnotationUtils.getFieldAnnotation(field2, TestFieldAnnotation.class);
        Assert.assertTrue(annotation2.required());
    }

    /**
     * 测试获取字段上的所有注解
     */
    @Test
    public void testGetFieldAnnotations() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("testField1");
        Annotation[] annotations = AnnotationUtils.getFieldAnnotations(field);
        Assert.assertNotNull(annotations);
        Assert.assertTrue(annotations.length > 0);
    }

    /**
     * 测试检查字段上是否存在指定注解
     */
    @Test
    public void testHasFieldAnnotation() throws NoSuchFieldException {
        Field field1 = TestClass.class.getDeclaredField("testField1");
        boolean hasAnnotation1 = AnnotationUtils.hasFieldAnnotation(field1, TestFieldAnnotation.class);
        Assert.assertTrue(hasAnnotation1);

        Field field3 = TestClass.class.getDeclaredField("testField3");
        boolean hasAnnotation3 = AnnotationUtils.hasFieldAnnotation(field3, TestFieldAnnotation.class);
        Assert.assertFalse(hasAnnotation3);
    }

    /**
     * 测试获取类中所有带指定注解的方法
     */
    @Test
    public void testGetMethodsWithAnnotation() {
        List<Method> methods = AnnotationUtils.getMethodsWithAnnotation(TestClass.class, TestMethodAnnotation.class);
        Assert.assertNotNull(methods);
        Assert.assertEquals(2, methods.size());
        
        // 验证方法名
        boolean hasTestMethod1 = false;
        boolean hasTestMethod2 = false;
        for (Method method : methods) {
            if ("testMethod1".equals(method.getName())) {
                hasTestMethod1 = true;
            } else if ("testMethod2".equals(method.getName())) {
                hasTestMethod2 = true;
            }
        }
        Assert.assertTrue(hasTestMethod1 && hasTestMethod2);
    }

    /**
     * 测试获取类中所有带指定注解的字段
     */
    @Test
    public void testGetFieldsWithAnnotation() {
        List<Field> fields = AnnotationUtils.getFieldsWithAnnotation(TestClass.class, TestFieldAnnotation.class);
        Assert.assertNotNull(fields);
        Assert.assertEquals(2, fields.size());
        
        // 验证字段名
        boolean hasTestField1 = false;
        boolean hasTestField2 = false;
        for (Field field : fields) {
            if ("testField1".equals(field.getName())) {
                hasTestField1 = true;
            } else if ("testField2".equals(field.getName())) {
                hasTestField2 = true;
            }
        }
        Assert.assertTrue(hasTestField1 && hasTestField2);
    }

    /**
     * 测试获取注解的属性值
     */
    @Test
    public void testGetAnnotationProperty() {
        TestClassAnnotation annotation = TestClass.class.getAnnotation(TestClassAnnotation.class);
        Object value = AnnotationUtils.getAnnotationProperty(annotation, "value");
        Assert.assertNotNull(value);
        Assert.assertEquals("Test Class", value.toString());

        Object order = AnnotationUtils.getAnnotationProperty(annotation, "order");
        Assert.assertNotNull(order);
        Assert.assertEquals(2, order);

        // 测试获取不存在的属性
        Object nonExistent = AnnotationUtils.getAnnotationProperty(annotation, "nonExistent");
        Assert.assertNull(nonExistent);
    }

    /**
     * 测试获取类上所有带指定注解的父类
     */
    @Test
    public void testGetSuperClassesWithAnnotation() {
        List<Class<?>> superClasses = AnnotationUtils.getSuperClassesWithAnnotation(TestClass.class, TestClassAnnotation.class);
        Assert.assertNotNull(superClasses);
        Assert.assertEquals(1, superClasses.size());
        Assert.assertEquals(TestSuperClass.class, superClasses.get(0));
    }

    /**
     * 测试获取类实现的所有带指定注解的接口
     */
    @Test
    public void testGetInterfacesWithAnnotation() {
        List<Class<?>> interfaces = AnnotationUtils.getInterfacesWithAnnotation(TestClass.class, TestInterfaceAnnotation.class);
        Assert.assertNotNull(interfaces);
        Assert.assertEquals(1, interfaces.size());
        Assert.assertEquals(TestInterface.class, interfaces.get(0));
    }

    /**
     * 测试空值处理
     */
    @Test
    public void testNullHandling() {
        // 测试空类
        TestClassAnnotation classAnnotation = AnnotationUtils.getClassAnnotation(null, TestClassAnnotation.class);
        Assert.assertNull(classAnnotation);

        // 测试空方法
        TestMethodAnnotation methodAnnotation = AnnotationUtils.getMethodAnnotation(null, TestMethodAnnotation.class);
        Assert.assertNull(methodAnnotation);

        // 测试空字段
        TestFieldAnnotation fieldAnnotation = AnnotationUtils.getFieldAnnotation(null, TestFieldAnnotation.class);
        Assert.assertNull(fieldAnnotation);

        // 测试空注解类
        Annotation[] annotations = AnnotationUtils.getClassAnnotations(TestClass.class);
        Assert.assertNotNull(annotations);
    }
}