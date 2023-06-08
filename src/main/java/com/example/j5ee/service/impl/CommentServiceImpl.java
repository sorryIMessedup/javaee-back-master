package com.example.j5ee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.j5ee.entity.Comment;
import com.example.j5ee.mapper.CommentMapper;
import com.example.j5ee.service.CommentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
* @author lenovo
* @description 针对表【comment】的数据库操作Service实现
* @createDate 2022-12-16 16:59:11
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

    @Resource
    private CommentMapper commentMapper;
    @Override
    public void insert(Comment comment) {
        String id = UUID.randomUUID().toString().substring(0,5);
        QueryWrapper<Comment> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("commentId",id);
        while (commentMapper.selectOne(queryWrapper)!=null){
            id = UUID.randomUUID().toString().substring(0,5);
        }
        comment.setDate(new Date(System.currentTimeMillis()));
        comment.setCommentId(id);
        //没有父评论，设置root为id
        if (comment.getParentCommentId()== null){
            comment.setParentCommentId(null);
            comment.setRoot(id);
        }else{
            comment.setRoot(commentMapper.selectComment(comment.getParentCommentId()).getRoot());
        }
        System.out.println(comment);
        commentMapper.insert(comment);
    }

    @Override
    public void delete(String commentId) {
        //是根评论则删除所有评论
        if (commentMapper.selectComment(commentId).getRoot().equals(commentId)){
            commentMapper.deleteCommentByRoot(commentId);
            return;
        }
        List<Comment> comments=commentMapper.selectCommentsByParent(commentId);
        if(comments!=null){
            for(Comment comment:comments){
                commentMapper.deleteById(comment);
            }
        }
        QueryWrapper<Comment> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("comment_id",commentId);
        commentMapper.delete(queryWrapper);
    }

    @Override
    public void update(String commentId, String content) {

        commentMapper.updateContent(commentId,content);
    }

    @Override
    public Comment selectComment(String commentId) {
        return commentMapper.selectComment(commentId);
    }

}




