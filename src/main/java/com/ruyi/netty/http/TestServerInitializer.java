package com.ruyi.netty.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class TestServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        //得到管道
        ChannelPipeline pipeline = ch.pipeline();
        //得到netty提供的httpServerCodec codec -> [coder-decoder]
        pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());
        pipeline.addLast("MyTestHttpServerHandler",new TestHttpServerHandler());

    }
}
