package com.ruyi.nio.zerocopy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NewIOServer {
    public static void main(String[] args) throws IOException {

        InetSocketAddress address = new InetSocketAddress(6666);

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(address);

        //创建buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);

        while (true){
            SocketChannel socketChannel = serverSocketChannel.accept();
            int readCount = 0;
            while(readCount != -1){
                try{
                    readCount = socketChannel.read(byteBuffer);
                } catch (Exception e){
                    e.printStackTrace();
                }
                //buffer 倒带 posotion = 0 , mark作废
                byteBuffer.rewind();

            }
        }

    }
}
