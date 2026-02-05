package io.github.jukejuke.api;

import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

/**
 * PageResponse测试类
 */
public class PageResponseTest {

    /**
     * 测试分页响应构造方法
     */
    @Test
    public void testPageResponseConstructor() {
        List<String> dataList = new ArrayList<>();
        dataList.add("item1");
        dataList.add("item2");
        
        long total = 100;
        int pageSize = 10;
        int pageNum = 1;
        
        PageResponse<String> pageResponse = new PageResponse<>(total, pageSize, pageNum, dataList);
        
        assertEquals(total, pageResponse.getTotal());
        assertEquals(pageSize, pageResponse.getPageSize());
        assertEquals(pageNum, pageResponse.getPageNum());
        assertEquals(10, pageResponse.getPages()); // 100 / 10 = 10
        assertEquals(dataList, pageResponse.getList());
    }

    /**
     * 测试分页响应构造方法（含总页数）
     */
    @Test
    public void testPageResponseConstructorWithPages() {
        List<String> dataList = new ArrayList<>();
        dataList.add("item1");
        
        long total = 5;
        int pageSize = 2;
        int pageNum = 1;
        int pages = 3; // 5 / 2 = 2.5，向上取整为3
        
        PageResponse<String> pageResponse = new PageResponse<>(total, pageSize, pageNum, pages, dataList);
        
        assertEquals(total, pageResponse.getTotal());
        assertEquals(pageSize, pageResponse.getPageSize());
        assertEquals(pageNum, pageResponse.getPageNum());
        assertEquals(pages, pageResponse.getPages());
        assertEquals(dataList, pageResponse.getList());
    }

    /**
     * 测试静态工厂方法of
     */
    @Test
    public void testOfMethod() {
        List<String> dataList = new ArrayList<>();
        dataList.add("item1");
        dataList.add("item2");
        dataList.add("item3");
        
        long total = 15;
        int pageSize = 3;
        int pageNum = 1;
        
        PageResponse<String> pageResponse = PageResponse.of(total, pageSize, pageNum, dataList);
        
        assertEquals(total, pageResponse.getTotal());
        assertEquals(pageSize, pageResponse.getPageSize());
        assertEquals(pageNum, pageResponse.getPageNum());
        assertEquals(5, pageResponse.getPages()); // 15 / 3 = 5
        assertEquals(dataList, pageResponse.getList());
    }

    /**
     * 测试toApiResponse方法
     */
    @Test
    public void testToApiResponse() {
        List<String> dataList = new ArrayList<>();
        dataList.add("item1");
        
        long total = 10;
        int pageSize = 5;
        int pageNum = 2;
        
        PageResponse<String> pageResponse = new PageResponse<>(total, pageSize, pageNum, dataList);
        ApiResponse<PageResponse<String>> apiResponse = pageResponse.toApiResponse();
        
        assertEquals(ApiCode.SUCCESS.getCode(), apiResponse.getCode());
        assertEquals(ApiCode.SUCCESS.getMessage(), apiResponse.getMessage());
        assertNotNull(apiResponse.getData());
        assertEquals(pageResponse, apiResponse.getData());
    }

    /**
     * 测试总页数计算
     */
    @Test
    public void testPagesCalculation() {
        // 测试整除情况
        PageResponse<String> response1 = new PageResponse<>(100, 10, 1, new ArrayList<>());
        assertEquals(10, response1.getPages());
        
        // 测试非整除情况（有余数）
        PageResponse<String> response2 = new PageResponse<>(101, 10, 1, new ArrayList<>());
        assertEquals(11, response2.getPages());
        
        // 测试刚好一页
        PageResponse<String> response3 = new PageResponse<>(5, 10, 1, new ArrayList<>());
        assertEquals(1, response3.getPages());
        
        // 测试零记录
        PageResponse<String> response4 = new PageResponse<>(0, 10, 1, new ArrayList<>());
        assertEquals(0, response4.getPages());
    }

    /**
     * 测试getter和setter方法
     */
    @Test
    public void testGettersAndSetters() {
        PageResponse<String> pageResponse = new PageResponse<>(0, 10, 1, new ArrayList<>());
        
        long testTotal = 200;
        int testPageSize = 20;
        int testPageNum = 2;
        int testPages = 10;
        List<String> testList = new ArrayList<>();
        testList.add("test1");
        
        pageResponse.setTotal(testTotal);
        pageResponse.setPageSize(testPageSize);
        pageResponse.setPageNum(testPageNum);
        pageResponse.setPages(testPages);
        pageResponse.setList(testList);
        
        assertEquals(testTotal, pageResponse.getTotal());
        assertEquals(testPageSize, pageResponse.getPageSize());
        assertEquals(testPageNum, pageResponse.getPageNum());
        assertEquals(testPages, pageResponse.getPages());
        assertEquals(testList, pageResponse.getList());
    }

    /**
     * 测试toString方法
     */
    @Test
    public void testToString() {
        List<String> dataList = new ArrayList<>();
        dataList.add("item1");
        
        PageResponse<String> pageResponse = new PageResponse<>(10, 5, 1, dataList);
        String toString = pageResponse.toString();
        
        assertNotNull(toString);
        assertTrue(toString.contains("total=10"));
        assertTrue(toString.contains("pageSize=5"));
        assertTrue(toString.contains("pageNum=1"));
        assertTrue(toString.contains("pages=2"));
        assertTrue(toString.contains("list=[item1]"));
    }

    /**
     * 测试不同数据类型的分页响应
     */
    @Test
    public void testGenericDataType() {
        // 测试字符串类型
        List<String> stringList = new ArrayList<>();
        stringList.add("string1");
        PageResponse<String> stringResponse = PageResponse.of(10, 5, 1, stringList);
        assertEquals(stringList, stringResponse.getList());
        
        // 测试整数类型
        List<Integer> intList = new ArrayList<>();
        intList.add(1);
        intList.add(2);
        PageResponse<Integer> intResponse = PageResponse.of(20, 10, 1, intList);
        assertEquals(intList, intResponse.getList());
        
        // 测试自定义对象类型
        class TestObject {
            private String name;
            public TestObject(String name) { this.name = name; }
            @Override
            public String toString() { return name; }
        }
        List<TestObject> objList = new ArrayList<>();
        objList.add(new TestObject("obj1"));
        PageResponse<TestObject> objResponse = PageResponse.of(5, 3, 1, objList);
        assertEquals(objList, objResponse.getList());
    }

    /**
     * 测试空数据列表的情况
     */
    @Test
    public void testEmptyDataList() {
        List<String> emptyList = new ArrayList<>();
        PageResponse<String> pageResponse = new PageResponse<>(0, 10, 1, emptyList);
        
        assertEquals(0, pageResponse.getTotal());
        assertEquals(10, pageResponse.getPageSize());
        assertEquals(1, pageResponse.getPageNum());
        assertEquals(0, pageResponse.getPages());
        assertEquals(emptyList, pageResponse.getList());
        assertTrue(pageResponse.getList().isEmpty());
    }
}
