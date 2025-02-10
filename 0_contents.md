# Android Compose with Deep Learning - 16-Week Curriculum

This document outlines a 16-week curriculum for learning Android Compose, incorporating Deep Learning concepts using OpenCV, TensorFlow Lite, and PyTorch. Each week builds upon the previous one, providing a comprehensive learning path.

## Week 1: Preliminary Setup

**Topics:**
- Setting up Android Studio: Installation, SDK setup, emulator configuration.
- Kotlin Fundamentals: Variables, data types, control flow, functions, classes.
- Basic Android Project Structure: Understanding the project layout, `build.gradle` files, `AndroidManifest.xml`.

**Assignments:**
- Install Android Studio and set up the Android SDK.
- Create a basic Kotlin project and write simple Kotlin programs.
- Explore the structure of a basic Android project.

## Week 2: Introduction to Compose and Setup

**Topics:**
- Overview of Jetpack Compose: What is it, why use it, declarative UI paradigm.
- Compose vs. traditional XML layouts: Key differences, advantages of Compose.
- Setting up the Compose environment: Project creation, adding Compose dependencies.
- Your first composable: `@Composable` annotation, `Text`, `Column`, `Row`, Previews.
- Understanding the Compose Compiler.

**Assignments:**
- Create a new Android project with Compose.
- Build a simple "Hello, Compose!" app.
- Experiment with basic composables and previews.

## Week 3: Layout Basics

**Topics:**
- `Column`, `Row`, `Box` in detail: Understanding their roles and usage.
- Modifiers: `padding`, `fillMaxWidth`, `fillMaxSize`, `weight`, `height`, `width`, `offset`, `clickable`.
- Alignment and arrangement: `Arrangement`, `Alignment`.
- Understanding Recomposition: How Compose updates the UI efficiently.

**Assignments:**
- Build a simple layout with nested `Column` and `Row`.
- Practice using modifiers to control layout behavior.
- Experiment with different alignment and arrangement options.

## Week 4: Common UI Elements

**Topics:**
- `Image`, `Button`, `TextField`, `Checkbox`, `RadioButton`, `Switch`.
- Handling user input and events: `onClick`, `onValueChange`.
- State in UI elements.

**Assignments:**
- Create a form with various UI elements.
- Implement basic event handling (e.g., button clicks, text input).

## Week 5: Lists and Grids

**Topics:**
- `LazyColumn`, `LazyRow`, `LazyVerticalGrid`: Efficiently displaying lists and grids.
- Working with lists of data: Displaying dynamic content.
- Item layouts and customization: Creating custom list item layouts.
- `LazyVerticalStaggeredGrid` and `LazyHorizontalStaggeredGrid`.

**Assignments:**
- Build a list of items using `LazyColumn`.
- Customize the appearance of list items.
- Create a grid layout using `LazyVerticalGrid`.

## Week 6: Advanced Layouts

**Topics:**
- `ConstraintLayout` in Compose: Building complex layouts with constraints.
- `Scaffold` for common app structures: `TopAppBar`, `BottomNavigation`, `FloatingActionButton`, `Drawer`.
- `TopAppBar`, `BottomNavigation`: Implementing common navigation patterns.

**Assignments:**
- Create a complex layout using `ConstraintLayout`.
- Build a basic app structure with `Scaffold`, `TopAppBar`, and `BottomNavigation`.

## Week 7: Introduction to Navigation

**Topics:**
- Introduction to Compose Navigation: Navigating between composables.
- Setting up a navigation graph: Defining screens and navigation paths.
- Navigating between composables: Using the `NavController`.
- Passing arguments: Passing data between screens.
- Deep Links.

**Assignments:**
- Create a multi-screen app with navigation.
- Pass data between screens using navigation arguments.
- Implement deep links.

## Week 8: OpenCV Fundamentals

**Topics:**
- **Introduction to OpenCV:**
    - What is OpenCV?
    - Basic image processing concepts.
    - Setting up OpenCV in Android.
    - Displaying images with OpenCV.
