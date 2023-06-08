package com.example.j5ee.service.impl;

import com.example.j5ee.common.CommonErrorCode;
import com.example.j5ee.common.CommonException;
import com.example.j5ee.entity.User;
import com.example.j5ee.mapper.UserMapper;
import com.example.j5ee.service.UserService;
import com.example.j5ee.util.MessageUtil;
import com.example.j5ee.util.PasswordUtil;
import com.example.j5ee.util.RandomVerifyCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/***
 * @author Urmeas
 * @date 2022/10/27 9:48 下午
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Value("${spring.mail.username}")
    private String from;

    @Resource
    private MailSender mailSender;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void register(String username, String password,String mail) {
        //用户查重
        List<User> list = userMapper.getAllUser();
        for(User user : list){
            if(user.getMail().equals(mail)){
                throw new CommonException(CommonErrorCode.MAIL_USED_ERROR);
            }
            if(user.getUsername().equals(username)){
                throw new CommonException(CommonErrorCode.REGISTER_ALREADY_EXIST);
            }
        }
        if (userMapper.getUserByUsername(username) != null)
            throw new CommonException(CommonErrorCode.REGISTER_ALREADY_EXIST);
        //处理密码
        String MD5password = PasswordUtil.convert(password);
        //验证码
           String code = RandomVerifyCodeUtil.getRandomVerifyCode();
        //注册
        if (userMapper.insertUser(username,MD5password,mail,code) != 1) {
            throw new CommonException(CommonErrorCode.SYSTEM_INTERNAL_ANOMALY);
        }
        //处理邮箱注册
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(mail);
            message.setSubject("【sharkUU】邮箱验证码");
            message.setText(MessageUtil.register(mail,code));
            mailSender.send(message);
            log.info("mail 发送完毕");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(CommonErrorCode.MAIL_USED_ERROR);
        }
    }

    @Override
    public int check(String mail, String code) {
        User user = userMapper.getUserByMail(mail);
        if(user==null){
            userMapper.deleteUserByMail(mail);
            throw new CommonException(CommonErrorCode.SYSTEM_INTERNAL_ANOMALY);
        }
        if(!code.equals(user.getCode())){
            userMapper.deleteUserByMail(mail);
            throw new CommonException(CommonErrorCode.VERIFY_FAILED);
        }
        return user.getId();
    }

    @Override
    public User getUserByUsernameAndPassword(String username, String password) {
        User user = userMapper.getUserByUsername(username);
        if(user==null){
            throw new CommonException(CommonErrorCode.USER_NOT_EXIST);
        }
        String MD5password = PasswordUtil.convert(password);
        if(!user.getPassword().equals(MD5password)){
            throw new CommonException(CommonErrorCode.LOGIN_FAILED);
        }
        return user;
    }

    @Override
    public User getUserByID(int id) {
        User userById = userMapper.getUserById(id);
        if(userById==null) throw new CommonException(CommonErrorCode.USER_NOT_EXIST);
        User safetyUser = getSafetyUser(userById);
        return safetyUser;
    }

    @Override
    public User getUserAllInfoById(int id){
        User userById = userMapper.getUserById(id);
        if(userById==null) throw new CommonException(CommonErrorCode.USER_NOT_EXIST);
        return userById;
    }

    /**
     * 拿到所有用户信息
     * @param username
     * @param session
     * @return
     */
    @Override
    public List<User> searchUsersByUsername(String username, HttpSession session){
        List<User> allUsers = new ArrayList<>();
        if(!isAdmin(session)){
            throw new CommonException(CommonErrorCode.USER_PERMISSION_DENIED);
        }
        //如果未指定username  那么返回所有的用户
        if(username==null){
           allUsers = userMapper.getAllUser();
            return allUsers.stream().map(user -> getSafetyUser(user)).collect(Collectors.toList());

        }
        //如果指定username  就返回一个用户
        User userByUsername = userMapper.getUserByUsername(username);
        //脱敏
        User safetyUser = getSafetyUser(userByUsername);

        allUsers.add(safetyUser);
        return allUsers;

    }

    /**
     * 删除用户
     * @param id
     * @param session
     * @return
     */
    @Override
    public boolean deleteUser(int id, HttpSession session){
        //1、鉴权
        if(!isAdmin(session)){
            throw new CommonException(CommonErrorCode.USER_PERMISSION_DENIED);
        }

        if(id<0){
            throw new CommonException(CommonErrorCode.PARAMS_ERROR);
        }
        int i = userMapper.updateIsDeleteById(id, 1);
        if(i==1)return true;
        return false;

    }

    /**
     * 脱敏
     * @param originalUser
     * @return
     */
    @Override
    public User getSafetyUser(User originalUser){
        if(originalUser==null){
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originalUser.getId());
        safetyUser.setUsername(originalUser.getUsername());
        safetyUser.setMail(originalUser.getMail());
        safetyUser.setImg(originalUser.getImg());
        safetyUser.setType(originalUser.getType());


        return safetyUser;
    }

    /**
     * 更新用户信息
     * @param user
     * @param loginUserId
     * @return
     */
    @Override
    public void updateUser(User user, int loginUserId){
        int id = user.getId();
        if(id<0)throw new CommonException(CommonErrorCode.PARAMS_ERROR);

        //鉴权处理
        if(!isAdminByUserId(loginUserId) && id !=loginUserId){
            throw new CommonException(CommonErrorCode.USER_PERMISSION_DENIED);
        }

        User userById = userMapper.getUserById(id);
        if(userById==null)
            throw new CommonException(CommonErrorCode.USER_NOT_EXIST);


        if(userMapper.updateInfo(user)!=1){
            throw new CommonException(CommonErrorCode.USER_UPDATE_FAILED);
        }
    }

    /**
     * 获取当前登录用户的id
     * @param session
     * @return
     */
    @Override
    public Integer getLoginUserId(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object id = session.getAttribute("id");

        if(id==null || (int)id<0){
            throw new CommonException(CommonErrorCode.INVALID_SESSION);
        }
        return (Integer) id;
    }



    /**
     * 鉴权1  传入session鉴权
     * @param session
     * @return
     */
    @Override
    public boolean isAdmin(HttpSession session){
        Object userId = session.getAttribute("id");
        int id = (int) userId;
        User userByID = getUserByID(id);
        if(userByID.getType()!=0){
            return false;
        }
        return true;
    }
    /**
     * 鉴权2  传入User鉴权
     * @param
     * @return
     */
    @Override
    public boolean isAdminByUserId(int id){

        User userById = userMapper.getUserById(id);
        int type = userById.getType();
        if(type!=0){
            return false;
        }
        return true;
    }
}
