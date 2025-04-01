**Jetpack Compose States in Kotlin**

Jetpack Compose is Android’s modern UI toolkit that enables building native UI with a declarative approach. One of its core concepts is **State**, which determines how the UI behaves and updates when data changes.

---

## 1. Understanding State in Jetpack Compose

| Concept         | Description |
|----------------|-------------|
| **State**      | Represents data that can change over time and triggers UI recomposition. |
| **Remember**   | Retains state across recompositions within a Composable function. |
| **MutableState** | A special type of state that allows modifying values and triggering recompositions. |
| **State Hoisting** | Moving state outside a Composable function to make it more reusable and testable. |
| **rememberSaveable** | Stores state across configuration changes like screen rotation. |
| **derivedStateOf** | Ensures recomputation occurs only when dependencies change. |

### 1.1 Creating and Using State

Jetpack Compose provides `remember` and `mutableStateOf` to handle state inside a Composable function:

| Function | Description |
|----------|-------------|
| **remember { mutableStateOf(value) }** | Stores state across recompositions but resets on configuration changes. |
| **rememberSaveable { mutableStateOf(value) }** | Retains state across recompositions and configuration changes. |
| **derivedStateOf { computation }** | Computes a value efficiently, only when dependencies change. |

```kotlin
@Composable
fun Counter() {
    var count by remember { mutableStateOf(0) }
    Column {
        Text("Count: $count")
        Button(onClick = { count++ }) {
            Text("Increment")
        }
    }
}
```

### 1.2 State Hoisting

State hoisting improves reusability by lifting state to the caller function:

```kotlin
@Composable
fun Counter(count: Int, onIncrement: () -> Unit) {
    Column {
        Text("Count: $count")
        Button(onClick = onIncrement) {
            Text("Increment")
        }
    }
}

@Composable
fun CounterScreen() {
    var count by remember { mutableStateOf(0) }
    Counter(count = count, onIncrement = { count++ })
}
```

| Benefit | Description |
|---------|-------------|
| **Reusability** | The `Counter` Composable does not manage its state, allowing reuse. |
| **Testability** | Makes it easier to test components separately. |

---

## 2. RememberSaveable for State Persistence

`rememberSaveable` retains state across configuration changes such as screen rotations.

```kotlin
@Composable
fun Counter() {
    var count by rememberSaveable { mutableStateOf(0) }
    Column {
        Text("Count: $count")
        Button(onClick = { count++ }) {
            Text("Increment")
        }
    }
}
```

| Feature | Benefit |
|---------|-------------|
| **remember** | Stores state only during composition. |
| **rememberSaveable** | Persists state across configuration changes. |

---

## 3. DerivedState for Optimizing Performance

`derivedStateOf` optimizes recomposition by computing derived values only when dependencies change.

```kotlin
@Composable
fun TemperatureConverter(celsius: Int) {
    val fahrenheit by remember { derivedStateOf { celsius * 9 / 5 + 32 } }
    Text("Fahrenheit: $fahrenheit")
}
```

| Concept | Description |
|---------|-------------|
| **derivedStateOf** | Ensures recomposition only happens when necessary. |

---

## 4. Combining remember and rememberSaveable

| Function | Use Case |
|----------|-------------|
| **remember** | Stores transient UI state, resets on config changes. |
| **rememberSaveable** | Stores UI state and retains it across config changes. |

### Example of Using Both:
```kotlin
@Composable
fun CounterScreen() {
    var localCount by remember { mutableStateOf(0) }
    var persistentCount by rememberSaveable { mutableStateOf(0) }

    Column {
        Text("Local Count: $localCount")
        Button(onClick = { localCount++ }) {
            Text("Increment Local")
        }

        Text("Persistent Count: $persistentCount")
        Button(onClick = { persistentCount++ }) {
            Text("Increment Persistent")
        }
    }
}
```

- `remember` keeps `localCount` but resets on screen rotation.
- `rememberSaveable` keeps `persistentCount` across rotations.

---

## 5. Conclusion

Managing state efficiently in Jetpack Compose is essential for building responsive and maintainable applications. By leveraging `remember`, `rememberSaveable`, `derivedStateOf`, and understanding state variations (`by remember`, `= remember`, nullable states), developers can ensure optimal UI performance and reusability.

🚀 **Jetpack Compose: Efficient UI State Management!**