- **OpenCV - Basic Image Manipulation:**
    - Basic image filters (e.g., grayscale, blur).
    - Image transformations (resize, rotate, crop).
    - Color space conversions (e.g., RGB to HSV).

**Assignments:**
- Display an image using OpenCV.
- Apply basic image filters (e.g., grayscale, blur).
- Implement image transformations using OpenCV.

## Week 9: State Management - Basics

**Topics:**
- What is state in Compose?
- State hoisting: Lifting state up to parent composables.
- State objects: `mutableStateOf`, `remember`, `derivedStateOf`.
- `rememberSaveable`.

**Assignments:**
- Implement state management in a simple app.
- Practice state hoisting.
- Use `rememberSaveable` to save the state.

## Week 10: State Management - Advanced

**Topics:**
- Managing state within a composable: Local state vs. hoisted state.
- Passing state to child composables: Sharing state effectively.
- Unidirectional data flow: The flow of data in Compose.
- Side Effects: `LaunchedEffect`, `rememberCoroutineScope`, `DisposableEffect`, `SideEffect`, `snapshotFlow`.

**Assignments:**
- Build an app with complex state interactions.
- Use side effects for actions outside of recomposition (e.g., network requests).

## Week 11: ViewModel Integration

**Topics:**
- Using `ViewModel` with Compose: Managing UI-related data.
- `LiveData` and `StateFlow` with Compose: Observing data changes.
- Observing state changes: Reacting to data updates in the UI.
- `viewModel()` function.

**Assignments:**
- Integrate `ViewModel` into an existing app.
- Observe and react to state changes from the `ViewModel`.

## Week 12: Theming and Styling

**Topics:**
- `MaterialTheme`, `Colors`, `Typography`, `Shapes`: Defining the app's visual style.
- Customizing the theme: Creating a unique look and feel.
- Dark mode support: Implementing dark mode.
- Styling Composables: Applying styles to individual UI elements.

**Assignments:**
- Create a custom theme for an app.
- Implement dark mode support.
- Style various composables using the custom theme.

## Week 13: Icons, Images, and Animations

**Topics:**
- Using vector drawables: Scalable icons.
- Loading images from resources and the network: Displaying images.
- Image transformations: Resizing, cropping, etc.
- Basic animations with `animate*AsState`: Creating simple animations.

**Assignments:**
- Add icons and images to an app.
- Implement simple animations using `animate*AsState`.

## Week 14: Advanced Animations and Custom Layouts

**Topics:**
- `AnimatedVisibility`: Animating the appearance and disappearance of elements.
- `AnimatedContent`: Animating content changes.
- Transitions: Creating complex animations between states.
- Creating custom layout composables: Building unique layouts.
- Measuring and positioning children: Controlling the layout of child elements.
- `Layout` composable: Building custom layouts from scratch.

**Assignments:**
- Create more complex animations using `AnimatedVisibility` and `AnimatedContent`.
- Build a custom layout.

## Week 15: Accessibility, Testing, Networking and Data Persistence

**Topics:**
- Making Compose UIs accessible: Semantic properties, content descriptions.
- Testing for accessibility: Ensuring the app is usable by everyone.
- Unit testing composables: Testing individual composables.
- UI testing with Compose: Testing the UI as a whole.
- Test tags: Identifying UI elements for testing.
- Fetching data from APIs: Making network requests.
- Using libraries like Retrofit: Simplifying network requests.
- Using Room for local data persistence.

**Assignments:**
- Implement accessibility features in an app.
- Write unit tests for composables.
- Perform UI testing with Compose.
- Fetch data from an API using Retrofit.
- Use Room for local data persistence.

## Week 16: TensorFlow Lite and PyTorch

**Topics:**
- TensorFlow Lite: Introduction, setup, and basic usage.
- PyTorch: Introduction, setup, and basic usage.

**Assignments:**
- Implement a simple TensorFlow Lite model in an app.
- Implement a simple PyTorch model in an app.