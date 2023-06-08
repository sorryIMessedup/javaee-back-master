package com.example.j5ee;

import com.example.j5ee.mapper.PaperMapper;
import com.example.j5ee.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class J5eeApplicationTests {

    @Autowired
    private PaperMapper paperMapper;

    @Autowired
    private UserMapper userMapper;


    @Test
    public void insertTest2(){
        userMapper.insertUser("ss","ss","aa","aa");
    }

}
