package com.ruyi.netty.codec2;

import com.ruyi.netty.codec.StudentPOJO;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.Random;

public class NettyClientHandler1 extends ChannelInboundHandlerAdapter {
    //当通道就绪就会触发
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //随机发送 Student 或 Worker 对象
        int random = new Random().nextInt(3);
        MyDataInfo.Mymessage myMessage = null;
        if(random == 0){
            myMessage = MyDataInfo.Mymessage.newBuilder()
                    .setDataType(MyDataInfo.Mymessage.DataType.StudentType)
                    .setStudent(MyDataInfo.Student.newBuilder().setId(2).setName("77Q").build())
                    .build();
        }else{
            myMessage = MyDataInfo.Mymessage.newBuilder()
                    .setDataType(MyDataInfo.Mymessage.DataType.WorkerType)
                    .setWorker(MyDataInfo.Worker.newBuilder().setName("worker1").setAge(33).build())
                    .build();
        }
        ctx.writeAndFlush(myMessage);

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
