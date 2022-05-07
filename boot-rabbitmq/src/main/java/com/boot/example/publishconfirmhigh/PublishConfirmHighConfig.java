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

    @Bean
    public DirectExchange publishConfirmHighExchange() {
        return new DirectExchange(PUBLISH_CONFIRM_HIGH_EXCHANGE, true, false, null);
    }

    @Bean
    public Queue publishConfirmHighQueue() {
        return QueueBuilder.durable(PUBLISH_CONFIRM_HIGH_QUEUE)
                .build();
    }

    @Bean
    public Binding queueBingingExchange() {
        return BindingBuilder.bind(publishConfirmHighQueue()).to(publishConfirmHighExchange()).with(PUBLISH_CONFIRM_HIGH_ROUTING_KEY);
    }

}
