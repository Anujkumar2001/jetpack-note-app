# Jetpack Compose Note App

A modern Android note-taking application built with Jetpack Compose and following Clean Architecture principles.

## Architecture

This application follows Clean Architecture with a clear separation of concerns:

### Domain Layer
- Contains business logic and rules
- Independent of other layers
- Includes:
  - Models (Note)
  - Repository interfaces
  - Use cases

### Data Layer
- Implements repository interfaces from domain layer
- Handles data operations and mapping
- Includes:
  - Repository implementations
  - Data sources (Firebase)
  - DTOs (Data Transfer Objects)

### Presentation Layer
- UI components built with Jetpack Compose
- ViewModels with StateFlow for state management
- Follows atomic design principles for UI components

## Key Features

- Create, read, update, and delete notes
- Reactive UI with Jetpack Compose
- State management with Kotlin Flow and StateFlow
- Dependency injection with Hilt
- Firebase Firestore integration
- Error handling and validation
- Unit and UI tests

## Tech Stack

- **UI**: Jetpack Compose
- **Architecture**: MVVM + Clean Architecture
- **State Management**: StateFlow
- **Dependency Injection**: Hilt
- **Database**: Firebase Firestore
- **Concurrency**: Kotlin Coroutines
- **Testing**: JUnit, MockK, Compose UI Testing

## Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/noteapp/
│   │   │   ├── di/                    # Dependency injection modules
│   │   │   ├── domain/                # Domain layer
│   │   │   │   ├── model/             # Domain models
│   │   │   │   ├── repository/        # Repository interfaces
│   │   │   │   └── usecase/           # Business logic use cases
│   │   │   ├── data/                  # Data layer
│   │   │   │   ├── model/             # Data models and mappers
│   │   │   │   ├── repository/        # Repository implementations
│   │   │   │   └── source/            # Data sources
│   │   │   ├── presentation/          # Presentation layer
│   │   │   │   ├── components/        # Reusable UI components
│   │   │   │   │   ├── atoms/         # Basic UI elements
│   │   │   │   │   ├── molecules/     # Composite components
│   │   │   │   │   └── organisms/     # Complex UI structures
│   │   │   │   ├── notes/             # Notes list screen
│   │   │   │   └── note_detail/       # Note detail/edit screen
│   │   │   ├── navigation/            # Navigation components
│   │   │   └── ui/                    # UI theme and styling
│   │   └── res/                       # Resources
│   ├── test/                          # Unit tests
│   └── androidTest/                   # UI tests
```

## Getting Started

### Prerequisites
- Android Studio Arctic Fox or newer
- JDK 11 or higher
- Firebase account (for Firestore)

### Setup
1. Clone the repository
2. Open the project in Android Studio
3. Connect the app to your Firebase project:
   - Create a new Firebase project in the Firebase console
   - Add an Android app to the project
   - Download the `google-services.json` file and place it in the app directory
4. Build and run the application

## Testing

### Unit Tests
Run unit tests with:
```
./gradlew test
```

### UI Tests
Run UI tests with:
```
./gradlew connectedAndroidTest
```

## Contributing

### Pull Request Process
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Write or update tests
5. Update documentation
6. Submit a pull request

### PR Template
When submitting a PR, please use the following template:

```markdown
## Description
[Description of the changes]

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## How Has This Been Tested?
[Describe the tests that you ran]

## Checklist
- [ ] My code follows the project's code style
- [ ] I have written tests for my changes
- [ ] I have updated the documentation
- [ ] My changes don't break existing functionality
```

## License
This project is licensed under the MIT License - see the LICENSE file for details.
