package com.boot.example.publishconfirmhigh;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @author TuoZhou
 */
@Component
@Slf4j
public class MyCallback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        assert correlationData != null;
        String id = correlationData.getId();
        if (ack) {
            log.info("交换机已经收到id为{}的消息", id);
        } else {
            log.info("交换机还未收到id为{}的消息，原因：{}", id, cause);
        }
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("消息{}被交换机{}退回，原因：{}，routingKey：{}", new String(message.getBody()), exchange, replyText, routingKey);
    }
}
