package com.microcommerce.userservice.dto;

/**
 * DTO pour les réponses de connexion
 * Contient le token JWT et les informations utilisateur
 */
public class LoginResponse {
    
    private String token;
    private String userId;
    private String email;
    private String name;
    private String role;
    private String message;
    private long expiresIn; // Durée de validité en millisecondes
    
    // Constructeurs
    public LoginResponse() {}
    
    public LoginResponse(String token, String userId, String email, String name, String role, String message) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.role = role;
        this.message = message;
        this.expiresIn = 86400000; // 24 heures par défaut
    }
    
    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String token;
        private String userId;
        private String email;
        private String name;
        private String role;
        private String message;
        private long expiresIn = 86400000; // 24 heures par défaut
        
        public Builder token(String token) {
            this.token = token;
            return this;
        }
        
        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }
        
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        public Builder role(String role) {
            this.role = role;
            return this;
        }
        
        public Builder message(String message) {
            this.message = message;
            return this;
        }
        
        public Builder expiresIn(long expiresIn) {
            this.expiresIn = expiresIn;
            return this;
        }
        
        public LoginResponse build() {
            LoginResponse response = new LoginResponse();
            response.token = this.token;
            response.userId = this.userId;
            response.email = this.email;
            response.name = this.name;
            response.role = this.role;
            response.message = this.message;
            response.expiresIn = this.expiresIn;
            return response;
        }
    }
    
    // Getters et Setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
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
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public long getExpiresIn() {
        return expiresIn;
    }
    
    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }
    
    @Override
    public String toString() {
        return "LoginResponse{" +
                "userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", message='" + message + '\'' +
                ", expiresIn=" + expiresIn +
                ", token='[PROTECTED]'" + // On ne log jamais le token !
                '}';
    }
}