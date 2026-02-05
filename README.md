# Test Crypto App

Android application built with Jetpack Compose, Clean Architecture, and Dynamic SDK, demonstrating email OTP authentication and EVM wallet management.


##  Architecture Description

The project follows Clean Architecture and is split into three modules:

app/  UI layer (Compose, Navigation, ViewModels)
domain/  Business logic (UseCases, Models, Interfaces)
data/  SDK integration, repositories, data sources

### Layer Responsibilities

####  App (UI Layer)
- Jetpack Compose UI
- Navigation using `NavHost`
- ViewModels
- UI state management via `StateFlow`
- Displays loading, success, and error states

####  Domain (Business Layer)
- Pure Kotlin module (no Android dependencies)
- UseCases (e.g. `SendEmailOTPUseCase`, `GetWalletUseCase`)
- Domain models
- Repository interfaces

####  Data (Data Layer)
- Integration with **Dynamic SDK**
-Repository implementations
-Observes SDK flows 
  -Maps SDK models to domain models

##  Dependency Injection

- **Koin** is used for dependency injection


##  How to Run

- Create **secrets.properties** in the project root
-  DYNAMIC_ENVIRONMENT_ID=your_environment_id_here
- Add secrets.properties to .gitignore

##  ScreenShots
(https://github.com/user-attachments/assets/9fcebb2b-f587-4439-a08e-63eb2bd1ba31)



