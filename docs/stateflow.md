
# States in MVVM

In Android's MVVM architecture, the ViewModel serves as a bridge between the Model (data layer) and the View (UI layer). The ViewModel exposes state or events, which the View observes to update the UI reactively. Below is a detailed explanation of the state types, their use cases, and examples.


| **State Type**         | **Declared in ViewModel**                                   | **Observed in View (Compose)**                              | **Use Case**                          | **Reactive?** |
|-------------------------|------------------------------------------------------------|------------------------------------------------------------|----------------------------------------|---------------|
| `State<T>`             | `val state = mutableStateOf("Hello")`                       | `val text = viewModel.state.value`                         | Simple local state                     | ✅ Yes        |
| `MutableState<T>`      | `private val _state = mutableStateOf(...)`                 | `val state = _state.value`                                 | Internal mutable, external readonly    | ✅ Yes        |
| `LiveData<T>`          | `val liveData = MutableLiveData("Hello")`                  | `val text by viewModel.liveData.observeAsState()`          | Legacy compatibility                   | ✅ Yes        |
| `StateFlow<T>`         | `private val _state = MutableStateFlow(...)`<br>`val state: StateFlow<T>` | `val state by viewModel.state.collectAsState()`            | Preferred reactive state for Compose   | ✅ Yes        |
| `SharedFlow<T>`        | `private val _event = MutableSharedFlow(...)`<br>`val event: SharedFlow<T>` | `LaunchedEffect(Unit) { viewModel.event.collect { ... } }` | One-time events (snackbar, navigation) | ➖ Partial    |
| `Channel<T>`           | `val channel = Channel<T>()`                               | `LaunchedEffect(Unit) { channel.receiveAsFlow().collect { ... } }` | Deprecated in favor of SharedFlow      | ➖ Partial    |
| `SnapshotStateList<T>` | `val list = mutableStateListOf<T>()`                        | Used directly in Compose                                   | Observable list                        | ✅ Yes        |
| `SnapshotStateMap<K, V>` | `val map = mutableStateMapOf<K, V>()`                     | Used directly in Compose                                   | Observable map                         | ✅ Yes        |


- Reactive State: Most state types (State, StateFlow, LiveData) are reactive, meaning the UI automatically updates when the state changes.
- One-Time Events: Use SharedFlow or Channel for events like navigation or showing a snackbar.
- Compose-Specific: SnapshotStateList and SnapshotStateMap are optimized for Compose and provide observable collections.

## 