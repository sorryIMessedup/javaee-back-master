package com.example.j5ee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.j5ee.common.CommonErrorCode;
import com.example.j5ee.common.CommonException;
import com.example.j5ee.common.PageInfo;
import com.example.j5ee.entity.Subject;
import com.example.j5ee.entity.User;
import com.example.j5ee.entity.request.SubjectUpdateRequest;
import com.example.j5ee.entity.vo.SubjectVO;
import com.example.j5ee.entity.vo.UserVO;
import com.example.j5ee.mapper.SubjectMapper;
import com.example.j5ee.mapper.UserMapper;
import com.example.j5ee.service.SubjectService;
import com.example.j5ee.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
* @author lenovo
* @description 针对表【subject】的数据库操作Service实现
* @createDate 2022-11-18 19:20:19
*/
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject>
    implements SubjectService{

    @Resource
    private UserService userService;

    @Resource
    private SubjectMapper subjectMapper;

    @Resource
    private UserMapper userMapper;
    /**
     * 添加subject
     * @param subject
     * @param loginUserId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addSubject(Subject subject,int loginUserId){
        if(subject==null)
            throw new CommonException(CommonErrorCode.PARAMS_ERROR);

        if(loginUserId<0){
          throw new CommonException(CommonErrorCode.INVALID_SESSION);
        }


        //3.校验subject每一项的值

        //标题校验
        String title = subject.getTitle();
        if(StringUtils.isBlank(title) || title.length()>30){
            throw new CommonException(CommonErrorCode.SUBJECT_FORMAT_ERROR);
        }

        //项目类型校验
        Integer type = subject.getType();
        if(type==null){
            subject.setType(0);
        }

        //时间校验
        Date startDate = subject.getStartDate();
        Date endDate = subject.getEndDate();
        Date date = new Date();
        if(date.before(startDate) || date.before(endDate)){
            throw new CommonException(CommonErrorCode.SUBJECT_FORMAT_ERROR);
        }

        //金额
        Integer fund = subject.getFund();
        if(fund==null||(int)fund<0){
            throw new CommonException(CommonErrorCode.SUBJECT_FORMAT_ERROR);
        }

        //金额
        Integer money = subject.getMoney();
        if(money==null||(int)money<0){
            throw new CommonException(CommonErrorCode.SUBJECT_FORMAT_ERROR);
        }

        String download = subject.getDownload();
        if(StringUtils.isBlank(download)){
            subject.setDownload("https://sie.ecnu.edu.cn/");
        }
        subject.setCollections(0);
        subject.setLikes(0);
        subject.setIsDelete(0);


        //4.插入
        subject.setSid(null);
        subject.setId(loginUserId);

        boolean result = this.save(subject);
        Integer subjectSid = subject.getSid();
        if(!result || subjectSid==null){
            throw new CommonException(CommonErrorCode.SUBJECT_INSERT_ERROR);
        }

        return subjectSid;
    }

    /**
     * 更新论文信息
     * @param subjectUpdateRequest
     * @param loginUserId
     * @return
     */
    @Override
    public boolean updateSubject(SubjectUpdateRequest subjectUpdateRequest, int loginUserId){
        if(subjectUpdateRequest==null){
            throw new CommonException(CommonErrorCode.PARAMS_ERROR);
        }

        Integer sid = subjectUpdateRequest.getSid();
        if(sid==null || sid<0){
            throw new CommonException(CommonErrorCode.PARAMS_ERROR);
        }

        //获得之前存储的老数据
        Subject oldSubject = this.getById(sid);
        if(oldSubject==null){
            throw new CommonException(CommonErrorCode.SUBJECT_NOT_EXIT);
        }

        //鉴权
        if(oldSubject.getId()!=loginUserId && !userService.isAdminByUserId(loginUserId)){
            throw new CommonException(CommonErrorCode.USER_PERMISSION_DENIED);
        }

        Subject newSub = new Subject();
        BeanUtils.copyProperties(subjectUpdateRequest,newSub);

        return this.updateById(newSub);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSubject(int sid, int loginUserId){
        Subject subject = getSubjectById(sid);
        Integer subjectId = subject.getSid();
        if(subject.getId() !=loginUserId){
            throw new CommonException(CommonErrorCode.USER_PERMISSION_DENIED);
        }

        return  this.removeById(subjectId);
    }


    /**
     * 查找我的项目
     * @param loginUserId
     * @return
     */
    @Override
    public List<SubjectVO> listMyUpdateSubject(int loginUserId){
        QueryWrapper<Subject> subjectQueryWrapper = new QueryWrapper<>();
        subjectQueryWrapper.eq("id",loginUserId);
        List<Subject> list = this.list(subjectQueryWrapper);

        if(CollectionUtils.isEmpty(list)){
            return new ArrayList<>();
        }

        List<SubjectVO> subjectVOList = new ArrayList<>();
        for(Subject subject:list){
            Integer id = subject.getId();
            User userByID = userService.getUserByID(id);
            if(userByID==null){
                continue;
            }

            if(subject.getIsDelete()==1){
                continue;
            }
            SubjectVO subjectVO = new SubjectVO();
            BeanUtils.copyProperties(subject,subjectVO);
            //用户脱敏
            if(userByID!=null){
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(userByID,userVO);
                subjectVO.setUserVO(userVO);
            }

            subjectVOList.add(subjectVO);

        }


       return subjectVOList;
    }


    private Subject getSubjectById(Integer sid){
        if(sid==null ||sid<0){
            throw new CommonException(CommonErrorCode.PARAMS_ERROR);
        }
        Subject subject = this.getById(sid);

        if(subject==null){
            throw new CommonException(CommonErrorCode.SUBJECT_NOT_EXIT);
        }
        return subject;
    }

@Override
    public PageInfo<Subject> getPageInfo(Integer pageNum, Integer pageSize) {
        Page<Subject> subjectVOPage = new Page<>(pageNum, pageSize);
       PageInfo<Subject> pageInfo = new PageInfo<>();
        pageInfo.setPageInfo(baseMapper.selectPage(subjectVOPage, null));
        return pageInfo;
    }

    @Override
    public Integer[] getSubjectsByMonth(int id, int year) {
        User user = userService.getUserByID(id);
        if(user==null){
            throw new CommonException(CommonErrorCode.USER_NOT_EXIST);
        }
        if(user.getType()==1)
            throw new CommonException(CommonErrorCode.USER_PERMISSION_DENIED);
        LinkedHashMap<String, Integer> papersByMonth = subjectMapper.getPapersByMonth(year);
        Integer[] sum = new Integer[12];

        sum[0]=Integer.parseInt(String.valueOf(papersByMonth.get("Jan")));
        sum[1]=Integer.parseInt(String.valueOf(papersByMonth.get("Feb")));
        sum[2]=Integer.parseInt(String.valueOf(papersByMonth.get("Mar")));
        sum[3]=Integer.parseInt(String.valueOf(papersByMonth.get("Apr")));
        sum[4]=Integer.parseInt(String.valueOf(papersByMonth.get("May")));
        sum[5]=Integer.parseInt(String.valueOf(papersByMonth.get("June")));
        sum[6]=Integer.parseInt(String.valueOf(papersByMonth.get("July")));
        sum[7]=Integer.parseInt(String.valueOf(papersByMonth.get("Aug")));
        sum[8]=Integer.parseInt(String.valueOf(papersByMonth.get("Sept")));
        sum[9]=Integer.parseInt(String.valueOf(papersByMonth.get("Oct")));
        sum[10]=Integer.parseInt(String.valueOf(papersByMonth.get("Nov")));
        sum[11]=Integer.parseInt(String.valueOf(papersByMonth.get("Dece")));

        return sum;
        //return subjectMapper.getPapersByMonth(year);
    }

    @Override
    public List<Subject> getAllPapers(int index, int size) {
        return subjectMapper.getAllPage(index,size);
    }

    @Override
    public List<Subject> getPapers(int id, int index, int size) {
        User user = userMapper.getUserById(id);
        if(user==null){
            throw new CommonException(CommonErrorCode.USER_PERMISSION_DENIED);
        }
        return subjectMapper.getPage(id,index,size);
    }


}




