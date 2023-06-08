package com.example.j5ee.service.impl;

import com.example.j5ee.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/***
 * @author Urmeas
 * @date 2022/11/20 10:08 下午
 */
@Service
@Slf4j
public class SearchServiceImpl implements SearchService {

    @Autowired
    public RestHighLevelClient restHighLevelClient;

    @Override
    public List<Map<String, Object>> searchByKeyword(String keyword,String str) throws IOException {
        SearchRequest searchRequest = new SearchRequest("paper_list");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(str,keyword);
        searchSourceBuilder.query(matchQueryBuilder);
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        //
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(100);
        //
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        //
        List<Map<String, Object>> results = new ArrayList<>();
        for(SearchHit documentFields : searchResponse.getHits().getHits()){
            results.add(documentFields.getSourceAsMap());
        }
        return results;
    }

//    public List<Map<String, Object>> searchByKeyword(String keyword,String str) throws IOException {
//        SearchRequest searchRequest = new SearchRequest("paper_list");
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        //
//        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(str,keyword);
//        searchSourceBuilder.query(matchQueryBuilder);
//        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
//        //
//        searchSourceBuilder.from(0);
//        searchSourceBuilder.size(100);
//        //
//        HighlightBuilder highlightBuilder = new HighlightBuilder();
//        highlightBuilder.field(str);
//        highlightBuilder.preTags("<span style='color:red'>");
//        highlightBuilder.postTags("</span>");
//        searchSourceBuilder.highlighter(highlightBuilder);
//        //
//        searchRequest.source(searchSourceBuilder);
//        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
//        //
//        SearchHits hits = searchResponse.getHits();
//        List<Map<String, Object>> results = new ArrayList<>();
//        for (SearchHit documentFields : hits.getHits()) {
//            //
//            Map<String, Object> sourceAsMap = documentFields.getSourceAsMap();
//            //
//            Map<String, HighlightField> highlightFields = documentFields.getHighlightFields();
//            HighlightField name = highlightFields.get(str);
//            //
//            if (name != null){
//                Text[] fragments = name.fragments();
//                StringBuilder new_name = new StringBuilder();
//                for (Text text : fragments) {
//                    new_name.append(text);
//                }
//                sourceAsMap.put(str,new_name.toString());
//            }
//            results.add(sourceAsMap);
//        }
//        return results;
//    }

}
