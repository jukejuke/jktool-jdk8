package io.github.jukejuke.tool.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 注解工具类，提供常用的注解操作方法
 */
public class AnnotationUtils {

    /**
     * 获取类上的指定注解
     *
     * @param clazz     目标类
     * @param annoClass 注解类
     * @param <T>       注解类型
     * @return 注解实例，不存在则返回null
     */
    public static <T extends Annotation> T getClassAnnotation(Class<?> clazz, Class<T> annoClass) {
        if (clazz == null || annoClass == null) {
            return null;
        }
        return clazz.getAnnotation(annoClass);
    }

    /**
     * 获取类上的所有注解
     *
     * @param clazz 目标类
     * @return 注解数组
     */
    public static Annotation[] getClassAnnotations(Class<?> clazz) {
        if (clazz == null) {
            return new Annotation[0];
        }
        return clazz.getAnnotations();
    }

    /**
     * 检查类上是否存在指定注解
     *
     * @param clazz     目标类
     * @param annoClass 注解类
     * @return 是否存在
     */
    public static boolean hasClassAnnotation(Class<?> clazz, Class<? extends Annotation> annoClass) {
        return getClassAnnotation(clazz, annoClass) != null;
    }

    /**
     * 获取方法上的指定注解
     *
     * @param method    目标方法
     * @param annoClass 注解类
     * @param <T>       注解类型
     * @return 注解实例，不存在则返回null
     */
    public static <T extends Annotation> T getMethodAnnotation(Method method, Class<T> annoClass) {
        if (method == null || annoClass == null) {
            return null;
        }
        return method.getAnnotation(annoClass);
    }

    /**
     * 获取方法上的所有注解
     *
     * @param method 目标方法
     * @return 注解数组
     */
    public static Annotation[] getMethodAnnotations(Method method) {
        if (method == null) {
            return new Annotation[0];
        }
        return method.getAnnotations();
    }

    /**
     * 检查方法上是否存在指定注解
     *
     * @param method    目标方法
     * @param annoClass 注解类
     * @return 是否存在
     */
    public static boolean hasMethodAnnotation(Method method, Class<? extends Annotation> annoClass) {
        return getMethodAnnotation(method, annoClass) != null;
    }

    /**
     * 获取字段上的指定注解
     *
     * @param field     目标字段
     * @param annoClass 注解类
     * @param <T>       注解类型
     * @return 注解实例，不存在则返回null
     */
    public static <T extends Annotation> T getFieldAnnotation(Field field, Class<T> annoClass) {
        if (field == null || annoClass == null) {
            return null;
        }
        return field.getAnnotation(annoClass);
    }

    /**
     * 获取字段上的所有注解
     *
     * @param field 目标字段
     * @return 注解数组
     */
    public static Annotation[] getFieldAnnotations(Field field) {
        if (field == null) {
            return new Annotation[0];
        }
        return field.getAnnotations();
    }

    /**
     * 检查字段上是否存在指定注解
     *
     * @param field     目标字段
     * @param annoClass 注解类
     * @return 是否存在
     */
    public static boolean hasFieldAnnotation(Field field, Class<? extends Annotation> annoClass) {
        return getFieldAnnotation(field, annoClass) != null;
    }

    /**
     * 获取类中所有带指定注解的方法
     *
     * @param clazz     目标类
     * @param annoClass 注解类
     * @return 方法列表
     */
    public static List<Method> getMethodsWithAnnotation(Class<?> clazz, Class<? extends Annotation> annoClass) {
        List<Method> result = new ArrayList<>();
        if (clazz == null || annoClass == null) {
            return result;
        }

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (hasMethodAnnotation(method, annoClass)) {
                result.add(method);
            }
        }
        return result;
    }

    /**
     * 获取类中所有带指定注解的字段
     *
     * @param clazz     目标类
     * @param annoClass 注解类
     * @return 字段列表
     */
    public static List<Field> getFieldsWithAnnotation(Class<?> clazz, Class<? extends Annotation> annoClass) {
        List<Field> result = new ArrayList<>();
        if (clazz == null || annoClass == null) {
            return result;
        }

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (hasFieldAnnotation(field, annoClass)) {
                result.add(field);
            }
        }
        return result;
    }

    /**
     * 获取注解的属性值
     *
     * @param annotation 注解实例
     * @param propertyName 属性名称
     * @return 属性值，获取失败则返回null
     */
    public static Object getAnnotationProperty(Annotation annotation, String propertyName) {
        if (annotation == null || propertyName == null || propertyName.isEmpty()) {
            return null;
        }

        try {
            Method method = annotation.annotationType().getMethod(propertyName);
            method.setAccessible(true);
            return method.invoke(annotation);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取类上所有带指定注解的父类
     *
     * @param clazz     目标类
     * @param annoClass 注解类
     * @param <T>       注解类型
     * @return 父类列表
     */
    public static <T extends Annotation> List<Class<?>> getSuperClassesWithAnnotation(Class<?> clazz, Class<T> annoClass) {
        List<Class<?>> result = new ArrayList<>();
        if (clazz == null || annoClass == null) {
            return result;
        }

        Class<?> superClass = clazz.getSuperclass();
        while (superClass != null && superClass != Object.class) {
            if (hasClassAnnotation(superClass, annoClass)) {
                result.add(superClass);
            }
            superClass = superClass.getSuperclass();
        }
        return result;
    }

    /**
     * 获取类实现的所有带指定注解的接口
     *
     * @param clazz     目标类
     * @param annoClass 注解类
     * @param <T>       注解类型
     * @return 接口列表
     */
    public static <T extends Annotation> List<Class<?>> getInterfacesWithAnnotation(Class<?> clazz, Class<T> annoClass) {
        List<Class<?>> result = new ArrayList<>();
        if (clazz == null || annoClass == null) {
            return result;
        }

        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> iface : interfaces) {
            if (hasClassAnnotation(iface, annoClass)) {
                result.add(iface);
            }
            // 递归检查父接口
            result.addAll(getInterfacesWithAnnotation(iface, annoClass));
        }
        return result;
    }
}