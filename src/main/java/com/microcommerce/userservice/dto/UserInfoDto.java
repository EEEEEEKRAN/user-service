package com.microcommerce.userservice.dto;

/**
 * DTO optimisé pour la communication inter-services
 * 
 * Contient uniquement les infos essentielles d'un utilisateur
 * pour les appels entre microservices (pas de mot de passe !)
 */
public class UserInfoDto {
    
    private String id;
    private String name;
    private String email;
    private String role;
    
    // Constructeurs
    public UserInfoDto() {}
    
    public UserInfoDto(String id, String name, String email, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }
    
    // Getters et Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
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
    
    @Override
    public String toString() {
        return "UserInfoDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}