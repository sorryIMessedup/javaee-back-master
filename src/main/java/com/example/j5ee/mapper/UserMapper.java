package com.example.j5ee.mapper;

import com.example.j5ee.MyMapper;
import com.example.j5ee.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/***
 * @author Urmeas
 * @date 2022/10/27 9:51 下午
 */
@Mapper
public interface UserMapper extends MyMapper<User> {

    @Insert("INSERT INTO user ( username,password,mail,code ) VALUES(#{username},#{password},#{mail},#{code} )")
    int insertUser(@Param("username")String username, @Param("password")String password, @Param("mail")String mail, @Param("code")String code);

    @Select("SELECT * FROM user ")
    List<User> getAllUser();

    @Select("SELECT * FROM user WHERE username=#{username}")
    User getUserByUsername(@Param("username")String username);

    @Select("SELECT * FROM user WHERE mail=#{mail}")
    User getUserByMail(@Param("mail")String mail);

    @Select("SELECT * FROM user WHERE id=#{id}")
    User getUserById(@Param("id")long id);

//    @Update("UPDATE user SET img=#{img} where id = #{id}")
//    int updateImgById(@Param("id")int id,@Param("img")String img);

    @Update("UPDATE user SET password=#{password} where id = #{id}")
    int updatePasswordById(@Param("id")int id,@Param("password")String password);

    @Update("UPDATE user SET isDelete=#{isDelete} where id = #{id}")
    int updateIsDeleteById(@Param("id")int id,@Param("isDelete")int isDelete);

    @Update("UPDATE user set username=#{username},password=#{password},mail=#{mail},img=#{img} where id=#{id}")
    int updateInfo(User user);

    @Delete("DELETE FROM user WHERE username=#{username}")
    int deleteUserByUsername(@Param("username")String username);

    @Delete("DELETE FROM user WHERE mail=#{mail}")
    int deleteUserByMail(@Param("mail")String mail);

}
