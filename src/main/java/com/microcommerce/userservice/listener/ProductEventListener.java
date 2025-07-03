package com.microcommerce.userservice.listener;

import com.microcommerce.userservice.config.RabbitMQConfig;
import com.microcommerce.userservice.event.ProductEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Listener pour traiter les événements produits reçus via RabbitMQ
 * 
 * Quand le product-service envoie un événement (création, mise à jour, suppression),
 * ce listener le reçoit et peut mettre à jour un cache local ou faire d'autres traitements.
 */
@Component
public class ProductEventListener {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductEventListener.class);
    
    /**
     * Écoute tous les événements produits sur la queue user-service.product.queue
     */
    @RabbitListener(queues = RabbitMQConfig.USER_SERVICE_PRODUCT_QUEUE)
    public void handleProductEvent(ProductEvent productEvent) {
        logger.info("Événement produit reçu: {}", productEvent);
        
        try {
            switch (productEvent.getEventType()) {
                case CREATED:
                    handleProductCreated(productEvent);
                    break;
                case UPDATED:
                    handleProductUpdated(productEvent);
                    break;
                case DELETED:
                    handleProductDeleted(productEvent);
                    break;
                default:
                    logger.warn("Type d'événement produit non géré: {}", productEvent.getEventType());
            }
        } catch (Exception e) {
            logger.error("Erreur lors du traitement de l'événement produit: {}", productEvent, e);
            // En production, on pourrait implémenter un système de retry ou DLQ (Dead Letter Queue)
        }
    }
    
    /**
     * Traite la création d'un nouveau produit
     */
    private void handleProductCreated(ProductEvent productEvent) {
        logger.info("Nouveau produit créé: {} - {} ({}€)", 
                   productEvent.getProductId(), 
                   productEvent.getName(), 
                   productEvent.getPrice());
        
        // Ici on pourrait:
        // - Mettre à jour un cache local des produits
        // - Envoyer une notification aux utilisateurs intéressés
        // - Mettre à jour des statistiques
        // - etc.
        
        // Exemple: log pour démonstration
        logger.debug("Cache local mis à jour avec le nouveau produit: {}", productEvent.getProductId());
    }
    
    /**
     * Traite la mise à jour d'un produit existant
     */
    private void handleProductUpdated(ProductEvent productEvent) {
        logger.info("Produit mis à jour: {} - {} ({}€, stock: {})", 
                   productEvent.getProductId(), 
                   productEvent.getName(), 
                   productEvent.getPrice(),
                   productEvent.getStock());
        
        // Ici on pourrait:
        // - Mettre à jour le cache local
        // - Notifier les utilisateurs qui ont ce produit dans leur panier
        // - Vérifier si le prix a changé et notifier
        // - etc.
        
        logger.debug("Cache local mis à jour pour le produit: {}", productEvent.getProductId());
    }
    
    /**
     * Traite la suppression d'un produit
     */
    private void handleProductDeleted(ProductEvent productEvent) {
        logger.info("Produit supprimé: {}", productEvent.getProductId());
        
        // Ici on pourrait:
        // - Supprimer du cache local
        // - Supprimer le produit des paniers des utilisateurs
        // - Notifier les utilisateurs concernés
        // - Archiver les données
        // - etc.
        
        logger.debug("Produit supprimé du cache local: {}", productEvent.getProductId());
    }
}