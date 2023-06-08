package com.example.j5ee.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/***
 * @author Urmeas
 * @date 2022/11/29 1:31 下午
 */
@Slf4j
@Component
public class loginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("session:--------"+request.getSession());
        if(request.getSession().getAttribute("username")!=null){
            String username = request.getSession().getAttribute("username").toString();
        }
        return true;
    }
}
