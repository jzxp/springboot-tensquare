package com.juzipi.demo.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

public class NettyServer {

    public void start(int port){

        System.out.println("准备启动netty");
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        //用来处理新连接的
        EventLoopGroup boos = new NioEventLoopGroup();
        //用来处理业务逻辑的，读写...
        EventLoopGroup worker = new NioEventLoopGroup();

        serverBootstrap.group(boos,worker)
//                .localAddress(port)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        //请求消息解码器
                        channel.pipeline().addLast(new HttpServerCodec());
                        //将多个消息转换为单一的 request 或者 response 对象
                        channel.pipeline().addLast(new HttpObjectAggregator(65536));
                        //处理 Websocket 的消息事件
                        channel.pipeline().addLast(new WebSocketServerProtocolHandler("/ws"));

                        //创建自己的 WebSocket 处理器,用来编写业务逻辑
                        MyWebSocketHandler myWebSocketHandler = new MyWebSocketHandler();
                        channel.pipeline().addLast(myWebSocketHandler);

                    }
                }).bind(port);

    }

}
