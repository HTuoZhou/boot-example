package com.boot.example.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author TuoZhou
 */
public class BioServer {

    public static void main(String[] args) throws Exception {
        // 线程池机制

        // 思路
        // 1、创建一个线程池
        // 2、如果有客户端连接，就创建一个线程，与之通讯(单独写一个方法)

        ExecutorService executorService = Executors.newCachedThreadPool();

        // 创建ServerSocket
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务端启动成功");

        while (true) {
            // 监听，等待客户端连接
            System.out.println("等待客户端连接");
            final Socket socket = serverSocket.accept();
            System.out.println("客户端连接成功，socket=" + socket);

            // 创建一个线程，与之通讯(单独写一个方法)
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    // 可以和客户端通讯
                    handler(socket);
                }
            });
        }
    }

    // 编写一个handler方法，和客户端通讯
    public static void handler(Socket socket) {
        try {
            System.out.println("线程信息：id=" + Thread.currentThread().getId() +
                    " name=" + Thread.currentThread().getName());
            byte[] bytes = new byte[1024];
            // 通过socket，获取输入流
            InputStream inputStream = socket.getInputStream();

            // 循环读取客户端发送的数据
            while (true) {
                System.out.println("读取客户端发送的数据");
                int read = inputStream.read(bytes);
                if (read != -1) {
                    // 输出客户端发送的数据
                    System.out.println(new String(bytes, 0, read));
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("客户端连接断开");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
