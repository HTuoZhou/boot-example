package com.boot.example.delaytime;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author TuoZhou
 */
@Configuration
public class DelayTimeConfig {

    /**
     * 正常交换机名称
     */
    public static final String EXCHANGE_X = "X";

    /**
     * 死信交换机名称
     */
    public static final String DEAD_LETTER_EXCHANGE_Y = "Y";

    /**
     * 正常队列名称
     */
    public static final String QUEUE_A = "QA";

    /**
     * 正常队列名称
     */
    public static final String QUEUE_B = "QB";

    /**
     * 正常队列名称
     */
    public static final String QUEUE_C = "QC";

    /**
     * 死信队列名称
     */
    public static final String DEAD_LETTER_QUEUE_D = "QD";

    @Bean
    public DirectExchange exchangeX() {
        return new DirectExchange(EXCHANGE_X, true, false, null);
    }

    @Bean
    public DirectExchange deadLetterExchangeY() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE_Y, true, false, null);
    }

    @Bean
    public Queue queueA() {
        return QueueBuilder.durable(QUEUE_A)
                .deadLetterExchange(DEAD_LETTER_EXCHANGE_Y)
                .deadLetterRoutingKey("YD")
                .ttl(10000)
                .build();
    }

    @Bean
    public Queue queueB() {
        return QueueBuilder.durable(QUEUE_B)
                .deadLetterExchange(DEAD_LETTER_EXCHANGE_Y)
                .deadLetterRoutingKey("YD")
                .ttl(40000)
                .build();
    }

    @Bean
    public Queue queueC() {
        return QueueBuilder.durable(QUEUE_C)
                .deadLetterExchange(DEAD_LETTER_EXCHANGE_Y)
                .deadLetterRoutingKey("YD")
                .build();
    }

    @Bean
    public Queue deadLetterQueueD() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE_D)
                .build();
    }

    @Bean
    public Binding queueABindingX() {
        return BindingBuilder.bind(queueA()).to(exchangeX()).with("XA");
    }

    @Bean
    public Binding queueBBindingX() {
        return BindingBuilder.bind(queueB()).to(exchangeX()).with("XB");
    }

    @Bean
    public Binding queueCBindingX() {
        return BindingBuilder.bind(queueC()).to(exchangeX()).with("XC");
    }

    @Bean
    public Binding queueDBindingY() {
        return BindingBuilder.bind(deadLetterQueueD()).to(deadLetterExchangeY()).with("YD");
    }

}
