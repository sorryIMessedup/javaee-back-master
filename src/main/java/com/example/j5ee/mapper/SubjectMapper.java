package com.example.j5ee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.j5ee.entity.Paper;
import com.example.j5ee.entity.Subject;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;

/**
* @author lenovo
* @description 针对表【subject】的数据库操作Mapper
* @createDate 2022-11-18 19:20:19
* @Entity com.example.j5ee.entity.Subject
*/
@Mapper
@Component
public interface SubjectMapper extends BaseMapper<Subject> {

    @Select("select\n" +
            "    sum(case month(endDate) when '1'  then 1 else 0 end) as Jan,\n" +
            "    sum(case month(endDate) when '2'  then 1 else 0 end) as Feb,\n" +
            "    sum(case month(endDate) when '3'  then 1 else 0 end) as Mar,\n" +
            "    sum(case month(endDate) when '4'  then 1 else 0 end) as Apr,\n" +
            "    sum(case month(endDate) when '5'  then 1 else 0 end) as May,\n" +
            "    sum(case month(endDate) when '6'  then 1 else 0 end) as June,\n" +
            "    sum(case month(endDate) when '7'  then 1 else 0 end) as July,\n" +
            "    sum(case month(endDate) when '8'  then 1 else 0 end) as Aug,\n" +
            "    sum(case month(endDate) when '9'  then 1 else 0 end) as Sept,\n" +
            "    sum(case month(endDate) when '10' then 1  else 0 end) as Oct,\n" +
            "    sum(case month(endDate) when '11' then 1  else 0 end) as Nov,\n" +
            "    sum(case month(endDate) when '12' then 1  else 0 end) as Dece\n" +
            "from subject\n" +
            "where year(endDate)=#{year};")
    LinkedHashMap<String,Integer> getPapersByMonth(@Param("year")int year);

    @Select("SELECT * FROM subject LIMIT #{index},#{size}")
    List<Subject> getAllPage(@Param("index")int index, @Param("size")int size);

    @Select("SELECT * FROM subject WHERE id=#{id} LIMIT #{index},#{size}")
    List<Subject> getPage(@Param("id")int id,@Param("index")int index,@Param("size")int size);
}




