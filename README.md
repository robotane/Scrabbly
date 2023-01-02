# Scrabbly

Un clone du jeu Scrabble développé avec libGDX, déployable sur trois plateformes
à partir d'un cœur de jeu partagé.

## Structure du projet

- `Scrabbly/` — logique de jeu partagée (règles, plateau, calcul des scores)
- `Scrabbly-android/` — client Android
- `Scrabbly-desktop/` — client Desktop (LWJGL)
- `Scrabbly-html/` — client Web (GWT)

## Build

Projet structuré selon l'organisation multi-module libGDX classique de l'époque
(Eclipse ADT / Ant, pas Gradle). Chaque module se compile indépendamment depuis
son propre dossier.

Projet réalisé dans un cadre d'apprentissage ; l'architecture multi-plateforme
(un seul moteur de jeu, trois frontends) était l'objectif principal.
