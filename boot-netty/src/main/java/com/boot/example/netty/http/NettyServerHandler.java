package com.boot.example.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author TuoZhou
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端【{}】加入连接", ctx.channel().remoteAddress());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        // 判断msg是不是HttpRequest请求
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            URI uri = new URI(request.uri());
            if (!Objects.equals("/favicon.ico", uri.getPath())) {
                log.info("读取到客户端【{}】消息【{}】", ctx.channel().remoteAddress(), msg.getClass());

                String sendMsg = "hello，我是服务端";
                ByteBuf byteBuf = Unpooled.copiedBuffer(sendMsg, StandardCharsets.UTF_8);

                DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
                response.headers()
                        .set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8")
                        .set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());

                log.info("发送消息【{}】给客户端【{}】", response, ctx.channel().remoteAddress());
                ctx.writeAndFlush(response);
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端【{}】退出连接", ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("服务端【{}】发生异常【{}】，通道关闭", ctx.channel().localAddress(), cause.getStackTrace());
        ctx.close();
    }
}
