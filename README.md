# 🥗 Click and Eat API

**Click and Eat API** est une API REST monolithique modulaire conçue pour gérer les fonctionnalités d'une application de
commande ou de recommandation alimentaire. Elle adopte une architecture **Domain-Driven Design (DDD)** et est déployée
via Docker avec **PostgreSQL** comme base de données et **Dragonfly** comme cache compatible Redis.

## 📖 Table des matières

- [Architecture](#-architecture-modulaire-ddd)
- [Prérequis](#-prérequis)
- [Lancement](#-lancement)
- [Image Docker](#-image-docker)
- [Migrations Flyway](#-migrations-flyway)
- [Configuration](#%EF%B8%8F-exemple-de-fichier-env)
- [Authentification](#-authentification--jwt)
- [Infrastructure](#-infrastructure-technique)

## 🧱 Architecture modulaire DDD

L'API est organisée en modules découplés selon les principes du **Domain-Driven Design** :

- **`authentication/`** : Module métier dédié à l'authentification.
- **`account/`** : Module métier dédié à la création et à la lecture du compte utilisateur.
- **`shared/`** : Contient les composants partagés (exceptions, types, middlewares, etc.).
- **`api/`** : Module central, point d'entrée de l'application, qui orchestre les modules métiers.

Chaque module métier est autonome, avec sa propre logique métier, ses contrôleurs et ses services. Les noms des modules
sont simples et sans préfixe/suffixe (ex. : `authentication`, pas `auth-module`).

## 📦 Prérequis

- **Docker** et **Docker Compose** installés.
- Accès au registre privé `ghcr.io`.
- Un fichier `.env` configuré à la racine du projet.

## 🚀 Lancement

1. Vérifiez que le fichier `.env` est présent et correctement configuré.
2. Lancez la stack avec la commande suivante :

   ```bash
   docker compose up -d
   ```

Cela déploie :

- L'API (conteneur principal).
- Une instance PostgreSQL.
- Une instance Dragonfly (cache compatible Redis).

## 🐳 Image Docker

L'image de l'API est disponible sur le registre privé :

```bash
docker pull ghcr.io/click-and-eat-organization/click-and-eat-api:0.0.1-snapshot
```

⚠️ **Note** : L'accès à l'image est restreint. Assurez-vous d'avoir les droits nécessaires.

## 🧬 Migrations Flyway

Le projet inclut une image Docker Flyway custom définie dans [`CustomFlyway.Dockerfile`](./CustomFlyway.Dockerfile).
Cette image copie les migrations SQL présentes dans [`migrations/src/main/resources/db/migration`](./migrations/src/main/resources/db/migration)
dans le conteneur Flyway, puis exécute les scripts contre la base PostgreSQL.

Pour lancer ces migrations en local, utilisez le script [`scripts/run-flyway-docker-image-dev.sh`](./scripts/run-flyway-docker-image-dev.sh).
Ce script build l'image Flyway custom puis lance le conteneur avec ces 3 variables d'environnement :

- `FLYWAY_URL` : URL JDBC de la base cible
- `FLYWAY_USER` : utilisateur PostgreSQL
- `FLYWAY_PASSWORD` : mot de passe PostgreSQL

Exemple :

```bash
./scripts/run-flyway-docker-image-dev.sh
```

Le script contient directement les valeurs utilisées en environnement de développement.

## ⚙️ Exemple de fichier .env

Voici un exemple de configuration pour le fichier `.env` :

```env
# API
PROJECT_MODE=development
SERVER_PORT=8080
USE_TESTCONTAINERS=false

# Base de données
DB_USERNAME=admin
DB_PASSWORD=securepassword
DB_URL=jdbc:postgresql://localhost:5432/clickandeat
DB_NAME=clickandeat

# JWT
JWT_SECRET=your_jwt_secret_key
EXPIRATION_ACCESS_TOKEN=15m
EXPIRATION_REFRESH_TOKEN=7d

# Cache
REDIS_HOST=localhost
REDIS_PORT=6379

# Docker Compose
DOCKER_DB_PORTS=5432:5432
```

## 🔐 Authentification – JWT

L'API utilise le standard **JWT** avec deux types de tokens :

- **Access Token** : Token de courte durée pour les requêtes authentifiées.
- **Refresh Token** : Token de longue durée pour renouveler les access tokens.

### Variables .env pour JWT

- `JWT_SECRET` : Clé secrète pour signer les tokens.
- `EXPIRATION_ACCESS_TOKEN` : Durée de vie de l'access token (ex. : `15m` pour 15 minutes).
- `EXPIRATION_REFRESH_TOKEN` : Durée de vie du refresh token (ex. : `7d` pour 7 jours).

### Routes de register

Les inscriptions sont maintenant séparées par type d'utilisateur :

- `POST /public/api/v1/authentication/register/consumer`
- `POST /public/api/v1/authentication/register/pro`
- `POST /private/api/v1/authentication/register/admin`

Le contrôleur public reste dédié au login, au refresh token et au logout.

### Routes de login

Les connexions sont également séparées par type d'utilisateur :

- `POST /public/api/v1/authentication/login/consumer`
- `POST /public/api/v1/authentication/login/pro`
- `POST /public/api/v1/authentication/login/admin`

Les routes publiques d'authentification gèrent aussi :

- `POST /public/api/v1/authentication/refresh-token`
- `DELETE /public/api/v1/authentication/logout`

### Route account

L'utilisateur authentifié peut récupérer ses informations de compte via l'identifiant `credentials_id` extrait du JWT :

- `GET /private/api/v1/account/me`
- `GET /private/api/v1/account/me/pro`

Le middleware JWT injecte cet identifiant dans le `CustomUserDetails`, et le controller le transmet au module `account`.
La route `/me` renvoie uniquement les informations communes du compte.
La route `/me/pro` renvoie les informations pro enrichies et est réservée aux comptes `PRO`.

## 🗃 Infrastructure technique

- **Base de données** : PostgreSQL
- **Cache** : Dragonfly (compatible Redis)
- **Architecture** : Monolithe modulaire
- **Langage et framework** : Java 24 / Spring

## 📚 Documentation Git & CI

Consultez les [règles Git et la configuration CI](./docs/GIT_GUIDELINES.md) pour comprendre le fonctionnement des branches, PR et des vérifications automatiques.
