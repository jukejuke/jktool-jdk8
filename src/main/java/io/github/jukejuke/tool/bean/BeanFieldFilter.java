package io.github.jukejuke.tool.bean;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Bean属性过滤工具类，用于根据排除字段集合筛选对象中的属性
 */
public class BeanFieldFilter {
    
    /**
     * 获取对象中除排除字段外的所有字段名和值
     * @param bean 待处理的JavaBean对象，不能为null
     * @param excludeFields 需要排除的字段名集合，可以为null（表示不排除任何字段）
     * @return 包含筛选后字段名和对应值的Map集合
     * @throws IllegalAccessException 当无法访问字段（如安全管理器限制）时抛出
     */
    public static Map<String, Object> getFilteredFields(Object bean, Set<String> excludeFields) throws IllegalAccessException {
        // Handle null excludeFields by treating as empty set
        if (excludeFields == null) {
            excludeFields = new HashSet<>();
        }
        Map<String, Object> result = new HashMap<>();
        Class<?> clazz = bean.getClass();

        // 获取所有声明的字段（包括 private）
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            String fieldName = field.getName();
            if (excludeFields.contains(fieldName)) {
                continue; // 跳过要排除的字段
            }

            field.setAccessible(true); // 允许访问私有字段
            Object value = field.get(bean);
            result.put(fieldName, value);
        }

        return result;
    }
}