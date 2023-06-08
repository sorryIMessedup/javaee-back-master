package com.example.j5ee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.example.j5ee.mapper")
@ServletComponentScan(basePackages = "com.example.j5ee.listener")
@EnableScheduling
public class J5eeApplication {

    public static void main(String[] args) {
        SpringApplication.run(J5eeApplication.class, args);
    }

}
