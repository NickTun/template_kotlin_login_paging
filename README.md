MVI Scaffold — UI Learning Tasks

This project contains a lightweight MVI scaffold intended for learning Compose UI by implementing the views yourself.

Structure
- `app/src/main/java/com/pedro/test_1/core` — shared constants
- `app/src/main/java/com/pedro/test_1/data/models` — domain/data models
- `app/src/main/java/com/pedro/test_1/data/source` — data source interfaces
- `app/src/main/java/com/pedro/test_1/data/repo` — repository interfaces and `FakeRepository`
- `app/src/main/java/com/pedro/test_1/domain/usecase` — simple use-case wrappers
- `app/src/main/java/com/pedro/test_1/ui/screen/*` — MVI primitives for each screen (intents, states, ui-actions, ViewModels)
- `app/src/main/java/com/pedro/test_1/ui/nav` — navigation destination constants

What I scaffolded for you
- Typed models: `User`, `ListItem`, `AuthResult`.
- `Repository` interface and `FakeRepository` that returns large lists and simulates login.
- Sealed `Intent`, `State`, and `UiAction` types for `splash`, `auth`, `main`, and `detail` screens.
- `ViewModel` implementations for each screen that expose `StateFlow` and `SharedFlow` for one-shot actions.

Learning tasks (for you)
1) Splash: implement `SplashScreen` Compose UI. Observe `SplashViewModel.state` and handle navigation with `actions`.
2) Auth: implement `AuthScreen` with a username field and password field. Enforce input limits (`MAX_INPUT_LENGTH`). Dispatch `AuthIntent.UpdateInput` and `AuthIntent.Submit`. Show loading/error states.
3) Main list: implement `MainScreen` showing a large list (use `LazyColumn` / `LazyListState`) and implement pull-to-refresh and infinite scroll by dispatching `FetchPage` intents. Use `MainViewModel.state` to render loading and loading-more indicators.
4) Detail: implement `DetailScreen` that reads item id from nav args and dispatches `DetailIntent.Load`. Render `DetailState.Data`.
5) Polishing: add accessibility labels, content descriptions, and responsive layouts.

Hints
- The `FakeRepository` provides 1000 items and simulates delays — use it to test list performance and pagination.
- ViewModels expose flows: observe `state` for UI updates and `actions` to perform navigation.
- Do not call repository directly from Composables — use intents.

How to run tests (stubs)
- There are small unit-test stubs planned; you can write tests against the ViewModels by injecting `FakeRepository`.

If you want, I can:
- Wire a simple `NavHost` and placeholder Composables to make the app runnable immediately.
- Add unit tests for ViewModels.
