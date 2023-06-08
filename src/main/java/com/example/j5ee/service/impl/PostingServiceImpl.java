package com.example.j5ee.service.impl;


import com.example.j5ee.entity.Paper;
import com.example.j5ee.entity.Posting;
import com.example.j5ee.entity.dto.LikedCountDTO;
import com.example.j5ee.mapper.PaperMapper;
import com.example.j5ee.mapper.PostingMapper;
import com.example.j5ee.service.PostingService;
import com.example.j5ee.service.RedisService;
import com.example.j5ee.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PostingServiceImpl implements PostingService {
    @Autowired
    private RedisService redisService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostingMapper postingMapper;

    @Autowired
    private PaperMapper paperMapper;


    @Override
    public Integer InsertLike(Posting posting) {
        return postingMapper.insertLike(posting);
    }

    @Override
    public List<Integer> getLikedListByPid(int pid) {
        List<Posting> postingList = postingMapper.getLikedListByPid(pid,1);
        ArrayList<Integer> ids = new ArrayList<>();
        for(Posting p : postingList){
            ids.add(p.getId());
        }
        return ids;
    }

    @Override
    public List<Integer> getLikedListById(int id) {
        List<Posting> postingList = postingMapper.getLikedListById(id,1);
        ArrayList<Integer> pids = new ArrayList<>();
        for(Posting p : postingList){
            pids.add(p.getPostid());
        }
        return pids;
    }

    @Override
    public Posting getByIdAndPid(int id, int pid) {
        return postingMapper.getByIdAndPid(id,pid);
    }

    @Override
    public void transLikedFromRedis2DB() {
        List<Posting> list = redisService.getLikedDataFromRedis();//redis 中的 list
        log.info("LikeList_before--->{}",list);
        if(list!=null){
            for (Posting p : list) {
                Posting ul = postingMapper.getByIdAndPid(p.getId(),p.getPid());
                if (ul == null){
                    //没有记录，直接存入
                    postingMapper.insertLike(p);
                }else{
                    //有记录，需要更新
                    ul.setStatus(p.getStatus());
                    postingMapper.updateStatus(ul.getId(),ul.getPid(),ul.getStatus());
                }
            }
        }
        list = redisService.getLikedDataFromRedis();
        log.info("LikeList_after--->{}",list);
    }

    @Override
    public void transLikedCountFromRedis2DB() {
        List<LikedCountDTO> list = redisService.getLikedCountFromRedis();
        //log.info("CountList--->{}",list);
        if(list!=null){
            for (LikedCountDTO dto : list) {
                Paper paper = paperMapper.getPaperByPid(Integer.parseInt(dto.getKey()));
                //点赞数量属于无关紧要的操作，出错无需抛异常
                if (paper != null){
                    int likes = paper.getLikes() + dto.getCount();
                    paper.setLikes(likes);
                    //更新点赞数量
                    paperMapper.updateLikes(paper.getPid(),paper.getLikes());
                    redisService.deleteLikedCountFromRedis(paper.getPid());
                }
            }
        }
    }

    @Override
    public int getLikedCountFromRedis2DBAndSQLByPid(int pid){
        List<LikedCountDTO> list = redisService.getLikedCountFromRedisNotDel();
        if(list!=null){
            for (LikedCountDTO dto : list) {
                int old_pid = Integer.parseInt(dto.getKey());
                Paper paper = paperMapper.getPaperByPid(old_pid);
                //点赞数量属于无关紧要的操作，出错无需抛异常
                if (paper != null && old_pid==pid){//user 不是null ，并且此用户是所查用户
                    int likes = paper.getLikes() + dto.getCount();
                    return likes;
                }
            }
        }
        Paper paper = paperMapper.getPaperByPid(pid);
        return paper.getLikes();
    }

}
