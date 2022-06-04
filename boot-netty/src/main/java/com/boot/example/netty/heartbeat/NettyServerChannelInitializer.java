package com.boot.example.netty.heartbeat;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author TuoZhou
 */
@Slf4j
public class NettyServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        log.info("服务端SocketChannel【{}】", ch);
        ch.pipeline().addLast(new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS));
        ch.pipeline().addLast(new NettyServerHandler());
    }

}
