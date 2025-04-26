package com.example.dependencyinjection

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dependencyinjection.ui.theme.ComposeMasterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeMasterTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var showDropdown by remember { mutableStateOf(false) }
    val menuItems = listOf("mvvm", "mvvm1", "mvvm2", "mvvm3", "mvvm4")
    var currentScreen by remember { mutableStateOf("home") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Compose Master") },
                actions = {
                    IconButton(onClick = { showDropdown = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options"
                        )
                    }
                    DropdownMenu(
                        expanded = showDropdown,
                        onDismissRequest = { showDropdown = false }
                    ) {
                        menuItems.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item) },
                                onClick = {
                                    currentScreen = item
                                    showDropdown = false
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (currentScreen) {
                "mvvm" -> MvvmScreen()
                "mvvm1" -> MvvmHiltScreen()
                "mvvm2" -> Mvvm2Screen()
                "mvvm3" -> Mvvm3Screen()
                "mvvm4" -> Mvvm4Screen()
                else -> Greeting(name = "Android")
            }
        }
    }
}

// Model: Represents the data
data class CounterModel(val count: Int = 0)

// ViewModel: Handles UI logic and state management
class CounterViewModel {
    // State for the counter
    private var _counterState = mutableStateOf(CounterModel())
    val counterState = _counterState

    // Operations to modify the state
    fun increment() {
        _counterState.value = CounterModel(_counterState.value.count + 1)
    }

    fun decrement() {
        if (_counterState.value.count > 0) {
            _counterState.value = CounterModel(_counterState.value.count - 1)
        }
    }

    fun reset() {
        _counterState.value = CounterModel(0)
    }
}

@Composable
fun MvvmScreen() {
    // Create ViewModel instance
    val viewModel = remember { CounterViewModel() }
    // Get the current state
    val counterState by viewModel.counterState

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "MVVM Architecture Example",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Counter Value: ${counterState.count}",
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Button(onClick = { viewModel.decrement() }) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Decrement")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Decrement")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(onClick = { viewModel.increment() }) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Increment")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Increment")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.reset() }) {
            Icon(Icons.Default.Delete, contentDescription = "Reset")
            Text("Reset")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "This is a MVVM implementation without dependency injection",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun MvvmHiltScreen() {
    Text(text = "MVVM1 Screen")
}

@Composable
fun Mvvm2Screen() {
    Text(text = "MVVM2 Screen")
}

@Composable
fun Mvvm3Screen() {
    Text(text = "MVVM3 Screen")
}

@Composable
fun Mvvm4Screen() {
    Text(text = "MVVM4 Screen")
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeMasterTheme {
        MainScreen()
    }
}
