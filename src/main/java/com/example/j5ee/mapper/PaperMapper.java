package com.example.j5ee.mapper;

import com.example.j5ee.MyMapper;
import com.example.j5ee.entity.Paper;
import org.apache.ibatis.annotations.*;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;

@Mapper
@Component
public interface PaperMapper extends MyMapper<Paper> {

//    @Param("id") int id, @Param("title") String title, @Param("author") String author, @Param("resource") String resource, @Param("type") int type,
//                    @Param("summary") String summary, @Param("publishDate") Date publishDate, @Param("uploadDate") Date uploadDate, @Param("deleteDate") Date deleteDate, @Param("collections") int collections, @Param("likes") int likes
//            , @Param("download") String download, @Param("link") String link, @Param("score") int score, @Param("status") int status
    @Insert("INSERT INTO paper (id,title,author,resource,type,summary,publishDate,uploadDate,deleteDate,collections,likes,download,link,score,status) " +
            "VALUES(#{id},#{title},#{author},#{resource},#{type},#{summary},#{publishDate},#{uploadDate},#{deleteDate},#{collections},#{likes},#{download},#{link},#{score},#{status} )")
    int insertPaper(Paper paper);

    @Select("select * from paper")
    List<Paper> getAllPapers();

    @Select("select * from paper where pid=#{pid}")
    Paper getPaperByPid(@Param("pid")int pid);

    @Select("select  * from paper where id=#{id}")
    List<Paper> getPaperById(@Param("id") int id);

    @Select("select  * from paper where title=#{title}")
    Paper getPaperByTitle(@Param("title") String title);

    @Select("select * from paper where resource=#{resource}")
    List<Paper> getPapersByResource(@Param("resource") String resource);

    @Select("select * from paper where type=#{type}")
    List<Paper> getPapersByType(@Param("type") String type);

    @Select("select * from paper where collections>=#{collections}")
    List<Paper> getPapersByCollections(@Param("collections") int collections);

    @Select("select * from paper where author=#{author}")
    List<Paper> getPapersByAuthor(@Param("author") String author);

    @Select("select * from paper where score >=#{min} and score <= #{max};")
    List<Paper> getPapersByScoreScope(@Param("min") int min, @Param("max") int max);

    @Select("select\n" +
            "    sum(case month(publishDate) when '1'  then 1 else 0 end) as Jan,\n" +
            "    sum(case month(publishDate) when '2'  then 1 else 0 end) as Feb,\n" +
            "    sum(case month(publishDate) when '3'  then 1 else 0 end) as Mar,\n" +
            "    sum(case month(publishDate) when '4'  then 1 else 0 end) as Apr,\n" +
            "    sum(case month(publishDate) when '5'  then 1 else 0 end) as May,\n" +
            "    sum(case month(publishDate) when '6'  then 1 else 0 end) as June,\n" +
            "    sum(case month(publishDate) when '7'  then 1 else 0 end) as July,\n" +
            "    sum(case month(publishDate) when '8'  then 1 else 0 end) as Aug,\n" +
            "    sum(case month(publishDate) when '9'  then 1 else 0 end) as Sept,\n" +
            "    sum(case month(publishDate) when '10' then 1  else 0 end) as Oct,\n" +
            "    sum(case month(publishDate) when '11' then 1  else 0 end) as Nov,\n" +
            "    sum(case month(publishDate) when '12' then 1  else 0 end) as Dece\n" +
            "from paper\n" +
            "where year(publishDate)=#{year};")
    LinkedHashMap<String,Integer> getPapersByMonth(@Param("year")int year);


    @Select("select * from paper where status=0")
    List<Paper> getPapersNotSet();

    @Update("UPDATE paper set title=#{title},author=#{author},resource=#{resource},type=#{type}," +
            "summary=#{summary},publishDate=#{publishDate},uploadDate=#{uploadDate}," +
            "deleteDate=#{deleteDate},collections=#{collections},likes=#{likes},download=#{download}," +
            "link=#{link},score=#{score},status=#{status} WHERE pid=#{pid}")
    int updateInfo(Paper paper);

    @Update("UPDATE paper set download=#{download} WHERE pid=#{pid}")
    int setDownload(@Param("download") String download,@Param("pid") int pid);

    @Update("update paper set status = #{status} where pid = #{pid}")
    int updateStatus(@Param("status")int status,@Param("pid")int pid);

    @Update("update paper set likes = #{likes} where pid = #{pid}")
    int updateLikes(@Param("pid")int pid,@Param("likes")int likes);

    @Delete("delete from paper where title=#{title}")
    int deleteByTitle(@Param("title")String title);

    @Delete("delete from paper where pid=#{pid}")
    int deleteByPid(@Param("pid")int pid);

    @Select("SELECT * FROM paper LIMIT #{index},#{size}")
    List<Paper> getAllPage(@Param("index")int index,@Param("size")int size);

    @Select("SELECT * FROM paper WHERE id=#{id} LIMIT #{index},#{size}")
    List<Paper> getPage(@Param("id")int id,@Param("index")int index,@Param("size")int size);

    @Select("SELECT * FROM paper WHERE id=#{id}")
    List<Paper> getPapers(@Param("id")int id);

}
