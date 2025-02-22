# Android Compose with Deep Learning - 13-Week Curriculum

This document outlines a 13-week curriculum for learning Android Compose while integrating Deep Learning concepts using OpenCV, TensorFlow Lite, and PyTorch. Each week builds upon the previous one for a comprehensive learning experience.

---

## Week 1: Preliminary Setup

**Topics:**

- Installing Android Studio, setting up the SDK, and configuring the emulator.
- Kotlin fundamentals: variables, data types, control flow, functions, and classes.
- Basic Android project structure: `build.gradle` files, `AndroidManifest.xml`, and project layout.

**Assignments:**

- Install Android Studio and configure the Android SDK.
- Create a basic Kotlin project and write simple programs.
- Explore the structure of a basic Android project.

---

## Week 2: Compose and Layout Basics

**Topics:**

- Overview of Jetpack Compose: benefits and the declarative UI paradigm.
- Comparison of Compose versus traditional XML layouts.
- Setting up the Compose environment and adding necessary dependencies.
- Creating your first composable using `@Composable` (e.g., `Text`, `Column`, `Row`, `Image`, `Button`, and preview functions).
- Understanding the Compose Compiler.
- Detailed usage of `Column`, `Row`, and `Box`.
- Using modifiers such as `padding`, `fillMaxWidth`, `fillMaxSize`, `weight`, `height`, `width`, `offset`, and `clickable`.
- Alignment and arrangement options.
- Recomposition and efficient UI updates.

**Assignments:**

- Create a new Android project using Compose.
- Build a simple "Hello, Compose!" app.
- Experiment with basic composables and previews.
- Build a layout with nested `Column` and `Row`.
- Practice using modifiers and alignment options.

---

## Week 3: UI Elements, Lists, Grids, and Advanced Layouts

**Topics:**

- **Common UI Elements:**
  - `TextField`, Checkbox, RadioButton, Switch
  - Handling user input and events using callbacks like `onClick` and `onValueChange`.
  - Managing UI state.

- **Lists and Grids:**
  - Using LazyColumn, LazyRow, and LazyVerticalGrid for efficient list rendering.
  - Working with dynamic data in lists.

- **Advanced Layouts:**
  - Using ConstraintLayout in Compose for complex arrangements.
  - Building common app structures with Scaffold (including TopAppBar, BottomNavigation, FloatingActionButton, and Drawer).
  - Implementing navigation patterns with TopAppBar and BottomNavigation.

**Assignments:**

- Create a form with `TextField`, Checkbox, RadioButton, and Switch.
- Build a list of items using LazyColumn and design a grid with LazyVerticalGrid.
- Create a complex layout using ConstraintLayout.
- Develop an app structure using Scaffold, TopAppBar, and BottomNavigation.

---

## Week 4: Icons, Images, Animations, and Custom Layouts

**Topics:**

- Using vector drawables for scalable icons.
- Loading and displaying images from resources or the network.
- Image transformations: resizing, cropping, and content scaling.
- Creating simple animations with `animate*AsState`.
- Using `AnimatedVisibility` for animating element appearance/disappearance.
- Animating content changes with `AnimatedContent`.
- Implementing transitions between UI states.
- Building custom layout composables with the `Layout` component.

**Assignments:**

- Add icons and images to an app and apply image transformations.
- Implement simple animations using `animate*AsState`.
- Create complex animations using `AnimatedVisibility` and `AnimatedContent`.
- Build a custom layout composable with unique arrangements.

---

## Week 5: Theming, Styling, and Navigation

**Topics:**

