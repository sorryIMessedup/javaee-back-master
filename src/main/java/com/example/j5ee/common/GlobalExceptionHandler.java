package com.example.j5ee.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/***
 * @author Urmeas
 * @date 2022/10/1 12:31 上午
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public Result defaultErrorHandler(HttpServletRequest request, Exception e) {
        Map<String, String> res = new HashMap<>();
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String value = request.getParameter(en);
                res.put(en, value);
            }
        }
        log.error("args: " + res.toString());
        log.error("e:" + e.getMessage());

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        e.printStackTrace(pw);
        pw.flush();
        sw.flush();
        log.error("exception: " + sw.toString());


        if(e instanceof CommonException) {
            log.error("commonException: " + ((CommonException) e).getCommonErrorCode().getErrorReason() + "\n" + sw.toString());
            return Result.result(((CommonException) e).getCommonErrorCode(),((CommonException) e).getErrorMsg());
        }
        return Result.fail(sw.toString(), null);
    }

}
