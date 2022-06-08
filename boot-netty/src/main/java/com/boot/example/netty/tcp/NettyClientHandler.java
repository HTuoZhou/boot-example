package com.boot.example.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author TuoZhou
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            String sendMsg = "hello，我是客户端" + i;
            log.info("发送消息【{}】给服务端【{}】", sendMsg, ctx.channel().remoteAddress());
            ctx.writeAndFlush(Unpooled.copiedBuffer(sendMsg, StandardCharsets.UTF_8));
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] bytes = new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        String readMsg = new String(bytes, StandardCharsets.UTF_8);
        log.info("读取到服务端【{}】消息【{}】", ctx.channel().remoteAddress(), readMsg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("客户端【{}】发生异常【{}】", ctx.channel().localAddress(), cause.getStackTrace());
    }
}
