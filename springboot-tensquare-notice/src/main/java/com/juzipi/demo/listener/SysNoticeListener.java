package com.juzipi.demo.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juzipi.demo.entity.Result;
import com.juzipi.demo.entity.StatusCode;
import com.juzipi.demo.netty.MyWebSocketHandler;
import com.rabbitmq.client.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

import java.util.HashMap;

public class SysNoticeListener implements ChannelAwareMessageListener {


    private static ObjectMapper MAPPER = new ObjectMapper();


    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        //获取用户 id，通过队列名称获取
        String queueName = message.getMessageProperties().getConsumerQueue();
        String userId = queueName.substring(queueName.lastIndexOf("-") + 1);

        io.netty.channel.Channel webSocketChannel = MyWebSocketHandler.userChannelMap.get(userId);
        //判断用户是否在线
        if (webSocketChannel !=null){
            //如果连接不为空表示用户在线
            //封装返回数据
            HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
            objectObjectHashMap.put("sysNoticeCount",1);
            Result result = new Result(true, StatusCode.OK,"查询成功",objectObjectHashMap);

            //把数据通过 WebSocket 连接主动推送给用户
            webSocketChannel.writeAndFlush(new TextWebSocketFrame(MAPPER.writeValueAsString(result)));

        }

    }

}
