package com.example.j5ee.controller;

import com.example.j5ee.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/***
 * @author Urmeas
 * @date 2022/11/20 10:05 下午
 */
@Slf4j
@RestController
@RequestMapping("/search")
@Api("查找Controller")
public class SearchController extends BaseController{
    @Autowired
    private SearchService searchService;

    @ApiOperation(value = "搜索")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "title||author||summary", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "keyword", value = "搜索内容", required = true, paramType = "query", dataType = "String")
    })
    @GetMapping("/search/{type}/{keyword}")
    public Object search(@PathVariable("type") String type,@PathVariable("keyword") String keyword) throws IOException {
        return searchService.searchByKeyword(keyword,type);
    }

}
