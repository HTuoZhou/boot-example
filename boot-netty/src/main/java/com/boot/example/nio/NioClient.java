package com.boot.example.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author TuoZhou
 */
public class NioClient {

    public static void main(String[] args) throws Exception {
        // 创建SocketChannel -> Socket
        SocketChannel socketChannel = SocketChannel.open();
        // 设置为非阻塞
        socketChannel.configureBlocking(false);

        // 连接服务器端
        if (!socketChannel.connect(new InetSocketAddress("127.0.0.1", 6666))) {
            while (!socketChannel.finishConnect()) {
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其它工作...");
            }
        }

        // 如果连接成功，就发送数据
        String str = "hello,尚硅谷";
        ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());
        System.out.println("客户端发送数据：" + str);
        // 发送数据，将byteBuffer写入到socketChannel
        socketChannel.write(byteBuffer);
        System.in.read();
    }

}
