package com.example.j5ee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.j5ee.common.PageInfo;
import com.example.j5ee.entity.Paper;
import com.example.j5ee.entity.Subject;
import com.example.j5ee.entity.request.SubjectUpdateRequest;
import com.example.j5ee.entity.vo.SubjectVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;

/**
* @author lenovo
* @description 针对表【subject】的数据库操作Service
* @createDate 2022-11-18 19:20:19
*/
public interface SubjectService extends IService<Subject> {

    @Transactional(rollbackFor = Exception.class)
    int addSubject(Subject subject, int loginUserId);

    boolean updateSubject(SubjectUpdateRequest subjectUpdateRequest, int loginUserId);


    boolean deleteSubject(int sid, int loginUserId);

    List<SubjectVO> listMyUpdateSubject(int loginUserId);

    PageInfo<Subject> getPageInfo(Integer pageNum, Integer pageSize);

    Integer[] getSubjectsByMonth(int id, int year);

    List<Subject> getAllPapers(int index, int size);

    List<Subject> getPapers(int id,int index,int size);
}
