package com.ruyi.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class BioServer {
    public static void main(String[] args) throws Exception {
        //思路
        //1、创建一个线程池
        //2、如果有客户连接，就创建一个线程，与之通讯

        //线程池创建
        ExecutorService threadPool =  new ThreadPoolExecutor(2,
                5,
                1,
                TimeUnit.HOURS,
                new ArrayBlockingQueue<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        //创建serverSocket
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动了");

        while(true){
            //监听
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端");

            //创建一个线程与之通讯
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    handler(socket);
                }
            });

        }

    }

    public static void handler(Socket socket){
        try {
            System.out.println("线程信息：id="+Thread.currentThread().getId() + " name=" + Thread.currentThread().getName());
            byte[] bytes = new byte[1024];
            //通过socket获取输入流
            InputStream inputStream = socket.getInputStream();

            //循环的读取客户端发送的数据
            while (true){
                System.out.println("线程信息：id="+Thread.currentThread().getId() + " name=" + Thread.currentThread().getName());
                int read = inputStream.read(bytes);
                if(read != -1){
                    System.out.println(new String(bytes, 0, read));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
