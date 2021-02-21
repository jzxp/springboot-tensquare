package com.juzipi.demo.netty;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juzipi.demo.config.ApplicationContextProvider;
import com.juzipi.demo.entity.Result;
import com.juzipi.demo.entity.StatusCode;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class MyWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    //解析 json 数据
    private static ObjectMapper MAPPER = new ObjectMapper();

    //存放 WebSocket 连接 Map，根据用户 id 存放
    public static ConcurrentHashMap<String, Channel> userChannelMap = new ConcurrentHashMap<>();

    //从 spring 容器中获取 rabbitTemplate
    RabbitTemplate rabbitTemplate = ApplicationContextProvider.getApplicationContext().getBean(RabbitTemplate.class);

    //从 spring 容器中获取消息监听器容器,处理订阅消息 sysNotice
    SimpleMessageListenerContainer sysNoticeContainer = (SimpleMessageListenerContainer) ApplicationContextProvider.getApplicationContext().getBean("sysNoticeContainer");

    SimpleMessageListenerContainer userNoticeContainer = (SimpleMessageListenerContainer) ApplicationContextProvider.getApplicationContext().getBean("userNoticeContainer");


    //用户请求 WebSocket 服务端，执行的方法
    //第一次请求的时候需要建立 WebSocket 连接
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        //约定用户第一次请求携带的数据是 userId

        //解析获取用户请求数据
        String json = textWebSocketFrame.text();
        //解析JSON数据，获取用户id
        String userId = MAPPER.readTree(json).get("userId").asText();

        //第一次请求的时候，建立连接 WebSocket 连接
        Channel channel = userChannelMap.get(userId);
        if (channel == null){
            //获取连接
            channel = channelHandlerContext.channel();

            //把连接放到容器中
            userChannelMap.put(userId,channel);
        }


        /**
         * 订阅类消息
         */

        //只用完成新消息提醒即可，只需要获取消息的数量
        //获取rabbitMQ的消息内容并发送给用户
        RabbitAdmin rabbitAdmin = new RabbitAdmin(rabbitTemplate);
        //拼接获取队列名称
        String queueName = "文章-订阅-"+ userId;
        //获取 rabbit 的  properties 容器
        Properties queueProperties = rabbitAdmin.getQueueProperties(queueName);

        //这里是获取消息数量
        int noticeCount = 0;
        //判断Properties 是否不为空
        if (queueProperties != null){
            //如果不为空，获取消息的数量
            noticeCount = (int) queueProperties.get("QUEUE_MESSAGE_COUNT");

        }


        /**
         * 点赞类消息
         */
        //拼接获取队列名称
        String userQueueName = "文章-点赞-"+ userId;
        //获取 rabbit 的  properties 容器
        Properties userQueueProperties = rabbitAdmin.getQueueProperties(userQueueName);

        //这里是获取消息数量
        int userNoticeCount = 0;
        //判断Properties 是否不为空
        if (userQueueProperties != null){
            //如果不为空，获取消息的数量
            userNoticeCount = (int) userQueueProperties.get("QUEUE_MESSAGE_COUNT");

        }

        //封装返回的数据
        HashMap<Object,Object> hashMap = new HashMap<>();
        //订阅类消息数量
        hashMap.put("sysNoticeCount",noticeCount);
        //点赞类消息数量
        hashMap.put("userNoticeCount",userNoticeCount);
        Result result = new Result(true, StatusCode.OK,"查询成功",hashMap);

        //把数据发送给用户
        channel.writeAndFlush(new TextWebSocketFrame(MAPPER.writeValueAsString(result)));

        //把消息从队列里面清空，否则 MQ 消息监听器会再消费一次
        if (noticeCount>0){
            rabbitAdmin.purgeQueue(queueName,true);
        }
        if (userNoticeCount>0){
            rabbitAdmin.purgeQueue(userQueueName,true);
        }

        //为用户的消息通知队列注册监听器，便于用户在线时一旦有消息可以主动推送给用户，不需要用户来请求服务器获取数据
        sysNoticeContainer.addQueueNames(queueName);
        userNoticeContainer.addQueueNames(userQueueName);

    }
}
