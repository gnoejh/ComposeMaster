# 🌀 Lifecycle in Jetpack Compose

## 📊 What Is Lifecycle?

In Android, **lifecycle** refers to the different stages a component (like an `Activity`, `Fragment`, or `Composable`) 
goes through from creation to destruction.

Jetpack Compose introduces a distinct **composition lifecycle** that integrates with, but also differs from, 
the traditional Android lifecycle. It is driven by UI state and recomposition rather than explicit lifecycle callbacks.

---

## 🔁 Android Lifecycle Diagram

Here’s a simplified diagram of the **Android Activity lifecycle** and its state transitions:

```
             +-----------------+
             |     onCreate     |
             +--------+--------+
                      |
                      v
             +--------+--------+
             |     onStart      |
             +--------+--------+
                      |
                      v
             +--------+--------+
             |    onResume     |
             +--------+--------+
                      |
       +--------------+--------------+
       |                             |
       v                             v
+--------------+           +-----------------+
|   onPause    | <-------- |   onRestart     |
+--------------+           +-----------------+
       |                             ^
       v                             |
+--------------+           +-----------------+
|   onStop     | --------> |   onDestroy     |
+--------------+           +-----------------+
```

- `onCreate` – Initialization logic (UI, bindings, ViewModel)
- `onStart` – UI becomes visible
- `onResume` – UI becomes interactive
- `onPause` – Partially obscured (e.g., dialog or multitasking)
- `onStop` – Completely hidden (background)
- `onRestart` – Coming back to foreground
- `onDestroy` – Cleanup before the component is destroyed

---

## 🔍 Android Lifecycle vs Compose Lifecycle

| Aspect                     | Android Lifecycle                                      | Compose Lifecycle                                       |
|----------------------------|--------------------------------------------------------|---------------------------------------------------------|
| **Owner**                  | `Activity` / `Fragment`                                | Composable function                                     |
| **Core stages**            | `onCreate`, `onStart`, `onResume`, `onPause`, etc.     | Enter composition, recomposition, leave composition     |
| **Managed by**             | Android OS + `LifecycleOwner`                         | Compose runtime engine                                  |
| **Observation mechanism**  | `LifecycleObserver`, `LiveData`, `Flow`, etc.         | `LaunchedEffect`, `DisposableEffect`, `SideEffect`      |
| **Purpose**                | Manage visibility and app state                        | Manage UI rendering and side effects                    |

---

## 🗖️ Observing Lifecycle in Compose

Use `LocalLifecycleOwner` to observe the Android lifecycle within a Composable.

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

✅ **Useful for**:
- Logging lifecycle events
- Tracking foreground/background transitions
- Controlling media (e.g., pause video on `ON_PAUSE`)

---

## 🔄 Composition Lifecycle in Compose

Jetpack Compose has its own lifecycle managed internally:

| Term               | Description                                                    |
|--------------------|----------------------------------------------------------------|
| **Composition**    | When a composable is initially added to the UI tree            |
| **Recomposition**  | When the composable is re-invoked due to state changes         |
| **Decomposition**  | When the composable is removed from the UI                     |

🛠 You can hook into these phases with:

- `LaunchedEffect`
- `DisposableEffect`
- `SideEffect`

---

## 🧪 Example: Respond to Lifecycle Events

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

✅ **Scenario**: You want to trigger an action (e.g., resume a game or video) when the app returns to the foreground.

---

## 🧰 Compose Lifecycle-Aware APIs

| API                      | Description                                                   | Triggered When                                 |
|--------------------------|---------------------------------------------------------------|------------------------------------------------|
| `LaunchedEffect(key)`    | Launch coroutine on first composition or when key changes     | On composition or key change                   |
| `DisposableEffect(key)`  | Setup/cleanup logic tied to composable's lifecycle            | On key change or disposal                      |
| `SideEffect`             | Execute sync logic after every recomposition                  | After each recomposition                       |
| `rememberUpdatedState`   | Capture the latest value inside long-lived effect blocks      | Keeps state up-to-date without recomposition   |

---

## ⚠️ Common Lifecycle Pitfalls

| Mistake                                             | Fix / Best Practice                                              |
|------------------------------------------------------|------------------------------------------------------------------|
| ❌ Triggering suspend functions outside effects       | ✅ Use `LaunchedEffect` to run coroutines                        |
| ❌ Not cleaning up listeners/observers                | ✅ Use `onDispose` block in `DisposableEffect`                   |
| ❌ Using `GlobalScope` inside Composables             | ✅ Use `rememberCoroutineScope`, `viewModelScope`, etc.          |
| ❌ Performing expensive work during recomposition     | ✅ Move side effects to `LaunchedEffect` or `SideEffect`         |

---

## 🧑‍💻 Practice Tasks for Mastery

| Task                                       | Learning Outcome                                             |
|--------------------------------------------|--------------------------------------------------------------|
| Log lifecycle event when screen resumes    | Use `ON_RESUME` with `LifecycleEventObserver`               |
| Pause/resume a countdown timer             | Observe `ON_PAUSE` and `ON_RESUME` to control state         |
| Cancel a network call when leaving screen  | Wrap logic in `LaunchedEffect` and clean up in `DisposableEffect` |
| Reflect state change after recomposition   | Use `SideEffect` to update sync UI values                   |

---

