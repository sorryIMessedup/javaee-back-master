package com.example.j5ee.util;

import java.sql.Timestamp;


/***
 * @author Urmeas
 * @date 2022/10/2 2:40 下午
 */
public class MessageUtil {

   //注册邮箱
   public static String register(String email,String code){
      Timestamp now = new Timestamp(System.currentTimeMillis());
      return "from【sharker友友】：您好！"+email+"\n您于 "+getNowTime()+"使用邮箱验证注册账号，验证码："+code+"。";
   }

   //找回账号
   public static String findUsername(String email,String code){
      Timestamp now = new Timestamp(System.currentTimeMillis());
      return "from【sharker友友】：您好！"+email+"\n您于 "+getNowTime()+"使用邮箱验证找回账号，验证码："+code+"。";
   }

   //找回密码
   public static String findPassword(String email,String code){
      Timestamp now = new Timestamp(System.currentTimeMillis());
      return "from【sharker友友】：您好！"+email+"\n您于 "+getNowTime()+"使用邮箱验证找回密码，验证码："+code+"。";
   }

   private static String getNowTime(){
      Timestamp timestamp = new Timestamp(System.currentTimeMillis());
      String timeStr = timestamp
              .toString()
              .substring(0,timestamp.toString().indexOf("."));
      return timeStr;
   }

}
