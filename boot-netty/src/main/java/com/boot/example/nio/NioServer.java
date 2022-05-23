package com.boot.example.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author TuoZhou
 */
public class NioServer {

    public static void main(String[] args) throws Exception {
        // 创建ServerSocketChannel -> ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        // 绑定一个端口6666，在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        // 创建Selector
        Selector selector = Selector.open();

        // 把serverSocketChannel注册到selector ，关心事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 循环等待客户端连接
        while (true) {
            // 这里我们等待1s，如果没有事件发生，返回
            if (selector.select(1000) == 0) {
                System.out.println("服务器等待了1s，无连接");
                continue;
            }

            // 如果返回>0，就获取到相关的SelectionKey集合
            // 1、如果返回>0，表示已经获取到关心的事件了
            // 2、selector.selectedKeys()，返回关心事件的集合
            // 3、通过selectionKeys，可以反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            // 遍历selectionKeys，使用迭代器遍历
            Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();
            while (selectionKeyIterator.hasNext()) {
                // 获取到SelectionKey，根据selectionKey对应的通道发生的事件做相应的处理
                SelectionKey selectionKey = selectionKeyIterator.next();

                // 如果是OP_ACCEPT，表示有新的客户端连接
                if (selectionKey.isAcceptable()) {
                    // 为该客户端生成一个SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    // 设置为非阻塞
                    socketChannel.configureBlocking(false);

                    System.out.println("客户端连接成功，socketChannel=" + socketChannel);

                    // 将socketChannel注册到selector，关心事件为OP_READ，同时给socketChannel关联一个Buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(15));
                }

                // 如果是OP_READ，表示读取到客户端发送的数据
                if (selectionKey.isReadable()) {
                    // 通过selectionKey，反向获取到对应的socketChannel
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    // 获取到该socketChannel关联的Buffer
                    ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
                    socketChannel.read(byteBuffer);
                    System.out.println("服务器端读取消息：" + new String(byteBuffer.array()));
                }

                // 手动从集合中移除当前的selectionKey，防止重复操作
                selectionKeyIterator.remove();
            }

        }
    }

}
