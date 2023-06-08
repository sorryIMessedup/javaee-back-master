package com.example.j5ee.util;

import java.util.regex.Pattern;

public class RegexUtil {

    public final static String MAIL_NUMBER = "[a-zA-Z0-9_]+@\\w+(\\.com|\\.cn){1}";

    public static void main(String[] args) {
        String content="3222179759@qq.com";
        System.out.println(Pattern.matches(MAIL_NUMBER, content));
    }

}
