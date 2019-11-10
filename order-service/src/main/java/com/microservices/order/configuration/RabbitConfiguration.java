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
        return new DirectExchange("directExchange");
    }

    @Bean
    public Queue paymentQueue() {
        return  new Queue("qpayment");
    }

    @Bean
    public Queue itemQueue() {
        return new Queue("qitem");
    }

    @Bean
    public Binding bindingPayment(DirectExchange directExchange, Queue paymentQueue) {
        return BindingBuilder.bind(paymentQueue).to(directExchange).with("payment");
    }

    @Bean
    public Binding bindItems(DirectExchange directExchange, Queue itemQueue) {
        return BindingBuilder.bind(itemQueue).to(directExchange).with("item");
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        ConnectionFactory connectionFactory = connectionFactory();
        return new RabbitTemplate(connectionFactory);
    }
}
