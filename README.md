# Bonial Brochures — Android Code Challenge

A native Android application that fetches and displays retailer brochures from the Bonial shelf API, built with Clean Architecture, MVVM, and Jetpack Compose.

---

## Features

- Fetches brochures from the Bonial shelf API
- Displays brochures in a responsive grid (2 columns portrait, 3 columns landscape)
- Premium brochures (`brochurePremium`) span the full width of the grid
- Filters out items with `contentType == superBannerCarousel`
- Filters out items with `distance >= 5.0km`
- Items with no GPS distance data are included
- Lazy-loaded brochure images with Coil
- Loading, success, and error states
- Bonial brand colour scheme (`#ED3136` primary)
- Status bar safe area padding

---

## Architecture

Clean Architecture with MVVM, strictly separated into three layers:

```
presentation/   — Compose UI, ViewModel, UiState
domain/         — Brochure model, BrochureRepository interface, GetFilteredBrochuresUseCase
data/           — DTOs, Retrofit API service, BrochureRepositoryImpl, ContentItemAdapter
di/             — Hilt NetworkModule, RepositoryModule
```

### Key decisions

- All filtering logic lives exclusively in `GetFilteredBrochuresUseCase` — zero filtering in the ViewModel or UI layer
- `ContentItemAdapter` is a custom Moshi adapter that safely handles the `superBannerCarousel` items whose `content` field is a JSON array instead of an object
- `BrochureContent` serves as the data-layer DTO, mapped to the domain `Brochure` model in the repository
- `isPremium` is a computed property on the `Brochure` domain model, not stored state

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin 2.2.10 |
| UI | Jetpack Compose + Material3 |
| Architecture | Clean Architecture + MVVM |
| DI | Hilt 2.59.2 + KSP |
| Networking | Retrofit 2 + Moshi |
| Image Loading | Coil 2.7.0 |
| Async | Kotlin Coroutines + StateFlow |
| Testing | JUnit 4 + MockK + Turbine + Coroutines Test |
| Coverage | JaCoCo 0.8.11 |

---

## API

**Endpoint:** `GET https://mobile-s3-test-assets.aws-sdlc-bonial.com/shelf.json`

**Filtering rules applied:**

- Include: `contentType` in `["brochure", "brochurePremium"]`
- Include: `distance == null` OR `distance < 5.0`
- Exclude: everything else

---

## Project Structure

```
app/src/main/java/com/jakana/bonialbrochures/
├── BonialApp.kt
├── MainActivity.kt
├── data/
│   ├── model/
│   │   ├── ShelfResponse.kt
│   │   └── ContentItemAdapter.kt
│   ├── remote/
│   │   └── ShelfApiService.kt
│   └── repository/
│       └── BrochureRepositoryImpl.kt
├── di/
│   ├── NetworkModule.kt
│   └── RepositoryModule.kt
├── domain/
│   ├── model/
│   │   └── Brochure.kt
│   ├── repository/
│   │   └── BrochureRepository.kt
│   └── usecase/
│       └── GetFilteredBrochureUseCase.kt
└── presentation/
    ├── BrochureUiState.kt
    ├── BrochureViewModel.kt
    ├── theme/
    │   ├── Color.kt
    │   ├── Theme.kt
    │   └── Type.kt
    └── ui/
        ├── BrochureScreen.kt
        ├── BrochureGrid.kt
        └── BrochureCard.kt
```

---

## Testing

Unit tests cover all testable business logic across domain, data, and presentation layers.

**Run tests:**
```bash
./gradlew testDebugUnitTest
```

**Generate coverage report:**
```bash
./gradlew clean testDebugUnitTest jacocoTestReport
```

Report output: `app/build/reports/jacoco/jacocoTestReport/html/index.html`

### Coverage summary

| Package | Coverage |
|---------|----------|
| `domain.usecase` | 100% |
| `presentation` (ViewModel) | 98% |
| `data.repository` | 96% |
| `data.model` (Adapter) | 90%+ |
| **Overall (testable classes)** | **~90%** |

> UI composables, theme declarations, and DI modules are excluded from coverage metrics — these are not unit-testable by nature.

### Test classes

| Test Class | Layer | Cases |
|-----------|-------|-------|
| `GetFilteredBrochuresUseCaseTest` | Domain | 8 |
| `BrochureRepositoryImplTest` | Data | 7 |
| `BrochureViewModelTest` | Presentation | 4 |
| `ContentItemAdapterTest` | Data | 5 |

---

## Setup

1. Clone the repository
2. Open in Android Studio Hedgehog or later
3. Sync Gradle
4. Run on an emulator or physical device (minSdk 24)

No API keys or additional configuration required.

---

## Build Requirements

- Android Studio Hedgehog+
- Kotlin 2.2.10
- AGP with built-in Kotlin support
- Minimum SDK: 24
- Target SDK: 36

---

## Notes

- The shelf API returns a `superBannerCarousel` item at index 0 whose `content` field is a JSON array. A custom Moshi adapter (`ContentItemAdapter`) handles this gracefully by skipping array-typed content fields and returning `null`, which is then filtered out in the repository layer.
- Distance filtering uses strict less-than (`< 5.0`) — items at exactly 5.0km are excluded.
- Items with `null` distance (no GPS data available) are always included per the business requirement.