package com.boot.example.priority;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author TuoZhou
 */
@RestController
@RequestMapping("/priority/producer")
@Slf4j
public class PriorityController {

    private final RabbitTemplate rabbitTemplate;

    public PriorityController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/sendMsg/{msg}")
    public String sendMsg(@PathVariable("msg") String msg) {
        log.info("发送消息：{}", msg);

        for (int i = 0; i < 10; i++) {
            if (Objects.equals(i + 1, 5)) {
                rabbitTemplate.convertAndSend(PriorityConfig.PRIORITY_EXCHANGE, PriorityConfig.PRIORITY_ROUTING_KEY, msg + (i + 1), (message -> {
                    MessageProperties messageProperties = message.getMessageProperties();
                    messageProperties.setPriority(5);
                    return message;
                }));
            } else {
                rabbitTemplate.convertAndSend(PriorityConfig.PRIORITY_EXCHANGE, PriorityConfig.PRIORITY_ROUTING_KEY, msg + (i + 1));
            }
        }
        return "发送消息成功";
    }

}
