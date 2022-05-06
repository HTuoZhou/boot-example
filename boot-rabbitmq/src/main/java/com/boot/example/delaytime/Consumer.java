package com.boot.example.delaytime;

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
public class Consumer {

    @RabbitListener(queues = "QD")
    public void receiveQDMsg(Message message) {
        String receiveMsg = new String(message.getBody());
        log.info("当前时间：{}，收到死信队列的消息：{}", LocalDateTime.now(), receiveMsg);
    }

}
