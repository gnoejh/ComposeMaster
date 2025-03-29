## Asynchronous Programming & Coroutines in Jetpack Compose

### ⏳ Why Async Matters in UI

Jetpack Compose apps often need to:
- Fetch remote data (network/API calls)
- Perform disk I/O (e.g., Room DB)
- Delay tasks, timers, animations

To keep UI **responsive**, these should run asynchronously using **Kotlin Coroutines**.

---

### 🤴 Suspend Functions vs Coroutines

| Term                | Description                                                                 |
|---------------------|-----------------------------------------------------------------------------|
| `suspend` function  | Function that can pause execution and resume later without blocking thread |
| Coroutine           | A light-weight thread-like job to run suspend functions concurrently        |

```kotlin
suspend fun fetchData(): String {
    delay(1000)
    return "Result"
}
```

---

### 📅 Where to Launch Async Code in Compose?

| Tool/API                 | Best For                                          | Tied to Lifecycle? | Common Usage                          |
|--------------------------|---------------------------------------------------|---------------------|----------------------------------------|
| `LaunchedEffect`         | Start async code on composition or key change     | ✅ Yes              | Initial data load, delays              |
| `rememberCoroutineScope`| Launch coroutine on UI events (e.g., button tap) | ✅ Yes              | Button clicks, manual triggers         |
| `ViewModelScope`         | Long-lived coroutines tied to ViewModel          | ✅ Yes              | Background tasks, DB access            |

---

### 📅 Example 1: Simple Async with `LaunchedEffect`
```kotlin
@Composable
fun AsyncHello() {
    var message by remember { mutableStateOf("Loading...") }

    LaunchedEffect(Unit) {
        delay(2000)
        message = "Hello from coroutine!"
    }

    Text(text = message)
}
```

---

### 📅 Example 2: Async Triggered by Event using `rememberCoroutineScope`
```kotlin
@Composable
fun AsyncButton() {
    val scope = rememberCoroutineScope()
    var result by remember { mutableStateOf("") }

    Button(onClick = {
        scope.launch {
            result = fetchData()
        }
    }) {
        Text("Load Data")
    }

    Text("Result: $result")
}
```

---

### 📅 Example 3: Async in ViewModel using `viewModelScope`
```kotlin
class MyViewModel : ViewModel() {
    var result by mutableStateOf("")
        private set

    fun load() {
        viewModelScope.launch {
            result = fetchData()
        }
    }
}

@Composable
fun ViewModelCaller(viewModel: MyViewModel = viewModel()) {
    Column {
        Button(onClick = { viewModel.load() }) {
            Text("Start")
        }
        Text("Result: ${viewModel.result}")
    }
}
```

---

### 🤔 Choosing the Right Scope

| Situation                         | Use                                  |
|----------------------------------|---------------------------------------|
| One-time task on UI appearance   | `LaunchedEffect`                      |
| Event-based trigger (e.g., click)| `rememberCoroutineScope`             |
| Persistent logic across screens  | `viewModelScope` inside ViewModel    |

---

### ⚠️ Common Mistakes in Async Compose

| Mistake                              | Fix / Tip                                                    |
|--------------------------------------|--------------------------------------------------------------|
| Blocking thread with `Thread.sleep()`| Use `delay()` in suspend function                            |
| Launching coroutine in composable body| Use `LaunchedEffect` or proper scope                         |
| Using non-lifecycle-aware scope      | Avoid `GlobalScope`; prefer `rememberCoroutineScope`/`viewModelScope` |

---

### 🔧 Practice Ideas

| Task                                  | Goal                                             |
|---------------------------------------|--------------------------------------------------|
| Delay + greet                         | Show message 2 seconds after screen opens        |
| Fetch API on button press             | Combine `rememberCoroutineScope` with click     |
| ViewModel coroutine + UI observer     | Use `viewModelScope` and observe `mutableStateOf` |

---

