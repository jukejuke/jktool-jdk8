package io.github.jukejuke.tool.bean;

import io.github.jukejuke.tool.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Bean工具类
 * 提供Bean拷贝、Bean转Map、Map转Bean等功能
 */
@Slf4j
public class BeanUtils {

    /**
     * 拷贝Bean对象属性到目标Bean
     * @param source 源Bean对象
     * @param targetClass 目标Bean类
     * @param <T> 目标Bean类型
     * @return 拷贝后的目标Bean对象
     * @throws Exception 反射异常
     */
    public static <T> T copyBean(Object source, Class<T> targetClass) throws Exception {
        if (source == null) {
            return null;
        }
        
        T target = targetClass.newInstance();
        copyBeanProperties(source, target);
        return target;
    }
    
    /**
     * 将源Bean的属性拷贝到目标Bean
     * @param source 源Bean对象
     * @param target 目标Bean对象
     * @throws Exception 反射异常
     */
    public static void copyBeanProperties(Object source, Object target) throws Exception {
        if (source == null || target == null) {
            return;
        }
        
        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();
        
        Field[] sourceFields = sourceClass.getDeclaredFields();
        for (Field sourceField : sourceFields) {
            String fieldName = sourceField.getName();
            Object fieldValue = getFieldValue(source, fieldName);
            
            try {
                setFieldValue(target, fieldName, fieldValue);
            } catch (Exception e) {
                // 如果目标Bean没有该字段，跳过
                continue;
            }
        }
    }
    
    /**
     * Bean对象转Map
     * @param bean Bean对象
     * @return Map对象
     * @throws Exception 反射异常
     */
    public static Map<String, Object> beanToMap(Object bean) throws Exception {
        if (bean == null) {
            return null;
        }
        
        Map<String, Object> map = new HashMap<>();
        Class<?> beanClass = bean.getClass();
        Field[] fields = beanClass.getDeclaredFields();
        
        for (Field field : fields) {
            String fieldName = field.getName();
            Object fieldValue = getFieldValue(bean, fieldName);
            
            // 处理日期类型
            if (fieldValue instanceof Date) {
                // 判断是否为纯日期（时间部分为00:00:00）
                Date date = (Date) fieldValue;
                String formattedDate;
                if (DateUtil.truncateTime(date).getTime() == date.getTime()) {
                    formattedDate = DateUtil.format(date, DateUtil.FORMAT_DATE);
                } else {
                    formattedDate = DateUtil.format(date, DateUtil.FORMAT_DATETIME);
                }
                map.put(fieldName, formattedDate);
            } else {
                map.put(fieldName, fieldValue);
            }
        }
        
        return map;
    }
    
    /**
     * Map转Bean对象
     * @param map Map对象
     * @param beanClass Bean类
     * @param <T> Bean类型
     * @return Bean对象
     * @throws Exception 反射异常
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> beanClass) throws Exception {
        if (map == null || map.isEmpty()) {
            return null;
        }
        
        T bean = beanClass.newInstance();
        
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue();
            
            try {
                setFieldValue(bean, fieldName, fieldValue);
            } catch (Exception e) {
                // 如果Bean没有该字段，跳过
                continue;
            }
        }
        
        return bean;
    }
    
    /**
     * 获取Bean对象的字段值
     * @param bean Bean对象
     * @param fieldName 字段名
     * @return 字段值
     * @throws Exception 反射异常
     */
    private static Object getFieldValue(Object bean, String fieldName) throws Exception {
        Class<?> beanClass = bean.getClass();
        
        // 尝试直接访问字段
        try {
            Field field = beanClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(bean);
        } catch (NoSuchFieldException e) {
            // 尝试通过getter方法访问
            String getterMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            try {
                Method getterMethod = beanClass.getMethod(getterMethodName);
                return getterMethod.invoke(bean);
            } catch (NoSuchMethodException ex) {
                // 尝试通过is方法访问（布尔类型）
                String isMethodName = "is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                try {
                    Method isMethod = beanClass.getMethod(isMethodName);
                    return isMethod.invoke(bean);
                } catch (NoSuchMethodException exc) {
                    throw new NoSuchFieldException("Field \"" + fieldName + "\" not found in class " + beanClass.getName());
                }
            }
        }
    }
    
    /**
     * 设置Bean对象的字段值
     * @param bean Bean对象
     * @param fieldName 字段名
     * @param fieldValue 字段值
     * @throws Exception 反射异常
     */
    private static void setFieldValue(Object bean, String fieldName, Object fieldValue) throws Exception {
        Class<?> beanClass = bean.getClass();
        
        // 尝试直接设置字段
        try {
            Field field = beanClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            
            // 处理日期字符串转换
            if (fieldValue instanceof String && field.getType() == Date.class) {
                String dateStr = (String) fieldValue;
                Date date;
                if (dateStr.length() == 10) {
                    // yyyy-MM-dd格式
                    date = DateUtil.parse(dateStr, DateUtil.FORMAT_DATE);
                } else {
                    // yyyy-MM-dd HH:mm:ss格式或其他格式
                    date = DateUtil.parse(dateStr, DateUtil.FORMAT_DATETIME);
                }
                field.set(bean, date);
            } else {
                field.set(bean, fieldValue);
            }
        } catch (NoSuchFieldException e) {
            // 尝试通过setter方法设置
            String setterMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            try {
                Method setterMethod = beanClass.getMethod(setterMethodName, getParameterType(fieldName, beanClass, fieldValue));
                
                // 处理日期字符串转换
                if (fieldValue instanceof String && setterMethod.getParameterTypes()[0] == Date.class) {
                    String dateStr = (String) fieldValue;
                    Date date;
                    if (dateStr.length() == 10) {
                        // yyyy-MM-dd格式
                        date = DateUtil.parse(dateStr, DateUtil.FORMAT_DATE);
                    } else {
                        // yyyy-MM-dd HH:mm:ss格式或其他格式
                        date = DateUtil.parse(dateStr, DateUtil.FORMAT_DATETIME);
                    }
                    setterMethod.invoke(bean, date);
                } else {
                    setterMethod.invoke(bean, fieldValue);
                }
            } catch (NoSuchMethodException ex) {
                throw new NoSuchFieldException("Field \"" + fieldName + "\" not found in class " + beanClass.getName());
            }
        }
    }
    
    /**
     * 获取setter方法的参数类型
     * @param fieldName 字段名
     * @param beanClass Bean类
     * @param fieldValue 字段值
     * @return 参数类型
     */
    private static Class<?> getParameterType(String fieldName, Class<?> beanClass, Object fieldValue) {
        // 尝试从字段获取类型
        try {
            Field field = beanClass.getDeclaredField(fieldName);
            return field.getType();
        } catch (NoSuchFieldException e) {
            // 如果字段不存在，返回值的类型
            return fieldValue != null ? fieldValue.getClass() : Object.class;
        }
    }
}