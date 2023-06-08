package com.example.j5ee.service;

import com.alibaba.fastjson.JSON;
import com.example.j5ee.common.CommonErrorCode;
import com.example.j5ee.common.CommonException;
import com.example.j5ee.entity.Search;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/***
 * @author Urmeas
 * @date 2022/12/6 6:59 下午
 */
@SpringBootTest
public class searchTest {
   @Autowired
   private RestHighLevelClient restHighLevelClient;

   @Test
   public void test(){
      try {
         Search search = new Search("title","author","summary");
         BulkRequest bulkRequest = new BulkRequest();
         bulkRequest.timeout("2m");
         bulkRequest.add(new IndexRequest("paper_list")
                 .source(JSON.toJSONString(search), XContentType.JSON)
         );
         BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
      } catch (Exception e){
         throw new CommonException(CommonErrorCode.SEARCH_INSERT_ERROR);
      }
   }
}
