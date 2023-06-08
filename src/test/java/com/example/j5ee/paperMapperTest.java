package com.example.j5ee;

import com.example.j5ee.entity.Paper;
import com.example.j5ee.mapper.PaperMapper;
import com.example.j5ee.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class paperMapperTest {

    @Autowired
    private PaperMapper paperMapper;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void test(){
        Paper paper = new Paper();
        paper.setId(13);
        paper.setPid(26);
        paper.setTitle("test123");
        paper.setAuthor("author");
        paperMapper.updateInfo(paper);
    }
}
