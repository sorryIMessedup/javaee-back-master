package com.example.j5ee.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;

/**
 * @author wl
 * @date 2021/7/14
 */
public class CommonUtils {
    private static final int DEFAULT_PAGE_NUM = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;

    public static <T> JSONArray listToJsonArray(List<T> list) {
        return JSONArray.parseArray(JSON.toJSONString(list, SerializerFeature.WriteMapNullValue));
    }

    public static <T> JSONArray listToJsonArray(List<T> list, boolean formatDate) {
        return formatDate ? JSONArray.parseArray(JSON.toJSONString(list, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue))
                : JSONArray.parseArray(JSON.toJSONString(list, SerializerFeature.WriteMapNullValue));
    }

}

