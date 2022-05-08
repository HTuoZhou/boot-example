package com.boot.example.publishconfirmhigh;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author TuoZhou
 */
@Configuration
public class PublishConfirmHighConfig {

    public static final String PUBLISH_CONFIRM_HIGH_EXCHANGE = "publish_confirm_high_exchange";
    public static final String PUBLISH_CONFIRM_HIGH_QUEUE = "publish_confirm_high_queue";
    public static final String PUBLISH_CONFIRM_HIGH_ROUTING_KEY = "publish_confirm_high_routing_key";

    public static final String PUBLISH_CONFIRM_HIGH_BACK_UP_EXCHANGE = "publish_confirm_high_back_up_exchange";
    public static final String PUBLISH_CONFIRM_HIGH_BACK_UP_QUEUE = "publish_confirm_high_back_up_queue";
    public static final String PUBLISH_CONFIRM_HIGH_WARNING_QUEUE = "publish_confirm_high_warning_queue";

    @Bean
    public DirectExchange publishConfirmHighExchange() {
        return ExchangeBuilder.directExchange(PUBLISH_CONFIRM_HIGH_EXCHANGE)
                .durable(true)
                .alternate(PUBLISH_CONFIRM_HIGH_BACK_UP_EXCHANGE)
                .build();
    }

    @Bean
    public FanoutExchange publishConfirmHighBackUpExchange() {
        return new FanoutExchange(PUBLISH_CONFIRM_HIGH_BACK_UP_EXCHANGE, true, false, null);
    }

    @Bean
    public Queue publishConfirmHighQueue() {
        return QueueBuilder.durable(PUBLISH_CONFIRM_HIGH_QUEUE)
                .build();
    }

    @Bean
    public Queue publishConfirmHighBackUpQueue() {
        return QueueBuilder.durable(PUBLISH_CONFIRM_HIGH_BACK_UP_QUEUE)
                .build();
    }

    @Bean
    public Queue publishConfirmHighWarningQueue() {
        return QueueBuilder.durable(PUBLISH_CONFIRM_HIGH_WARNING_QUEUE)
                .build();
    }

    @Bean
    public Binding queueBingingExchange() {
        return BindingBuilder.bind(publishConfirmHighQueue()).to(publishConfirmHighExchange()).with(PUBLISH_CONFIRM_HIGH_ROUTING_KEY);
    }

    @Bean
    public Binding backupQueueBingingBackupExchange() {
        return BindingBuilder.bind(publishConfirmHighBackUpQueue()).to(publishConfirmHighBackUpExchange());
    }

    @Bean
    public Binding warningQueueBingingBackupExchange() {
        return BindingBuilder.bind(publishConfirmHighWarningQueue()).to(publishConfirmHighBackUpExchange());
    }

}
