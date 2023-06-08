package com.example.j5ee.controller;

import com.example.j5ee.common.CommonErrorCode;
import com.example.j5ee.common.CommonException;
import com.example.j5ee.entity.User;
import com.example.j5ee.service.UserService;
import com.example.j5ee.util.RegexUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/***
 * @author Urmeas
 * @date 2022/10/27 10:36 下午
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Api("用户Controller")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @ApiOperation(value = "test")
    @GetMapping("/test")
    public Object test(){
        return "test";
    }

    /**
     * 注册
     * @param username
     * @param password
     * @param mail
     * @return
     */
    @GetMapping("/register/{username}/{password}/{mail}")
    @ApiOperation(value = "email注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码(长度6-20)", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "mail", value = "邮箱号", required = true, paramType = "query", dataType = "String"),
    })
    public Object register(@NotBlank @Size(max = 20,min = 3) @PathVariable("username")String username,
                           @NotBlank @PathVariable("password") @Size(min = 6,max = 20) String password,
                           @PathVariable("mail")@Pattern(regexp = RegexUtil.MAIL_NUMBER)String mail){
        userService.register(username, password,mail);
        return "新建用户成功";
    }

    /**
     * 注册时邮箱验证码
     * @param mail
     * @param code
     * @return
     */
    @GetMapping("/check/{mail}/{code}")
    @ApiOperation(value = "注册验证码填写")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mail", value = "邮箱号", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, paramType = "query", dataType = "String")
    })
    public Object check(@PathVariable("mail")String mail,
                        @PathVariable("code")String code){
        return userService.check(mail,code);
    }

    /**
     *登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @GetMapping("/login/{username}/{password}")
    @ApiOperation(value = "登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query", dataType = "String")
    })
    public Object login(@PathVariable("username")String username,
                        @PathVariable("password")String password ,
                        HttpSession session,
                        HttpServletResponse response) {
        User data = userService.getUserByUsernameAndPassword(username,password);
        session.setAttribute("id",data.getId());
        session.setAttribute("username",data.getUsername());
        //test
        log.info("username----->"+getUsernameFromSession(session));
        log.info("id----------->"+getIdFromSession(session));
        return data.getId();
    }


    @RequestMapping("/test_user")
    public Object test_user(HttpSession session){
        String username = getUsernameFromSession(session);
        int id = getIdFromSession(session);
        return userService.getUserByID(id);
    }


    /**
     * 管理员拿到所有用户信息
     * 若username为空  返回所有用户信息；不为空，返回一个用户
     * @param username
     * @param session
     * @return
     */
    @GetMapping("/list_users")
    @ApiOperation(value = "获得所有用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, paramType = "query", dataType = "String"),

    })
    public Object searchUsers(String username, HttpSession session){
        return  userService.searchUsersByUsername(username,session);
    }

    /**
     * 当前用户查看自己的信息  返回的是所有信息（未脱敏，方便后面修改用户信息时上传所有属性
     * @param id
     * @return
     */
    @GetMapping("/list/my/info/{id}")
    public Object listMyUserInfo(@PathVariable("id")int id){
        return userService.getUserAllInfoById(id);
    }

    @GetMapping("/update")
    @ApiOperation(value = "更新用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "用户", required = true, paramType = "query", dataType = "User"),

    })
    public Object updateUser(User user, Integer userId) {
        // 校验参数是否为空
        if (user == null) {
            throw new CommonException(CommonErrorCode.PARAMS_ERROR);
        }
//        if(id==null || (int)id<0){
//            throw new CommonException(CommonErrorCode.INVALID_SESSION);
//        }

        userService.updateUser(user, userId);
        return "更新成功";
    }

    /**
     * 管理员删除用户
     * @param id
     * @param session
     * @return
     */
    @GetMapping("/deleteuser/{id}")
    @ApiOperation(value = "删除用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true, paramType = "query", dataType = "int"),

    })
    public Object deleteUser(@PathVariable("id") int id,HttpSession session){
        if(userService.deleteUser(id,session)){
            return "删除成功";
        }
        return "删除失败";
    }

}

