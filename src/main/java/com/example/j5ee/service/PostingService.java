package com.example.j5ee.service;




import com.example.j5ee.entity.Posting;

import java.util.List;

public interface PostingService {
    /**
     * 插入点赞记录
     */
    Integer InsertLike(Posting posting);


    /**
     * 根据被点赞论文的pid查询点赞列表（即查询都谁给这个论文点赞过）
     * @return
     */
    List<Integer> getLikedListByPid(int pid);

    /**
     * 根据点赞人的id查询点赞列表（即查询这个人都给谁点赞过）
     * @return
     */
    List<Integer> getLikedListById(int id);

    /**
     * 通过被点赞人和点赞人id查询是否存在点赞记录
     * @return
     */
    Posting getByIdAndPid(int id, int pid);

    /**
     * 将Redis里的 点赞数据 存入数据库中
     */
    void transLikedFromRedis2DB();

    /**
     * 将Redis中的 点赞数量 存入数据库
     */
    void transLikedCountFromRedis2DB();

    /**
     * 获取此论文被点赞数 ： 数据库中 + redis中（但不进行录入数据库的操作）
     */
    int getLikedCountFromRedis2DBAndSQLByPid(int pid);
}
