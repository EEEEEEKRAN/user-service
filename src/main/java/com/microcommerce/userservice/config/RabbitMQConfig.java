package com.microcommerce.userservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration RabbitMQ pour le service utilisateur
 * 
 * Le user-service écoute les événements produits pour maintenir
 * un cache local des infos produits (pour éviter les appels HTTP)
 */
@Configuration
public class RabbitMQConfig {

    // Noms des exchanges et queues (doivent correspondre au product-service)
    public static final String PRODUCT_EXCHANGE = "product.exchange";
    public static final String USER_SERVICE_PRODUCT_QUEUE = "user-service.product.queue";
    
    // Routing keys pour écouter tous les événements produits
    public static final String PRODUCT_ALL_ROUTING_KEY = "product.*";

    /**
     * Exchange principal pour les événements produits (déclaré aussi côté product-service)
     */
    @Bean
    public TopicExchange productExchange() {
        return new TopicExchange(PRODUCT_EXCHANGE);
    }

    /**
     * Queue spécifique au user-service pour recevoir les événements produits
     */
    @Bean
    public Queue userServiceProductQueue() {
        return QueueBuilder.durable(USER_SERVICE_PRODUCT_QUEUE).build();
    }

    /**
     * Binding pour écouter tous les événements produits (created, updated, deleted)
     * Le pattern "product.*" capture product.created, product.updated, product.deleted
     */
    @Bean
    public Binding userServiceProductBinding() {
        return BindingBuilder
                .bind(userServiceProductQueue())
                .to(productExchange())
                .with(PRODUCT_ALL_ROUTING_KEY);
    }

    /**
     * Convertisseur JSON pour désérialiser les messages
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Template RabbitMQ avec le convertisseur JSON
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}