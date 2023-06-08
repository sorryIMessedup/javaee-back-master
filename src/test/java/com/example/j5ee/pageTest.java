package com.example.j5ee;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.j5ee.entity.Paper;
import com.example.j5ee.mapper.PaperMapper;
import com.example.j5ee.service.PaperService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/***
 * @author Urmeas
 * @date 2022/12/6 1:26 下午
 */
@SpringBootTest
public class pageTest {


    @Autowired
    private PaperService paperService;

    @Autowired
    private PaperMapper paperMapper;

    @Test
    public void test(){
        System.out.println(paperService.getPapers(13,1,2));
    }

    @Test
    public void test2(){
        System.out.println(paperService.getAllPapers(1,2));
    }
}
