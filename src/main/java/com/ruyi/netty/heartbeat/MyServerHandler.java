package com.ruyi.netty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

public class MyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent) evt;
        String eventType = null;
        switch (event.state()){
            case READER_IDLE -> {
                eventType = "读空闲";
            }case WRITER_IDLE -> {
                eventType = "写空闲";
            }case ALL_IDLE -> {
                eventType = "读写空闲";
            }

        }
        System.out.println(ctx.channel().remoteAddress() + "----超时处理事件---" + eventType);
        System.out.println("服务器做相应处理");
        ctx.close();

    }
}
