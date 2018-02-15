package com.qbao.creditmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by YcY_YcY on 2017/12/4
 */
@Configuration
public class RabbitMQConfiguration {


    final static String QUEUE_NAME = "recharge.datacenter.queue";
    final static String EXCHANGE_NAME = "recharge.datacenter.exchange";

    /**
     * RabbitMQ 连接主机
     */
    @Value("${spring.rabbitmq.host}")
    private String rabbitmqHost;

    /**
     * RabbitMQ 端口
     */
    @Value("${spring.rabbitmq.port}")
    private Integer rabbitmqPort;

    /**
     * RabbitMQ 用户名
     */
    @Value("${spring.rabbitmq.username}")
    private String rabbitmqUsername;

    /**
     * RabbitMQ 密码
     */
    @Value("${spring.rabbitmq.password}")
    private String rabbitmqPassword;


    /**
     * 连接到RabbitMQ
     *
     * @return RabbitMQ Connection factory
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory =
                new CachingConnectionFactory(rabbitmqHost, rabbitmqPort);

        connectionFactory.setUsername(rabbitmqUsername);
        connectionFactory.setPassword(rabbitmqPassword);
        //  connectionFactory.setVirtualHost(rabbitmqVirtualHost);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }


    @Bean
    TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }


    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME);
        container.setMessageListener(listenerAdapter);
        return container;
    }


    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }
}
