package com.example.architecture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.architecture.ui.theme.ComposeMasterTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeMasterTheme {
                ArchitectureApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchitectureApp() {
    var selectedArchitecture by remember { mutableStateOf("MVC") }
    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Architecture Models") },
                actions = {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        listOf("MVC", "MVP", "MVVM", "MVI").forEach { architecture ->
                            DropdownMenuItem(
                                text = { Text(architecture) },
                                onClick = {
                                    selectedArchitecture = architecture
                                    menuExpanded = false
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            when (selectedArchitecture) {
                "MVC" -> MVCExample()
                "MVP" -> MVPExample()
                "MVVM" -> MVVMExample()
                "MVI" -> MVIExample()
            }
        }
    }
}

/*=======================================================================
  MVC (Model-View-Controller)
 */


@Composable
fun MVCExample() {
    val model = remember { CounterModelMVC() }
    val controller = remember { CounterController(model) }

    CounterView(
        count = model.count,
        onIncrement = { controller.increment() },
        onDecrement = { controller.decrement() }
    )
}

class CounterModelMVC {
    var count by mutableStateOf(0)
}

class CounterController(private val model: CounterModelMVC) {
    fun increment() {
        model.count++
    }

    fun decrement() {
        model.count--
    }
}

@Composable
fun CounterView(count: Int, onIncrement: () -> Unit, onDecrement: () -> Unit) {
    Column {
        Text("Count: $count")
        Button(onClick = onIncrement) { Text("Increment") }
        Button(onClick = onDecrement) { Text("Decrement") }
    }
}

/*=======================================================================
  MVP (Model-View-Presenter)
 */

class CounterModelMVP {
    var count by mutableStateOf(0)
}

class CounterPresenter(private val model: CounterModelMVP) {
    val count: Int
        get() = model.count

    fun increment() {
        model.count++
    }

    fun decrement() {
        model.count--
    }
}

@Composable
fun MVPExample() {
    val model = remember { CounterModelMVP() }
    val presenter = remember { CounterPresenter(model) }
    CounterView(
        count = presenter.count,
        onIncrement = { presenter.increment() },
        onDecrement = { presenter.decrement() }
    )
}

/*=======================================================================
  MVVM (Model-View-ViewModel)
 */
class CounterModel {
    var count by mutableStateOf(0)
}

class CounterViewModel(private val model: CounterModel) {
    val count: Int
        get() = model.count

    fun increment() {
        model.count++
    }

    fun decrement() {
        model.count--
    }
}

@Composable
fun MVVMExample() {
    val model = remember { CounterModel() }
    val viewModel = remember { CounterViewModel(model) }
    CounterView(
        count = viewModel.count,
        onIncrement = { viewModel.increment() },
        onDecrement = { viewModel.decrement() }
    )
}


/*=======================================================================
  MVI (Model-View-Intent)
 */

@Composable
fun MVIExample() {
    val model = remember { CounterMVIModel() }

    CounterView(
        count = model.state.count,
        onIncrement = { model.dispatch(CounterIntent.Increment) },
        onDecrement = { model.dispatch(CounterIntent.Decrement) }
    )
}

data class CounterState(val count: Int = 0)

sealed class CounterIntent {
    object Increment : CounterIntent()
    object Decrement : CounterIntent()
}

class CounterMVIModel {
    var state by mutableStateOf(CounterState())
        private set

    fun dispatch(intent: CounterIntent) {
        state = when (intent) {
            CounterIntent.Increment -> state.copy(count = state.count + 1)
            CounterIntent.Decrement -> state.copy(count = state.count - 1)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeMasterTheme {
        ArchitectureApp()
    }
}