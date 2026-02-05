package io.github.jukejuke.tool.bean;

import com.alibaba.fastjson2.JSON;
import io.github.jukejuke.tool.date.DateUtil;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * BeanUtils测试类
 */
public class BeanUtilsTest {
    
    /**
     * 测试Bean类
     */
    static class TestBean {
        private String name;
        private int age;
        private Date birthday;
        private Date createTime;
        
        // getter和setter方法
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public int getAge() {
            return age;
        }
        
        public void setAge(int age) {
            this.age = age;
        }
        
        public Date getBirthday() {
            return birthday;
        }
        
        public void setBirthday(Date birthday) {
            this.birthday = birthday;
        }
        
        public Date getCreateTime() {
            return createTime;
        }
        
        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }
    }
    
    /**
     * 测试Bean拷贝功能
     */
    @Test
    public void testCopyBean() throws Exception {
        // 创建源Bean
        TestBean source = new TestBean();
        source.setName("测试");
        source.setAge(25);
        source.setBirthday(DateUtil.parse("1990-01-01", DateUtil.FORMAT_DATE));
        source.setCreateTime(new Date());
        
        // 拷贝Bean
        TestBean target = BeanUtils.copyBean(source, TestBean.class);
        
        // 验证拷贝结果
        assertNotNull(target);
        assertEquals(source.getName(), target.getName());
        assertEquals(source.getAge(), target.getAge());
        assertEquals(source.getBirthday(), target.getBirthday());
        assertEquals(source.getCreateTime(), target.getCreateTime());
    }
    
    /**
     * 测试Bean转Map功能
     */
    @Test
    public void testBeanToMap() throws Exception {
        // 创建Bean对象
        TestBean bean = new TestBean();
        bean.setName("测试");
        bean.setAge(25);
        bean.setBirthday(DateUtil.parse("1990-01-01", DateUtil.FORMAT_DATE));
        bean.setCreateTime(new Date());
        
        // Bean转Map
        Map<String, Object> map = BeanUtils.beanToMap(bean);

        System.out.println(JSON.toJSONString(bean));
        System.out.println(JSON.toJSONString(map));

        // 验证转换结果
        assertNotNull(map);
        assertEquals(4, map.size());
        assertEquals("测试", map.get("name"));
        assertEquals(25, map.get("age"));
        // 日期应该格式化为yyyy-MM-dd
        assertEquals("1990-01-01", map.get("birthday"));
        // 时间应该格式化为yyyy-MM-dd HH:mm:ss
        assertTrue(map.get("createTime") instanceof String);
        assertTrue(((String) map.get("createTime")).matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }
    
    /**
     * 测试Map转Bean功能
     */
    @Test
    public void testMapToBean() throws Exception {
        // 创建Map对象
        Map<String, Object> map = new HashMap<>();
        map.put("name", "测试");
        map.put("age", 25);
        map.put("birthday", "1990-01-01");
        map.put("createTime", "2023-01-01 12:00:00");
        
        // Map转Bean
        TestBean bean = BeanUtils.mapToBean(map, TestBean.class);
        
        // 验证转换结果
        assertNotNull(bean);
        assertEquals("测试", bean.getName());
        assertEquals(25, bean.getAge());
        assertNotNull(bean.getBirthday());
        assertNotNull(bean.getCreateTime());
        // 验证日期格式转换
        assertEquals("1990-01-01", DateUtil.format(bean.getBirthday(), DateUtil.FORMAT_DATE));
        assertEquals("2023-01-01 12:00:00", DateUtil.format(bean.getCreateTime(), DateUtil.FORMAT_DATETIME));
    }
}