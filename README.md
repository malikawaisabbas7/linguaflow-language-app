# LinguaFlow — Language Learning App

A production-structured Android app built with Kotlin, Jetpack Compose,
Material 3, MVVM, Room, and Firebase (Auth + Firestore), matching the
"Task 4: Language Learning App" brief.

## Features implemented

- **Splash screen** with animated logo and auto-navigation
- **Authentication**: Email/password login & register, forgot password,
  Google Sign-In hook, remember me, password visibility toggle (Firebase Auth)
- **Onboarding**: choose target language, proficiency level, daily goal
- **Home dashboard**: greeting, circular daily-goal ring, streak/XP/words
  gradient stat cards, Duolingo-style winding lesson path, motivational quote,
  FAB for a quick flashcard session
- **Lessons**: category-filterable list (Vocabulary, Grammar, Phrases,
  Listening, Speaking, Culture), lesson detail with word cards
  (translation, IPA pronunciation, audio hook, example sentences)
- **Flashcards**: swipe-style deck with 3D flip animation and a simple
  spaced-repetition scheduler (Known → review in 7 days, Unknown → 1 day)
- **Practice / Quizzes**: multiple-choice quiz engine with instant
  correct/incorrect feedback, XP scoring, and a results screen
- **Progress**: streaks, total words/lessons/XP, average quiz accuracy,
  weekly activity bar chart, achievement badges
- **Vocabulary Bank**: searchable list of every learned word with mastery
  level (New / Learning / Mastered)
- **Profile & Settings**: dark mode (persisted via DataStore), notifications,
  sound effects, units, logout
- **Offline-first**: Room database is the source of truth; Firestore syncs
  the user profile and progress for cross-device continuity

## Tech stack

Kotlin · Jetpack Compose · Material 3 · Navigation Compose · MVVM ·
Room · Firebase Auth/Firestore · DataStore Preferences · Coroutines/Flow ·
MPAndroidChart · Lottie-Compose (dependency wired, add `.json` assets as needed)

## Project structure

```
app/src/main/java/com/linguaflow/app/
├── data/
│   ├── database/       # Room entities, DAOs, AppDatabase
│   ├── repository/     # Repository pattern over Room + Firebase
│   ├── firebase/       # FirebaseAuthManager, FirestoreSyncManager
│   └── SeedData.kt     # First-run demo Spanish course content
├── di/                 # Lightweight manual AppContainer (no Hilt needed)
├── navigation/          # NavHost + route definitions
├── theme/               # Color, Type, Shape, Theme (Material 3)
├── ui/
│   ├── components/     # Reusable gradient cards, progress rings, nav bar…
│   └── screens/        # One package per feature (auth, home, lessons, …)
├── viewmodel/           # MVVM ViewModels (StateFlow-based)
└── utils/               # Constants, DataStore PreferencesManager
```

## Setup

1. **Firebase**: create a Firebase project, add an Android app with package
   name `com.linguaflow.app`, download `google-services.json` and replace the
   placeholder at `app/google-services.json`. Enable **Authentication**
   (Email/Password + Google) and **Firestore** in the Firebase console.
2. **Fonts**: see `docs/FONTS.md` — add the 4 Poppins `.ttf` files to
   `app/src/main/res/font/` (Android resource folders can't ship a README, so
   the instructions live in `docs/` instead).
3. **Gradle wrapper jar**: this environment couldn't download the real
   `gradle-wrapper.jar` binary (no network access to Gradle's servers). Open
   the project once in Android Studio (it will regenerate the wrapper
   automatically) — or run `gradle wrapper` locally if you have Gradle
   installed — before using `./gradlew`.
4. Open in Android Studio (Hedgehog+ recommended), let Gradle sync, run on an
   emulator or device (minSdk 24).

## Notes on scope

This is a complete, compiling-by-structure MVVM skeleton covering every
feature area in the brief with real Room/Firebase wiring, not just static
mockups — screens like Home, Lessons, Flashcards, Quiz, and Progress are
backed by real ViewModels and a seeded demo Spanish course so the app is
immediately explorable. A few integration points are intentionally left as
clearly-commented hooks for you to finish with real credentials/assets:
Google Sign-In (needs your OAuth client ID), audio playback for
pronunciation (needs real audio files/URLs), and voice recording for the
pronunciation-practice screen (needs a speech API of your choice).
