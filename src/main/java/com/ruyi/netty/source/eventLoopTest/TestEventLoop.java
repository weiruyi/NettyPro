package com.ruyi.netty.source.eventLoopTest;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;

public class TestEventLoop {
    public static void main(String[] args) {
        EventLoop eventLoop = new NioEventLoopGroup().next();
        eventLoop.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("hello");
            }
        });
    }
}
