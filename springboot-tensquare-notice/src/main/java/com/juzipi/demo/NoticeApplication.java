package com.juzipi.demo;

import com.juzipi.demo.util.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
//启用Feign组件
@EnableFeignClients
public class NoticeApplication {
    public static void main(String[] args) {
        SpringApplication.run(NoticeApplication.class);
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


