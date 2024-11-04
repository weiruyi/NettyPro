package com.ruyi.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * scattering :将数据写入到buffer的时候，可以采用buffer数组，几次写入【分散】
 * gathering： 从buffer读取数据时，可以采用buffer数组，依次读
 */
public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws Exception{
        /**
         * serverSocketChannel 和 socketChannel
         */
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(6666);

        //绑定端口到socket，并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        //创建buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        //等待客户端连接
        SocketChannel socketChannel = serverSocketChannel.accept();
        int messageLength = 8; //假设从客户端接收8个字节
        while(true){
            int byteRead = 0;
            while(byteRead < messageLength){
                long l = socketChannel.read(byteBuffers);
                byteRead += l;
                System.out.println("byteRead="+byteRead);
                //使用流打印，看看当前buffer的position和limit
                Arrays.asList(byteBuffers).stream()
                        .map(byteBuffer -> "position="+ byteBuffer.position()+",limit="+byteBuffer.limit())
                        .forEach(System.out::println);

            }

            //将所有buffer进行flip
            Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.flip());

            //将数据读出显示到客户端
            long byteWrite = 0;
            while(byteWrite < messageLength){
                long w = socketChannel.write(byteBuffers);
                byteWrite += w;
            }

            //将所有buffer进行clear
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.clear());

            System.out.println("byteRead="+byteRead + ",bytewrite="+byteWrite+",messageLength="+messageLength);

        }

    }
}
