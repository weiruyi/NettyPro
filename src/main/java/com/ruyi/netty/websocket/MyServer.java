package com.ruyi.netty.websocket;

import com.ruyi.netty.heartbeat.MyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class MyServer {
    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            //基于http协议，使用http协议的编解码器
                            pipeline.addLast(new HttpServerCodec());
                            //以块方式写，添加ChunkdWriter处理器
                            pipeline.addLast(new ChunkedWriteHandler());
                            /**
                             *说明：
                             * 1、http数据传输过程中是分段的，HttpObjectAggregator 可以将多个段聚合起来
                             * 2、这就是为什么当浏览器发送大量数据时就会发出多次http请求
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            /**
                             * 对于websocket数据是以帧的方式传递，webSocketFrame下面有6个子类
                             * 浏览器请求的时候 ws://localhost:6666/hello
                             * WebSocketServerProtocolHandler 核心功能是将http协议升级成ws协议，保持长连接  --通过一个状态码101
                             *
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

                            //自定义handler，处理业务逻辑
                            pipeline.addLast(new MyTextWebsocketFrameHandler());


                        }
                    });
            System.out.println("netty server start......");

            ChannelFuture cf = bootstrap.bind(9999).sync();

            cf.channel().closeFuture().sync();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
