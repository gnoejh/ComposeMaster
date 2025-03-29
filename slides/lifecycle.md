## Lifecycle in Jetpack Compose

### 📊 What Is Lifecycle?

In Android, **lifecycle** refers to the stages a component (Activity, Fragment, or Composable) goes through during its existence.

Jetpack Compose brings its own **composition lifecycle** that integrates with the traditional Android lifecycle, but behaves differently from Views.

---

### 📆 Android Lifecycle vs Compose Lifecycle

| Aspect                    | Android Lifecycle                | Compose Lifecycle                          |
|---------------------------|----------------------------------|--------------------------------------------|
| Who owns it?              | Activity / Fragment              | Composable functions                        |
| Key stages                | `onCreate`, `onStart`, `onResume`, etc. | Enter/exit composition, recomposition     |
| Managed by                | OS + LifecycleOwner              | Compose runtime                            |
| Observed via              | `LifecycleObserver` or `LiveData`| `DisposableEffect`, `LaunchedEffect`       |

---

### 📅 LifecycleOwner in Compose

To observe Android lifecycle inside Compose, use `LocalLifecycleOwner`:

```kotlin
@Composable
fun ObserveLifecycle() {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            Log.d("Lifecycle", "Observed: $event")
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}
```

✅ Good for logging, tracking foreground/background, or controlling media, etc.

---

### 🔄 Composition Lifecycle in Compose

| Term               | Description                                                    |
|--------------------|----------------------------------------------------------------|
| **Composition**    | When a composable first enters the UI                          |
| **Recomposition**  | When a composable is re-executed due to state change           |
| **Decomposition**  | When a composable leaves the composition (removed from UI)     |

You can use effects like `LaunchedEffect`, `DisposableEffect`, and `SideEffect` to hook into these moments.

---

### 📅 Example: Respond to Lifecycle Events

```kotlin
@Composable
fun LogOnResume() {
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                Log.d("ComposeLifecycle", "App resumed")
            }
        }
        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}
```

---

### 🔼 Compose Lifecycle Tools

| API                  | Purpose                                             | Runs On                              |
|----------------------|-----------------------------------------------------|--------------------------------------|
| `LaunchedEffect`     | Run coroutine on enter or key change               | Composition                          |
| `DisposableEffect`   | Run setup/cleanup when entering/leaving composition| Composition                          |
| `SideEffect`         | Sync with UI tree after every recomposition        | Recomposition                         |
| `rememberUpdatedState` | Get latest state inside side-effect block         | Anytime                              |

---

### ⚠️ Common Lifecycle Mistakes

| Mistake                                         | Fix                                                         |
|------------------------------------------------|--------------------------------------------------------------|
| Triggering side effects outside effects         | Use `LaunchedEffect` or `DisposableEffect` only              |
| Forgetting to clean up observers/listeners     | Always use `onDispose` block in `DisposableEffect`           |
| Using `GlobalScope` instead of lifecycle-aware | Use `rememberCoroutineScope`, `viewModelScope`, etc.        |

---

### 🔧 Practice Ideas

| Task                                   | Goal                                                   |
|----------------------------------------|--------------------------------------------------------|
| Log when screen enters foreground      | Use `ON_RESUME` with `LifecycleEventObserver`          |
| Pause/resume timer based on lifecycle  | Track `ON_PAUSE`, `ON_RESUME`                         |
| Cancel coroutine on exit               | Wrap in `LaunchedEffect` + cancel in `DisposableEffect`|

---

