package com.boot.example.priority;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author TuoZhou
 */
@Component
@Slf4j
public class PriorityConsumer {

    @RabbitListener(queues = PriorityConfig.PRIORITY_QUEUE)
    public void receiveMsg(Message message) {
        log.info("接收消息：{}", new String(message.getBody()));
    }

}
