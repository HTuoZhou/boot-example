package com.boot.example.workqueue.manualack;

import com.boot.example.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;

/**
 * @author TuoZhou
 */
public class Producer {

    /**
     * 队列名称
     */
    public static final String QUEUE_NAME = "hello";

    /**
     * 发送消息
     */
    public static void main(String[] args) throws Exception {
        // 获取信道
        Channel channel = RabbitMqUtils.getChannel();

        // 生成队列
        // 1、队列名称
        // 2、队列里面的消息是否持久化(磁盘) 默认情况消息存储在内存中
        // 3、该队列是否只供一个消费者进行消费 默认情况可供多个消费者进行消费
        // 4、是否自动删除
        // 5、其它参数
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        // 从控制台接收消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String s = scanner.next();

            // 发送消息
            // 1、交换机名称
            // 2、路由key
            // 3、其它参数
            // 4、消息体
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, s.getBytes());

            System.out.println("Producer消息发送成功：" + s);
        }
    }

}
