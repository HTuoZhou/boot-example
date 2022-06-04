package com.boot.example.netty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author TuoZhou
 */
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端【{}】加入连接", ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("读取到客户端【{}】消息【{}】", ctx.channel().remoteAddress(), msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端【{}】退出连接", ctx.channel().remoteAddress());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            switch (idleStateEvent.state()) {
                case READER_IDLE:
                    log.info("服务端【{}】读空闲，通道关闭", ctx.channel().remoteAddress());
                    ctx.channel().close();
                    break;
                case WRITER_IDLE:
                    log.info("服务端【{}】写空闲，通道关闭", ctx.channel().remoteAddress());
                    ctx.channel().close();
                    break;
                case ALL_IDLE:
                    log.info("服务端【{}】读写空闲，通道关闭", ctx.channel().remoteAddress());
                    ctx.channel().close();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("服务端【{}】发生异常【{}】", ctx.channel().localAddress(), cause.getStackTrace());
    }
}
