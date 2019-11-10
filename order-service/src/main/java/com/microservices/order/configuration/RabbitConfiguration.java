package com.microservices.order.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfiguration {

    @Bean
    public ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory("localhost");
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("exchange");
    }

    @Bean
    public Queue orderQueue() {
        return  new Queue("qorder");
    }

    @Bean
    public Binding binding() {
        DirectExchange exchange = directExchange();
        Queue orderQueue = orderQueue();
        String routingKey = "order";
        return BindingBuilder.bind(orderQueue).to(exchange).with(routingKey);
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        ConnectionFactory connectionFactory = connectionFactory();
        return new RabbitTemplate(connectionFactory);
    }
}
