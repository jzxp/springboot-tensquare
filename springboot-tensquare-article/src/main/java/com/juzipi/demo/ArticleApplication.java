package com.juzipi.demo;

import com.juzipi.demo.util.IdWorker;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
//配置mapper包扫描
@MapperScan("com.juzipi.demo.mapper")
public class ArticleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArticleApplication.class, args);
    }

    /**
     * 创建 IdWorker 实例
     * @return
     */
    @Bean
    public IdWorker createIdWorker() {
        //参数为：机器编号和序列号
        return new IdWorker(1,1);
    }

}
