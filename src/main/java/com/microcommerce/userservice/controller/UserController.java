package com.microcommerce.userservice.controller;

import com.microcommerce.userservice.dto.RegisterRequest;
import com.microcommerce.userservice.dto.UserResponse;
import com.microcommerce.userservice.dto.UserInfoDto;
import com.microcommerce.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
// Imports Spring Security temporairement désactivés (on les remettra plus tard)
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Notre contrôleur pour gérer les utilisateurs
 * 
 * Tout ce qu'il faut pour les utilisateurs :
 * - CRUD classique
 * - Recherche et filtrage
 * - Gestion des profils
 * - Administration (pour les admins)
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * Récupère tous les utilisateurs (normalement admin seulement)
     * GET /api/users
     */
    @GetMapping
    // @PreAuthorize("hasRole('ADMIN')") // Temporairement désactivé // Temporairement désactivé
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    /**
     * Chope un utilisateur par son ID
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN') or @userService.isOwner(#id, authentication.name)") // Temporairement désactivé
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        try {
            // TODO: Vérifier les permissions quand on remettra Spring Security
            UserResponse user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Récupère le profil de l'utilisateur connecté
     * GET /api/users/me
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        // TODO: Implémenter quand on remettra Spring Security
        return ResponseEntity.ok().build();
    }
    
    /**
     * Met à jour un utilisateur
     * PUT /api/users/{id}
     */
    @PutMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN') or @userService.isOwner(#id, authentication.name)")
    public ResponseEntity<?> updateUser(
            @PathVariable String id,
            @Valid @RequestBody RegisterRequest request
    ) {
        try {
            // TODO: Vérifier les permissions quand on remettra Spring Security
            UserResponse updatedUser = userService.updateUser(id, request);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Erreur lors de la mise à jour",
                "message", e.getMessage()
            ));
        }
    }
    
    /**
     * Supprime un utilisateur (normalement admin seulement)
     * DELETE /api/users/{id}
     */
    @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')") // Temporairement désactivé
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(Map.of(
                "message", "Utilisateur supprimé avec succès"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Recherche des utilisateurs par nom (normalement admin seulement)
     * GET /api/users/search?name=...
     */
    @GetMapping("/search")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> searchUsers(@RequestParam String name) {
        List<UserResponse> users = userService.searchUsersByName(name);
        return ResponseEntity.ok(users);
    }
    
    /**
     * Récupère les utilisateurs par rôle (normalement admin seulement)
     * GET /api/users/role/{role}
     */
    @GetMapping("/role/{role}")
    // @PreAuthorize("hasRole('ADMIN')") // Temporairement désactivé
    public ResponseEntity<List<UserResponse>> getUsersByRole(@PathVariable String role) {
        List<UserResponse> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }
    

    
    /**
     * Statistiques des utilisateurs (nombre total, par rôle, etc.)
     * GET /api/users/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getUserStats() {
        Map<String, Long> stats = userService.getUserStats();
        return ResponseEntity.ok(stats);
    }
    
    /**
     * Endpoint interne pour les autres services
     * Retourne juste les infos essentielles d'un utilisateur (sans mot de passe évidemment)
     * GET /api/internal/users/{id}
     */
    @GetMapping("/internal/{id}")
    public ResponseEntity<UserInfoDto> getUserInfoForService(@PathVariable String id) {
        try {
            UserInfoDto userInfo = userService.getUserInfoDto(id);
            return ResponseEntity.ok(userInfo);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Test de l'API utilisateurs (pour vérifier que tout fonctionne)
     * GET /api/users/test
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test() {
        return ResponseEntity.ok(Map.of(
            "message", "API utilisateurs fonctionnelle !",
            "service", "user-service",
            "version", "1.0.0"
        ));
    }
}