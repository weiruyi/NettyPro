package com.ruyi.nio.zerocopy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NewIOClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 6666));

        String fileName = "/Users/lance/Desktop/Java/NettyPro/src/main/java/com/ruyi/nio/BasicBuffer.java";
        //得到一个文件channel
        FileChannel fileChannel = new FileInputStream(fileName).getChannel();
        //准备发送
        long startTime = System.currentTimeMillis();

        // Linux下一个transferTo方法可以完成传输
        // windows 下一次调用transferTo 只能发送8M文件，需要分段传输文件，而且要注意传输事的位置
        // transferTo底层使用零拷贝
        long transferCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);

        //不使用零拷贝
//        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
//        while(true){
//            byteBuffer.clear();
//            int read = fileChannel.read(byteBuffer);
//            if(read == -1){
//                break;
//            }
//            byteBuffer.flip();
//            socketChannel.write(byteBuffer);
//        }

        System.out.println("耗时："+ (System.currentTimeMillis() - startTime));

        //关闭
        fileChannel.close();

    }
}
