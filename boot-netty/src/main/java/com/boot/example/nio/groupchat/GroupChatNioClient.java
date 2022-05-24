package com.boot.example.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author TuoZhou
 */
public class GroupChatNioClient {

    public static void main(String[] args) throws Exception {
        // 创建SocketChannel -> Socket
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 6666));
        // 设置为非阻塞
        socketChannel.configureBlocking(false);

        // 创建Selector
        Selector selector = Selector.open();

        // 把socketChannel注册到selector ，关心事件为OP_ACCEPT
        socketChannel.register(selector, SelectionKey.OP_READ);
        System.out.println("客户端准备就绪");

        new Thread() {
            public void run() {
                while (true) {
                    readFromServer(selector);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String writeMsg = scanner.nextLine();
            writeToServer(socketChannel, writeMsg);
        }
    }

    /**
     * 发送消息给服务器端
     */
    private static void writeToServer(SocketChannel socketChannel, String writeMsg) {
        try {
            ByteBuffer byteBuffer = ByteBuffer.wrap(writeMsg.getBytes());
            socketChannel.write(byteBuffer);
            System.out.println("客户端发送消息：" + writeMsg + "给服务器端：" + "/127.0.0.1:6666");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取来自服务器端的消息
     */
    private static void readFromServer(Selector selector) {
        try {
            int select = selector.select();
            if (select > 0) {
                Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
                while (selectionKeyIterator.hasNext()) {
                    SelectionKey selectionKey = selectionKeyIterator.next();

                    if (selectionKey.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                        ByteBuffer byteBuffer = ByteBuffer.allocate(6);
                        int read = socketChannel.read(byteBuffer);
                        if (read > 0) {
                            String readMsg = new String(byteBuffer.array());
                            System.out.println("客户端端读取到服务器端消息：" + readMsg);
                        }
                    }

                    selectionKeyIterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
