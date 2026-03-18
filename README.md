# 🏥 Hospital Patient Manager

> Application de bureau complète pour la gestion hospitalière, développée en **Java 25 + JavaFX** dans le cadre d'un mini-projet académique pour le module IHM (Interfaces Homme-Machine).

---

## 📋 Table des matières

- [Présentation](#présentation)
- [Fonctionnalités](#fonctionnalités)
- [Technologies utilisées](#technologies-utilisées)
- [Structure du projet](#structure-du-projet)
- [Interfaces Java](#interfaces-java)
- [Design Patterns](#design-patterns)
- [Prérequis](#prérequis)
- [Installation et configuration](#installation-et-configuration)
- [Lancement de l'application](#lancement-de-lapplication)
- [Identifiants de connexion](#identifiants-de-connexion)
- [Persistance des données](#persistance-des-données)
- [Auteur](#auteur)

---

## Présentation

Hospital Patient Manager est une application de bureau JavaFX qui centralise la gestion des opérations hospitalières. Elle permet aux administrateurs de gérer les patients, les médecins, les chambres et les rendez-vous via une interface utilisateur moderne, animée, avec support du mode sombre et sauvegarde automatique des données.

---

## Fonctionnalités

### 👥 Gestion des Patients
- Ajouter, modifier et supprimer des patients
- Recherche par nom ou maladie
- Assigner les patients à des chambres hospitalières
- Opérations CRUD complètes via les interfaces Java

### 👨‍⚕️ Gestion des Médecins
- Ajouter, modifier et supprimer des médecins
- Enregistrer la spécialisation et le numéro de téléphone
- Liaison avec les rendez-vous

### 🛏️ Gestion des Chambres
- Ajouter et supprimer des chambres (Simple, Double, VIP, Urgence)
- Assigner / libérer des patients
- Suivi du statut en temps réel (Disponible / Occupée)
- Lignes colorées dans le tableau (vert = disponible, rouge = occupée)

### 📅 Gestion des Rendez-vous
- Planifier des rendez-vous entre patients et médecins
- Définir la date et l'heure
- Modifier et supprimer les rendez-vous

### 📊 Statistiques
- Graphique circulaire (Pie Chart) — taux d'occupation des chambres
- Graphique à barres (Bar Chart) — vue générale de l'hôpital
- Taux d'occupation en temps réel avec barre visuelle

### ✨ Fonctionnalités supplémentaires
- 🔐 Page de connexion sécurisée avec animations (fade + shake)
- 🌙 Basculement Dark / Light mode
- 💾 Sauvegarde automatique en JSON (les données persistent après redémarrage)
- 🔔 Notifications popup (succès / erreur)
- ✨ Animations fluides (slide-in, fade-in, pulse, shake)
- 📜 Formulaires scrollables sur toutes les pages

---

## Technologies utilisées

| Technologie | Version | Utilisation |
|-------------|---------|-------------|
| Java | 25 (Zulu JDK) | Langage principal |
| JavaFX | 25.0.2 | Framework graphique |
| FXML | — | Mise en page de l'interface |
| CSS | — | Style et thèmes |
| JSON (manuel) | — | Persistance des données |
| IntelliJ IDEA | 2025.3.2 | IDE de développement |

---

## Structure du projet

```
HospitalPatientManager/
│
├── src/
│   ├── MainApp.java                    ← Point d'entrée JavaFX
│   │
│   ├── interfaces/
│   │   ├── Manageable.java             ← Interface CRUD générique
│   │   ├── Displayable.java            ← Interface d'affichage
│   │   └── Searchable.java             ← Interface de recherche
│   │
│   ├── models/
│   │   ├── Patient.java                ← Implémente les 3 interfaces
│   │   ├── Doctor.java
│   │   ├── Room.java
│   │   └── Appointment.java
│   │
│   ├── managers/
│   │   ├── DataStore.java              ← Singleton — données partagées
│   │   ├── PatientManager.java         ← Implémente les 3 interfaces
│   │   └── JsonManager.java            ← Sauvegarde/Chargement JSON
│   │
│   └── controllers/
│       ├── MainController.java         ← Navigation + Page Patients
│       ├── DoctorController.java       ← Page Médecins
│       ├── RoomController.java         ← Page Chambres
│       ├── AppointmentController.java  ← Page Rendez-vous
│       ├── StatsController.java        ← Page Statistiques
│       ├── LoginController.java        ← Page de connexion
│       ├── AnimationManager.java       ← Gestionnaire des animations
│       └── ThemeManager.java           ← Gestionnaire Dark/Light mode
│
└── resources/
    ├── main.fxml                       ← Mise en page principale + Patients
    ├── doctors.fxml                    ← Page Médecins
    ├── rooms.fxml                      ← Page Chambres
    ├── appointments.fxml               ← Page Rendez-vous
    ├── stats.fxml                      ← Page Statistiques
    ├── login.fxml                      ← Page de connexion
    └── style.css                       ← Feuille de style globale
```

---

## Interfaces Java

Ce projet utilise intensivement les **interfaces Java génériques**, conformément aux exigences du module IHM :

### `Manageable<T>`
```java
public interface Manageable<T> {
    void add(T item);                  // Ajouter un élément
    void remove(T item);               // Supprimer un élément
    void update(T oldItem, T newItem); // Mettre à jour un élément
}
```
Définit les opérations CRUD de base pour n'importe quel type.

### `Displayable<T>`
```java
public interface Displayable<T> {
    List<T> display(); // Retourner la liste complète
}
```
Retourne la liste complète des éléments pour l'affichage dans la TableView.

### `Searchable<T>`
```java
public interface Searchable<T> {
    List<T> search(String keyword); // Rechercher par mot-clé
}
```
Permet la recherche par mot-clé dans n'importe quelle collection.

**Exemple d'implémentation :**
```java
// La classe Patient implémente les 3 interfaces
public class Patient implements Manageable<Patient>,
                                Displayable<Patient>,
                                Searchable<Patient> { ... }

// Le gestionnaire implémente également les 3 interfaces
public class PatientManager implements Manageable<Patient>,
                                       Displayable<Patient>,
                                       Searchable<Patient> { ... }
```

---

## Design Patterns

### Singleton — `DataStore`
La classe `DataStore` est implémentée en tant que Singleton pour garantir une source de données unique et partagée entre tous les contrôleurs :

```java
public class DataStore {
    private static DataStore instance;

    // Constructeur privé — empêche l'instanciation directe
    private DataStore() { }

    // Point d'accès unique à l'instance
    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }
}
```

### Architecture MVC
- **Modèle (Model)** — package `models/` (Patient, Doctor, Room, Appointment)
- **Vue (View)** — fichiers FXML + CSS dans `resources/`
- **Contrôleur (Controller)** — package `controllers/`

---

## Prérequis

- **Java JDK 17 ou supérieur** (testé avec Zulu 25)
- **JavaFX SDK 25.0.2** — [Télécharger ici](https://gluonhq.com/products/javafx/)
- **IntelliJ IDEA** (recommandé)

---

## Installation et configuration

### 1. Télécharger le projet
Télécharger ou cloner le projet dans votre répertoire de travail.

### 2. Télécharger le SDK JavaFX
Télécharger JavaFX SDK 25.0.2 depuis [Gluon](https://gluonhq.com/products/javafx/) et extraire vers :
```
C:\javafx\javafx-sdk-25.0.2\
```

### 3. Configurer IntelliJ IDEA

**Ajouter JavaFX comme bibliothèque :**
1. `Fichier → Structure du projet → Bibliothèques → +`
2. Sélectionner `C:\javafx\javafx-sdk-25.0.2\lib\`
3. Choisir **Jar Directory** → OK

**Ajouter les options VM :**
1. `Exécuter → Modifier les configurations`
2. `Modifier les options → Ajouter les options VM`
3. Coller :
```
--module-path "C:\javafx\javafx-sdk-25.0.2\lib" --add-modules javafx.controls,javafx.fxml
```

### 4. Marquer le dossier Resources
Clic droit sur `src/resources/` → **Marquer le répertoire comme → Racine des ressources**

---

## Lancement de l'application

1. Ouvrir `MainApp.java`
2. Cliquer sur le bouton ▶️ Exécuter
3. La page de connexion s'affiche

---

## Identifiants de connexion

| Champ | Valeur |
|-------|--------|
| Nom d'utilisateur | `admin` |
| Mot de passe | `admin123` |

---

## Persistance des données

Toutes les données sont automatiquement sauvegardées en JSON dans le dossier :
```
~/HospitalData/
  ├── patients.json
  ├── doctors.json
  ├── rooms.json
  └── appointments.json
```

Les données sont chargées automatiquement au démarrage. Si aucune donnée sauvegardée n'existe, des données de démonstration par défaut sont chargées.

---

## Auteur

| | |
|---|---|
| **Nom** | Youssef B'SIBISS |
| **Filière** | DUT Informatique |
| **Module** | IHM — Interfaces Homme-Machine |
| **Encadrant** | Pr. Hattab Kamal |
| **Année universitaire** | 2025 – 2026 |

---

> *"Un code propre ne se contente pas de fonctionner — il communique clairement son intention."*
