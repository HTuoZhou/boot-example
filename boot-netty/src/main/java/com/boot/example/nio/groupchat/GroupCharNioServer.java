package com.boot.example.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Objects;

/**
 * @author TuoZhou
 */
public class GroupCharNioServer {

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
        System.out.println("服务器端准备就绪");

        while (true) {
            int count = selector.select();
            if (count > 0) {
                Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
                while (selectionKeyIterator.hasNext()) {
                    SelectionKey selectionKey = selectionKeyIterator.next();

                    if (selectionKey.isAcceptable()) {
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);

                        socketChannel.register(selector, SelectionKey.OP_READ);
                        System.out.println("客户端：" + socketChannel.getRemoteAddress() + "加入连接");
                    }

                    if (selectionKey.isReadable()) {
                        readFromClient(selector, selectionKey);
                    }

                    selectionKeyIterator.remove();
                }
            }
        }
    }

    /**
     * 读取来自客户端的消息
     */
    private static void readFromClient(Selector selector, SelectionKey selectionKey) {
        SocketChannel socketChannel = null;
        try {
            socketChannel = (SocketChannel) selectionKey.channel();

            ByteBuffer byteBuffer = ByteBuffer.allocate(6);
            int read = socketChannel.read(byteBuffer);
            if (read > 0) {
                String readMsg = new String(byteBuffer.array());
                System.out.println("服务器端读取到客户端消息：" + readMsg);

                writeToClient(selector, socketChannel, readMsg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息给其它客户端
     */
    private static void writeToClient(Selector selector, SocketChannel socketChannel, String writeMsg) {
        try {
            for (SelectionKey selectionKey : selector.keys()) {
                Channel channel = selectionKey.channel();

                if (channel instanceof SocketChannel) {
                    SocketChannel otherSocketChannel = (SocketChannel) channel;
                    if (!Objects.equals(socketChannel, otherSocketChannel)) {
                        ByteBuffer byteBuffer = ByteBuffer.wrap(writeMsg.getBytes());
                        otherSocketChannel.write(byteBuffer);
                        System.out.println("服务器端发送消息：" + writeMsg + "给客户端：" + otherSocketChannel.getRemoteAddress());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
