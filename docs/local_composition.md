## Local Composition in Jetpack Compose

### 🌐 What Is Local Composition?

Local Composition allows **contextual data** (like theme, resources, or user settings) to be passed down the UI tree **without explicitly passing it through function parameters**. This is achieved using **CompositionLocal** and related APIs.

---

### 🔍 Commonly Used CompositionLocals

| CompositionLocal        | Type                          | Purpose                                    |
|-------------------------|-------------------------------|--------------------------------------------|
| `LocalContext`          | `Context`                     | Android context for showing toasts, etc.   |
| `LocalLifecycleOwner`   | `LifecycleOwner`              | Observing lifecycle events                 |
| `LocalDensity`          | `Density`                     | Convert dp to px                            |
| `LocalConfiguration`    | `Configuration`               | Screen dimensions, locale, orientation     |
| `LocalLayoutDirection`  | `LayoutDirection`             | LTR or RTL                                 |

---

### 📅 Example 1: Access `LocalContext` to Show Toast

```kotlin
@Composable
fun ToastButton() {
    val context = LocalContext.current

    Button(onClick = {
        Toast.makeText(context, "Hello!", Toast.LENGTH_SHORT).show()
    }) {
        Text("Show Toast")
    }
}
```

---

### 📅 Example 2: Use `LocalLifecycleOwner` to Observe Events

```kotlin
@Composable
fun LifecycleLogger() {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            Log.d("Lifecycle", "Event: $event")
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}
```

---

### 🔼 Create Your Own `CompositionLocal`

#### Step 1: Declare It
```kotlin
val LocalUser = compositionLocalOf<String> { error("No user provided") }
```

#### Step 2: Provide It
```kotlin
@Composable
fun MyApp() {
    CompositionLocalProvider(LocalUser provides "Lauren") {
        GreetingCard()
    }
}
```

#### Step 3: Consume It
```kotlin
@Composable
fun GreetingCard() {
    val name = LocalUser.current
    Text("Hello, $name!")
}
```

---

### 🤔 CompositionLocal vs Parameters

| Aspect                 | CompositionLocal                            | Parameters                             |
|------------------------|---------------------------------------------|-----------------------------------------|
| Usage                 | Implicit context                            | Explicit values                         |
| Best For              | Global/app-wide data                        | Scoped/local data                       |
| Testability           | Harder to mock                              | Easy to inject                          |
| Readability           | Cleaner UI tree for shared data             | Clearer flow for unique data            |

---

### ⚠️ Best Practices and Tips

| Tip                                      | Why it Matters                                          |
|------------------------------------------|----------------------------------------------------------|
| Use only for ambient or shared values    | Prevents clutter and excessive reliance on globals      |
| Avoid deep nesting of CompositionLocals  | Be cautious with readability and traceability           |
| Don't mutate CompositionLocal directly   | Use `.provides` and propagate changes properly          |

---

### 🔧 Practice Ideas

| Task                                      | Goal                                                    |
|-------------------------------------------|----------------------------------------------------------|
| Use `LocalContext` to open an activity    | Learn intent + context interaction                      |
| Provide a custom theme color              | Create and access your own `CompositionLocal<Color>`    |
| Log orientation using `LocalConfiguration`| React to configuration changes                          |
| Observe lifecycle                         | Practice `LocalLifecycleOwner` and `DisposableEffect`    |

---

