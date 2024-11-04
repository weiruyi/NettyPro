package com.ruyi.nio;

import java.nio.IntBuffer;

public class BasicBuffer {
    public static void main(String[] args) {
        //举例说明Buffer的使用

        //创建一个Buffer，大小为5，寄可以存放5个int
        IntBuffer intBuffer = IntBuffer.allocate(5);

        //向buffer中存放数据
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i * 2);
        }

        //从buffer中读取数据
        //将buffer转换，读写切换(!!!)
        intBuffer.flip();
//        intBuffer.limit(2); //设置缓冲区限制
        while( intBuffer.hasRemaining() ){
            System.out.println(intBuffer.get());
        }

    }
}
