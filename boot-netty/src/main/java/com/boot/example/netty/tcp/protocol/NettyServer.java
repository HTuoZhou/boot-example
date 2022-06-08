package com.boot.example.netty.tcp.protocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author TuoZhou
 */
@Slf4j
public class NettyServer {

    public static void main(String[] args) throws Exception {
        // 创建bossGroup和workerGroup
        // 说明
        // 1、创建两个线程组bossGroup和workerGroup
        // 2、bossGroup只是处理连接请求，真正和客户端的业务处理，会交给workerGroup完成
        // 3、两个都是无限循环
        // 4、bossGroup和workerGroup含有的子线程(NioEventLoop)个数默认是 CPU核数*2
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 创建服务端的启动对象，配置参数
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            // 使用链式编程来进行设置
            // 设置两个线程组
            serverBootstrap.group(bossGroup, workerGroup)
                    // 使用NioServerSocketChannel作为服务端的通道
                    .channel(NioServerSocketChannel.class)
                    // 设置线程队列等待连接的个数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 设置保持活动连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 给我们的workerGroup的NioEventLoop对应的pipeline设置处理器
                    .childHandler(new NettyServerChannelInitializer());

            log.info("服务端准备就绪,绑定端口【{}】", 8888);

            // 启动服务端并绑定端口
            ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress("127.0.0.1", 8888)).sync();

            // 给channelFuture注册监听器，监听我们关心得事件
            channelFuture.addListener((ChannelFutureListener) future -> {
                if (channelFuture.isSuccess()) {
                    log.info("服务端监听端口【{}】成功，服务端地址【{}】", 8888, channelFuture.channel().localAddress().toString());
                }
            });

            // 对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
