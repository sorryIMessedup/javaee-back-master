package com.example.j5ee.mapper;

import com.example.j5ee.entity.Posting;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PostingMapper {

    @Insert("INSERT INTO posting ( postid,id,pid,status) VALUES (\n" +
            "            #{postid},#{id},#{pid},#{status}\n" +
            "        )")
    int insertLike(Posting posting);

    @Select("SELECT * FROM posting WHERE pid = #{pid} AND status = #{status}")
    List<Posting> getLikedListByPid(@Param("pid")int pid, @Param("status")int status);

    @Select("SELECT * FROM posting WHERE id = #{id} AND status = #{status}")
    List<Posting> getLikedListById(@Param("id")int id, @Param("status")int status);

    @Select(" SELECT * FROM posting WHERE id = #{id} AND pid = #{pid}")
    Posting getByIdAndPid(@Param("id")int id, @Param("pid")int pid);

    @Update(" UPDATE posting SET\n" +
            "            status = #{status}\n" +
            "        WHERE id = #{id} AND pid = #{pid}")
    int updateStatus(@Param("id")int id, @Param("pid")int pid, @Param("status")int status);

}
