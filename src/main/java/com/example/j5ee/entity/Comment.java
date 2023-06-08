package com.example.j5ee.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName comment
 */
@TableName(value ="comment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("评论")
public class Comment implements Serializable {
    /**
     * 当前评论的id
     */
    @TableId
    private String commentId;

    /**
     * 父级评论的id
     */
    private String parentCommentId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 评论的论文id
     */
    private Integer paperId;

    /**
     * 
     */
    private Integer column_5;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 最顶级评论的id
     */
    private String root;

    /**
     * 
     */
    private Date date;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}