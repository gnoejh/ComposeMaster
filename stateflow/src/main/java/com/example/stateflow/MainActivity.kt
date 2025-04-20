package com.example.stateflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.example.stateflow.ui.theme.ComposeMasterTheme
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeMasterTheme {
                StateFlowScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StateFlowScreen() {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Select an option") }
    var showLiveData by remember { mutableStateOf(false) }
    var showStateFlow by remember { mutableStateOf(false) }
    var showSharedFlow by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Flow Examples") },
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "Localized description")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("LiveData") },
                            onClick = {
                                selectedOption = "LiveData Selected"
                                expanded = false
                                showLiveData = true
                                showStateFlow = false
                                showSharedFlow = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("StateFlow") },
                            onClick = {
                                selectedOption = "StateFlow Selected"
                                expanded = false
                                showLiveData = false
                                showStateFlow = true
                                showSharedFlow = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("SharedFlow") },
                            onClick = {
                                selectedOption = "SharedFlow Selected"
                                expanded = false
                                showLiveData = false
                                showStateFlow = false
                                showSharedFlow = true
                            }
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = selectedOption)
                Spacer(modifier = Modifier.height(20.dp))
                if (showLiveData) {
                    LiveDataExample()
                }
                if (showStateFlow) {
                    StateFlowExample()
                }
                if (showSharedFlow) {
                    SharedFlowExample()
                }
            }
        }
    }
}

/*===============================================
 * LiveData Example: The purpose of LiveData is to hold
 * UI-related data that is lifecycle-aware.
 *==============================================*/
// Data class to hold the state of the UI
data class UiMessage(val message: String, val isVisible: Boolean)

@Composable
fun LiveDataExample() {
    val viewModel: LiveDataViewModel = LiveDataViewModel()
    val uiMessage: UiMessage? by viewModel.uiMessage.observeAsState()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (uiMessage?.isVisible == true) {
            Text(text = uiMessage!!.message)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { viewModel.showMessage() }) {
            Text("Show Message")
        }
        Button(onClick = { viewModel.hideMessage() }) {
            Text("Hide Message")
        }
    }
}

class LiveDataViewModel : ViewModel() {
    private val _uiMessage = MutableLiveData<UiMessage>()
    val uiMessage: LiveData<UiMessage> = _uiMessage

    fun showMessage() {
        // Simulate showing a message
        viewModelScope.launch {
            delay(500)
            _uiMessage.value = UiMessage("Hello from LiveData!", true)
        }
    }
    fun hideMessage(){
        viewModelScope.launch {
            delay(500)
            _uiMessage.value = UiMessage("Hello from LiveData!", false)
        }
    }
}

/*===============================================
 * StateFlow Example: The purpose of StateFlow is
 * to hold a state and emit updates to that state.
 *==============================================*/

//sealed class for define the status of the operation
sealed class UiState {
    object Loading : UiState()
    data class Success(val count: Int) : UiState()
    data class Error(val message: String) : UiState()
}

@Composable
fun StateFlowExample() {
    val viewModel: StateFlowViewModel = StateFlowViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        when (uiState) {
            is UiState.Loading -> Text("Loading...")
            is UiState.Success -> {
                val count = (uiState as UiState.Success).count
                Text(text = "Counter: $count")
            }

            is UiState.Error -> Text(text = "Error: ${(uiState as UiState.Error).message}")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { viewModel.incrementCounter() }) {
            Text("Increment Counter")
        }
        Button(onClick = { viewModel.showError() }) {
            Text("Show Error")
        }
    }
}

class StateFlowViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            delay(1000)
            _uiState.value = UiState.Success(0)
        }
    }

    fun incrementCounter() {
        viewModelScope.launch {
            if (_uiState.value is UiState.Success) {
                val currentCount = (_uiState.value as UiState.Success).count
                _uiState.value = UiState.Success(currentCount + 1)
            }
        }
    }
    fun showError() {
        viewModelScope.launch {
            _uiState.value = UiState.Error("An error occurred")
        }
    }
}

/*===============================================
 *SharedFlow Example: The purpose of SharedFlow is to emit
 * events to multiple collectors.
 *==============================================*/
@Composable
fun SharedFlowExample() {
    val viewModel: SharedFlowViewModel = SharedFlowViewModel()
    val message by viewModel.message.collectAsStateWithLifecycle(initialValue = "")

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Message: $message")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { viewModel.sendMessage() }) {
            Text("Send Message")
        }
        Button(onClick = { viewModel.sendAnotherMessage() }) {
            Text("Send Another Message")
        }
    }
}

class SharedFlowViewModel : ViewModel() {
    private val _message = MutableSharedFlow<String>()
    val message: SharedFlow<String> = _message.asSharedFlow()

    fun sendMessage() {
        viewModelScope.launch {
            _message.emit("New Message at ${System.currentTimeMillis()}")
        }
    }
    fun sendAnotherMessage() {
        viewModelScope.launch {
            _message.emit("Another Message at ${System.currentTimeMillis()}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeMasterTheme {
        StateFlowScreen()
    }
}