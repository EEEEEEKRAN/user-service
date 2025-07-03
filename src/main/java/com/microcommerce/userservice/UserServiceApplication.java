package com.microcommerce.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * Classe principale pour démarrer le User Service
 * 
 * Ce service gère tout ce qui concerne les utilisateurs :
 * - Inscription et authentification
 * - Gestion des profils utilisateurs
 * - Génération de tokens JWT
 * - Sécurité et autorisation
 */
@SpringBootApplication
@EnableMongoAuditing // Pour les timestamps automatiques
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
        System.out.println("🚀 User Service démarré avec succès!");
        System.out.println("📱 API disponible sur: http://localhost:8082/api/users");
        System.out.println("🔐 Endpoints d'auth sur: http://localhost:8082/api/auth");
    }
}