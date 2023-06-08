package com.example.j5ee.util;

import org.springframework.stereotype.Component;

@Component
public class RedisKeyUtil {

   //保存用户点赞数据的key
   public static final String MAP_KEY_USER_LIKED = "MAP_USER_LIKED";
   //保存论文被点赞数量的key
   public static final String MAP_KEY_PAPER_LIKED_COUNT = "MAP_PAPER_LIKED_COUNT";

   /**
    * @return
    */
   public String getLikedKey(int id,int pid){
      return  id + "::" + pid;
   }

}
