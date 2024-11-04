package com.ruyi.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioFileChannel02 {
    public static void main(String[] args) throws Exception {

        //创建文件的输入流
        File file = new File("/Users/lance/Downloads/file01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        //通过fileInputStream获取对应的fileChannel 实际类型fileChannelImpl
        FileChannel fileChannel = fileInputStream.getChannel();

        //创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        //将通道的数据读入到buffer
        fileChannel.read(byteBuffer);

        System.out.println(new String(byteBuffer.array()));
        fileInputStream.close();
    }
}
