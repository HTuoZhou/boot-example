package com.boot.example.netty.tcp.protocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author TuoZhou
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端【{}】加入连接", ctx.channel().remoteAddress());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        int length = msg.getLength();
        byte[] bytes = msg.getContent();
        String readMsg = new String(bytes, StandardCharsets.UTF_8);
        log.info("读取到客户端【{}】消息【{}】", ctx.channel().remoteAddress(), readMsg);

        String sendMsg = "hello，我是服务端";
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLength(sendMsg.getBytes(StandardCharsets.UTF_8).length);
        messageProtocol.setContent(sendMsg.getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(messageProtocol);
        log.info("发送消息【{}】给客户端【{}】", sendMsg, ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端【{}】退出连接", ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("服务端【{}】发生异常【{}】", ctx.channel().localAddress(), cause.getStackTrace());
    }
}
