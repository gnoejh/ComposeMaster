## Jetpack Compose: State Management

### 🔹 What Is State?
State in Jetpack Compose represents **data that can change over time** and trigger UI updates when modified. Unlike traditional Android UI frameworks, where Views need to be updated manually, Jetpack Compose **automatically recomposes** the UI when state changes.

State should be **immutable** and should be kept inside **Composable functions or ViewModels**, depending on its lifespan.

| Concept          | Description                                                                 |
|------------------|-----------------------------------------------------------------------------|
| State            | A value that changes over time and affects the UI                          |
| Recomposition    | Process where Compose redraws affected parts of the UI on state change     |
| Declarative UI   | UI is described as a function of current state                             |

---

### 🔸 Types of State in Jetpack Compose

#### 1. Local State (Inside a Composable)
- Used for UI elements whose state does not need to persist beyond the Composable’s lifecycle.
- Managed using `remember` and `mutableStateOf`.

```kotlin
@Composable
fun Counter() {
    var count by remember { mutableIntStateOf(0) }
    Button(onClick = { count++ }) {
        Text("Count: $count")
    }
}
```

#### 2. Persistent State (Across Configuration Changes)
- Used for UI data that needs to survive **configuration changes** like screen rotation.
- Managed using `rememberSaveable`.

```kotlin
@Composable
fun CounterWithSaveable() {
    var count by rememberSaveable { mutableIntStateOf(0) }
    Button(onClick = { count++ }) {
        Text("Count: $count")
    }
}
```

#### 3. Shared State (ViewModel-based State)
- Used when state needs to be shared across multiple Composables.
- Managed using `ViewModel`.

```kotlin
class CounterViewModel : ViewModel() {
    var count by mutableIntStateOf(0)
        private set
    fun increment() { count++ }
}

@Composable
fun CounterScreen(viewModel: CounterViewModel = viewModel()) {
    Button(onClick = { viewModel.increment() }) {
        Text("Count: ${viewModel.count}")
    }
}
```

---

### 🔄 State Flow in Compose

```
User Action → State Changes → Recomposition → Updated UI
```

You don’t directly modify the UI. You change the state, and Compose handles the rest.

---

### 🧠 Understanding `remember` and Its Internal Values

The `remember` function is used in Jetpack Compose to store values across recompositions without persisting data across configuration changes (e.g., screen rotation). It helps avoid unnecessary recomputation by caching values within the Composable lifecycle.

#### When to Use `remember`?
- When you need to **store UI state** that should persist across recompositions.
- When you want to **avoid re-creating objects** in every recomposition.

#### Comparison: `by remember` vs `= remember`

| Syntax | Usage |
|--------|--------|
| `var state by remember { mutableStateOf(value) }` | Uses property delegation with `by`, making `state` directly accessible as a variable. |
| `val state = remember { mutableStateOf(value) }` | Stores the value inside a `State<T>` object, requiring `.value` to access and modify it. |

| Form                | Usage                                                      | Access       | Readability | Recommended |
|---------------------|------------------------------------------------------------|--------------|-------------|-------------|
| `var x by remember` | Uses Kotlin delegation; directly reads/writes the value    | `x`          | High        | Yes         |
| `val x = remember`  | Returns `MutableState<T>`; requires `.value` to access     | `x.value`    | Medium      | Sometimes   |

---

### 🧠 remember vs rememberSaveable vs derivedStateOf

| Function             | Purpose                                                             | Persists Across Configuration Changes? |
|----------------------|----------------------------------------------------------------------|-----------------------------------------|
| `remember`           | Caches values within the same Composable instance                   | ❌ No                                    |
| `rememberSaveable`   | Caches values AND saves state across configuration changes           | ✅ Yes                                   |
| `derivedStateOf`     | Calculates a derived value from other states (memoized)             | ❌ No                                    |

---

### 📚 State Management Variants in Jetpack Compose

| State Type | Purpose | Triggers Recomposition? | Example Usage |
|------------|---------|--------------------------|---------------|
| `mutableStateOf<T>()` | Stores a single mutable object value | ✅ Yes | `var name by remember { mutableStateOf("") }` |
| `mutableIntStateOf()` | Stores a primitive `Int` without boxing | ✅ Yes | `var count by remember { mutableIntStateOf(0) }` |
| `mutableDoubleStateOf()` | Stores a primitive `Double` without boxing | ✅ Yes | `var price by remember { mutableDoubleStateOf(0.0) }` |
| `mutableFloatStateOf()` | Stores a primitive `Float` without boxing | ✅ Yes | `var ratio by remember { mutableFloatStateOf(0.0f) }` |
| `mutableLongStateOf()` | Stores a primitive `Long` without boxing | ✅ Yes | `var time by remember { mutableLongStateOf(0L) }` |
| `mutableStateListOf<T>()` | Stores a mutable list | ✅ Yes | `val items = remember { mutableStateListOf("Item 1", "Item 2") }` |
| `mutableStateMapOf<K, V>()` | Stores a mutable map | ✅ Yes | `val userSettings = remember { mutableStateMapOf("darkMode" to true) }` |
| `mutableStateOf(setOf<T>())` | Stores a mutable set | ✅ Yes | `val items = remember { mutableStateOf(setOf("Item1", "Item2")) }` |
| `derivedStateOf<T>()` | Creates a derived state from existing state | ✅ Yes (on dependency change) | `val isEven = derivedStateOf { count % 2 == 0 }` |
| `snapshotFlow()` | Converts `State<T>` into a Flow for observing changes | ✅ Yes | `snapshotFlow { count }.collect { println(it) }` |

---

### ⚠️ Common Mistakes and Best Practices

| Mistake                               | Fix / Recommendation                                       |
|----------------------------------------|-------------------------------------------------------------|
| Using regular `var` for state          | Use `remember { mutableStateOf(...) }`                      |
| Forgetting `.value` with `= remember`  | Use `by` delegation to avoid manual `.value` management     |
| Ignoring configuration changes         | Use `rememberSaveable` where needed                         |
| Unnecessary boxing of primitives       | Use `mutableIntStateOf()`, etc., for primitive types        |

---
