package com.boot.example.priority;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author TuoZhou
 */
@Configuration
public class PriorityConfig {

    public static final String PRIORITY_EXCHANGE = "priority_exchange";
    public static final String PRIORITY_QUEUE = "priority_queue";
    public static final String PRIORITY_ROUTING_KEY = "priority_routingKey";

    @Bean
    public DirectExchange priorityExchange() {
        return ExchangeBuilder.directExchange(PRIORITY_EXCHANGE)
                .durable(true)
                .build();
    }

    @Bean
    public Queue priorityQueue() {
        return QueueBuilder.durable(PRIORITY_QUEUE)
                .maxPriority(10)
                .build();
    }

    @Bean
    public Binding queueBindingExchange() {
        return BindingBuilder.bind(priorityQueue()).to(priorityExchange()).with(PRIORITY_ROUTING_KEY);
    }

}
