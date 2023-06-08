package com.example.j5ee.controller;

import com.example.j5ee.common.CommonErrorCode;
import com.example.j5ee.common.CommonException;
import com.example.j5ee.entity.Paper;
import com.example.j5ee.entity.User;
import com.example.j5ee.entity.request.PaperRequest;
import com.example.j5ee.entity.request.SubjectUpdateRequest;
import com.example.j5ee.mapper.PaperMapper;
import com.example.j5ee.service.PaperService;
import com.example.j5ee.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

/**
 * 点赞paper、引用（别人论文）、搜索论文、分类、上传下载、图
 */
@Slf4j
@RestController
@RequestMapping("/paper")
@Api("论文Controller")
public class PaperController extends BaseController{
    @Autowired
    private PaperService paperService;

    @Autowired
    private UserService userService;

    @Autowired
    private PaperMapper paperMapper;

    @PostMapping("/insertPaper")
    public Object addPaper(PaperRequest paperRequest, HttpSession session){
        if (paperRequest == null) {
            throw new CommonException(CommonErrorCode.PARAMS_ERROR);
        }
        int id = getIdFromSession(session);
        Paper paper = new Paper();
        BeanUtils.copyProperties(paperRequest, paper);
        paperService.insertPaper(id,paper.getTitle(), paper.getAuthor(), paper.getResource(),
                paper.getType(), paper.getSummary(), paper.getPublishDate(),
                paper.getUploadDate(),null, paper.getLink());
        return "添加成功";
    }


    @ApiOperation(value = "上传论文文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="file",value = "文件",dataTypeClass = MultipartFile.class,required = true),
            @ApiImplicitParam(name ="title",value = "文章标题",dataTypeClass = String.class,required = true),
    })
    @PostMapping("/uploadFile")
    public Object uploadFile(@RequestParam(value = "file") MultipartFile file,
                             @RequestParam(value = "title") String title,
                             @RequestParam(value = "id") int id
    ) throws IOException {
        if(file.isEmpty()){
            throw new CommonException(CommonErrorCode.PARAMS_ERROR);
        }
        int pid = paperService.uploadFile(file,title,id);
        paperService.setUrl(pid);
        return "上传成功";
    }


    @ApiOperation(value = "获取论文")
    @GetMapping("/getPaper/{pid}")
    public Object getPaper(@PathVariable("pid")int pid, HttpSession session){
        int id = getIdFromSession(session);
        return paperService.getPaperByPid(id, pid);
    }



    @GetMapping("/deletePaper/{pid}")
    public Object deletePaper(@PathVariable("pid")int pid, HttpSession session){
        int id = getIdFromSession(session);
        paperService.deletePaper(id,pid);
        return "删除成功";
    }

    @GetMapping("/checkPaperStatus/{pid}")
    public Object checkPaperStatus(@PathVariable("pid")int pid, HttpSession session){
        int id = getIdFromSession(session);
        int status = paperService.checkPaperStatus(id, pid);
        return status;
    }

    @GetMapping("/judgePaper/{pid}/{score}")
    public Object judgePaper(@PathVariable("pid")int pid, @PathVariable("score")int score, HttpSession session){
        int id = getIdFromSession(session);
        paperService.judgePaper(id,pid,score);
        return "设置分数成功";
    }

    @GetMapping("/setPaperStatus/{pid}/{score}/{status}")
    public Object setPaperStatus(@PathVariable("pid")int pid, @PathVariable("score")int score,@PathVariable("status")int status, HttpSession session){
        int id = getIdFromSession(session);
       paperService.setPaperStatus(id,pid,status,score);
        return "评价论文并设置分数成功";
    }


    @GetMapping("/getAllPapers/{index}/{size}")
    public Object getAllPapers(@PathVariable("index")int index,@PathVariable("size")int size){
        return paperService.getAllPapers(index,size);
    }

    @GetMapping("/getPapers/{index}/{size}")
    public Object getPapers(@PathVariable("index")int index,
                            @PathVariable("size")int size,
                            HttpSession session){
        int id = getIdFromSession(session);
        return paperService.getPapers(id,index,size);
    }

    @GetMapping("/getPapers2")
    public Object getPapers2(HttpSession session){
        int id = getIdFromSession(session);
        return paperService.getPapers2(id);
    }

    @GetMapping("/getPapersByMonth/{id}/{year}")
    public Object getPapersByMonth(@PathVariable("id")int id,@PathVariable("year")int year){
        return paperService.getPapersByMonth(id,year);
    }


    /**
     * 管理员拿到所有未审核的论文
     * @param id
     * @return
     */
    @GetMapping("/getPapersNotSet/{id}")
    public Object getPapersNotSet(@PathVariable("id") int id){
        return paperService.getPapersNotSet(id);
    }


    @GetMapping("/updateInfo/{pid}/{title}/{author}/{resource}/{summary}/{link}")
    public Object updateInfo(
            @PathVariable("pid")int pid,
            @PathVariable("title")String title,
            @PathVariable("author")String author,
            @PathVariable("resource")String resource,
            @PathVariable("summary")String summary,
            @PathVariable("link")String link,
            HttpSession session) {
        int id = getIdFromSession(session);
        Paper paper = paperMapper.getPaperByPid(pid);
        paper.setTitle(title);
        paper.setAuthor(author);
        paper.setResource(resource);
        paper.setSummary(summary);
        paper.setLink(link);
        paperService.updateInfo(paper, id);
        return "更新成功";
    }
}
