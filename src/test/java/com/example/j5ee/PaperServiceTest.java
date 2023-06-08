package com.example.j5ee;

import com.example.j5ee.entity.Paper;
import com.example.j5ee.entity.User;
import com.example.j5ee.service.PaperService;
import com.example.j5ee.service.impl.PaperServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class PaperServiceTest {

    User user  = User.builder().id(1).type(1).build();
    User manager = User.builder().id(2).type(0).build();
    Paper paper1 = Paper.builder().id(1).pid(9).summary("bad").build();
    Paper paper = Paper.builder()
            .id(1).title("aaa").publishDate(new Date()).author("aaa").download("aaa").link("aaa").resource("aaa").link("aaa").summary("aaa").type(1).uploadDate(new Date())
            .pid(9).download("pp").build();
    @Autowired
    private PaperService paperService;
    @Test
    public void paperInsert(){


        //int i = paperService.insertPaper(user, "ti", "tian", "pp", 1, "good", new Date(), new Date(), "p", "pp");

    }
    @Test
    public void paper2(){
        paperService.deletePaper(2,1);
    }
//    @Test
//    public void revise(){
//        paperService.revisePaper();
//    }
    @Test
    public void checkStatue(){
        int i = paperService.checkPaperStatus(2,10);
        System.out.println(i);
    }
//    @Test
//    public void judge(){
//        paperService.judgePaper(manager,paper,87);
//    }
//
    @Test
    public void setStatus(){
      paperService.setPaperStatus(2,10,1,100);
    }
//
//    @Test
//    public void getPapersByScoresTest(){
//        List<Paper> papersByScores = paperService.getPapersByScores(manager, 50, 100);
//        System.out.println(papersByScores);
//    }
//    @Test
//    public void getScopeScorePapers(){
//        Map<Integer, List<Paper>> papersListByScores = paperService.getPapersListByScores(manager, 50, 60);
//        System.out.println(papersListByScores);
//    }
}
