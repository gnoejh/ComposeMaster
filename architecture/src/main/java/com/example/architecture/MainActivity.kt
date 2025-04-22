package com.example.architecture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.architecture.ui.theme.ComposeMasterTheme
import kotlin.random.Random
import androidx.compose.foundation.text.KeyboardOptions

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
                        listOf("MVC", "MVP", "MVVM", "MVI", "Lab").forEach { architecture ->
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
                "Lab" -> Lab()
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

/*=======================================================================
  Lab (Experimental)
 */
data class GameState(
    val attempts: Int = 0,
    val isGameStarted: Boolean = false,
    val isSuccess: Boolean = false,
    val isFail: Boolean = false,
    val lastFeedback: String = ""
)

class GameModel {
    private var secretNumber: Int = 0
    var gameState = GameState()
        private set

    init {
        startNewGame()
    }

    fun startNewGame() {
        secretNumber = Random.nextInt(1, 101) // Generates a number between 1 and 100
        gameState = GameState(isGameStarted = true)
    }

    fun makeGuess(guess: Int) {
        gameState = when {
            guess < secretNumber -> gameState.copy(
                attempts = gameState.attempts + 1,
                lastFeedback = "Too low!"
            )

            guess > secretNumber -> gameState.copy(
                attempts = gameState.attempts + 1,
                lastFeedback = "Too high!"
            )

            else -> gameState.copy(
                isSuccess = true,
                lastFeedback = "Correct!"
            )
        }
    }
}

class GameViewModel(private val model: GameModel = GameModel()) {
    var gameState by mutableStateOf(model.gameState)
        private set

    fun newGame() {
        model.startNewGame()
        gameState = model.gameState
    }

    fun submitGuess(guess: Int) {
        model.makeGuess(guess)
        gameState = model.gameState
    }
}


@Composable
fun Lab() {
    val model = remember { GameModel() }
    val viewModel = remember { GameViewModel(model) }
    GameScreen(viewModel)
}

@Composable
fun GameScreen(viewModel: GameViewModel) {
    val gameState = viewModel.gameState
    var guessText by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!gameState.isGameStarted) {
            Button(onClick = { viewModel.newGame() }) {
                Text("Start Game")
            }
        } else {
            Text("Attempts: ${gameState.attempts}")
            Text(text = gameState.lastFeedback)

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = guessText,
                onValueChange = { guessText = it },
                label = { Text("Enter your guess") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.padding(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val guess = guessText.text.toIntOrNull()
                    if (guess != null) {
                        viewModel.submitGuess(guess)
                        guessText = TextFieldValue("")
                    }
                }
            ) {
                Text("Submit Guess")
            }

            if (gameState.isSuccess) {
                Text("You win!")
                Button(onClick = { viewModel.newGame() }) {
                    Text("Play Again")
                }
            }

            if (gameState.isFail) {
                Text("You Lose!")
            }
        }

    }
}


/*========================================================================
  Preview
 */

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeMasterTheme {
        ArchitectureApp()
    }
}