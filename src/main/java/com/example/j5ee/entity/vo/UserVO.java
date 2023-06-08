package com.example.j5ee.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserVO implements Serializable {
    private static final long serialVersionUID = -5566831532642608203L;

    @ApiModelProperty("用户id")
    private int id;

    @ApiModelProperty("用户名")
    private String username;


    @ApiModelProperty("邮箱号")
    private String mail;


    @ApiModelProperty("头像")
    private String img;

    @ApiModelProperty("类型")
    private int type;
}
