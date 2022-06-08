package com.boot.example.netty.protobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * @author TuoZhou
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<NettyMessage.Message> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 随机发送Message1或Message2对象
        int random = new Random().nextInt(3);
        NettyMessage.Message message = null;
        if (random == 0) {
            // 发送Message1对象
            message = NettyMessage.Message.newBuilder()
                    .setMessageTypeEnum(NettyMessage.Message.MessageTypeEnum.message1Type)
                    .setMessage1(NettyMessage.Message1.newBuilder().setId1(1).setName1("姓名").build()).build();
        } else {
            // 发送Message2对象
            message = NettyMessage.Message.newBuilder()
                    .setMessageTypeEnum(NettyMessage.Message.MessageTypeEnum.message2Type)
                    .setMessage2(NettyMessage.Message2.newBuilder().setId2(1).setName2("姓名").build()).build();
        }
        log.info("发送消息【{}】给服务端【{}】", message, ctx.channel().remoteAddress());

        ctx.writeAndFlush(message);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NettyMessage.Message msg) throws Exception {
        log.info("读取到服务端【{}】消息【{}】", ctx.channel().remoteAddress(), msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("客户端【{}】发生异常【{}】", ctx.channel().localAddress(), cause.getStackTrace());
    }
}
