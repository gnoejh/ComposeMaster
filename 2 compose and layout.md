# **Compose and Layout Basics**

This guide introduces Jetpack Compose, its benefits, layout structures, and essential modifiers to help you build dynamic UI components.

---
## **1. What is Jetpack Compose?**

- Modern UI toolkit for Android
- Uses **Declarative UI paradigm**
- Built entirely with **Kotlin**
- Replaces traditional **XML layouts**
- First released in **2021**

---
## **2. Why Choose Jetpack Compose?**

- Reduces **boilerplate code**
- Provides **intuitive APIs**
- Offers **better tooling support**
- Improves **performance**
- Fully supports **Material Design 3**
- **Interoperable** with existing **View-based UI**

---
## **3. Declarative vs. Imperative UI**

| Declarative (Compose) | Imperative (XML)         |
| --------------------- | ------------------------ |
| Describe what to show | Describe how to update   |
| **State-driven** UI updates | **Manual** updates required |
| **Automatic** UI recomposition | Requires **explicit view manipulation** |
| **Less** code | **More** boilerplate |

---
## **4. Setting Up Jetpack Compose**

### **Gradle Configuration**
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

---
## **5. Writing Your First Composable**

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

---
## **6. Core Layout Composables**

| Composable | Description |
|------------|-------------|
| **Column** | Arranges child components **vertically** |
| **Row** | Arranges child components **horizontally** |
| **Box** | Overlays child components **stacked** on top of each other |

---
## **7. Column Example**

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

---
## **8. Row Example**

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

---
## **9. Box Example**

```kotlin
@Composable
fun OverlayLayout() {
    Box {
        Image(/*...*/)
        Text("Overlay text")
    }
}
```

---
## **10. Basic Modifiers**

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

---
## **11. Size Modifiers**

| Modifier | Description |
|----------|------------|
| `width()` / `height()` | Set explicit width or height |
| `size()` | Define both width & height together |
| `fillMaxWidth()` | Expand to full width |
| `fillMaxHeight()` | Expand to full height |
| `fillMaxSize()` | Expand to full screen size |
| `wrapContentSize()` | Adapt to content size |

---
## **12. Spacing Modifiers**

| Modifier | Description |
|----------|------------|
| `padding()` | Adds space **inside** the component |
| `offset()` | Moves a component **without affecting layout** |
| `spacedBy()` | Adds spacing **between elements** in `Column`/`Row` |
| `weight()` | Distributes space **proportionally** |
| `aspectRatio()` | Defines a **fixed width-to-height ratio** |

---
## **13. Interactive Modifiers**

| Modifier | Description |
|----------|------------|
| `clickable()` | Makes a component **clickable** |
| `draggable()` | Enables **drag interactions** |
| `focusable()` | Allows keyboard **focus** handling |
| `pointerInput()` | Custom **gesture recognition** |
| `scrollable()` | Enables **scrolling** interactions |

---
## **14. Alignment Modifiers**

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

---
## **15. Arrangement Modifiers**

```kotlin
Column(
    verticalArrangement = Arrangement.SpaceEvenly
) {
    Text("First")
    Text("Second")
    Text("Third")
}
```

---
## **16. Recomposition**

- **Automatic UI updates** based on state changes
- **Triggers on state modifications**
- **Optimized updates** to only affected components

---
## **17. State Management**

```kotlin
@Composable
fun Counter() {
    var count by remember { mutableStateOf(0) }
    Button(onClick = { count++ }) {
        Text("Count: $count")
    }
}
```

---
## **18. Common Pitfalls**

- **Modifier order matters** (e.g., `padding()` before `background()` affects layout)
- **Avoid unnecessary recompositions**
- **Use `remember` to retain state**
- **Keep UI nesting shallow** to avoid performance issues

---
## **19. Best Practices**

- Keep composables **small and reusable**
- Use **meaningful function names**
- Apply **state hoisting** where necessary
- Follow **composition local pattern**
- Optimize **modifier order** for clarity and performance

---
## **20. Practice Exercise**

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

---
🚀 **Mastering Compose layouts makes UI design faster, cleaner, and more efficient!**

