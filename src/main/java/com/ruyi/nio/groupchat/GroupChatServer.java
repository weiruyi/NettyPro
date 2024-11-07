package com.ruyi.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupChatServer {
    //定义属性
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private static final int PORT = 6667;

    //构造器
    public GroupChatServer(){
        try{
            //得到构造器
            selector = Selector.open();
            //初始化 serverSocketChannel
            serverSocketChannel = ServerSocketChannel.open();
            //绑定端口
            serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
            //设置非阻塞模式
            serverSocketChannel.configureBlocking(false);
            //将 serverSocketChannel 注册到 selector
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //监听
    public void listen(){
        try{
            //循环处理
            while (true){
                int count = selector.select(2000);
                if(count > 0){ // 有时间要处理
                    //遍历selectorKey集合
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while(iterator.hasNext()){
                        SelectionKey key = iterator.next();

                        //监听到accept
                         if(key.isAcceptable()){
                            SocketChannel sc = serverSocketChannel.accept();
                            sc.configureBlocking(false);
                            // 注册到selector
                            sc.register(selector, SelectionKey.OP_READ);
                            //tip
                            System.out.println(sc.getRemoteAddress() + "  上线  ");
                        }

                        //监听到read事件
                        if(key.isReadable()){
                            //处理读
                            readData(key);
                        }

                        //当前key删除，防止重复处理
                        iterator.remove();

                    }

                }
//                else{
//                    System.out.println("等待...");
//                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void readData(SelectionKey key){
        //定义一个socketChannel
        SocketChannel channel = null;
        try {
            //得到channel
            channel = (SocketChannel) key.channel();
            //创建buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int read = channel.read(buffer);
            //根据count值做处理
            if(read > 0){
                String msg = new String(buffer.array());
                System.out.println("from client: " + msg.trim());

                //向其他客户端转发消息
                sendInfoToOtherClients(msg, channel);
            }

        }catch (IOException e){
            try{
                System.out.println(channel.getRemoteAddress() + "离线了");
                //取消注册
                key.cancel();
                //关闭通道
                channel.close();
            }catch (IOException e1){
                e1.printStackTrace();
            }
        }

    }

    //转发消息给其他客户
    private void sendInfoToOtherClients(String msg, SocketChannel self) throws IOException{
        System.out.println("服务器转发消息中...");
        //遍历所有注册到selector的socketChannel ,排除self
        for(SelectionKey key : selector.keys()){
            Channel targetChannel = key.channel();
            //排除自己
            if(targetChannel instanceof SocketChannel && targetChannel != self){
                SocketChannel dest = (SocketChannel) targetChannel;
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                dest.write(buffer);
            }
        }


    }

    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }
}
