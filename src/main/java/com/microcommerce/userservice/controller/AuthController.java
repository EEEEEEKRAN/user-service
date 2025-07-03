package com.microcommerce.userservice.controller;

import com.microcommerce.userservice.dto.AuthResponse;
import com.microcommerce.userservice.dto.LoginRequest;
import com.microcommerce.userservice.dto.LoginResponse;
import com.microcommerce.userservice.dto.RegisterRequest;
import com.microcommerce.userservice.service.AuthService;
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
    
    @Autowired
    private AuthService authService;
    
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
     * Connexion d'un utilisateur avec JWT
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Erreur lors de la connexion",
                "message", e.getMessage()
            ));
        }
    }
    
    /**
     * Validation d'un token JWT
     * POST /api/auth/validate
     */
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Token manquant",
                    "message", "Header Authorization avec Bearer token requis"
                ));
            }

            String token = authHeader.substring(7);
            boolean isValid = authService.validateToken(token);

            return ResponseEntity.ok(Map.of(
                "valid", isValid,
                "message", isValid ? "Token valide" : "Token invalide"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Erreur de validation",
                "message", e.getMessage()
            ));
        }
    }
    
    /**
     * Rafraîchissement d'un token JWT
     * POST /api/auth/refresh
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Token manquant",
                    "message", "Header Authorization avec Bearer token requis"
                ));
            }

            String token = authHeader.substring(7);
            LoginResponse response = authService.refreshToken(token);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Échec du rafraîchissement",
                "message", e.getMessage()
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