## Side Effects in Jetpack Compose

### 🌍 What Are Side Effects?

A **side effect** in Jetpack Compose is an operation that affects something **outside the scope** of a composable function. This includes:
- Logging, displaying a toast, showing a dialog
- Performing I/O or network calls
- Accessing Android system resources

Side effects **must be controlled** so they don't happen on every recomposition.

---

### 🪜 Why Are Side Effects Special in Compose?

Jetpack Compose is declarative and recomposes frequently. If you run side effects directly inside a composable, they may trigger **multiple times**, causing unexpected behavior. That’s why Compose provides **special APIs** to handle side effects.

---

### 📊 Side Effect APIs Comparison

| API                   | Purpose                                              | Runs When?                                  | Lifecycle-aware? | Cancelable? | Common Use Case                     |
|-----------------------|------------------------------------------------------|----------------------------------------------|------------------|-------------|------------------------------------|
| `LaunchedEffect`      | Launch coroutine when key changes or enters scope   | On entering composition or key change        | ✅ Yes           | ✅ Yes      | Fetching data, one-time effects     |
| `rememberUpdatedState`| Keep latest value in a lambda or side effect block  | Used inside effects to reference fresh value | ❌ No            | ❌ No       | Timer or callback referencing state|
| `DisposableEffect`    | Set up and clean up external resources              | On entering and leaving composition          | ✅ Yes           | ✅ Yes      | Add/remove listeners, callbacks     |
| `SideEffect`          | Run after successful composition                    | On every successful recomposition            | ❌ No            | ❌ No       | Syncing data with view system       |
| `rememberCoroutineScope` | Creates a scope tied to composable lifespan     | Whenever needed                              | ✅ Yes           | ✅ Yes      | Launching user-initiated actions    |

---

### 📅 Example 1: `LaunchedEffect`
Run side effects only **once** or when a key changes.

```kotlin
@Composable
fun GreetingMessage(userId: String) {
    LaunchedEffect(userId) {
        val data = fetchUserData(userId)
        println("Fetched data for $userId")
    }
}
```

- The block only runs when `userId` changes or the composable first enters the composition.

---

### 📅 Example 2: `rememberUpdatedState`
Prevents capturing stale values inside coroutines or listeners.

```kotlin
@Composable
fun Timer(message: String) {
    val currentMessage by rememberUpdatedState(message)

    LaunchedEffect(Unit) {
        delay(3000)
        println(currentMessage) // always prints latest message
    }
}
```

---

### 📅 Example 3: `DisposableEffect`
Used when you need to set up something when a composable enters and clean it up when it leaves.

```kotlin
@Composable
fun EventLogger(tag: String) {
    DisposableEffect(tag) {
        println("Start logging: $tag")
        onDispose {
            println("Stop logging: $tag")
        }
    }
}
```

---

### 📅 Example 4: `SideEffect`
Run side effects that must occur after every recomposition.

```kotlin
@Composable
fun WidthLogger(width: Int) {
    SideEffect {
        println("Current width is $width")
    }
}
```

---

### 🔧 Example 5: `rememberCoroutineScope`
Use this when launching coroutines **on events** (like button clicks).

```kotlin
@Composable
fun DownloadButton() {
    val scope = rememberCoroutineScope()

    Button(onClick = {
        scope.launch {
            delay(2000)
            println("Download complete!")
        }
    }) {
        Text("Download")
    }
}
```

---

### ⚠️ Common Pitfalls

| Mistake                                      | Fix / Advice                                                |
|---------------------------------------------|-------------------------------------------------------------|
| Triggering side effects directly in composable | Use `LaunchedEffect` or `SideEffect` to contain them        |
| Capturing stale values in coroutine         | Use `rememberUpdatedState`                                  |
| Forgetting to clean up resources            | Use `DisposableEffect` to register/unregister listeners      |

---

### 🔬 Practice Ideas

| Task                           | Goal                                         |
|--------------------------------|----------------------------------------------|
| Display toast after 3 seconds | Practice `LaunchedEffect`                    |
| Track lifecycle                | Use `DisposableEffect` to log composition    |
| Count recompositions           | Use `SideEffect` to log each recomposition   |
| Launch async from button       | Use `rememberCoroutineScope` with `Button`  |

---

