# User Service - Gestion des utilisateurs

Le service qui s'occupe de tout ce qui touche aux utilisateurs : inscription, connexion, profils, etc. Rien de trop fancy pour l'instant, mais ça fait l'taff !

## C'que ça fait

### Fonctionnalités principales

- **Gestion des utilisateurs** : Création, modification, suppression
- **Authentification basique** : Login/logout simple
- **Profils utilisateur** : Infos perso, préférences
- **Rôles** : Admin, User (extensible)
- **Validation** : Email unique, mots de passe sécurisés

### Endpoints disponibles

#### Publics
- `POST /api/users/register` - Inscription d'un nouvel utilisateur
- `POST /api/users/login` - Connexion
- `GET /api/users/test` - Test que le service tourne

#### Protégés (auth requise)
- `GET /api/users` - Liste tous les utilisateurs (admin only)
- `GET /api/users/{id}` - Récupère un utilisateur par ID
- `PUT /api/users/{id}` - Met à jour un utilisateur
- `DELETE /api/users/{id}` - Supprime un utilisateur
- `GET /api/users/profile` - Profil de l'utilisateur connecté
- `GET /api/users/search` - Recherche par nom
- `GET /api/users/role/{role}` - Utilisateurs par rôle
- `GET /api/users/stats` - Statistiques des utilisateurs

#### Internes (pour les autres services)
- `GET /internal/users/{id}` - Infos utilisateur allégées
- `POST /internal/users/validate` - Validation d'existence

## Stack technique

- **Spring Boot 3.2** : Framework principal
- **Spring Security** : Authentification et autorisation
- **MongoDB** : Base de données
- **Spring Data MongoDB** : ORM pour MongoDB
- **BCrypt** : Hashage des mots de passe
- **Validation API** : Validation des données
- **WebClient** : Communication avec les autres services

## Comment lancer ?

### Prérequis
- Java 17+
- Maven
- MongoDB qui tourne (port 27017)

### Lancement

```bash
# Depuis le dossier user-service
mvn spring-boot:run
```

Le service démarre sur le port **8082**.

### Avec Docker

```bash
# Build l'image
docker build -t user-service .

# Run le container
docker run -p 8082:8082 user-service
```

## Configuration

### Variables d'environnement

- `MONGODB_URI` : URI de connexion MongoDB (défaut: mongodb://localhost:27017/userdb)
- `JWT_SECRET` : Clé secrète pour les tokens JWT
- `SERVER_PORT` : Port du service (défaut: 8082)

### Base de données

Le service crée automatiquement :
- Collection `users` : Données utilisateurs
- Index unique sur l'email
- Utilisateur admin par défaut (si pas d'admin existant)

## Modèle de données

### User
```json
{
  "id": "string",
  "firstName": "string",
  "lastName": "string", 
  "email": "string (unique)",
  "password": "string (hashé)",
  "role": "USER|ADMIN",
  "isActive": "boolean",
  "createdAt": "datetime",
  "updatedAt": "datetime",
  "lastLogin": "datetime"
}
```

## Exemples d'utilisation

### Inscription
```bash
curl -X POST http://localhost:8082/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "password": "motdepasse123"
  }'
```

### Connexion
```bash
curl -X POST http://localhost:8082/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "motdepasse123"
  }'
```

### Récupérer son profil
```bash
curl -H "Authorization: Bearer ton-jwt-token" \
  http://localhost:8082/api/users/profile
```

## Sécurité

- **Mots de passe** : Hashés avec BCrypt (force 12)
- **JWT** : Tokens avec expiration (24h par défaut)
- **Validation** : Email format, mot de passe minimum 6 caractères
- **CORS** : Configuré pour le développement
- **Endpoints protégés** : Authentification requise

## Logs et monitoring

- Logs structurés avec Logback
- Métriques Spring Boot Actuator
- Health check sur `/actuator/health`
- Infos service sur `/actuator/info`

## Problèmes courants

**Service ne démarre pas ?**
→ Vérifie que MongoDB tourne et que le port 8082 est libre

**Erreur de connexion MongoDB ?**
→ Check l'URI de connexion dans application.properties

**JWT invalide ?**
→ Vérifie que la clé secrète est bien configurée

**Email déjà utilisé ?**
→ Normal, l'email doit être unique par utilisateur

---
