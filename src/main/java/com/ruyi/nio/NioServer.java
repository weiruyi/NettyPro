package com.ruyi.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioServer {
    public static void main(String[] args)  throws Exception{

        //创建Serversocketchannel -> ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //得到一个Selector对象
        Selector selector = Selector.open();

        //绑定端口
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        //把serversocketChannel 注册到 selector  关心事件为 OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //循环等待客户端的连接
        while(true){
            if(selector.select(1000) == 0){
                //没有事件发生
                System.out.println("服务器等待了1秒，无连接");
                continue;
            }
            //如果返回的大于0,就获取到相关的selectionKey集合
            //1、如果返回的大于0，表示已经获取到关注的事件
            //2、selector.selectedKeys() 返回关注事件的集合
            // 通过selectionKeys可以反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            //使用迭代器遍历
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while(keyIterator.hasNext()){
                //获取selectionKey
                SelectionKey key = keyIterator.next();
                //根据key对应的通道发生的事件做相应的处理
                if(key.isAcceptable()){//如果是OP_ACCESS 有新的客户端连接
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    System.out.println("client connect successful, socketChannel ID:"+ socketChannel.hashCode());
                    //将socketChannel注册到 selector ，关注事件为OP_READ,同时给socketChannel关联一个buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                }
                if(key.isReadable()){ //OP_READ
                    //通过key反向获取对应channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    //获取该channel关联的buffer
                    ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
                    channel.read(byteBuffer);
                    System.out.println("from client:" + new String(byteBuffer.array()));
                }
                //手动从集合中删除移除selectionKey,防止重复操作
                keyIterator.remove();
            }
        }
    }
}
