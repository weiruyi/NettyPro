package com.ruyi.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
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
                            /**
                             * 说明：IdleStateHandler netty 提供的处理空闲状态的处理器
                             * 1、long readerIdleTime,表示多长时间没有读，会发一个心跳检测包检测是否连接
                             * 2、long writerIdleTime,表示多长时间没有读，会发一个心跳检测包检测是否连接
                             * 3、long allIdleTime，表示多长时间没有读写，会发一个心跳检测包检测是否连接
                             * 4、会出发一个 IdleStateEvent,触发后会传递给管道的下一个handler去处理
                             * 通过回调触发下一个handler的userEventTriggered()方法，在该方法中去处理
                             */
                            pipeline.addLast(new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS));
                            //加入一个对空闲检测进一步处理的handler（自定义）
                            pipeline.addLast(new MyServerHandler());

                        }
                    });
            System.out.println("netty server start......");

            ChannelFuture cf = bootstrap.bind(6666).sync();

            cf.channel().closeFuture().sync();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
