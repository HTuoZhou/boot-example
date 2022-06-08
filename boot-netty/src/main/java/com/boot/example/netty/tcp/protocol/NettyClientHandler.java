package com.boot.example.netty.tcp.protocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author TuoZhou
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            String sendMsg = "hello，我是客户端" + i;
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLength(sendMsg.getBytes(StandardCharsets.UTF_8).length);
            messageProtocol.setContent(sendMsg.getBytes(StandardCharsets.UTF_8));

            log.info("发送消息【{}】给服务端【{}】", sendMsg, ctx.channel().remoteAddress());
            ctx.writeAndFlush(messageProtocol);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        int length = msg.getLength();
        byte[] bytes = msg.getContent();
        String readMsg = new String(bytes, StandardCharsets.UTF_8);
        log.info("读取到服务端【{}】消息【{}】", ctx.channel().remoteAddress(), readMsg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("客户端【{}】发生异常【{}】", ctx.channel().localAddress(), cause.getStackTrace());
    }
}
