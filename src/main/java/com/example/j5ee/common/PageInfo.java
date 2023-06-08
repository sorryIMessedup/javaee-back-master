package com.example.j5ee.common;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.j5ee.util.CommonUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wl
 * @date 2021/7/27
 */
@Data
public class PageInfo<T> implements Serializable {
    private static final long serialVersionUID = -1097393152919726886L;
    private JSONArray records;
    private long total;
    private long pageSize;
    private long current;

    public void setPageInfo(IPage<T> page) {
        this.total = page.getTotal();
        this.pageSize = page.getSize();
        this.current = page.getCurrent();
        this.records = CommonUtils.listToJsonArray(page.getRecords());
    }
}

