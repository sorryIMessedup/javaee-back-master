package com.example.j5ee.mapper;

import com.example.j5ee.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
* @author lenovo
* @description 针对表【comment】的数据库操作Mapper
* @createDate 2022-12-17 00:29:55
* @Entity com.example.j5ee.entity.Comment
*/
public interface CommentMapper extends BaseMapper<Comment> {
    @Select("select * from comment where paperId=#{paperId}")
    List<Comment> getAllByPaper(Integer paperId);

    @Select("select * from comment where commentId=#{commentId}")
    Comment selectComment(String commentId);

    @Delete("delete from comment where root=#{root}")
    void deleteCommentByRoot(String root);

    @Select("select * from comment where parentCommentId=#{id}")
    List<Comment> selectCommentsByParent(String id);

    @Update("update comment set parentCommentId=null where commentId=#{commentId}")
    void updateNull(String commentId);

    @Update("update comment set content=#{content} where commentId=#{commentId}")
    void updateContent(String commentId,String content);
}




