package com.boot.example.delaytime.plugin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author TuoZhou
 */
@Component
@Slf4j
public class PluginConsumer {

    @RabbitListener(queues = "delayed.queue")
    public void receiveDelayedQueueMsg(Message message) {
        String receiveMsg = new String(message.getBody());
        log.info("当前时间：{}，收到延时队列的消息：{}", LocalDateTime.now(), receiveMsg);
    }

}
