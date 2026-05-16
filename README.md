# Grama-Suvidha Portal

A modern Android application for rural infrastructure transparency, built with **Kotlin**, **Jetpack Compose**, **MVVM**, **Room**, **Retrofit**, **Coroutines + Flow**, **Coil**, and **Material 3**.

## Purpose

Grama-Suvidha Portal helps Panchayat citizens track government-funded village infrastructure work in a simple, accessible, bilingual experience. The interface uses a rural development palette of green and blue, large readable cards, smooth progress animations, and offline-first cached project data.

## Core Features

- Splash screen with village/government-inspired branding and the tagline **“Digital Progress for Every Village”**.
- Home dashboard with Panchayat summary metrics, completed-project badges, search, status filters, pull-to-refresh, offline banner, and progress analytics chart.
- Project list for:
  - Main Road Repair
  - Borewell Installation
  - Community Hall Construction
  - Pond Rejuvenation
  - Streetlight Installation
- Project detail screen with banner image, department, contractor, budget utilization, timeline tracker, animated progress bar, before/after gallery, recent updates, and notification indicator.
- Citizen feedback module with 1–5 star rating, report issue categories, description field, optional image placeholder, submit action, and success snackbar.
- Bilingual English/Kannada support through Android string resources.
- Offline-first Room cache backed by a repository that seeds and refreshes from local JSON mock data.
- Dark mode support through Material 3 color schemes.

## Architecture

```text
app/src/main/java/com/gramasuvidha/portal/
├── data/
│   ├── local/          # Room database, DAO, entity, converters
│   ├── mock/           # Local JSON asset loader and DTO
│   ├── model/          # Domain models and project status enum
│   └── remote/         # Retrofit API contract for future backend integration
├── domain/repository/  # Repository interface
├── repository/         # Offline-first repository implementation
├── ui/
│   ├── components/     # Shared Compose widgets and chart
│   ├── navigation/     # Navigation Compose routes and graph
│   ├── screens/        # Splash, Home, Details, Feedback UI
│   └── theme/          # Material 3 light/dark theme
├── utils/              # Connectivity and localization helpers
└── viewmodel/          # MVVM state holders
```

## Mock Data

Mock project data lives in [`app/src/main/assets/projects.json`](app/src/main/assets/projects.json). The Room cache is seeded from this asset on first launch and refreshed through the repository.

Example payload:

```json
{
  "id": 1,
  "title": "Main Road Repair",
  "budget": 1500000,
  "progress": 65,
  "status": "In Progress",
  "expectedCompletion": "2026-08-10"
}
```

## Localization

- English: `app/src/main/res/values/strings.xml`
- Kannada: `app/src/main/res/values-kn/strings.xml`

The top app bar language toggle switches UI labels between English and Kannada using Android resources.

## Build

```bash
gradle :app:assembleDebug
```

If Android SDK is not configured on the host, install Android Studio or set `ANDROID_HOME` to a valid SDK path before building.

## Screenshots

Screenshot placeholders are provided in [`screenshots/README.md`](screenshots/README.md). Replace them with Android Studio emulator captures before publishing a release.

## Mock API Documentation

See [`docs/mock-api.md`](docs/mock-api.md) for the mock API contract and future Retrofit integration notes.
