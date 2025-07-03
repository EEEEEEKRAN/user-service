package com.microcommerce.userservice.config;

import com.microcommerce.userservice.model.User;
import com.microcommerce.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Initialisateur de données pour créer des utilisateurs de test
 * 
 * Se lance au démarrage de l'application pour peupler la base
 * avec quelques utilisateurs par défaut
 */
@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    // @Autowired
    // private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        
        // Supprimer toutes les données existantes pour réinitialiser avec la nouvelle structure
        if (userRepository.count() > 0) {
            System.out.println("🗑️ Suppression des anciennes données pour réinitialisation...");
            userRepository.deleteAll();
        }
        
        System.out.println("🔄 Initialisation des données utilisateurs...");
        
        // Créer un admin
        User admin = new User();
        admin.setName("Admin System");
        admin.setEmail("admin@microcommerce.com");
        admin.setPassword("admin123"); // TODO: Hash avec PasswordEncoder
        admin.setRole("ADMIN");
        userRepository.save(admin);
        
        // Créer quelques utilisateurs de test
        User[] testUsers = {
            createUser("Jean Dupont", "jean.dupont@email.com", "password123"),
            createUser("Marie Martin", "marie.martin@email.com", "password123"),
            createUser("Pierre Durand", "pierre.durand@email.com", "password123"),
            createUser("Sophie Leroy", "sophie.leroy@email.com", "password123"),
            createUser("Thomas Moreau", "thomas.moreau@email.com", "password123")
        };
        
        for (User user : testUsers) {
            userRepository.save(user);
        }
        
        System.out.println("✅ Données initialisées avec succès !");
        System.out.println("👤 Admin créé: admin@microcommerce.com / admin123");
        System.out.println("👥 " + testUsers.length + " utilisateurs de test créés");
        System.out.println("📊 Total: " + userRepository.count() + " utilisateurs en base");
    }
    
    /**
     * Méthode utilitaire pour créer un utilisateur
     */
    private User createUser(String name, String email, String password) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password); // TODO: Hash avec PasswordEncoder
        user.setRole("USER");
        return user;
    }
}