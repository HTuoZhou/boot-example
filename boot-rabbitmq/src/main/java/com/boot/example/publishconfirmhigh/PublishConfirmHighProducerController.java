package com.boot.example.publishconfirmhigh;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * @author TuoZhou
 */
@RestController
@RequestMapping("/publishConfirmHigh/producer")
@Slf4j
public class PublishConfirmHighProducerController {

    private final RabbitTemplate rabbitTemplate;

    public PublishConfirmHighProducerController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(new MyCallback());
        rabbitTemplate.setReturnCallback(new MyCallback());
    }

    @GetMapping("/sendMsg/{msg}")
    public String sendMsg(@PathVariable("msg") String msg) {
        log.info("发送消息内容：{}", msg);

        CorrelationData correlationData1 = new CorrelationData();
        correlationData1.setId("1");
        rabbitTemplate.convertAndSend(PublishConfirmHighConfig.PUBLISH_CONFIRM_HIGH_EXCHANGE,
                PublishConfirmHighConfig.PUBLISH_CONFIRM_HIGH_ROUTING_KEY,
                msg,
                correlationData1);

        CorrelationData correlationData2 = new CorrelationData();
        correlationData2.setId("2");
        rabbitTemplate.convertAndSend(PublishConfirmHighConfig.PUBLISH_CONFIRM_HIGH_EXCHANGE + "aa",
                PublishConfirmHighConfig.PUBLISH_CONFIRM_HIGH_ROUTING_KEY,
                msg,
                correlationData2);

        CorrelationData correlationData3 = new CorrelationData();
        correlationData3.setId("3");
        rabbitTemplate.convertAndSend(PublishConfirmHighConfig.PUBLISH_CONFIRM_HIGH_EXCHANGE,
                PublishConfirmHighConfig.PUBLISH_CONFIRM_HIGH_ROUTING_KEY + "aa",
                msg,
                correlationData3);

        return msg;
    }

}
