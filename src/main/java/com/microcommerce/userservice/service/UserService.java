package com.microcommerce.userservice.service;

import com.microcommerce.userservice.dto.*;
import com.microcommerce.userservice.model.User;
import com.microcommerce.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
// Imports Spring Security temporairement désactivés
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service principal pour la gestion des utilisateurs
 * 
 * Ça gère tout ce qui concerne les utilisateurs :
 * - Inscription et authentification
 * - CRUD des utilisateurs
 * - Conversion entre entités et DTOs
 */
@Service
public class UserService { // implements UserDetailsService temporairement désactivé
    
    @Autowired
    private UserRepository userRepository;
    
    // @Autowired
    // private PasswordEncoder passwordEncoder;
    
    // @Autowired
    // private JwtService jwtService;
    
    // @Autowired
    // private AuthenticationManager authenticationManager;
    
    /**
     * Inscription d'un nouvel utilisateur
     */
    public AuthResponse register(RegisterRequest request) {
        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Un compte avec cet email existe déjà");
        }
        
        // Créer le nouvel utilisateur
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // TODO: Hash du mot de passe avec PasswordEncoder
        user.setRole("USER"); // Rôle par défaut
        
        // Sauvegarder en base
        User savedUser = userRepository.save(user);
        
        // TODO: Générer le token JWT
        String token = "temporary-token";
        
        return new AuthResponse(
            token,
            savedUser.getId(),
            savedUser.getEmail(),
            savedUser.getName(),
            savedUser.getRole()
        );
    }
    
    /**
     * Connexion d'un utilisateur
     */
    public AuthResponse login(LoginRequest request) {
        // TODO: Authentifier l'utilisateur avec AuthenticationManager
        
        // Récupérer l'utilisateur
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        // TODO: Vérifier le mot de passe
        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Mot de passe incorrect");
        }
        
        // TODO: Générer le token JWT
        String token = "temporary-token";
        
        return new AuthResponse(
            token,
            user.getId(),
            user.getEmail(),
            user.getName(),
            user.getRole()
        );
    }
    
    /**
     * Récupérer tous les utilisateurs
     */
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
            .stream()
            .map(this::convertToUserResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Récupérer un utilisateur par ID
     */
    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + id));
        return convertToUserResponse(user);
    }
    
    /**
     * Récupérer un utilisateur par email
     */
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'email: " + email));
        return convertToUserResponse(user);
    }
    
    /**
     * Mettre à jour un utilisateur
     */
    public UserResponse updateUser(String id, RegisterRequest request) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + id));
        
        // Vérifier si le nouvel email n'est pas déjà pris par quelqu'un d'autre
        if (!user.getEmail().equals(request.getEmail()) && 
            userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Cet email est déjà utilisé par un autre compte");
        }
        
        // Mettre à jour les champs
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        
        // TODO: Mettre à jour le mot de passe avec PasswordEncoder
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(request.getPassword());
        }
        
        User updatedUser = userRepository.save(user);
        return convertToUserResponse(updatedUser);
    }
    
    /**
     * Supprimer un utilisateur
     */
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Utilisateur non trouvé avec l'ID: " + id);
        }
        userRepository.deleteById(id);
    }
    
    /**
     * Rechercher des utilisateurs par nom
     */
    public List<UserResponse> searchUsersByName(String name) {
        return userRepository.searchByName(name)
            .stream()
            .map(this::convertToUserResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Récupérer les utilisateurs par rôle
     */
    public List<UserResponse> getUsersByRole(String role) {
        return userRepository.findByRole(role)
            .stream()
            .map(this::convertToUserResponse)
            .collect(Collectors.toList());
    }
    

    
    /**
     * Méthode requise par UserDetailsService pour Spring Security - temporairement désactivée
     */
    // @Override
    // public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    //     return userRepository.findByEmail(email)
    //         .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + email));
    // }
    
    /**
     * Récupérer les infos d'un utilisateur pour la communication inter-services
     */
    public UserInfoDto getUserInfoDto(String id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + id));
        return convertToUserInfoDto(user);
    }
    
    /**
     * Convertit une entité User en UserResponse
     */
    private UserResponse convertToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        return response;
    }
    
    /**
     * Convertit une entité User en UserInfoDto (pour les appels inter-services)
     */
    private UserInfoDto convertToUserInfoDto(User user) {
        return new UserInfoDto(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole()
        );
    }
    
    /**
     * Statistiques des utilisateurs
     */
    public Map<String, Long> getUserStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("adminUsers", userRepository.countByRole("ADMIN"));
        stats.put("regularUsers", userRepository.countByRole("USER"));
        return stats;
    }
}