package com.aliyara.procurementservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String STOCK_INCREASE_EXCHANGE = "procurement.stock-increase";
    public static final String STOCK_INCREASE_QUEUE = "inventory.stock-increase.queue";
    public static final String STOCK_INCREASE_ROUTING_KEY = "stock.increase";

    @Bean
    public TopicExchange stockIncreaseExchange() {
        return ExchangeBuilder.topicExchange(STOCK_INCREASE_EXCHANGE)
                .durable(true)
                .build();
    }

    @Bean
    public Queue stockIncreaseQueue() {
        return QueueBuilder.durable(STOCK_INCREASE_QUEUE).build();
    }

    @Bean
    public Binding stockIncreaseBinding(Queue stockIncreaseQueue,
            TopicExchange stockIncreaseExchange) {
        return BindingBuilder
                .bind(stockIncreaseQueue)
                .to(stockIncreaseExchange)
                .with(STOCK_INCREASE_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
            MessageConverter jackson2JsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jackson2JsonMessageConverter);
        // Retry is configured via application.yml
        // (spring.rabbitmq.listener.simple.retry)
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter jackson2JsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jackson2JsonMessageConverter);
        return factory;
    }
}
