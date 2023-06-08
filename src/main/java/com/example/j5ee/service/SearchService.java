package com.example.j5ee.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/***
 * @author Urmeas
 * @date 2022/11/20 10:06 下午
 */
public interface SearchService {
   List<Map<String,Object>> searchByKeyword(String keyword,String str)throws IOException;
}
