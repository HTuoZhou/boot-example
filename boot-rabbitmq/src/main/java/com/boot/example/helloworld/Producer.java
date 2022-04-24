package com.boot.example.helloworld;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author TuoZhou
 * 生产者：发送消息
 */
public class Producer {

    /**
     * 队列名称
     */
    public static final String QUEUE_NAME = "hello";

    /**
     * 发送消息
     */
    public static final String SEND_MESSAGE = "hello world";

    /**
     * 发送消息
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

        // 生成队列
        // 1、队列名称
        // 2、队列里面的消息是否持久化(磁盘) 默认情况消息存储在内存中
        // 3、该队列是否只供一个消费者进行消费 默认情况可供多个消费者进行消费
        // 4、是否自动删除
        // 5、其它参数
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 发送消息
        // 1、交换机名称
        // 2、路由key
        // 3、其它参数
        // 4、消息体
        channel.basicPublish("", QUEUE_NAME, null, SEND_MESSAGE.getBytes());

        System.out.println("Producer消息发送成功：" + SEND_MESSAGE);
    }

}
