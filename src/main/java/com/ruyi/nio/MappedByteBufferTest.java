package com.ruyi.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MappedByteBufferTest {
    /**
     * 说明：
     * 1、MappedByteBuffer 可以让文件直接在内存（堆外内存）修改，操作系统不需要拷贝一次
     */
    public static void main(String[] args) throws Exception {

        RandomAccessFile randomAccessFile = new RandomAccessFile("/Users/lance/Downloads/file01.txt", "rw");
        //获取对应的通道
        FileChannel channel = randomAccessFile.getChannel();

        /**
         * 参数一：FileChannel.MapMode.READ_WRITE 使用的读写模式
         * 参数二：0 可以直接修改的起始位置
         * 参数三： 5 是映射到内存的大小，即可以将多少个字节映射到内存
         * 可以直接修改的内存范围是0-5
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        mappedByteBuffer.put(0, (byte) 'Q');
        mappedByteBuffer.put(4, (byte) 'Q');

        randomAccessFile.close();

    }
}
