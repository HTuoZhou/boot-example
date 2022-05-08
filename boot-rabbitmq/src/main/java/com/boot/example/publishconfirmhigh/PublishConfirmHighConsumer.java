package com.boot.example.publishconfirmhigh;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author TuoZhou
 */
@Component
@Slf4j
public class PublishConfirmHighConsumer {

    @RabbitListener(queues = PublishConfirmHighConfig.PUBLISH_CONFIRM_HIGH_QUEUE)
    public void receiveMsg(Message message) {
        String s = new String(message.getBody());
        log.info("接收消息内容：{}", s);
    }

    @RabbitListener(queues = PublishConfirmHighConfig.PUBLISH_CONFIRM_HIGH_BACK_UP_QUEUE)
    public void receiveBackupMsg(Message message) {
        String s = new String(message.getBody());
        log.info("接收备份消息内容：{}", s);
    }

    @RabbitListener(queues = PublishConfirmHighConfig.PUBLISH_CONFIRM_HIGH_WARNING_QUEUE)
    public void receiveWarningMsg(Message message) {
        String s = new String(message.getBody());
        log.info("接收报警消息内容：{}", s);
    }

}
