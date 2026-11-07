# 🪙 Market Coins - Kotlin Multiplatform App

![Kotlin](https://img.shields.io/badge/Kotlin-2.0%2B-blue.svg?logo=kotlin)
![Compose Multiplatform](https://img.shields.io/badge/Compose_Multiplatform-UI-4285F4?logo=jetpackcompose)
![Android](https://img.shields.io/badge/Android-Supported-3DDC84?logo=android)
![iOS](https://img.shields.io/badge/iOS-Supported-000000?logo=apple)
![Architecture](https://img.shields.io/badge/Architecture-MVI_%7C_Clean-orange)

A modern, fully functional cryptocurrency tracking application built with **Kotlin Multiplatform (KMP)** and **Compose Multiplatform**. This project serves as a showcase of modern mobile development practices, sharing **100% of the UI and Business Logic** across iOS and Android.

---

## 📱 Screenshots

> **Note:** Add screenshots or a GIF here later.
> 
> | Android (Material 3) | iOS (Native feel) | Coin Details & Chart |
> | :---: | :---: | :---: |
> | `<img src="docs/android_list.png" width="250"/>` | `<img src="docs/ios_list.png" width="250"/>` | `<img src="docs/details_chart.png" width="250"/>` |

---

## ✨ Features

- **Real-time Market Data:** Fetches top cryptocurrencies using the CoinGecko API.
- **Interactive Price Charts:** Custom Canvas-based smoothed line charts with gradient fills, supporting multiple timeframes (24H, 7D, 30D, 1Y).
- **Watchlist (Favorites):** Save favorite coins to a local database. Data persists across app restarts.
- **Cross-Platform UI:** Beautiful, responsive UI built entirely with Compose Multiplatform, adapting safely to iOS notches/Dynamic Islands and Android system bars.
- **Resilient Error Handling:** Graceful handling of network errors and API Rate Limits (HTTP 429) with non-blocking, auto-dismissible custom notifications.

---

## 🏗 Architecture (MVI + Clean Architecture)

This project strictly adheres to **Unidirectional Data Flow (UDF)** using a custom-built **MVI (Model-View-Intent)** framework. This ensures predictability, thread safety, and extreme testability.

### Core MVI Components:
- **`BaseViewModel`**: Binds the architectural components together. Automatically handles `viewModelScope` cancellation and lifecycle events.
- **`StateUpdater` (Pure Functions)**: Receives the current `State` and an `Action`, returning a new `State` along with optional `SideEffect`s and UI `Event`s. Contains **no** asynchronous code, making it 100% unit-testable without mocks.
- **`Processor`**: Listens for `SideEffect`s, performs asynchronous tasks (Network, Database), and feeds the result back as an `Action`.

---

## 🛠 Tech Stack

- **[Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)** - Core business logic sharing.
- **[Compose Multiplatform](https://www.jetbrains.com/lp/compose-mpp/)** - Declarative UI shared across Android and iOS.
- **[Ktor 3](https://ktor.io/)** - Asynchronous HTTP client with Content Negotiation & Logging.
- **[Koin](https://insert-koin.io/)** - Lightweight Dependency Injection framework.
- **[SQLDelight 2](https://sqldelight.github.io/sqldelight/)** - Type-safe, multiplatform SQLite database for local caching (Watchlist).
- **[Coil 3](https://coil-kt.github.io/coil/)** - Fast, multiplatform image loading.
- **[Navigation 3 (Jetpack)](https://developer.android.com/guide/navigation)** - Multiplatform navigation with type-safe arguments.
- **[Kotlinx Serialization & Datetime](https://github.com/Kotlin/kotlinx.serialization)** - Cross-platform JSON parsing and date handling.

---

## 🧪 Testing

The presentation layer is heavily unit-tested. Because the MVI `StateUpdater` relies purely on inputs and outputs without side effects, testing the presentation logic is fast and reliable across both platforms (`commonTest`).

Run the tests using:
```bash
./gradlew :shared:testAndroidHostTest
```

---

## 🚀 Getting Started

### Prerequisites
- [Android Studio](https://developer.android.com/studio) (Koala or newer)
- [Xcode 15+](https://developer.apple.com/xcode/) (for iOS development)
- JDK 17+

### Build & Run
1. Clone the repository:
   ```bash
   git clone https://github.com/YourUsername/MarketCoinsList.git
   ```
2. Open the project in Android Studio.
3. **Android**: Select the `androidApp` configuration and press **Run** (▶️).
4. **iOS**: Select the `iosApp` configuration and an iPhone Simulator, then press **Run** (▶️). Alternatively, open `iosApp/iosApp.xcodeproj` in Xcode.

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
