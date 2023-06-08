package com.example.j5ee.controller;

import javax.servlet.http.HttpSession;

/***
 * @author Urmeas
 * @date 2022/10/27 9:57 下午
 */
public class BaseController {
    /**
     * 从session中获取id
     */
    protected final int getIdFromSession(HttpSession session){
        return Integer.parseInt(session.getAttribute("id").toString());
    }

    /**
     * 从session中获取username
     */
    protected final String getUsernameFromSession(HttpSession session){
        return session.getAttribute("username").toString();
    }
}
