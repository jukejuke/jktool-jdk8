package io.github.jukejuke.api;

import java.io.Serializable;
import java.util.List;

/**
 * 分页响应结果封装类
 * 用于封装分页查询的结果，包含总记录数、每页记录数、当前页码、总页数和数据列表
 * @param <T> 数据类型
 */
public class PageResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 总记录数
     */
    private long total;
    
    /**
     * 每页记录数
     */
    private int pageSize;
    
    /**
     * 当前页码
     */
    private int pageNum;
    
    /**
     * 总页数
     */
    private int pages;
    
    /**
     * 数据列表
     */
    private List<T> list;
    
    /**
     * 构造方法
     * @param total 总记录数
     * @param pageSize 每页记录数
     * @param pageNum 当前页码
     * @param list 数据列表
     */
    public PageResponse(long total, int pageSize, int pageNum, List<T> list) {
        this.total = total;
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.list = list;
        // 计算总页数
        this.pages = (int) Math.ceil((double) total / pageSize);
    }
    
    /**
     * 构造方法
     * @param total 总记录数
     * @param pageSize 每页记录数
     * @param pageNum 当前页码
     * @param pages 总页数
     * @param list 数据列表
     */
    public PageResponse(long total, int pageSize, int pageNum, int pages, List<T> list) {
        this.total = total;
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.pages = pages;
        this.list = list;
    }
    
    /**
     * 创建分页响应实例
     * @param <T> 数据类型
     * @param total 总记录数
     * @param pageSize 每页记录数
     * @param pageNum 当前页码
     * @param list 数据列表
     * @return PageResponse实例
     */
    public static <T> PageResponse<T> of(long total, int pageSize, int pageNum, List<T> list) {
        return new PageResponse<>(total, pageSize, pageNum, list);
    }
    
    /**
     * 创建分页响应实例
     * @param <T> 数据类型
     * @param total 总记录数
     * @param pageSize 每页记录数
     * @param pageNum 当前页码
     * @param pages 总页数
     * @param list 数据列表
     * @return PageResponse实例
     */
    public static <T> PageResponse<T> of(long total, int pageSize, int pageNum, int pages, List<T> list) {
        return new PageResponse<>(total, pageSize, pageNum, pages, list);
    }
    
    /**
     * 将分页响应包装为ApiResponse
     * @return ApiResponse实例
     */
    public ApiResponse<PageResponse<T>> toApiResponse() {
        return ApiResponse.success(this);
    }
    
    /**
     * 获取总记录数
     * @return 总记录数
     */
    public long getTotal() {
        return total;
    }
    
    /**
     * 设置总记录数
     * @param total 总记录数
     */
    public void setTotal(long total) {
        this.total = total;
    }
    
    /**
     * 获取每页记录数
     * @return 每页记录数
     */
    public int getPageSize() {
        return pageSize;
    }
    
    /**
     * 设置每页记录数
     * @param pageSize 每页记录数
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    
    /**
     * 获取当前页码
     * @return 当前页码
     */
    public int getPageNum() {
        return pageNum;
    }
    
    /**
     * 设置当前页码
     * @param pageNum 当前页码
     */
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
    
    /**
     * 获取总页数
     * @return 总页数
     */
    public int getPages() {
        return pages;
    }
    
    /**
     * 设置总页数
     * @param pages 总页数
     */
    public void setPages(int pages) {
        this.pages = pages;
    }
    
    /**
     * 获取数据列表
     * @return 数据列表
     */
    public List<T> getList() {
        return list;
    }
    
    /**
     * 设置数据列表
     * @param list 数据列表
     */
    public void setList(List<T> list) {
        this.list = list;
    }
    
    @Override
    public String toString() {
        return "PageResponse{" +
                "total=" + total +
                ", pageSize=" + pageSize +
                ", pageNum=" + pageNum +
                ", pages=" + pages +
                ", list=" + list +
                '}';
    }
}