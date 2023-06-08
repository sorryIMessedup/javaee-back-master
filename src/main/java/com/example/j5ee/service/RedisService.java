package com.example.j5ee.service;


import com.example.j5ee.entity.Posting;
import com.example.j5ee.entity.dto.LikedCountDTO;

import java.util.List;

public interface RedisService {
    /**
     * redis 中增加点赞信息
     */
    void doLike(int id,int pid);

    /**
     * redis 中增加取消点赞信息
     */
    void unLike(int id,int pid);

    /**
     * 从Redis中删除一条点赞数据
     */
    void deleteLikedFromRedis(int id,int pid);

    /**
     *从Redis中删除一条数量纪录
     */
    void deleteLikedCountFromRedis(int pid);
    /**
     * 该论文的点赞数加1
     */
    void incrementLikedCount(int pid);

    /**
     * 该论文的点赞数减1
     */
    void decrementLikedCount(int pid);

    /**
     * 获取Redis中存储的所有点赞数据，并且从Redis中删除记录
     */
    List<Posting> getLikedDataFromRedis();

    /**
     * 获取Redis中存储的所有点赞数据
     */
    List<Posting> getLikedDataFromRedisNotDel();

    /**
     * 获取Redis中存储的所有点赞数量，并且从Redis中删除记录
     */
    List<LikedCountDTO> getLikedCountFromRedis();

    /**
     * 获取Redis中存储的所有点赞数量
     */
    List<LikedCountDTO> getLikedCountFromRedisNotDel();

}
