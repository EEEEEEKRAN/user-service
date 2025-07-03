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
    public static final String PRODUCT_CREATED_ROUTING_KEY = "product.created";
    public static final String PRODUCT_UPDATED_ROUTING_KEY = "product.updated";
    public static final String PRODUCT_DELETED_ROUTING_KEY = "product.deleted";
    
    // Queue spécifique au user-service pour les événements produits
    public static final String USER_SERVICE_PRODUCT_QUEUE = "user-service.product.queue";
    
    // Configuration pour les événements utilisateurs
    public static final String USER_EXCHANGE = "user.exchange";
    public static final String USER_CREATED_ROUTING_KEY = "user.created";
    public static final String USER_UPDATED_ROUTING_KEY = "user.updated";
    public static final String USER_DELETED_ROUTING_KEY = "user.deleted";
    
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
    
    // Configuration pour les événements utilisateurs
    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(USER_EXCHANGE);
    }
    
    // Configuration pour écouter les événements commandes
    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String ORDER_ALL_ROUTING_KEY = "order.*";
    public static final String USER_SERVICE_ORDER_QUEUE = "user-service.order.queue";
    
    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }
    
    @Bean
    public Queue userServiceOrderQueue() {
        return new Queue(USER_SERVICE_ORDER_QUEUE, true);
    }
    
    @Bean
    public Binding userServiceOrderBinding() {
        return BindingBuilder
            .bind(userServiceOrderQueue())
            .to(orderExchange())
            .with(ORDER_ALL_ROUTING_KEY);
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