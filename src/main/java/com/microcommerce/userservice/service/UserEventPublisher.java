package com.microcommerce.userservice.service;

import com.microcommerce.userservice.event.UserEvent;
import com.microcommerce.userservice.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service pour publier les événements utilisateurs vers RabbitMQ
 * 
 * Chaque fois qu'un utilisateur est créé, modifié ou supprimé,
 * on envoie un event pour que les autres services se synchronisent.
 */
@Service
public class UserEventPublisher {
    
    private static final Logger logger = LoggerFactory.getLogger(UserEventPublisher.class);
    
    // Noms des exchanges et routing keys
    public static final String USER_EXCHANGE = "user.exchange";
    public static final String USER_CREATED_ROUTING_KEY = "user.created";
    public static final String USER_UPDATED_ROUTING_KEY = "user.updated";
    public static final String USER_DELETED_ROUTING_KEY = "user.deleted";
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    /**
     * Publie un événement de création d'utilisateur
     */
    public void publishUserCreated(User user) {
        UserEvent event = createUserEvent(user, UserEvent.EventType.CREATED);
        publishEvent(event, USER_CREATED_ROUTING_KEY);
        logger.info("Événement USER_CREATED publié pour l'utilisateur: {}", user.getId());
    }
    
    /**
     * Publie un événement de mise à jour d'utilisateur
     */
    public void publishUserUpdated(User user) {
        UserEvent event = createUserEvent(user, UserEvent.EventType.UPDATED);
        publishEvent(event, USER_UPDATED_ROUTING_KEY);
        logger.info("Événement USER_UPDATED publié pour l'utilisateur: {}", user.getId());
    }
    
    /**
     * Publie un événement de suppression d'utilisateur
     */
    public void publishUserDeleted(String userId) {
        UserEvent event = new UserEvent();
        event.setUserId(userId);
        event.setEventType(UserEvent.EventType.DELETED);
        publishEvent(event, USER_DELETED_ROUTING_KEY);
        logger.info("Événement USER_DELETED publié pour l'utilisateur: {}", userId);
    }
    
    /**
     * Crée un événement utilisateur à partir d'un objet User
     */
    private UserEvent createUserEvent(User user, UserEvent.EventType eventType) {
        return new UserEvent(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole(),
            eventType
        );
    }
    
    /**
     * Envoie l'événement vers RabbitMQ
     */
    private void publishEvent(UserEvent event, String routingKey) {
        try {
            rabbitTemplate.convertAndSend(
                USER_EXCHANGE,
                routingKey,
                event
            );
            logger.debug("Événement utilisateur envoyé avec succès: {}", event);
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de l'événement utilisateur: {}", event, e);
            // En production, on pourrait implémenter un retry ou stocker l'event pour retry plus tard
        }
    }
}