package com.example.j5ee.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SubjectVO implements Serializable {
    private static final long serialVersionUID = -1839614375607739861L;
    @ApiModelProperty("项目sid")
    private Integer sid;

    /**
     *
     */
    @ApiModelProperty("用户id")
    private Integer id;

    /**
     *
     */
    @ApiModelProperty("项目标题title")
    private String title;

    /**
     * 开始日期
     */
    @ApiModelProperty("项目开始时间startDate")
    private Date startDate;

    /**
     *
     */
    @ApiModelProperty("项目结束时间endDate")
    private Date endDate;

    /**
     *
     */
    @ApiModelProperty("项目金额money")
    private Integer money;

    /**
     * 自然基金

     */
    @ApiModelProperty("项目自然基金fund")
    private Integer fund;

    /**
     * 合作单位
     */
    @ApiModelProperty("项目合作单位company")
    private String company;

    /**
     * 下载链接
     */
    @ApiModelProperty("项目下载链接download")
    private String download;

    /**
     * 收藏
     */
    @ApiModelProperty("项目收藏量collections")
    private Integer collections;

    /**
     * 点赞
     */
    @ApiModelProperty("项目点赞数likes")
    private Integer likes;

    /**
     * 项目类型  0-默认 1-国创  2-市创  3-校创
     */
    @ApiModelProperty("项目类型type")
    private Integer type;

   private UserVO userVO;
}
