package com.microcommerce.userservice.event;

import java.time.LocalDateTime;

/**
 * Événement utilisateur pour la synchronisation entre services
 * 
 * Quand un utilisateur est créé, modifié ou supprimé,
 * on envoie cet event pour que les autres services se mettent à jour.
 */
public class UserEvent {
    
    private String userId;
    private String name;
    private String email;
    private String role;
    private EventType eventType;
    private LocalDateTime timestamp;
    
    // Types d'événements possibles
    public enum EventType {
        CREATED,    // Nouvel utilisateur
        UPDATED,    // Utilisateur modifié
        DELETED     // Utilisateur supprimé
    }
    
    // Constructeur par défaut pour Jackson
    public UserEvent() {
    }
    
    public UserEvent(String userId, String name, String email, String role, EventType eventType) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.eventType = eventType;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters et setters
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
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
        return "UserEvent{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", eventType=" + eventType +
                ", timestamp=" + timestamp +
                '}';
    }
}