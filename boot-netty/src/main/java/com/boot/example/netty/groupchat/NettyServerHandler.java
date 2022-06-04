package com.boot.example.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author TuoZhou
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 定义一个channelGroup，管理所有的客户端
     * GlobalEventExecutor.INSTANCE,全局事件处理器，是一个单例
     */
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端【{}】加入连接", ctx.channel().remoteAddress());

        // 该方法会遍历channelGroup中已经存在的所有channel,并将该客户端加入连接的信息推送给其它相应的客户端
        channelGroup.writeAndFlush("客户端：" + ctx.channel().remoteAddress() + "加入连接");
        channelGroup.add(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.info("读取到客户端【{}】消息【{}】", ctx.channel().remoteAddress(), msg);

        // 遍历channelGroup，转发消息给其它相应的客户端
        for (Channel channel : channelGroup) {
            if (!Objects.equals(ctx.channel(), channel)) {
                channel.writeAndFlush("客户端：" + channel.remoteAddress() + "发送了消息" + msg);
            } else {
                channel.writeAndFlush("自己：" + channel.remoteAddress() + "发送了消息" + msg);
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端【{}】退出连接", ctx.channel().remoteAddress());

        // 不需要调用 netty内部默认会主动进行移除
        // channelGroup.remove(ctx.channel());

        // 该方法会遍历channelGroup中已经存在的所有channel,并将该客户端加入连接的信息推送给相应的channel
        channelGroup.writeAndFlush("客户端：" + ctx.channel().remoteAddress() + "退出连接");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("服务端【{}】发生异常【{}】", ctx.channel().localAddress(), cause.getStackTrace());
    }
}
