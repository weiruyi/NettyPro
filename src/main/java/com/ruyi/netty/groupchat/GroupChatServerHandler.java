package com.ruyi.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    //定义一个channel组，管理所有channel
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.forEach(ch -> {
            if(channel != ch){
                ch.writeAndFlush("[客户端：" + channel.remoteAddress() + "]:" + msg + "\n");
            }else {
                ch.writeAndFlush("[自己]:" + msg + "\n");
            }
        });

    }

    //表示连接被建立，一旦连接，第一个被执行
    //将当前channel加入到channelGroup
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将该客户加入聊天的信息推送给所有客户端
        channelGroup.writeAndFlush("[client :" + channel.remoteAddress() + "加入聊天]\n");
        channelGroup.add(channel);
    }

    //客户端离开聊天室
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将该客户加入聊天的信息推送给所有客户端
        channelGroup.writeAndFlush("[client :" + channel.remoteAddress() + "离开聊天]\n");
    }

    //表示channel处于活动状态，提示xxx上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[===上线提醒===]"+ctx.channel().remoteAddress() + "上线");
    }

    //当channel处于非活动状态时，提示xxx下线了
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[===离线提醒===]"+ctx.channel().remoteAddress() + "离线");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
