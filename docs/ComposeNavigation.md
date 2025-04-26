# Jetpack Compose Navigation

This documentation provides a comprehensive guide to using the Navigation component in Jetpack Compose applications.

## Table of Contents
- [Setup](#setup)
- [Basic Concepts](#basic-concepts)
- [Navigation Structure](#navigation-structure)
- [Route Patterns](#route-patterns)
- [Passing Arguments](#passing-arguments)
- [Back Stack Management](#back-stack-management)
- [Navigation Actions](#navigation-actions)
- [Example Implementation](#example-implementation)

## Setup

To use Navigation in your Compose application, add the following dependencies to your app's `build.gradle` file:

```kotlin
dependencies {
    implementation("androidx.navigation:navigation-compose:2.7.x") // Use the latest version
}
```

## Basic Concepts

### NavController
The `NavController` is the central component that keeps track of the back stack of composables that make up the navigation structure and the state of each screen. It handles navigating between destinations and maintaining the back stack.

```kotlin
val navController = rememberNavController()
```

### NavHost
A `NavHost` links the `NavController` with a navigation graph that defines the composable destinations. The `NavHost` displays the current destination of the navigation graph.

```kotlin
NavHost(navController = navController, startDestination = "home") {
    // Define navigation destinations here
}
```

## Navigation Structure

Navigation in Compose is built around destinations, which are composable functions that represent different screens in your app.

```kotlin
NavHost(navController = navController, startDestination = "home") {
    composable("home") { HomeScreen(navController) }
    composable("profile") { ProfileScreen(navController) }
    // More destinations...
}
```

## Route Patterns

### Simple Route
A basic route is just a string identifier:

```kotlin
composable("home") { HomeScreen(navController) }
```

### Route with Required Parameters
Required parameters are enclosed in curly braces `{}` and will be part of the route path:

```kotlin
composable(
    route = "screen2/{data}",
    arguments = listOf(navArgument("data") { type = NavType.StringType })
) { backStackEntry ->
    Screen2(navController, data = backStackEntry.arguments?.getString("data"))
}
```

### Route with Optional Parameters
Optional parameters are defined using query parameter syntax with a question mark:

```kotlin
composable("screen3?optionalData={optionalData}",
    arguments = listOf(navArgument("optionalData"){
        type = NavType.StringType
        defaultValue = "Default Optional Data"
    })
) { backStackEntry ->
    Screen3(navController, backStackEntry.arguments?.getString("optionalData"))
}
```

### Route with Multiple Parameters
Multiple query parameters are separated by ampersands:

```kotlin
composable(
    route = "screen5?name={name}&age={age}",
    arguments = listOf(
        navArgument("name") {
            type = NavType.StringType
            defaultValue = "Guest"
            nullable = true
        },
        navArgument("age") {
            type = NavType.IntType
            defaultValue = 0
        }
    )
) { backStackEntry ->
    Screen5(
        navController = navController,
        name = backStackEntry.arguments?.getString("name"),
        age = backStackEntry.arguments?.getInt("age") ?: 0
    )
}
```

## Passing Arguments

### NavArgument Types
Navigation arguments must declare a type:
- `NavType.StringType`: For string values
- `NavType.IntType`: For integer values
- `NavType.FloatType`: For float values
- `NavType.BoolType`: For boolean values
- `NavType.LongType`: For long values

### Required Arguments
Required arguments are part of the path:

```kotlin
// Route definition
composable(
    route = "screen2/{data}",
    arguments = listOf(navArgument("data") { type = NavType.StringType })
)

// Navigation
navController.navigate("screen2/HelloFromHome")
```

### Optional Arguments
Optional arguments use query parameter syntax and specify default values:

```kotlin
// Route definition
composable(
    "screen3?optionalData={optionalData}",
    arguments = listOf(navArgument("optionalData"){
        type = NavType.StringType
        defaultValue = "Default Optional Data"
    })
)

// Navigation with parameter
navController.navigate("screen3?optionalData=PassedOptionalData")

// Navigation without parameter (uses default value)
navController.navigate("screen3")
```

### Multiple Arguments
Multiple arguments can be combined:

```kotlin
// Route definition
composable(
    route = "screen5?name={name}&age={age}",
    arguments = listOf(
        navArgument("name") {
            type = NavType.StringType
            defaultValue = "Guest"
            nullable = true
        },
        navArgument("age") {
            type = NavType.IntType
            defaultValue = 0
        }
    )
)

// Navigation
navController.navigate("screen5?name=JohnDoe&age=30")
```

## Back Stack Management

### Simple Navigation
Simple navigation adds destinations to the back stack:

```kotlin
navController.navigate("screen1")
```

### Going Back
Pop the top destination from the back stack:

```kotlin
navController.popBackStack()
```

### Clearing Back Stack
Navigate to a destination while clearing the back stack:

```kotlin
navController.navigate("home") {
    popUpTo("home") {
        inclusive = true
    }
}
```

Options:
- `inclusive = true`: Removes the destination specified in `popUpTo`
- `inclusive = false`: Keeps the destination specified in `popUpTo`

## Navigation Actions

### navigate()
Navigate to a destination, adding it to the back stack:

```kotlin
navController.navigate("route")
```

### popBackStack()
Return to the previous destination on the back stack:

```kotlin
navController.popBackStack()
```

### popUpTo()
Pop back stack up to a specific destination:

```kotlin
navController.navigate("home") {
    popUpTo("home") {
        inclusive = true  // Remove "home" too
        // or
        // inclusive = false  // Keep "home"
    }
}
```

## Example Implementation

Here's a full example of a navigation system:

```kotlin
@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("screen1") { Screen1(navController) }
        composable(
            route = "screen2/{data}",
            arguments = listOf(navArgument("data") { type = NavType.StringType })
        ) { backStackEntry ->
            Screen2(navController, data = backStackEntry.arguments?.getString("data"))
        }
        composable("screen3?optionalData={optionalData}",
            arguments = listOf(navArgument("optionalData"){
                type = NavType.StringType
                defaultValue = "Default Optional Data"
            })
        ) { backStackEntry ->
            Screen3(navController, backStackEntry.arguments?.getString("optionalData"))
        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Home Screen")
        Button(onClick = { navController.navigate("screen1") }) {
            Text("Go to Screen 1")
        }
        Button(onClick = { navController.navigate("screen2/HelloFromHome") }) {
            Text("Go to Screen 2 with Data")
        }
        Button(onClick = { navController.navigate("screen3?optionalData=PassedOptionalData")}) {
            Text("Go to Screen 3 with Optional Data")
        }
    }
}
```

This implementation shows how to set up navigation, define routes with different parameter types, and navigate between screens with or without parameters.
