package com.ruyi.nio.groupchat;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class GroupChatClient {
    //定义属性
    private final String HOST = "127.0.0.1";
    private final int PORT = 6667;
    private Selector selector;
    private SocketChannel sc;
    private String username;

    public GroupChatClient(){
        try {
            selector = Selector.open();
            sc = SocketChannel.open(new InetSocketAddress(HOST, PORT));
            sc.configureBlocking(false);
            sc.register(selector, SelectionKey.OP_READ);
            //得到username
            username = sc.getLocalAddress().toString().substring(1);
            System.out.println(username + "is ok...");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //向服务器发送消息
    public void sendInfo(String info){
        info = username + ":" + info;
        try {
            sc.write(ByteBuffer.wrap(info.getBytes()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //读取从服务器端回复的消息
    public void readInfo(){
        try{
            int select = selector.select();
            if(select > 0){
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while(iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    if(key.isReadable()){
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        socketChannel.read(buffer);
                        String msg = new String(buffer.array());
                        System.out.println(msg.trim());
                    }
                    iterator.remove();
                }
            }else {
                System.out.println("没有可用的通道");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        //启动客户端
        GroupChatClient groupChatClient = new GroupChatClient();

        new Thread(){
            @Override
            public void run() {
                while (true){
                    groupChatClient.readInfo();
                    try {
                        Thread.currentThread().sleep(3000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNextLine()){
            String s = scanner.nextLine();
            groupChatClient.sendInfo(s);
        }

    }

}
