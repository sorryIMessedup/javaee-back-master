package com.example.j5ee.task;


import com.example.j5ee.service.PostingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 点赞的定时任务
 */
@Slf4j
@Component
public class LikeTask {

   @Autowired
   PostingService postingService;

   private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

   //每隔2h执行一次
//   @Scheduled(cron = "0 0 */2 * * ?")
   @Scheduled(cron = "0 */1 * * * ?")
   public void reportCurrentTime() {
      log.info("LikeTask-------- {}", sdf.format(new Date()));

      //将 Redis 里的点赞信息同步到数据库里
      postingService.transLikedFromRedis2DB();
      postingService.transLikedCountFromRedis2DB();
   }
}
