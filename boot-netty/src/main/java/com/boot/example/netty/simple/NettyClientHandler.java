package com.boot.example.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author TuoZhou
 */
@Slf4j
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String msg = "hello，我是客户端";
        log.info("发送消息【{}】给服务端【{}】", msg, ctx.channel().remoteAddress());

        ByteBuf byteBuf = Unpooled.copiedBuffer(msg, StandardCharsets.UTF_8);
        ctx.writeAndFlush(byteBuf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        log.info("读取到服务端【{}】消息【{}】", ctx.channel().remoteAddress(), byteBuf.toString(StandardCharsets.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // log.info("客户端【{}】发生异常【{}】，通道关闭",ctx.channel().localAddress(),cause.getStackTrace());
        // ctx.close();
    }
}
