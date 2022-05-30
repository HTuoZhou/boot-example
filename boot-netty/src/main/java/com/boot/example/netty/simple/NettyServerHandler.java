package com.boot.example.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

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
        ByteBuf byteBuf = (ByteBuf) msg;
        log.info("读取到客户端【{}】消息【{}】", ctx.channel().remoteAddress(), byteBuf.toString(StandardCharsets.UTF_8));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 比如我们这里有一个非常耗时的业务
        // Thread.sleep(10 * 1000);
        //
        // String msg = "hello，我是服务端";
        // log.info("发送消息【{}】给客户端【{}】", msg, ctx.channel().remoteAddress());
        //
        // ByteBuf byteBuf = Unpooled.copiedBuffer(msg, StandardCharsets.UTF_8);
        // ctx.writeAndFlush(byteBuf);

        // 解决方案1 用户程序自定义的普通任务 提交到该channel对应的NioEventLoop的taskQueue中
        // 分别在10秒之后 和 30(10 + 20)秒之后执行
        // ctx.channel().eventLoop().execute(() -> {
        //     try {
        //         Thread.sleep(10 * 1000);
        //     } catch (InterruptedException e) {
        //         e.printStackTrace();
        //     }
        //
        //     String msg = "hello，我是服务端";
        //     log.info("发送消息【{}】给客户端【{}】", msg, ctx.channel().remoteAddress());
        //
        //     ByteBuf byteBuf = Unpooled.copiedBuffer(msg, StandardCharsets.UTF_8);
        //     ctx.writeAndFlush(byteBuf);
        // });
        //
        // ctx.channel().eventLoop().execute(() -> {
        //     try {
        //         Thread.sleep(20 * 1000);
        //     } catch (InterruptedException e) {
        //         e.printStackTrace();
        //     }
        //
        //     String msg = "hello，我是服务端";
        //     log.info("发送消息【{}】给客户端【{}】", msg, ctx.channel().remoteAddress());
        //
        //     ByteBuf byteBuf = Unpooled.copiedBuffer(msg, StandardCharsets.UTF_8);
        //     ctx.writeAndFlush(byteBuf);
        // });

        // 解决方案2 用户程序自定义的定时任务 提交到该channel对应的NioEventLoop的scheduleTaskQueue中
        // 分别在10秒之后 和 20秒之后执行
        // ctx.channel().eventLoop().schedule(() -> {
        //     String msg = "hello，我是服务端";
        //     log.info("发送消息【{}】给客户端【{}】", msg, ctx.channel().remoteAddress());
        //
        //     ByteBuf byteBuf = Unpooled.copiedBuffer(msg, StandardCharsets.UTF_8);
        //     ctx.writeAndFlush(byteBuf);
        // },10, TimeUnit.SECONDS);
        //
        // ctx.channel().eventLoop().schedule(() -> {
        //     String msg = "hello，我是服务端";
        //     log.info("发送消息【{}】给客户端【{}】", msg, ctx.channel().remoteAddress());
        //
        //     ByteBuf byteBuf = Unpooled.copiedBuffer(msg, StandardCharsets.UTF_8);
        //     ctx.writeAndFlush(byteBuf);
        // },20, TimeUnit.SECONDS);

        // 解决方案3 非当前Reactor线程调用Channel的各种方法

        log.info("go on");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端【{}】退出连接", ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // log.info("服务端【{}】发生异常【{}】，通道关闭",ctx.channel().localAddress(),cause.getStackTrace());
        // ctx.close();
    }
}
