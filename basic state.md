# What is State?
State in Jetpack Compose represents **data that can change over time** and trigger UI updates when modified. Unlike traditional Android UI frameworks, where Views need to be updated manually, Jetpack Compose **automatically recomposes** the UI when state changes.

State should be **immutable** and should be kept inside **Composable functions or ViewModels**, depending on its lifespan.

## **Types of State in Jetpack Compose**

### **1. Local State (Inside a Composable)**
- Used for UI elements whose state does not need to persist beyond the Composable’s lifecycle.
- Managed using `remember` and `mutableStateOf`.

**Example:**
```kotlin
@Composable
fun Counter() {
    var count by remember { mutableStateOf(0) }
    Button(onClick = { count++ }) {
        Text("Count: $count")
    }
}
```

### **2. Persistent State (Across Configuration Changes)**
- Used for UI data that needs to survive **configuration changes** like screen rotation.
- Managed using `rememberSaveable`.

**Example:**
```kotlin
@Composable
fun CounterWithSaveable() {
    var count by rememberSaveable { mutableStateOf(0) }
    Button(onClick = { count++ }) {
        Text("Count: $count")
    }
}
```

### **3. Shared State (ViewModel-based State)**
- Used when state needs to be shared across multiple Composables.
- Managed using `ViewModel`.

**Example:**
```kotlin
class CounterViewModel : ViewModel() {
    var count by mutableStateOf(0)
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

## **Understanding `remember` and Its Internal Values**
The `remember` function is used in Jetpack Compose to store values across recompositions without persisting data across configuration changes (e.g., screen rotation). It helps avoid unnecessary recomputation by caching values within the Composable lifecycle.

### **When to Use `remember`?**
- When you need to **store UI state** that should persist across recompositions.
- When you want to **avoid re-creating objects** in every recomposition.

### **Difference Between `remember` and `rememberSaveable`**
| Function | Purpose | Persists Across Configuration Changes? |
|----------|---------|----------------------------------|
| `remember` | Caches values within the same Composable instance | ❌ No |
| `rememberSaveable` | Caches values AND saves state across configuration changes | ✅ Yes |

### **Difference Between `by remember` and `= remember`**
Both `by remember` and `= remember` are used to store values across recompositions, but they behave slightly differently in terms of syntax and property delegation.

| Syntax | Usage |
|--------|--------|
| `var state by remember { mutableStateOf(value) }` | Uses property delegation with `by`, making `state` directly accessible as a variable. |
| `val state = remember { mutableStateOf(value) }` | Stores the value inside a `State<T>` object, requiring `.value` to access and modify it. |

---

## **State Management Variants in Jetpack Compose**

| State Type | Purpose | Triggers Recomposition? | Example Usage |
|------------|---------|------------------|--------------|
| `mutableStateOf<T>()` | Stores a single mutable value | ✅ Yes | `var count by remember { mutableStateOf(0) }` |
| `mutableStateListOf<T>()` | Stores a list of mutable elements | ✅ Yes | `val items = remember { mutableStateListOf("Item 1", "Item 2") }` |
| `mutableStateMapOf<K, V>()` | Stores a mutable map | ✅ Yes | `val userSettings = remember { mutableStateMapOf("darkMode" to true) }` |
| `derivedStateOf<T>()` | Creates a derived state from existing state | ✅ Yes (only when dependencies change) | `val isEven = derivedStateOf { count.value % 2 == 0 }` |
| `snapshotFlow()` | Converts `State<T>` into a Flow for observing changes | ✅ Yes | `snapshotFlow { count.value } .collect { println("Updated: $it") }` |

---

## **Related Components**

## **1. LazyColumn and LazyRow**
- `LazyColumn` is an optimized scrolling list that only composes visible items.
- `LazyRow` provides horizontal scrolling similar to `LazyColumn`.
- `LazyListState` allows for scroll tracking and restoration.

**Example:**
```kotlin
val listState = rememberLazyListState()
LazyColumn(state = listState) {
    items(10) { index ->
        Text("Item $index")
    }
}
```

---

## **2. TextField and rememberSaveable**
- `TextField` captures user input.
- `rememberSaveable` retains state across configuration changes.

**Example:**
```kotlin
var text by rememberSaveable { mutableStateOf("") }
TextField(value = text, onValueChange = { text = it })
```

---

## **3. Box and Alignment**
- `Box` stacks elements, and `Alignment` controls positioning.

**Example:**
```kotlin
Box(modifier = Modifier.fillMaxSize()) {
    Text("Centered", modifier = Modifier.align(Alignment.Center))
}
```

---

## **4. AnimatedVisibility and MutableState**
- `AnimatedVisibility` toggles UI visibility smoothly.

**Example:**
```kotlin
var visible by remember { mutableStateOf(true) }
AnimatedVisibility(visible) {
    Text("Visible Content")
}
```

---

## **5. rememberCoroutineScope and LaunchedEffect**
- Used to run coroutines within Composables.

**Example:**
```kotlin
val scope = rememberCoroutineScope()
LaunchedEffect(Unit) {
    delay(1000)
    println("Executed after 1s")
}
```

---

## **6. Surface and Shape**
- `Surface` defines container styles and shapes.

**Example:**
```kotlin
Surface(shape = RoundedCornerShape(8.dp), color = Color.Gray) {
    Text("Inside Surface")
}
```

---

This document lists key **Jetpack Compose components**, **state management mechanisms**, and their related **functions and properties**, providing structured examples for better understanding.

