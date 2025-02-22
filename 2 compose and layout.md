# Week 2: Compose and Layout Basics

## Slide 1: What is Jetpack Compose?

- Modern UI toolkit for Android
- Declarative UI paradigm
- Built with Kotlin
- Replaces XML layouts
- Released in 2021

## Slide 2: Why Compose?

- Less boilerplate code
- More intuitive APIs
- Better tooling support
- Improved performance
- Material Design 3 ready
- Interoperable with Views

## Slide 3: Declarative vs Imperative UI


| Declarative (Compose) | Imperative (XML)         |
| --------------------- | ------------------------ |
| Describe what to show | Describe how to update   |
| State-driven          | Manual updates           |
| Automatic UI updates  | Manual view manipulation |
| Less code             | More boilerplate         |

## Slide 4: Setting Up Compose

```kotlin
// build.gradle.kts
android {
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
}
```

## Slides 5: First Composable

```kotlin
@Composable
fun Greeting(name: String) {
Text("Hello $name!")
}

@Preview
@Composable
fun GreetingPreview() {
Greeting("Android")
}
```
## Slide 6: Core Layout Composables
- Column: Vertical arrangement
- Row: Horizontal arrangement
- Box: Stack-based layout
- Each can contain multiple child composables

## Slide 7: Column Example

```kotlin
@Composable
fun VerticalLayout() {
    Column {
        Text("First")
        Text("Second")
        Text("Third")
    }
}
```

## Slide 8: Row Example

```kotlin
@Composable
fun HorizontalLayout() {
    Row {
        Text("Left")
        Text("Center")
        Text("Right")
    }
}
```

## Slide 9: Box Example

```kotlin
@Composable
fun OverlayLayout() {
    Box {
        Image(/*...*/)
        Text("Overlay text")
    }
}
```

## Slide 10 Basic Modifiers
```kotlin
@Composable
fun ModifierExample() {
    Text(
        text = "Hello",
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .background(Color.Gray)
    )
}
```

## Slide 11:Size Modifiers
- width() / height()
- size()
- fillMaxWidth()
- fillMaxHeight()
- fillMaxSize()
- wrapContentSize()

## Slide 12: Spacing Modifiers
- padding()
- offset()
- spacedBy()
- weight()
- aspectRatio()

## Slide 13: Interactive Modifiers
- clickable()
- draggable()
- focusable()
- pointerInput()
- scrollable()

## Slide 14: Alignment Modifiers
```kotlin
Column(
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Text("Centered")
}

Row(
    verticalAlignment = Alignment.CenterVertically
) {
    Text("Centered")
}
```

## Slide 15: Arrangement Modifiers
```kotlin
Column(
    verticalArrangement = Arrangement.SpaceEvenly
) {
    Text("First")
    Text("Second")
    Text("Third")
}
```

## Slide 16: Recomposition
- Automatic UI updates
- State-based triggers
- Smart update system
- Only affected components

## Slide 17: State Management
```kotlin
@Composable
fun Counter() {
    var count by remember { mutableStateOf(0) }
    Button(onClick = { count++ }) {
        Text("Count: $count")
    }
}
```

## Slide 18: Common Pitfalls
- Modifiers order matters
- Recomposition side effects
- Unnecessary state
- Missing remember
- Layout nesting depth

## Slide 19: Best Practices
- Keep composables small
- Use meaningful names
- Follow composition local pattern
- Hoist state when needed
- Use proper modifier order

## Slide 20: Practice Exercise
```kotlin
@Composable
fun ProfileCard() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Profile Name",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Description",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
```