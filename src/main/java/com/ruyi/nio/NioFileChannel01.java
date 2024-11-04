package com.ruyi.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioFileChannel01 {
    public static void main(String[] args) throws Exception {
        String str = "hello, channel";

        //创建一个输出流->channel
        FileOutputStream fileOutputStream = new FileOutputStream("/Users/lance/Downloads/file01.txt");
        //通过fileOutputStream获取对应的fileChannel
        //这个fileChannel真实类型是FileChannelImpl
        FileChannel channel = fileOutputStream.getChannel();

        //穿件一个缓冲区 ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //将str放入 bytebuffer
        byteBuffer.put(str.getBytes());

        //对bytebuffer进行flip
        byteBuffer.flip();

        //将bytebuffer数据写入到fileChannel
        channel.write(byteBuffer);
        fileOutputStream.close();

    }
}
