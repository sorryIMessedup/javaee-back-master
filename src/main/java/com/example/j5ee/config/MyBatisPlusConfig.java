package com.example.j5ee.config;

import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/***
 * @author Urmeas
 * @date 2022/12/6 1:32 下午
 */
@Configuration
@EnableTransactionManagement
@MapperScan("com.example.j5ee.mapper")
public class MyBatisPlusConfig {
    @Bean
    public PaginationInnerInterceptor paginationInnerInterceptor(){
        return new PaginationInnerInterceptor();
    }

}
