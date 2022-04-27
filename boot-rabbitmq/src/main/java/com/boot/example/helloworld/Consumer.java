package com.boot.example.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author TuoZhou
 * 消费者：接收消息
 */
public class Consumer {

    /**
     * 队列的名称
     */
    public static final String QUEUE_NAME = "hello-world";

    /**
     * 接收消息
     */
    public static void main(String[] args) throws Exception {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 工厂IP
        factory.setHost("localhost");
        // 用户名
        factory.setUsername("guest");
        // 密码
        factory.setPassword("guest");

        // 创建连接
        Connection connection = factory.newConnection();
        // 获取信道
        Channel channel = connection.createChannel();

        // 接收消息
        // 1、队列名称
        // 2、是否自动应答
        // 3、消息传递回调
        // 4、消费者取消消费回调
        channel.basicConsume(QUEUE_NAME, true,
                (consumerTag, message) -> {
                    System.out.println("消息接收成功：" + "\n" +
                            "consumerTag：" + consumerTag + "\n" +
                            "message：" + new String(message.getBody()));
                },
                (consumerTag) -> {
                    System.out.println("消费者取消消费：" + "\n" +
                            "consumerTag：" + consumerTag);
                });
    }

}
