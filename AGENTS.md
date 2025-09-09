# Repository Guidelines

## Project Structure & Modules
- `androidApp/`: Android app (Jetpack Compose, Koin, Firebase, Ktor). Unit tests in `androidApp/src/test/...`.
- `iosApp/`: SwiftUI shell that consumes the shared KMP framework.
- `shared/`: Kotlin Multiplatform logic (`commonMain`, `androidMain`, `iosMain`). Android host/device tests in `shared/src/androidHostTest` and `shared/src/androidDeviceTest`.
- Tooling: Gradle Kotlin DSL (`build.gradle.kts`, `settings.gradle.kts`), GitHub Actions in `.github/workflows/`, git hooks in `.githooks/`.

## Build, Test, and Development
- Build all: `./gradlew build` — compiles all modules.
- Android debug APK: `./gradlew :androidApp:assembleDebug` — outputs under `androidApp/build/`.
- Tests: `./gradlew test` — runs unit tests (JUnit 5 platform).
- Lint: `./gradlew lint` — Android Lint checks.
- Formatting: `./gradlew ktlintCheck` / `ktlintFormat` — verify/auto-fix Kotlin style.
- Coverage: `./gradlew jacocoTestReport` — HTML/XML at `androidApp/build/reports/jacoco/...`.

Requirements: JDK 17, Android SDK compile 35 (minSdk 26). For Firebase features, place `androidApp/google-services.json` locally.

## Coding Style & Naming
- Kotlin with 4-space indentation; trailing whitespace trimmed (see `.editorconfig`).
- Ktlint enforced via Gradle and pre-commit hook; function/property naming rules are relaxed.
- Packages: `io.github.hanihashemi.tomaten...`. UI files follow `*Screen`, `*Dialog`, `*ViewModel`, and features under `ui/components`, `ui/screens`.

## Testing Guidelines
- Frameworks: JUnit 5 (`useJUnitPlatform()`), Mockito-Kotlin, Turbine, Coroutines Test.
- Locations: Android unit tests in `androidApp/src/test/...`; shared Android host/device tests in `shared/src/androidHostTest` and `shared/src/androidDeviceTest`.
- Coverage: JaCoCo excludes UI and app wiring; CI enforces ~40% overall and ~60% changed-files coverage. Prefer testing business logic in `shared/` and ViewModels.

## Commit & Pull Requests
- Messages: use conventional prefixes (`feat:`, `fix:`, `refactor:`, `chore:`, `ci:`). Example: `feat: add tag filtering to stats`.
- Branching: open PRs against `development`.
- PRs should include: clear description, linked issues, test plan, and screenshots/GIFs for UI.
- Enable hooks: `git config core.hooksPath .githooks` (runs `ktlintCheck` on commit). Ensure CI (tests, lint, ktlint) passes.

