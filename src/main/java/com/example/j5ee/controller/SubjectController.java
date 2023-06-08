package com.example.j5ee.controller;

import com.alibaba.fastjson.JSONArray;
import com.example.j5ee.common.CommonErrorCode;
import com.example.j5ee.common.CommonException;
import com.example.j5ee.common.PageInfo;
import com.example.j5ee.entity.Subject;
import com.example.j5ee.entity.request.SubjectAddRequest;
import com.example.j5ee.entity.request.SubjectDeleteRequest;
import com.example.j5ee.entity.request.SubjectUpdateRequest;
import com.example.j5ee.entity.vo.SubjectVO;
import com.example.j5ee.service.SubjectService;
import com.example.j5ee.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 点赞、评论区
 * 1 项目成员string（点击跳转项目组长展示界面）
 */
@Slf4j
@RestController
@RequestMapping("/subject")
@Api("项目Subject")
public class SubjectController extends BaseController {
    @Resource
    private SubjectService subjectService;

    @Resource
    private UserService userService;

    @PostMapping("/add")
    public Object addSubject(SubjectAddRequest subjectAddRequest, HttpSession session) {
        if (subjectAddRequest == null) {
            throw new CommonException(CommonErrorCode.PARAMS_ERROR);
        }
        Integer loginUserId = userService.getLoginUserId(session);
        Subject subject = new Subject();
        System.out.println("插入的company是" + subjectAddRequest.getCompany());
        BeanUtils.copyProperties(subjectAddRequest, subject);
        System.out.println("插入的company是" + subjectAddRequest.getCompany());
        System.out.println("转换后的company是" + subject.getCompany());
        int i = subjectService.addSubject(subject, loginUserId);
        System.out.println("插入数据库的company是" + subjectService.getById(i).getCompany());
        return "添加成功";
    }

    @GetMapping("/update")
    public Object updateSubject(SubjectUpdateRequest subjectUpdateRequest, Integer id) {
        if (subjectUpdateRequest == null) {
            throw new CommonException(CommonErrorCode.PARAMS_ERROR);
        }

        //Integer loginUserId = userService.getLoginUserId(session);

        boolean b = subjectService.updateSubject(subjectUpdateRequest, id);
        if (!b) {
            throw new CommonException(CommonErrorCode.SUBJECT_UPDATE_ERROR);
        }
        return "更新成功";
    }

    @PostMapping("/delete")
    public Object deleteSubject(SubjectDeleteRequest subjectDeleteRequest, HttpSession session) {
        if (subjectDeleteRequest == null) {
            throw new CommonException(CommonErrorCode.PARAMS_ERROR);
        }
        int sid = subjectDeleteRequest.getSid();
        Integer loginUserId = userService.getLoginUserId(session);

        boolean result = subjectService.deleteSubject(sid, loginUserId);
        if (!result) {
            throw new CommonException(CommonErrorCode.SUBJECT_DELETE_ERROR);
        }
        return "删除成功";
    }

    @GetMapping("/list/my/subject")
    public Object listMyUpdateSubject(HttpSession session) {
        if (session == null) {
            throw new CommonException(CommonErrorCode.PARAMS_ERROR);
        }
        Integer loginUserId = userService.getLoginUserId(session);
        List<SubjectVO> subjectVOList = subjectService.listMyUpdateSubject(loginUserId);
        return subjectVOList;
    }

//    @GetMapping("/list/pages/{pageNmu}/{pageSize}")
//    public Object listSubjectByPages(@RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
//                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
//        PageInfo<Subject> pageInfo = subjectService.getPageInfo(pageNum, pageSize);
//        JSONArray records = pageInfo.getRecords();
//        return pageInfo;
//    }

    @GetMapping("/getAllSubjects/{index}/{size}")
    public Object getAllPapers(@PathVariable("index")int index,@PathVariable("size")int size){
        return subjectService.getAllPapers(index,size);
    }

    @GetMapping("/getSubjects/{index}/{size}")
    public Object getPapers(@PathVariable("index")int index,
                            @PathVariable("size")int size,
                            HttpSession session){
        int id = getIdFromSession(session);
        return subjectService.getPapers(id,index,size);
    }

    @GetMapping("/getSubjectByMonth/{id}/{year}")
    public Object listSubjectByMonth(@PathVariable("id")int id,@PathVariable("year")int year){
        return subjectService.getSubjectsByMonth(id,year);
    }
}



