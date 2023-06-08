package com.example.j5ee.entity.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/***
 * @author Urmeas
 * @date 2022/11/20 9:04 下午
 */
@Data
public class PaperRequest implements Serializable {

    @ApiModelProperty("发布者id")
    private int id;

    @ApiModelProperty("论文标题")
    private String title;

    @ApiModelProperty("论文作者")
    private String author;

    @ApiModelProperty("发布来源")
    private String resource;

    @ApiModelProperty("论文摘要")
    private String summary;

    @ApiModelProperty("论文类型")
    private int type;

    @ApiModelProperty("发表日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date publishDate;

    @ApiModelProperty("上传日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date uploadDate;

    @ApiModelProperty("下载链接")
    private String download;

    @ApiModelProperty("知网链接")
    private String link;

}
