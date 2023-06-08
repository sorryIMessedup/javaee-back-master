package com.example.j5ee.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("Paper 论文")
public class Paper {
    @Id
    @ApiModelProperty("论文pid")
    private int pid;

    @ApiModelProperty("发布者id")
    private int id;

    @ApiModelProperty("论文标题")
    private String title;

    @ApiModelProperty("论文作者")
    private String author;

    @ApiModelProperty("发布来源")
    private String resource;

    @ApiModelProperty("论文类型")
    private int type;

    @ApiModelProperty("论文摘要")
    private String summary;

    @ApiModelProperty("发表日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date publishDate;

    @ApiModelProperty("上传日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date uploadDate;

    @ApiModelProperty("删除日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date deleteDate;

    @ApiModelProperty("收藏数")
    private int collections;

    @ApiModelProperty("点赞数")
    private int likes;

    @ApiModelProperty("下载链接")
    private String download;

    @ApiModelProperty("知网链接")
    private String link;

    @ApiModelProperty("论文分数")
    private int score;

    @ApiModelProperty("审核状态")
    private int status;
}
