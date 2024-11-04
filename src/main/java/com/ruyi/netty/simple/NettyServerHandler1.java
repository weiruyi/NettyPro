package com.ruyi.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * 自定义一个Handler，需要继承netty规定好的某个 HandlerAdapter
 */
public class NettyServerHandler1 extends ChannelInboundHandlerAdapter {

    //读取数据事件，可以读取客户端发送的消息
    /**
     * 1、ChannelHandlerContext ctx 上下文对象含有 管道pipeline， 通道channel ，地址
     * 2、Object msg 客户端发过来的数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server ctx = " + ctx );
        //将msg 转成一个ByteBuf
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("client send:" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("client address:" + ctx.channel().remoteAddress());

        //如果有一个耗时非常久的任务，可以异步执行，提交到channel对应的NIOEventloop的taskQueue中
//        Thread.sleep(10 * 1000);
//        ctx.writeAndFlush(Unpooled.copiedBuffer("hello client 222222222222", CharsetUtil.UTF_8));
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello client 222222222222", CharsetUtil.UTF_8));
            }
        });

        //2、用户自定义定时任务->提交到scheduleTaskQueue中
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5 * 1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello client 444444444444", CharsetUtil.UTF_8));
            }
        }, 5 , TimeUnit.SECONDS);

    }

    //数据读取完毕之后
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将数据写入缓存并刷新
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello client", CharsetUtil.UTF_8));
    }

    //处理异常，一般是关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
