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
    public ConnectionFactory getConnectionFactory() {
        return new CachingConnectionFactory("localhost");
    }

    @Bean
    public DirectExchange getDirectExchange() {
        return new DirectExchange("exchange");
    }

    @Bean
    public Queue getOrderQueue() {
        return  new Queue("qorder");
    }

    @Bean
    public Binding getBinding() {
        DirectExchange exchange = getDirectExchange();
        Queue queue = getOrderQueue();
        String routingKey = "order";
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    @Bean
    public RabbitTemplate getRabbitTemplate() {
        ConnectionFactory connectionFactory = getConnectionFactory();
        return new RabbitTemplate(connectionFactory);
    }
}
