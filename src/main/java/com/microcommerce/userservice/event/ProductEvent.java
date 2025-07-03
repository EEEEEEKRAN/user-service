package com.microcommerce.userservice.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Événement produit reçu via RabbitMQ
 * 
 * Copie du ProductEvent du product-service pour pouvoir
 * désérialiser les messages reçus.
 */
public class ProductEvent {
    
    private String productId;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String category;
    private EventType eventType;
    private LocalDateTime timestamp;
    
    // Constructeur par défaut pour Jackson
    public ProductEvent() {
    }
    
    public ProductEvent(String productId, String name, String description, 
                       BigDecimal price, Integer stock, String category, EventType eventType) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.eventType = eventType;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters et setters
    public String getProductId() {
        return productId;
    }
    
    public void setProductId(String productId) {
        this.productId = productId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public Integer getStock() {
        return stock;
    }
    
    public void setStock(Integer stock) {
        this.stock = stock;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public EventType getEventType() {
        return eventType;
    }
    
    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public String toString() {
        return "ProductEvent{" +
                "productId='" + productId + '\'' +
                ", name='" + name + '\'' +
                ", eventType=" + eventType +
                ", timestamp=" + timestamp +
                '}';
    }
    
    /**
     * Types d'événements possibles pour un produit
     */
    public enum EventType {
        CREATED,    // Produit créé
        UPDATED,    // Produit mis à jour
        DELETED     // Produit supprimé
    }
}