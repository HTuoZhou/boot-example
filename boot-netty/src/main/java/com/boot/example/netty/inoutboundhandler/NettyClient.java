package com.boot.example.netty.inoutboundhandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * @author TuoZhou
 */
@Slf4j
public class NettyClient {

    public static void main(String[] args) throws Exception {
        // 创建eventLoopGroup
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {
            // 创建客户端的启动对象，配置参数
            // 注意客户端使用的不是ServerBootstrap，而是Bootstrap
            Bootstrap bootstrap = new Bootstrap();

            // 使用链式编程来进行设置
            // 设置eventLoopGroup线程组
            bootstrap.group(eventLoopGroup)
                    // 使用NioSocketChannel作为客户端的通道
                    .channel(NioSocketChannel.class)
                    // 给我们的eventLoopGroup的NioEventLoop对应的pipeline设置处理器
                    .handler(new NettyClientChannelInitializer());

            log.info("客户端准备就绪，连接端口【{}】", 8888);

            // 启动客户端连接服务端
            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("127.0.0.1", 8888)).sync();

            // 给channelFuture注册监听器，监听我们关心得事件
            channelFuture.addListener((ChannelFutureListener) future -> {
                if (channelFuture.isSuccess()) {
                    log.info("客户端连接端口【{}】成功，客户端地址【{}】", 8888, channelFuture.channel().localAddress().toString());
                }
            });

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                Channel channel = channelFuture.channel();
                channel.writeAndFlush(scanner.nextLine());
            }

            // 对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

}