- Material theme components: \`MaterialTheme\`, Colors, Typography, and Shapes.
- Custom theme design, dark mode implementation, and styling individual composables using the custom theme.
- Navigating composables with Compose Navigation.
- Setting up a navigation graph and defining screens.
- Navigating between screens using the \`NavController\`.
- Passing arguments between screens and implementing deep links.

**Assignments:**

- Create a custom theme for an app and implement dark mode.
- Style various composables with the custom theme.
- Develop a multi-screen app using Compose Navigation.
- Pass data between screens with navigation arguments.
- Implement deep linking.

---

## Week 6: Compose Lifecycles, Core Android Components, and Architectural Patterns

**Topics:**

- **Compose Lifecycles:**
  - Lifecycle-aware composables and integration with Android lifecycle events.

- **Android Components:**
  - Activity lifecycle and state management, including data passing with intents.
  - Managing background and foreground services.
  - Service lifecycle management and UI communication.
  - Working with broadcast receivers (both static and dynamic).
  - Managing shared data with content providers.

- **Architectural Patterns and Dependency Injection:**
  - Overview of modern architectural patterns such as MVVM and Clean Architecture.
  - Introduction to dependency injection frameworks (e.g., Hilt, Dagger).

**Assignments:**

- Create a Compose app that observes lifecycle events.
- Implement an Activity with proper data passing via intents.
- Develop a background service that updates the Compose UI.
- Build a Broadcast Receiver to handle custom broadcasts.
- Create a simple Content Provider.
- Refactor a small project to incorporate MVVM with DI.

---

## Week 7: Midterm Exam

---

## Week 8: OpenCV Fundamentals

**Topics:**

- Introduction to OpenCV and basic image processing.
- Setting up OpenCV in Android.
- Displaying images with OpenCV.
- Applying basic image filters and transformations such as grayscale, blur, rotation, and cropping.
- Converting color spaces (e.g., RGB to HSV).

**Assignments:**

- Display an image using OpenCV.
- Apply basic image filters.
- Implement image transformations with OpenCV.

---

## Week 9: State Management - Basics

**Topics:**

- Understanding state in Compose.
- State hoisting: lifting state up to parent composables.
- Using state objects like `mutableStateOf`, `remember`, and `derivedStateOf`.
- Using `rememberSaveable` for persisting state.

**Assignments:**

- Implement state management in a simple app.
- Practice state hoisting.
- Use `rememberSaveable` to persist state.

---

## Week 10: State Management - Advanced

**Topics:**

- Managing local state versus hoisted state.
- Sharing state between child composables.
- Unidirectional data flow in Compose.
- Handling side effects with `LaunchedEffect`, `rememberCoroutineScope`, `DisposableEffect`, `SideEffect`, and `snapshotFlow`.

**Assignments:**

- Build an app that involves complex state interactions.
- Use side effects to perform actions outside recomposition (e.g., network requests).

---

## Week 11: ViewModel Integration

**Topics:**

- Integrating `ViewModel` with Compose.
- Using LiveData and StateFlow in Compose to observe data changes.
- Observing state changes and reacting in the UI using the `viewModel()` function.

**Assignments:**

- Integrate a ViewModel into an existing project.
- Observe and respond to state changes originating from the ViewModel.

---

## Week 12: TensorFlow Lite, PyTorch, and Advanced Deep Learning Integration

**Topics:**

- Basics of TensorFlow Lite and PyTorch.
- Setting up and using each framework in an Android app.
- Advanced Deep Learning Integration:
  - On-device training.
  - Transfer learning.
  - Optimization techniques for deep learning models.

**Assignments:**

- Implement a simple TensorFlow Lite model in an app.
- Implement a simple PyTorch model in an app.
- Explore advanced deep learning concepts in a small project.

---

## Week 13: Accessibility, Testing, Networking, Data Persistence, Error Handling, and Performance Optimization

**Topics:**

- Enhancing accessibility with semantic properties and content descriptions.
- Testing composables: unit tests, UI tests, and using test tags.
- Fetching data from APIs using libraries like Retrofit.
- Persisting data locally with Room.
- Error Handling & Logging:
  - Strategies for error management and logging best practices.
- Performance Optimization:
  - Profiling, memory management, leveraging best practices for Compose performance.

**Assignments:**

- Add accessibility features to an app.
- Write unit tests and UI tests for composables.
- Fetch data from an API using Retrofit.
- Use Room for local data persistence.
- Implement error handling and logging in a project.
- Profile and optimize a Compose-based UI.

---

## Week 14: Project Presentation

---

## Week 15: Final Exam

---

## Week 16: Make-up Week

---