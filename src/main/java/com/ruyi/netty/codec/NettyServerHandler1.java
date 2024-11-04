package com.ruyi.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

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
        StudentPOJO.Student student = (StudentPOJO.Student) msg;
        System.out.println("客户端发送的数据，id=" + student.getId() + ",name=" + student.getName());
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