package com.juzipi.demo.config;

import com.juzipi.demo.util.IdWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CreateIdWorker {

    @Bean
    public IdWorker createIdWorker(){
        return new IdWorker(1,1);
    }
}
