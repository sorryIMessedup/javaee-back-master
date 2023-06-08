package com.example.j5ee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.j5ee.entity.Comment;

/**
* @author lenovo
* @description 针对表【comment】的数据库操作Service
* @createDate 2022-12-16 16:59:11
*/
public interface CommentService extends IService<Comment> {

    void insert(Comment comment);
    void delete(String commentId);
    void update(String commentId,String content);
    Comment selectComment(String commentId);
}
