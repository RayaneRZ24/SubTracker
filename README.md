# 📊 SubTracker - Gestionnaire d'Abonnements

**SubTracker** est une application de bureau moderne développée en **JavaFX** pour vous aider à gérer vos abonnements (Netflix, Spotify, assurances, etc.), suivre vos dépenses mensuelles et analyser votre budget.

## 🚀 Fonctionnalités

*   **Tableau de Bord** : Vue d'ensemble de vos abonnements actifs et du coût total mensuel.
*   **Analytique** : Graphiques visuels (Camembert, Barres) pour comprendre la répartition de vos dépenses par catégorie.
*   **Gestion Complète** : Ajoutez, modifiez et supprimez vos abonnements facilement.
*   **Authentification Sécurisée** : Chaque utilisateur possède son propre compte et ses propres données isolées.
*   **Mode Sombre** : Interface élégante et moderne (Dark Theme).
*   **Paramètres** : Gestion de profil, changement de mot de passe et préférences.

## 🛠️ Prérequis

Avant de lancer l'application, assurez-vous d'avoir :

*   **Java JDK 21** ou supérieur.
*   **Maven** (installé et configuré dans le PATH).
*   **SQL Server** (Express ou Developer edition).

## ⚙️ Installation et Configuration

### 1. Base de Données (SQL Server)

1.  Ouvrez **SQL Server Management Studio (SSMS)**.
2.  Créez une nouvelle base de données nommée `subtracker_db`.
3.  Exécutez le script SQL situé dans `database/schema.sql` pour créer les tables nécessaires (`users`, `abonnements`).

### 2. Configuration de l'Application

1.  Ouvrez le fichier `src/main/resources/database.properties`.
2.  Modifiez les informations de connexion si nécessaire (par défaut configuré pour l'authentification Windows ou User 'sa') :
    ```properties
    db.url=jdbc:sqlserver://localhost:1433;databaseName=subtracker_db;encrypt=true;trustServerCertificate=true
    db.user=sa
    db.password=votre_mot_de_passe
    ```

### 3. Compilation

À la racine du projet, lancez :
```bash
mvn clean install
```

## ▶️ Comment Lancer l'Application

### Via le script automatique (Recommandé sur Windows)
Double-cliquez sur le fichier `run.bat` à la racine du projet, ou lancez-le depuis un terminal :
```cmd
.\run.bat
```
*Note : Ce script configure automatiquement la variable `JAVA_HOME` pour la session.*

### Via Maven
```bash
mvn clean javafx:run
```

## 📂 Structure du Projet

*   `src/main/java` : Code source Java (Contrôleurs, Modèles, Services, DAO).
*   `src/main/resources` : Fichiers FXML (Vues), CSS, Images et configuration.
*   `database` : Scripts SQL d'initialisation.

## 🤝 Contribution

Projet réalisé dans le cadre académique.
