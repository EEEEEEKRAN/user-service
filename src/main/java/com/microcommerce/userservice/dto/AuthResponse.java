package com.microcommerce.userservice.dto;

/**
 * DTO pour les réponses d'authentification
 * 
 * Contient le token JWT et les infos de base de l'utilisateur
 * C'est ce qu'on renvoie après une connexion ou inscription réussie
 */
public class AuthResponse {
    
    private String token;
    private String type = "Bearer"; // Type de token standard
    private String userId;
    private String email;
    private String name;
    private String role;
    
    // Constructeurs
    public AuthResponse() {}
    
    public AuthResponse(String token, String userId, String email, String name, String role) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.role = role;
    }
    
    // Getters et Setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    @Override
    public String toString() {
        return "AuthResponse{" +
                "type='" + type + '\'' +
                ", userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", token='[PROTECTED]'" + // On ne log jamais le token complet
                '}';
    }
}