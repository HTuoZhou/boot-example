package com.boot.example.delaytime.plugin;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author TuoZhou
 */
@Configuration
public class PluginDelayTimeConfig {

    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";

    public static final String DELAYED_QUEUE_NAME = "delayed.queue";

    public static final String DELAYED_ROUTING_KEY_NAME = "delayed.routingKey";

    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> arguments = new HashMap<>(1);
        arguments.put("x-delayed-type", "direct");
        return new CustomExchange(DELAYED_EXCHANGE_NAME, "x-delayed-message", true, false, arguments);
    }

    @Bean
    public Queue delayedQueue() {
        return QueueBuilder.durable(DELAYED_QUEUE_NAME)
                .exclusive()
                .autoDelete()
                .build();
    }

    @Bean
    public Binding delayedQueueBingingDelayedExchange() {
        return BindingBuilder.bind(delayedQueue()).to(delayedExchange()).with(DELAYED_ROUTING_KEY_NAME).noargs();
    }

}
