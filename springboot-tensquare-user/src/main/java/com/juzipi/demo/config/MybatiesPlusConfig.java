package com.juzipi.demo.config;


import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatiesPlusConfig {

    @Bean
    //分页插件
    public PaginationInterceptor createPaginationInterceptor(){
        return new PaginationInterceptor();
    }



}
