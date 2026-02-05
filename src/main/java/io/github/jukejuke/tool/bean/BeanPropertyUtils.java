package io.github.jukejuke.tool.bean;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Bean属性操作工具类
 * 提供通过反射机制移除Bean对象指定属性的功能
 */
public class BeanPropertyUtils {

    /**
     * 移除Bean对象中指定的属性（将属性值设为null）
     * @param bean 目标Bean对象
     * @param propertyNames 要移除的属性名称数组
     * @param <T> Bean类型
     * @return 处理后的Bean对象
     * @throws IllegalAccessException 反射访问异常
     */
    public static <T> T removeProperties(T bean, String... propertyNames) throws IllegalAccessException {
        if (bean == null || propertyNames == null || propertyNames.length == 0) {
            return bean;
        }

        // 使用Set存储属性名，提高查找效率
        Set<String> propertiesToRemove = new HashSet<>(Arrays.asList(propertyNames));

        // 获取类的所有字段（包括私有字段）
        Class<?> beanClass = bean.getClass();
        Field[] fields = beanClass.getDeclaredFields();

        for (Field field : fields) {
            // 如果字段在要移除的属性列表中
            if (propertiesToRemove.contains(field.getName())) {
                // 设置私有字段可访问
                field.setAccessible(true);
                // 将字段值设为null
                field.set(bean, null);
            }
        }

        return bean;
    }

    /**
     * 移除Bean对象中指定的属性（将属性值设为null），忽略异常
     * @param bean 目标Bean对象
     * @param propertyNames 要移除的属性名称数组
     * @param <T> Bean类型
     * @return 处理后的Bean对象，若发生异常则返回原对象
     */
    public static <T> T removePropertiesSafe(T bean, String... propertyNames) {
        try {
            return removeProperties(bean, propertyNames);
        } catch (IllegalAccessException e) {
            // 记录异常日志，实际应用中可替换为日志框架
            e.printStackTrace();
            return bean;
        }
    }
}