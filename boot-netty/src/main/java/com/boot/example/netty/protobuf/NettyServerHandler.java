package com.boot.example.netty.protobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author TuoZhou
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<NettyMessage.Message> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端【{}】加入连接", ctx.channel().remoteAddress());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NettyMessage.Message msg) throws Exception {
        log.info("读取到客户端【{}】消息【{}】", ctx.channel().remoteAddress(), msg);
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
