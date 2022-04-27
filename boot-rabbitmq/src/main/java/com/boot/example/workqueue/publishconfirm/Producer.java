package com.boot.example.workqueue.publishconfirm;

import com.boot.example.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author TuoZhou
 */
public class Producer {

    /**
     * 队列名称
     */
    public static final String QUEUE_NAME = "hello";

    /**
     * 消息数量
     */
    public static final Integer SEND_MESSAGE_COUNT = 100;

    /**
     * 批量消息数量
     */
    public static final Integer SEND_MESSAGE_BATCH_COUNT = 10;

    /**
     * 发送消息
     */
    public static final String SEND_MESSAGE = "hello world";

    /**
     * 发送消息
     */
    public static void main(String[] args) throws Exception {

        // 单个发布确认
        // Producer.singlePublishConfirm();

        // 批量发布确认
        // Producer.batchPublishConfirm();

        // 异步发布确认
        Producer.asyncPublishConfirm();

    }

    public static void singlePublishConfirm() throws Exception {
        // 获取信道
        Channel channel = RabbitMqUtils.getChannel();

        // 开启发布确认
        channel.confirmSelect();

        // 生成队列
        // 1、队列名称
        // 2、队列里面的消息是否持久化(磁盘) 默认情况消息存储在内存中
        // 3、该队列是否只供一个消费者进行消费 默认情况可供多个消费者进行消费
        // 4、是否自动删除
        // 5、其它参数
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < SEND_MESSAGE_COUNT; i++) {
            String s = SEND_MESSAGE + i;
            // 发送消息
            // 1、交换机名称
            // 2、路由key
            // 3、其它参数
            // 4、消息体
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, s.getBytes());

            boolean b = channel.waitForConfirms();
            if (b) {
                System.out.println("Producer消息发送成功：" + s);
            }
        }
        System.out.println("Producer消息发送成功，耗时：" + (System.currentTimeMillis() - startTime) + "ms");

    }

    public static void batchPublishConfirm() throws Exception {
        // 获取信道
        Channel channel = RabbitMqUtils.getChannel();

        // 开启发布确认
        channel.confirmSelect();

        // 生成队列
        // 1、队列名称
        // 2、队列里面的消息是否持久化(磁盘) 默认情况消息存储在内存中
        // 3、该队列是否只供一个消费者进行消费 默认情况可供多个消费者进行消费
        // 4、是否自动删除
        // 5、其它参数
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        long startTime = System.currentTimeMillis();
        int count = 0;
        for (int i = 0; i < SEND_MESSAGE_COUNT; i++) {
            count++;
            String s = SEND_MESSAGE + i;
            // 发送消息
            // 1、交换机名称
            // 2、路由key
            // 3、其它参数
            // 4、消息体
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, s.getBytes());

            if (count == SEND_MESSAGE_BATCH_COUNT) {
                boolean b = channel.waitForConfirms();
                if (b) {
                    System.out.println("Producer消息发送成功：" + s);
                }

                count = 0;
            }
        }
        System.out.println("Producer消息发送成功，耗时：" + (System.currentTimeMillis() - startTime) + "ms");
    }

    public static void asyncPublishConfirm() throws Exception {
        // 获取信道
        Channel channel = RabbitMqUtils.getChannel();

        // 开启发布确认
        channel.confirmSelect();

        // 生成队列
        // 1、队列名称
        // 2、队列里面的消息是否持久化(磁盘) 默认情况消息存储在内存中
        // 3、该队列是否只供一个消费者进行消费 默认情况可供多个消费者进行消费
        // 4、是否自动删除
        // 5、其它参数
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        // 记录发送的消息集合
        ConcurrentSkipListMap<Long, String> concurrentSkipListMap = new ConcurrentSkipListMap<>();

        // 准备消息的监听器 监听哪些消息发送成功 哪些消息发送失败
        channel.addConfirmListener(
                (deliveryTag, multiple) -> {
                    // 删除发送成功的消息
                    if (multiple) {
                        ConcurrentNavigableMap<Long, String> concurrentNavigableMap = concurrentSkipListMap.headMap(deliveryTag);
                        concurrentNavigableMap.clear();
                    } else {
                        concurrentSkipListMap.remove(deliveryTag);
                    }

                    System.out.println("Producer消息发送成功：" + "\n" +
                            "deliveryTag：" + deliveryTag + "\n" +
                            "multiple：" + multiple);
                },
                (deliveryTag, multiple) -> {
                    System.out.println("Producer消息发送失败：" + "\n" +
                            "deliveryTag：" + deliveryTag + "\n" +
                            "multiple：" + multiple + "\n" +
                            "message：" + concurrentSkipListMap.get(deliveryTag));
                });

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < SEND_MESSAGE_COUNT; i++) {
            String s = SEND_MESSAGE + i;
            // 发送消息
            // 1、交换机名称
            // 2、路由key
            // 3、其它参数
            // 4、消息体
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, s.getBytes());

            concurrentSkipListMap.put(channel.getNextPublishSeqNo(), s);
        }
        System.out.println("Producer消息发送成功，耗时：" + (System.currentTimeMillis() - startTime) + "ms");
    }

}
