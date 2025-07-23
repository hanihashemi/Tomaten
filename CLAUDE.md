# Claude Context

This file contains important context and configuration for Claude Code to understand and work with this project effectively.

## Project Overview
Tomaten - A Pomodoro timer application

## Development Commands
- Build: `./gradlew build`
- Test: `./gradlew test`  
- Lint: `./gradlew lint`
- Ktlint check: `./gradlew ktlintCheck`
- Ktlint format: `./gradlew ktlintFormat`

## Git Hooks Setup
To enable pre-commit ktlint checking for all contributors:

```bash
# Set up git hooks directory
git config core.hooksPath .githooks
```

This will run `./gradlew ktlintCheck` before each commit and prevent commits with formatting issues.

## Important Notes
- Pre-commit hook runs ktlint check automatically
- Use `./gradlew ktlintFormat` to fix formatting issues
- CI runs tests, lint, and ktlint checks in parallel