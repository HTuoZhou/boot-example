package com.boot.example.workqueue.manualack;

import com.boot.example.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;

/**
 * @author TuoZhou
 */
public class Consumer01 {

    /**
     * 队列的名称
     */
    public static final String QUEUE_NAME = "work-queue";

    /**
     * 接收消息
     */
    public static void main(String[] args) throws Exception {
        // 获取信道
        Channel channel = RabbitMqUtils.getChannel();

        // 设置分发方式 0、轮询分发(默认) 1、不公平分发
        channel.basicQos(1);

        // 接收消息
        // 1、队列名称
        // 2、是否自动应答
        // 3、消息传递回调
        // 4、消费者取消消费回调
        channel.basicConsume(QUEUE_NAME, false,
                (consumerTag, message) -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("消息接收成功：" + "\n" +
                            "consumerTag：" + consumerTag + "\n" +
                            "message：" + new String(message.getBody()));

                    // 手动应答 basicAck basicNack basicReject
                    // 1、消息的标记
                    // 2、是否批量应答
                    channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
                },
                (consumerTag) -> {
                    System.out.println("消费者取消消费：" + "\n" +
                            "consumerTag：" + consumerTag);
                });
    }

}
