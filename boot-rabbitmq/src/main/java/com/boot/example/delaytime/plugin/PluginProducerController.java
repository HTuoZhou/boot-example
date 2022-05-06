package com.boot.example.delaytime.plugin;

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
@RequestMapping("/plugin/delayTime/producer")
@Slf4j
public class PluginProducerController {

    private final RabbitTemplate rabbitTemplate;

    public PluginProducerController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/sendMsg/{msg}/{delayTime}")
    public String sendMsg(@PathVariable("msg") String msg, @PathVariable("delayTime") Integer delayTime) {
        log.info("当前时间：{}，发送消息给延时队列", LocalDateTime.now());

        rabbitTemplate.convertAndSend("delayed.exchange", "delayed.routingKey", msg, (message) -> {
            message.getMessageProperties().setDelay(delayTime);
            return message;
        });

        return msg + delayTime;
    }

}
