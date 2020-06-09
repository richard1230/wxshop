package com.github.wxshop.entity;

import java.util.List;

public class PageResponse<T> {
    private int pageNum;
    private int pageSize;
    private int totalPage;
    private List<T> data;

    public static <T> PageResponse<T> pagedData (int pageNum, int pageSize, int totalPage, List<T> data){
        PageResponse<T> result = new PageResponse<>();
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        result.setTotalPage(totalPage);
        result.setData(data);
        return result;
    }

    public PageResponse(){

    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
