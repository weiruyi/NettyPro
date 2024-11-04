package com.ruyi.netty.codec;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

public class NettyClientDemo1 {
    public static void main(String[] args) throws InterruptedException {

        //客户端只需要一个事件循环组
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            //客户端启动对象
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("encoder", new ProtobufEncoder());

                            pipeline.addLast(new NettyClientHandler1());
                        }
                    });

            System.out.println("... client is ok...");

            //启动客户端连接服务器端
            ChannelFuture cf = bootstrap.connect("127.0.0.1", 6666).sync();

            cf.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
