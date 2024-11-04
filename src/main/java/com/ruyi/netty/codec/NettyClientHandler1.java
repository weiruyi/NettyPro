package com.ruyi.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyClientHandler1 extends ChannelInboundHandlerAdapter {
    //当通道就绪就会触发
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //发送一个Student对象到服务器
        StudentPOJO.Student student = StudentPOJO.Student.newBuilder().setId(3).setName("ruyi").build();
        ctx.writeAndFlush(student);

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //将msg 转成一个ByteBuf
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("server send:" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("server address:" + ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
