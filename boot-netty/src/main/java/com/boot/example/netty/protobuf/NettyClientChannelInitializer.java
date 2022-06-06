package com.boot.example.netty.protobuf;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author TuoZhou
 */
@Slf4j
public class NettyClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        log.info("客户端SocketChannel【{}】", ch);
        ch.pipeline().addLast(new ProtobufEncoder());
        ch.pipeline().addLast(new ProtobufDecoder(NettyMessage.Message.getDefaultInstance()));
        ch.pipeline().addLast(new NettyClientHandler());
    }

}
