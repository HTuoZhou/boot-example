package com.boot.example.netty.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

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
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyClientHandler());
                        }
                    });

            log.info("客户端准备就绪，绑定端口【{}】", 6666);

            // 启动客户端连接服务端
            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("127.0.0.1", 6666)).sync();

            // 对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

}
