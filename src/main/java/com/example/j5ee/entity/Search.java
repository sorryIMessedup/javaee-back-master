package com.example.j5ee.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/***
 * @author Urmeas
 * @date 2022/11/20 9:59 下午
 */
@Data
@AllArgsConstructor
public class Search {
    @ApiModelProperty("论文标题")
    private String title;

    @ApiModelProperty("论文作者")
    private String author;

    @ApiModelProperty("论文摘要")
    private String summary;
}
