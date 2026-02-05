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
![telegram-cloud-photo-size-2-5197715468700029321-y](https://github.com/user-attachments/assets/af4d7f4d-af2c-4159-ae1d-e08d586982cd)
![telegram-cloud-photo-size-2-5197715468700029322-y](https://github.com/user-attachments/assets/b83b0918-1659-4cb8-90d5-74c35be702ac)
![telegram-cloud-photo-size-2-5197715468700029323-y](https://github.com/user-attachments/assets/3e3023ea-d816-4924-853c-3285262a2489)





