package com.boot.example.netty.inoutboundhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author TuoZhou
 */
@Slf4j
public class NettyServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        log.info("服务端SocketChannel【{}】", ch);
        ch.pipeline().addLast(new LongToByteEncoder());
        // ch.pipeline().addLast(new ByteToLongDecoder());
        ch.pipeline().addLast(new ByteToLongReplayingDecoder());
        ch.pipeline().addLast(new NettyServerHandler());
    }

}
