package com.boot.example.delaytime;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author TuoZhou
 */
@RestController
@RequestMapping("/delayTime/producer")
@Slf4j
public class ProducerController {

    private final RabbitTemplate rabbitTemplate;

    public ProducerController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/sendMsg/{msg}")
    public String sendMsg(@PathVariable("msg") String msg) {
        log.info("当前时间：{}，发送消息给两个TTL队列", LocalDateTime.now());

        rabbitTemplate.convertAndSend("X", "XA", msg);
        rabbitTemplate.convertAndSend("X", "XB", msg);

        return msg;
    }

    @GetMapping("/sendMsg/{msg}/{ttl}")
    public String sendMsg(@PathVariable("msg") String msg, @PathVariable("ttl") String ttl) {
        log.info("当前时间：{}，发送消息给TTL队列", LocalDateTime.now());

        rabbitTemplate.convertAndSend("X", "XC", msg, (message) -> {
            message.getMessageProperties().setExpiration(ttl);
            return message;
        });

        return msg + ttl;
    }

}
