package com.example.j5ee.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/***
 * @author Urmeas
 * @date 2022/10/27 9:47 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private int id;

    private String username;

    private String password;

    private String mail;

    private String code;

    private String img;

    private int type;

    private int isDelete;

}
