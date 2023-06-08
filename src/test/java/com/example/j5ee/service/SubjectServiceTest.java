package com.example.j5ee.service;
import java.util.Date;
import java.util.List;

import com.example.j5ee.entity.Subject;
import com.example.j5ee.entity.request.SubjectUpdateRequest;
import com.example.j5ee.entity.vo.SubjectVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SubjectServiceTest {

    @Resource
    private SubjectService subjectService;

    @Resource
    private UserService userService;

    @Test
    public void add(){

        Subject subject = new Subject();


        subject.setTitle("python");
        subject.setStartDate(new Date());
        subject.setEndDate(new Date());
        subject.setMoney(10000);
        subject.setFund(20000);
        subject.setCompany("华东师范大学");
        subject.setDownload("https://www.ecnu.edu.cn/");

        int i = subjectService.addSubject(subject, 13);
        System.out.println(subject.getId());
        Assertions.assertEquals(7,i);


    }

    @Test
    public void listMySubject(){
        List<SubjectVO> subjectVOList = subjectService.listMyUpdateSubject(13);
        System.out.println(subjectVOList);

    }

    @Test
    public void delete(){
        boolean b = subjectService.deleteSubject(4, 0);
        Assertions.assertTrue(b);
    }

    @Test
    public void update(){
        SubjectUpdateRequest subjectUpdateRequest = new SubjectUpdateRequest();

        subjectUpdateRequest.setSid(3);
        subjectUpdateRequest.setTitle("c++");
        subjectUpdateRequest.setStartDate(new Date());
        subjectUpdateRequest.setEndDate(new Date());
        subjectUpdateRequest.setMoney(3500);
        subjectUpdateRequest.setFund(4500);
        subjectUpdateRequest.setCompany("华东师范大学");
        subjectUpdateRequest.setDownload("https://www.ecnu.edu.cn/");
        subjectUpdateRequest.setType(2);

        subjectService.updateSubject(subjectUpdateRequest,13);


    }
}