package com.microcommerce.userservice.repository;

import com.microcommerce.userservice.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour les opérations CRUD sur les utilisateurs
 * 
 * MongoDB nous donne plein de méthodes automatiques grâce aux noms des méthodes
 * Spring Data fait la magie derrière pour générer les requêtes
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    
    /**
     * Trouve un utilisateur par son email
     * Utilisé pour l'authentification
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Vérifie si un email existe déjà
     * Pratique pour éviter les doublons lors de l'inscription
     */
    boolean existsByEmail(String email);
    
    /**
     * Trouve tous les utilisateurs par rôle
     * Utile pour lister les admins, les clients, etc.
     */
    List<User> findByRole(String role);
    

    
    /**
     * Recherche d'utilisateurs par nom (insensible à la casse)
     * Pratique pour une barre de recherche admin
     */
    @Query("{'name': {'$regex': ?0, '$options': 'i'}}")
    List<User> searchByName(String name);
    

    
    /**
     * Compte le nombre d'utilisateurs par rôle
     * Utile pour des statistiques
     */
    long countByRole(String role);
    
    /**
     * Trouve les utilisateurs créés après une certaine date
     * Pour des stats de croissance
     */
    @Query("{'createdAt': {'$gte': ?0}}")
    List<User> findUsersCreatedAfter(String date);
}