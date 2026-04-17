# MiniCart 🛒

MiniCart is a feature-rich, offline-first Android shopping application built with **Jetpack Compose** and **Room Database**. It demonstrates modern Android development practices, including MVVM architecture, reactive UI, and persistent storage.

## 🚀 Features

### 🔐 Authentication
*   **Login & Signup:** Secure entry point for the application.
*   **Predefined Credentials:** 
    *   **Email:** `admin@gmail.com`
    *   **Password:** `admin123`
*   **Validation:** Basic signup validation for new users.

### 📦 Product Catalog
*   **Categorized Listings:** Browse products across **Electronics**, **Accessories**, and **Others**.
*   **Real-time Search:** Filter products instantly using the integrated search bar.
*   **Dynamic Inventory:** 18+ pre-populated items with high-quality resource mapping.

### 🛒 Shopping Cart
*   **Persistent Storage:** Items stay in your cart even after app restarts.
*   **Cart Management:** Add products, view total price, and remove items easily.
*   **Checkout:** Simulated checkout flow that clears the cart upon success.

### 👤 User Profile
*   **Personalization:** View and edit your Name and Email.
*   **Preferences:** Toggle app settings like "Receive Notifications."
*   **Persistent Data:** User details are saved locally using Room.

## 🛠 Tech Stack

*   **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material 3.
*   **Language:** [Kotlin](https://kotlinlang.org/).
*   **Database:** [Room Persistence Library](https://developer.android.com/training/data-storage/room).
*   **Architecture:** MVVM (Model-View-ViewModel) + Repository Pattern.
*   **Asynchronous:** [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & Flow.
*   **Build System:** Gradle (Kotlin DSL).

## 📸 Screenshots

*(Add your screenshots here after uploading them to your repository)*

## 📥 Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/KaransCode/MiniCart.git
   ```
2. Open the project in **Android Studio (Ladybug or newer)**.
3. Sync Project with Gradle Files.
4. Run the app on an Emulator or Physical Device.

## 📝 Project Structure
*   `data/`: Room entities, DAOs, and Database configuration.
*   `repository/`: Data source abstraction.
*   `viewmodel/`: Business logic and UI state management.
*   `ui/theme/`: Compose UI screens, components, and Material 3 theme definitions.

---
*Developed as part of a Project Based Learning (PBL) initiative.*
