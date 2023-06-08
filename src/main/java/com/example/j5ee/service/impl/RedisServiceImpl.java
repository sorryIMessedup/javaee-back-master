package com.example.j5ee.service.impl;


import com.example.j5ee.entity.Posting;
import com.example.j5ee.entity.dto.LikedCountDTO;
import com.example.j5ee.service.RedisService;
import com.example.j5ee.util.RedisKeyUtil;
import com.example.j5ee.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RedisServiceImpl implements RedisService {

    @Resource
    RedisTemplate redisTemplate;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    RedisKeyUtil redisKeyUtil;

    @Override
    public void doLike(int id,int pid) {
        String key = redisKeyUtil.getLikedKey(id,pid);
        redisUtil.hset(RedisKeyUtil.MAP_KEY_USER_LIKED,key,1);
    }

    @Override
    public void unLike(int id,int pid) {
        String key = redisKeyUtil.getLikedKey(id,pid);
        redisUtil.hset(RedisKeyUtil.MAP_KEY_USER_LIKED,key,0);
    }

    @Override
    public void deleteLikedFromRedis(int id,int pid) {
        String key = redisKeyUtil.getLikedKey(id,pid);
        redisUtil.hdel(RedisKeyUtil.MAP_KEY_USER_LIKED, key);
    }

    @Override
    public void deleteLikedCountFromRedis(int pid) {
        Integer pid2 = pid;
        redisUtil.hdel(RedisKeyUtil.MAP_KEY_PAPER_LIKED_COUNT,pid2.toString());
    }


    @Override
    public void incrementLikedCount(int pid) {
        Integer pid2 = pid;
        redisUtil.hincr(RedisKeyUtil.MAP_KEY_PAPER_LIKED_COUNT, pid2.toString(), 1);
    }

    @Override
    public void decrementLikedCount(int pid){
        Integer pid2 = pid;
        redisUtil.hincr(RedisKeyUtil.MAP_KEY_PAPER_LIKED_COUNT, pid2.toString(), -1);
    }


    @Override
    public List<Posting> getLikedDataFromRedis() {
        try {
            List<Posting> list = new ArrayList<>();
            Cursor<Map.Entry<Object,Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtil.MAP_KEY_USER_LIKED, ScanOptions.NONE);
            while (cursor.hasNext()){
                Map.Entry<Object,Object> entry = cursor.next();

                String key = (String) entry.getKey();
                //分离出
                String[] split = key.split("::");
                Integer id = Integer.valueOf(split[0]);
                Integer pid = Integer.valueOf(split[1]);
                Integer status = (Integer) entry.getValue();

                //组装成 post 对象
                Posting post = new Posting(id,pid,status);
                list.add(post);

                //存到 list 后从 Redis 中删除
                redisUtil.hdel(RedisKeyUtil.MAP_KEY_USER_LIKED,key);
            }
            System.out.println(list);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    @Override
    public List<Posting> getLikedDataFromRedisNotDel() {
        try {
            List<Posting> list = new ArrayList<>();
            Cursor<Map.Entry<Object,Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtil.MAP_KEY_USER_LIKED, ScanOptions.NONE);
            while (cursor.hasNext()){
                Map.Entry<Object,Object> entry = cursor.next();

                String key = (String) entry.getKey();
                //分离
                String[] split = key.split("::");
                Integer id = Integer.valueOf(split[0]);
                Integer pid = Integer.valueOf(split[1]);
                Integer status = (Integer) entry.getValue();

                //组装成 post 对象
                Posting post = new Posting(id,pid,status);
                list.add(post);
            }
            System.out.println(list);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<LikedCountDTO> getLikedCountFromRedis() {
        try{
            Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtil.MAP_KEY_PAPER_LIKED_COUNT, ScanOptions.NONE);
            List<LikedCountDTO> list = new ArrayList<>();
            while (cursor.hasNext()){
                Map.Entry<Object, Object> map = cursor.next();
                //将点赞数量存储在 LikedCountDTO
                String key = (String)map.getKey();//用户的id
                LikedCountDTO dto = new LikedCountDTO(key, (Integer) map.getValue());//用户的id 和 点赞数量
                list.add(dto);
                //从Redis中删除这条记录
                redisUtil.hdel(RedisKeyUtil.MAP_KEY_PAPER_LIKED_COUNT, key);
            }
            return list;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<LikedCountDTO> getLikedCountFromRedisNotDel() {
        try{
            Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtil.MAP_KEY_PAPER_LIKED_COUNT, ScanOptions.NONE);
            List<LikedCountDTO> list = new ArrayList<>();
            while (cursor.hasNext()){
                Map.Entry<Object, Object> map = cursor.next();
                //将点赞数量存储在 LikedCountDTO
                String key = (String)map.getKey();//用户的id
                LikedCountDTO dto = new LikedCountDTO(key, (Integer) map.getValue());//用户的id 和 点赞数量
                list.add(dto);
            }
            return list;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


}
