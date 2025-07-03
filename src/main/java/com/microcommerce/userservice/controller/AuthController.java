package com.microcommerce.userservice.controller;

import com.microcommerce.userservice.dto.AuthResponse;
import com.microcommerce.userservice.dto.LoginRequest;
import com.microcommerce.userservice.dto.RegisterRequest;
import com.microcommerce.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Contrôleur pour l'authentification
 * 
 * Gère les endpoints publics :
 * - Inscription des nouveaux utilisateurs
 * - Connexion des utilisateurs existants
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // En prod, spécifier les domaines autorisés
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    /**
     * Inscription d'un nouvel utilisateur
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthResponse response = userService.register(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Erreur lors de l'inscription",
                "message", e.getMessage()
            ));
        }
    }
    
    /**
     * Connexion d'un utilisateur
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = userService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Erreur lors de la connexion",
                "message", "Email ou mot de passe incorrect"
            ));
        }
    }
    
    /**
     * Test de l'API d'authentification
     * GET /api/auth/test
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test() {
        return ResponseEntity.ok(Map.of(
            "message", "API d'authentification fonctionnelle !",
            "service", "user-service",
            "version", "1.0.0"
        ));
    }
}