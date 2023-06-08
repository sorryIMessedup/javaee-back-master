package com.example.j5ee.service;

import com.example.j5ee.entity.User;

import javax.servlet.http.HttpSession;
import java.util.List;

/***
 * @author Urmeas
 * @date 2022/10/27 9:47 下午
 */
public interface UserService {
    void register(String username,String password,String mail);

    int check(String mail,String code);

    User getUserByUsernameAndPassword(String username, String password);

    User getUserByID(int id);

    User getUserAllInfoById(int id);

    List<User> searchUsersByUsername(String username, HttpSession session);

    boolean deleteUser(int id, HttpSession session);

    User getSafetyUser(User originalUser);

    void updateUser(User user, int loginUserId);

    /**
     * 获取当前登录用户信息
     * @return
     */

    Integer getLoginUserId(HttpSession session);

    boolean isAdmin(HttpSession session);

    boolean isAdminByUserId(int id);
}
