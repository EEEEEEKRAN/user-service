package com.microcommerce.userservice.controller;

import com.microcommerce.userservice.dto.RegisterRequest;
import com.microcommerce.userservice.dto.UserResponse;
import com.microcommerce.userservice.dto.UserInfoDto;
import com.microcommerce.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
// Imports Spring Security temporairement désactivés
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Contrôleur pour la gestion des utilisateurs
 * 
 * Gère tous les endpoints liés aux utilisateurs :
 * - CRUD des utilisateurs
 * - Recherche et filtrage
 * - Gestion des profils
 * - Administration
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * Récupérer tous les utilisateurs (admin seulement)
     * GET /api/users
     */
    @GetMapping
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    /**
     * Récupérer un utilisateur par ID
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN') or @userService.isOwner(#id, authentication.name)")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        try {
            // TODO: Vérifier les permissions après réactivation de Spring Security
            UserResponse user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Récupérer le profil de l'utilisateur connecté
     * GET /api/users/me
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        // TODO: Implémenter après réactivation de Spring Security
        return ResponseEntity.ok().build();
    }
    
    /**
     * Mettre à jour un utilisateur
     * PUT /api/users/{id}
     */
    @PutMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN') or @userService.isOwner(#id, authentication.name)")
    public ResponseEntity<?> updateUser(
            @PathVariable String id,
            @Valid @RequestBody RegisterRequest request
    ) {
        try {
            // TODO: Vérifier les permissions après réactivation de Spring Security
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
     * Supprimer un utilisateur (admin seulement)
     * DELETE /api/users/{id}
     */
    @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
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
     * Rechercher des utilisateurs par nom (admin seulement)
     * GET /api/users/search?name=...
     */
    @GetMapping("/search")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> searchUsers(@RequestParam String name) {
        List<UserResponse> users = userService.searchUsersByName(name);
        return ResponseEntity.ok(users);
    }
    
    /**
     * Récupérer les utilisateurs par rôle (admin seulement)
     * GET /api/users/role/{role}
     */
    @GetMapping("/role/{role}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getUsersByRole(@PathVariable String role) {
        List<UserResponse> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }
    

    
    /**
     * Statistiques des utilisateurs
     * GET /api/users/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getUserStats() {
        Map<String, Long> stats = userService.getUserStats();
        return ResponseEntity.ok(stats);
    }
    
    /**
     * Endpoint interne pour la communication inter-services
     * Retourne les infos essentielles d'un utilisateur (sans mot de passe)
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
     * Test de l'API utilisateurs
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