package com.microcommerce.userservice.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderEventListener.class);
    
    @RabbitListener(queues = "user-service.order.queue")
    public void handleOrderEvent(OrderEvent orderEvent) {
        logger.info("Événement commande reçu dans user-service: {}", orderEvent);
        
        try {
            switch (orderEvent.getEventType()) {
                case "ORDER_CREATED":
                    handleOrderCreated(orderEvent);
                    break;
                case "ORDER_STATUS_UPDATED":
                    handleOrderStatusUpdated(orderEvent);
                    break;
                case "ORDER_CANCELLED":
                    handleOrderCancelled(orderEvent);
                    break;
                case "ORDER_DELETED":
                    handleOrderDeleted(orderEvent);
                    break;
                default:
                    logger.warn("Type d'événement commande non géré: {}", orderEvent.getEventType());
            }
        } catch (Exception e) {
            logger.error("Erreur lors du traitement de l'événement commande: {}", orderEvent, e);
        }
    }
    
    private void handleOrderCreated(OrderEvent orderEvent) {
        logger.info("Commande créée - ID: {}, Utilisateur: {}, Montant: {}", 
                   orderEvent.getOrderId(), orderEvent.getUserId(), orderEvent.getTotalAmount());
        
        // Ici on pourrait mettre à jour des statistiques utilisateur
        // ou envoyer une notification
    }
    
    private void handleOrderStatusUpdated(OrderEvent orderEvent) {
        logger.info("Statut de commande mis à jour - ID: {}, Nouveau statut: {}", 
                   orderEvent.getOrderId(), orderEvent.getStatus());
        
        // Ici on pourrait envoyer une notification à l'utilisateur
        // ou mettre à jour des métriques
    }
    
    private void handleOrderCancelled(OrderEvent orderEvent) {
        logger.info("Commande annulée - ID: {}, Utilisateur: {}", 
                   orderEvent.getOrderId(), orderEvent.getUserId());
        
        // Ici on pourrait envoyer une notification d'annulation
        // ou mettre à jour des statistiques
    }
    
    private void handleOrderDeleted(OrderEvent orderEvent) {
        logger.info("Commande supprimée - ID: {}, Utilisateur: {}", 
                   orderEvent.getOrderId(), orderEvent.getUserId());
        
        // Ici on pourrait nettoyer des données associées
        // ou mettre à jour des métriques
    }
}