package com.example.j5ee.controller;

import com.example.j5ee.entity.Comment;
import com.example.j5ee.mapper.CommentMapper;
import com.example.j5ee.mapper.UserMapper;
import com.example.j5ee.service.CommentService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/comment")
@Api("评论comment")
public class CommonController {

    @Resource
    private CommentService commentService;
    @Resource
    private UserMapper userMapper;
    @Resource
    private CommentMapper commentMapper;
    //comment加root字段
    //获取该文章所有评论,根评论的父亲评论也要传


    @GetMapping("/selectAllComments/{paperId}")
    public Object getAll(@PathVariable Integer paperId){
        JSONArray jsonArray=new JSONArray();

        List<Comment> comments=commentMapper.getAllByPaper(paperId);
        for (Comment comment1:comments){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("commentId",comment1.getCommentId());
            jsonObject.put("content",comment1.getContent());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  // 设置日期格式
            String strTime = simpleDateFormat.format(comment1.getDate());
            jsonObject.put("date",strTime);
            if (comment1.getParentCommentId()==null)
                jsonObject.put("parentCommentId","");
            else
                jsonObject.put("parentCommentId",comment1.getParentCommentId());
            jsonObject.put("userName",userMapper.getUserById(comment1.getUserId()).getUsername());
            jsonObject.put("publisherId",comment1.getUserId());
            jsonObject.put("root",comment1.getRoot());
            if (comment1.getParentCommentId()==null)
                jsonObject.put("parentUserName","");
            else
                jsonObject.put("parentUserName",userMapper.getUserById(commentMapper.selectComment(comment1.getParentCommentId()).getUserId()).getUsername());
            jsonArray.add(jsonObject);
        }

        return jsonArray;
    }

    //删除评论
    @GetMapping("/deleteComment/{commentId}")
    public Object deleteComment(@PathVariable("commentId")String commentId){
        commentService.delete(commentId);
        return "true";
    }
    //更改评论,commentId,content//
    @GetMapping("/updateComment/{commentId}/{content}")
    public Object updateComment(@PathVariable("commentId")String commentId,@PathVariable("content")  String content){

        commentService.update(commentId,content);
        return "true";
    }
    //添加评论,需要parentId,content,userId,id,root//
    @RequestMapping(value = {"/insertComment/{userId}/{paperId}/{content}/{parentCommentId}","/insertComment/{userId}/{paperId}/{content}"})
    //@GetMapping("/insertComment/{userId}/{paperId}/{content}/{parentCommentId}")
    public Object insert(@PathVariable Integer userId,@PathVariable Integer paperId,
                         @PathVariable String content,@PathVariable(required = false) String parentCommentId){
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setPaperId(paperId);
        comment.setContent(content);
        if(parentCommentId!=null){
            comment.setParentCommentId(parentCommentId);
        }
        commentService.insert(comment);
        return "true";
    }


    @GetMapping("/insert")
    public Object insert2(@RequestParam Comment comment){
        commentService.insert(comment);
        return "添加成功";
    }
//    //查询单条评论内容//
//    @GetMapping("/searchComment/{id}")
//    public Object selectOne(@PathVariable String id){
//
//        return commentService.selectComment(id).getContent();
//    }

}
