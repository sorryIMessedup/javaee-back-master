package com.example.j5ee.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName subject
 */
@TableName(value ="subject")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("Subject 项目")
public class Subject implements Serializable {
    /**
     * 
     */

    @TableId(type = IdType.AUTO)
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

    /**
     * 是否删除   0-未删除   1-已删除
     */
    @TableLogic
    @ApiModelProperty("项目是否删除isDelete")
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}