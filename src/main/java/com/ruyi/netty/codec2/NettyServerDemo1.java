package com.ruyi.netty.codec2;

import com.ruyi.netty.codec.StudentPOJO;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;

public class NettyServerDemo1 {
    public static void main(String[] args) throws InterruptedException {

        //创建BossGroup 和 Workgroup
        //说明：
        //1、创建两个线程组 bossGroup 和 workerGroup
        //2、 bossGroup 只是处理连接请求，真正和客户端业务处理会交给 workerGroup处理
        //3、两个都是无限循环
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //创建服务器端启动对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boosGroup, workerGroup) //设置两个线程组
                    .channel(NioServerSocketChannel.class) //使用 NioServerSocketChannel 作为服务器通道实现
                    .option(ChannelOption.SO_BACKLOG, 128) //设置线程队列得到的连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) //设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {//创建一个通道初始化对象
                        //给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //ProtobufDecoder 需要指定对哪种对象解码
                            ch.pipeline().addLast("decoder", new ProtobufDecoder(MyDataInfo.Mymessage.getDefaultInstance()));
                            ch.pipeline().addLast(new NettyServerHandler1());

                        }
                    }); //给 WorkerGroup 的 Eventloop 对应的管道设置处理器

            System.out.println("...server is ready...");

            //绑定一个端口并同步,生成一个 ChannelFuture 对象
            //启动服务器
            ChannelFuture cf = bootstrap.bind(6666).sync();

            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if(channelFuture.isSuccess()){
                        System.out.println("监听端口成功");
                    }else {
                        System.out.println("监听端口失败");
                    }
                }
            });

            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } finally {
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}