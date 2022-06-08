package com.boot.example.netty.inoutboundhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author TuoZhou
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<Long> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Long sendMsg = 123456L;
        log.info("发送消息【{}】给服务端【{}】", sendMsg, ctx.channel().remoteAddress());
        ctx.writeAndFlush(sendMsg);

        // 这种情况不会走到LongToByteEncoder->encode
        // String sendMsg = "abcdefghijklmnop";
        // log.info("发送消息【{}】给服务端【{}】",sendMsg,ctx.channel().remoteAddress());
        // ctx.writeAndFlush(Unpooled.copiedBuffer(sendMsg, StandardCharsets.UTF_8));
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        log.info("读取到服务端【{}】消息【{}】", ctx.channel().remoteAddress(), msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("客户端【{}】发生异常【{}】", ctx.channel().localAddress(), cause.getStackTrace());
    }
}
