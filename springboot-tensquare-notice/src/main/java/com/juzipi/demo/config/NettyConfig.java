package com.juzipi.demo.config;


import com.juzipi.demo.netty.NettyServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NettyConfig {

    @Bean
    public NettyServer createNettyServer(){
        NettyServer nettyServer = new NettyServer();

        //启动 netty 服务，使用新的线程启动
        new Thread(){
            @Override
            public void run() {
                nettyServer.start(4420);
            }
        }.start();

        return nettyServer;
    }

}
