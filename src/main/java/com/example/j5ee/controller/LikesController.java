package com.example.j5ee.controller;

import com.example.j5ee.entity.Posting;
import com.example.j5ee.service.PostingService;
import com.example.j5ee.service.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.List;

/***
 * @author Urmeas
 * @date 2022/11/20 10:22 下午
 */
@RestController
@Api(tags = "点赞论文接口")
@RequestMapping("/likes")
@Validated
@Slf4j
public class LikesController extends BaseController{
   @Autowired
   private RedisService redisService;

   @Autowired
   private PostingService postingService;

   @GetMapping("/doLikeTest/{id}/{pid}")
   public Object doLikeTest(@PathVariable("pid") int pid, @PathVariable("id") int id){
      Posting prePosting = postingService.getByIdAndPid(id,pid);
      Integer result;
      if(prePosting!=null) result = prePosting.getStatus();
      else result = 0;
      //redis
      List<Posting> list = redisService.getLikedDataFromRedisNotDel();
      if(list!=null){
         for(Posting p : list){
            if(p.getId()==id && p.getPid()==pid && p.getStatus()==1){
               result++;
            }else if(p.getId()==id && p.getPid()==pid && p.getStatus()==0){
               result--;
            }
         }
      }
      if(result<=0) result = 0;
      if(result>=1) result = 1;
      if(result==1) return "SHAKE";
      redisService.doLike(id,pid);
      redisService.incrementLikedCount(pid);
      return "OK";
   }

   @GetMapping("/alreadyLikeTest/{id}")
   public Object alreadyLikeTest( @PathVariable("id") int id){
      return postingService.getLikedListById(id);
   }


   @ApiOperation(value = "点赞")
   @ApiImplicitParams({
           @ApiImplicitParam(name ="pid",value = "所赞论文的id",dataTypeClass = Long.class,required = true),
   })
   @PostMapping("/doLike/{pid}")
   public Object doLike(@NotNull @PathVariable("pid") int pid, HttpSession session){
      int id = getIdFromSession(session);
      Posting prePosting = postingService.getByIdAndPid(id,pid);
      Integer result;
      if(prePosting!=null) result = prePosting.getStatus();
      else result = 0;
      //redis
      List<Posting> list = redisService.getLikedDataFromRedisNotDel();
      if(list!=null){
         for(Posting p : list){
            if(p.getId()==id && p.getPid()==pid && p.getStatus()==1){
               result++;
            }else if(p.getId()==id && p.getPid()==pid && p.getStatus()==0){
               result--;
            }
         }
      }
      if(result<=0) result = 0;
      if(result>=1) result = 1;
      if(result==1) return "SHAKE";
      redisService.doLike(id,pid);
      redisService.incrementLikedCount(pid);
      return "OK";
   }

   @ApiOperation(value = "当前用户近期点赞过的论文")
   @ApiImplicitParams({
           @ApiImplicitParam(name ="token",value = "token",dataTypeClass = String.class,required = true)
   })
   @GetMapping("/alreadyLike")
   public Object alreadyLike(HttpSession session){
      int id = getIdFromSession(session);
      return postingService.getLikedListById(id);
   }


}
