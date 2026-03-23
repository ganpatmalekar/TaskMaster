# TaskMaster 📝

A robust and modern Android Todo application built to demonstrate clean architecture, reactive
programming, and best practices in Android development.

## 🎯 Purpose

TaskMaster is designed to help users efficiently manage their daily tasks with a smooth and
responsive interface. It leverages a REST API to simulate real-world data fetching, updating, and
deletion of todo items, providing a seamless user experience with optimized searching and filtering.

## 📸 Screenshots

| Main Screen                                                                                                                                 | Add Task                                                                                                                           | Update Task                                                                                                                  |
|---------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------|
| <img alt="Main Screen" height="500" src="C:\Users\ganpat.m\Downloads\WhatsApp%20Image%202026-03-23%20at%2011.11.38%20AM.jpeg" width="250"/> | <img alt="Add Task" height="500" src="C:\Users\ganpat.m\Downloads\WhatsApp Image 2026-03-23 at 11.11.37 AM (1).jpeg" width="250"/> | <img alt="Search" height="500" src="C:\Users\ganpat.m\Downloads\WhatsApp Image 2026-03-23 at 11.11.37 AM.jpeg" width="250"/> |

## 🚀 Features

- **View Tasks:** Fetches and displays a list of todo items from a remote API.
- **Add New Task:** Quickly add tasks to your list with immediate local updates.
- **Update Task:** Edit task titles and toggle completion status seamlessly.
- **Delete Task:** Remove tasks with smooth transition animations and UI feedback.
- **Dynamic Filtering:** Search and filter tasks by User ID with an optimized search experience (
  implemented with debounce and flow operators).
- **Reactive UI:** Built using Kotlin Flows (`StateFlow` for state & `SharedFlow` for one-time
  events like Toasts) for seamless data updates.
- **One-time Events:** Proper handling of Toast messages using `SharedFlow` to prevent re-triggering
  on configuration changes.
- **Clean Architecture:** Strictly follows the MVVM pattern for better maintainability and
  testability.

## 🛠 Tech Stack

- **Language:** [Kotlin](https://kotlinlang.org/)
- **Architecture:** MVVM (Model-View-ViewModel)
- **Dependency Injection:** [Hilt](https://dagger.dev/hilt/)
- **Networking:
  ** [Retrofit](https://square.github.io/retrofit/) & [Gson](https://github.com/google/gson)
- **Concurrency:
  ** [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Asynchronous Flow](https://kotlinlang.org/docs/flow.html)
- **Jetpack Components:**
    - ViewModel
    - View Binding
    - Lifecycle (repeatOnLifecycle)
- **UI Components:**
    - RecyclerView with `ListAdapter` & `DiffUtil` for efficient list updates.
    - Material Design 3 Components.
    - Custom DialogFragments for input.

## 🏗 Project Structure

- **`data`**: Contains data models (`TodoItem`) and the Repository pattern implementation.
- **`network`**: Retrofit API definitions and service configuration.
- **`ui`**: UI components including Activities, ViewModels, and Adapters.
- **`di`**: Hilt modules for providing network and repository dependencies.
- **`utils`**: Helper classes and application constants.

## 🚦 Getting Started

1. Clone the repository.
2. Open the project in **Android Studio (Ladybug or newer)**.
3. Ensure you have the latest Gradle distribution.
4. Sync Gradle and run the app on an emulator or physical device.

---
Developed with focus on performance and code quality. 🚀
