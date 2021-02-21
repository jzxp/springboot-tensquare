package com.juzipi.demo.config;


import com.juzipi.demo.listener.SysNoticeListener;
import com.juzipi.demo.listener.UserNoticeListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean("sysNoticeContainer")
    public SimpleMessageListenerContainer creatSys(ConnectionFactory connectionFactory){
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(connectionFactory);

        //使用 channel 通道监听
        simpleMessageListenerContainer.setExposeListenerChannel(true);
        //设置自己编写的监听器
        simpleMessageListenerContainer.setMessageListener(new SysNoticeListener());

        return simpleMessageListenerContainer;

    }


    @Bean("userNoticeContainer")
    public SimpleMessageListenerContainer creatUser(ConnectionFactory connectionFactory){
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(connectionFactory);

        //使用 channel 通道监听
        simpleMessageListenerContainer.setExposeListenerChannel(true);
        //设置自己编写的监听器
        simpleMessageListenerContainer.setMessageListener(new UserNoticeListener());

        return simpleMessageListenerContainer;

    }

}
