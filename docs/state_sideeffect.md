## Combining State and Side Effects in Jetpack Compose

### 📊 Overview

State and side effects often work **together** in Compose. State drives UI, while side effects handle **events and reactions** based on that state (e.g., loading data, showing a toast, saving preferences).

---

### 📅 Common Integration Patterns

| Pattern                                  | When to Use                                           | Example                               |
|------------------------------------------|--------------------------------------------------------|----------------------------------------|
| State triggers `LaunchedEffect`          | React to state changes (like inputs or toggles)        | Input debounce, navigation, API call  |
| State drives UI + `SideEffect`           | Sync UI or system side effects (e.g., scroll, metrics) | Syncing animation with data           |
| `rememberUpdatedState` inside coroutine  | Access latest state in long-running task               | Toast delay, timers                   |
| ViewModel state + Compose side effects   | UI updates + async actions                            | Loading data on screen open           |

---

### 📅 Example 1: State Triggers API Call

```kotlin
@Composable
fun SearchScreen(query: String) {
    var results by remember { mutableStateOf(emptyList<String>()) }

    LaunchedEffect(query) {
        results = searchApi(query) // triggered on every query change
    }

    LazyColumn {
        items(results) {
            Text(it)
        }
    }
}
```

---

### 📅 Example 2: Debounce with State + `snapshotFlow`

```kotlin
@Composable
fun DebouncedSearch() {
    var query by remember { mutableStateOf("") }
    var results by remember { mutableStateOf(emptyList<String>()) }

    LaunchedEffect(Unit) {
        snapshotFlow { query }
            .debounce(300)
            .collectLatest {
                results = searchApi(it)
            }
    }

    Column {
        TextField(value = query, onValueChange = { query = it })
        LazyColumn {
            items(results) { Text(it) }
        }
    }
}
```

---

### 🤔 State vs Side Effect Responsibilities

| Concern                 | Should Use       | Why?                                            |
|-------------------------|------------------|-------------------------------------------------|
| UI contents             | State            | Reflects values like count, text, loading flag  |
| One-time operation      | Side Effect      | Toast, log, navigation, API call                |
| Async on user input     | Side Effect      | Use `LaunchedEffect`, `snapshotFlow`            |
| Persisted UI behavior   | State            | Keep scroll, visibility, toggle flags           |

---

### 📅 Example 3: Loading State + Side Effect

```kotlin
@Composable
fun LoginScreen(viewModel: LoginViewModel = viewModel()) {
    val state = viewModel.uiState

    LaunchedEffect(state.loginSuccess) {
        if (state.loginSuccess) {
            navigateToHome()
        }
    }

    if (state.isLoading) {
        CircularProgressIndicator()
    } else {
        Button(onClick = { viewModel.login() }) {
            Text("Login")
        }
    }
}
```

---

### 🔍 Comparison Table: State vs Side Effect

| Feature                   | State                          | Side Effect                         |
|---------------------------|----------------------------------|--------------------------------------|
| Definition                | UI-driven observable value       | External behavior triggered by UI    |
| Changes across recomposes | ✅ Yes                           | ✅ Depends on trigger                |
| Keeps value across frames | ✅ Yes                           | ❌ (Runs and finishes)               |
| Examples                  | `count`, `query`, `isLoading`    | Toast, API call, navigation          |
| Typical APIs              | `mutableStateOf`, `remember`     | `LaunchedEffect`, `SideEffect`, `snapshotFlow` |

---

### 🔧 Practice Ideas

| Task                                   | Goal                                               |
|----------------------------------------|----------------------------------------------------|
| Show toast on toggle switch            | Side effect responding to state                    |
| Delay search API with debounce         | Practice `snapshotFlow` with `debounce()`          |
| Navigate on login success              | Trigger side effect from state change              |
| Loading + button disable state         | Combine UI state with async handling               |

---

