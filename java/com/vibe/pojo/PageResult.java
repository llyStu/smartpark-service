package com.vibe.pojo;

import java.util.List;


public class PageResult<T> {

    private List<T> data;
    private long total;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }


}
