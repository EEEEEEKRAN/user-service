package com.microcommerce.userservice.service;

import com.microcommerce.userservice.dto.LoginRequest;
import com.microcommerce.userservice.dto.LoginResponse;
import com.microcommerce.userservice.model.User;
import com.microcommerce.userservice.repository.UserRepository;
import com.microcommerce.userservice.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service d'authentification
 * Gère la connexion des utilisateurs et la génération de tokens JWT
 */
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Authentifie un utilisateur et génère un token JWT
     */
    public LoginResponse login(LoginRequest loginRequest) {
        // Rechercher l'utilisateur par email
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
        
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Utilisateur non trouvé");
        }
        
        User user = userOptional.get();
        
        // Vérifier le mot de passe
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Mot de passe incorrect");
        }
        
        // Générer le token JWT
        String token = jwtUtil.generateToken(user.getEmail(), user.getId(), user.getRole().toString());
        
        return LoginResponse.builder()
                .token(token)
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().toString())
                .message("Connexion réussie")
                .build();
    }

    /**
     * Valide un token JWT
     */
    public boolean validateToken(String token) {
        try {
            return jwtUtil.validateToken(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extrait les informations utilisateur d'un token
     */
    public User getUserFromToken(String token) {
        String email = jwtUtil.extractUsername(token);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    /**
     * Rafraîchit un token JWT
     */
    public LoginResponse refreshToken(String token) {
        if (!jwtUtil.validateToken(token)) {
            throw new RuntimeException("Token invalide");
        }
        
        User user = getUserFromToken(token);
        String newToken = jwtUtil.generateToken(user.getEmail(), user.getId(), user.getRole().toString());
        
        return LoginResponse.builder()
                .token(newToken)
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().toString())
                .message("Token rafraîchi avec succès")
                .build();
    }
}