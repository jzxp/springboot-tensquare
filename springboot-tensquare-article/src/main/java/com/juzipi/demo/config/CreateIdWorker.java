package com.juzipi.demo.config;

import com.juzipi.demo.util.IdWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CreateIdWorker {


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
