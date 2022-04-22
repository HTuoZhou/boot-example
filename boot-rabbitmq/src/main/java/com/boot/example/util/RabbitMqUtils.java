package com.boot.example.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author TuoZhou
 * @date 2022/4/22
 */
public class RabbitMqUtils {

    /**
     * 获取信道
     */
    public static Channel getChannel() throws Exception {
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

        // 获取信道并返回
        return connection.createChannel();
    }

}
