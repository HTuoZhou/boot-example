package com.boot.example.deadletter;

import com.boot.example.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author TuoZhou
 */
public class Producer {

    /**
     * 正常交换机名称
     */
    public static final String NORMAL_EXCHANGE_NAME = "normal-direct";

    /**
     * 死信交换机名称
     */
    public static final String DEAD_LETTER_EXCHANGE_NAME = "dead-letter-direct";

    /**
     * 正常队列名称
     */
    public static final String NORMAL_QUEUE_NAME = "normal-routing";

    /**
     * 死信队列名称
     */
    public static final String DEAD_LETTER_QUEUE_NAME = "dead-letter-routing";

    public static void main(String[] args) throws Exception {
        // 获取信道
        Channel channel = RabbitMqUtils.getChannel();

        // 生成交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE_NAME, "direct", true, false, null);
        channel.exchangeDeclare(DEAD_LETTER_EXCHANGE_NAME, "direct", true, false, null);

        // 生成队列
        // 1、队列名称
        // 2、队列里面的消息是否持久化(磁盘) 默认情况消息存储在内存中
        // 3、该队列是否只供一个消费者进行消费 默认情况可供多个消费者进行消费
        // 4、是否自动删除
        // 5、其它参数
        Map<String, Object> map = new HashMap<>(16);
        // 消息TTL过期
        // map.put("x-message-ttl",10000);
        // 队列最大长度
        // map.put("x-max-length",5);
        // 消息被拒
        map.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE_NAME);
        map.put("x-dead-letter-routing-key", "dead-letter");
        channel.queueDeclare(NORMAL_QUEUE_NAME, true, false, false, map);
        channel.queueDeclare(DEAD_LETTER_QUEUE_NAME, true, false, false, null);

        // 队列绑定
        channel.queueBind(NORMAL_QUEUE_NAME, NORMAL_EXCHANGE_NAME, "normal");
        channel.queueBind(DEAD_LETTER_QUEUE_NAME, DEAD_LETTER_EXCHANGE_NAME, "dead-letter");

        // 从控制台接收消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String s = scanner.next();

            // 发送消息
            // 1、交换机名称
            // 2、路由key
            // 3、其它参数
            // AMQP.BasicProperties props = MessageProperties.PERSISTENT_TEXT_PLAIN
            //         .builder()
            //         .expiration("10000").build();
            channel.basicPublish(NORMAL_EXCHANGE_NAME, "normal", MessageProperties.PERSISTENT_TEXT_PLAIN, s.getBytes());

            System.out.println("Producer消息发送成功：" + s);
        }
    }
}
