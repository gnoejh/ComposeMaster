package com.example.architecture

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.architecture.ui.theme.ComposeMasterTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

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
                        listOf("MVC", "MVP", "MVVM", "MVI", "Lab", "Lab2").forEach { architecture ->
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
                "Lab2" -> Lab2()
                else -> {
                    Text("Select an architecture model from the menu.")
                }
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
  0. your photo/name/id: mandatory

    I will choose one the following
    Must be different: no copy
  1. make or Buttons for level change (1-10, 1-100, 1-1000),
  2. put timer for run out,
  3. use emoji for feedback,
  4. show animation for success and fail,
  5. show hint for number (even, odd, prime, etc.),
  6. design
  7. others
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

/*=======================================================================
  Lab2 (Experimental)
  */

// Hangman Model - Handles game logic
data class HangmanGameState(
    val word: String = "",
    val hiddenWord: String = "",
    val guessedLetters: Set<Char> = setOf(),
    val incorrectGuesses: Int = 0,
    val maxIncorrectGuesses: Int = 6,
    val isGameOver: Boolean = false,
    val isWon: Boolean = false,
    val showAnimation: Boolean = false
)

class HangmanModel {
    // List of words for the game
    private val wordList = listOf("ANDROID", "KOTLIN", "COMPOSE", "JETPACK", "DEVELOPER", 
                                "PROGRAMMING", "FUNCTION", "VARIABLE", "INTERFACE", "ANIMATION")
    
    var gameState by mutableStateOf(HangmanGameState())
        private set
    
    init {
        startNewGame()
    }
    
    fun startNewGame() {
        val newWord = wordList.random()
        val hiddenWord = "_".repeat(newWord.length)
        gameState = HangmanGameState(word = newWord, hiddenWord = hiddenWord)
    }
    
    fun guessLetter(letter: Char) {
        if (gameState.isGameOver || letter in gameState.guessedLetters) {
            return
        }
        
        val upperLetter = letter.uppercaseChar()
        val newGuessedLetters = gameState.guessedLetters + upperLetter
        
        val isCorrectGuess = gameState.word.contains(upperLetter)
        val newIncorrectGuesses = if (isCorrectGuess) 
                                    gameState.incorrectGuesses 
                                  else 
                                    gameState.incorrectGuesses + 1
        
        // Update hidden word to reveal correctly guessed letters
        val newHiddenWord = gameState.word.map { char ->
            if (char in newGuessedLetters) char else '_'
        }.joinToString("")
        
        val isWon = !newHiddenWord.contains('_')
        val isLost = newIncorrectGuesses >= gameState.maxIncorrectGuesses
        
        gameState = gameState.copy(
            guessedLetters = newGuessedLetters,
            hiddenWord = newHiddenWord,
            incorrectGuesses = newIncorrectGuesses,
            isWon = isWon,
            isGameOver = isWon || isLost,
            showAnimation = isWon || isLost
        )
    }
}

// Hangman ViewModel
class HangmanViewModel(private val model: HangmanModel = HangmanModel()) {
    val gameState = model.gameState
    
    fun guessLetter(letter: Char) {
        model.guessLetter(letter)
    }
    
    fun newGame() {
        model.startNewGame()
    }
    
    fun getDisplayWord(): String {
        return gameState.hiddenWord.map { char -> 
            if (char == '_') "_ " else "$char "
        }.joinToString("")
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Lab2() {
    val model = remember { HangmanModel() }
    val viewModel = remember { HangmanViewModel(model) }
    
    var inputText by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Main game UI
        HangmanGame(viewModel)
        
        // Keyboard listener
        BasicTextField(
            value = inputText,
            onValueChange = { newText ->
                if (newText.isNotEmpty() && inputText != newText) {
                    val lastChar = newText.last()
                    if (lastChar.isLetter()) {
                        viewModel.guessLetter(lastChar)
                        // Show a toast for feedback
                        Toast.makeText(context, "Guessed: ${lastChar.uppercase()}", Toast.LENGTH_SHORT).show()
                    }
                }
                inputText = ""  // Clear after processing
            },
            modifier = Modifier
                .width(1.dp)
                .height(1.dp)
                .focusRequester(focusRequester)
                .align(Alignment.Center),
            // Hide visual representation
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.Transparent),
        )
        
        // Request focus when component enters composition
        LaunchedEffect(Unit) {
            delay(100) // Short delay to ensure UI is ready
            focusRequester.requestFocus()
        }
        
        // "Tap to enable keyboard" button
        Button(
            onClick = { focusRequester.requestFocus() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            Text("Tap to enable keyboard input")
        }
    }
}

@Composable
fun HangmanGame(viewModel: HangmanViewModel) {
    // Observe gameState with derivedStateOf to prevent unnecessary recompositions
    val gameState by remember { derivedStateOf { viewModel.gameState } }
    val isGameOver = gameState.isGameOver
    
    // Animation for hangman figure
    val hangmanAlpha by animateFloatAsState(
        targetValue = gameState.incorrectGuesses.toFloat() / gameState.maxIncorrectGuesses,
        animationSpec = tween(500),
        label = "hangmanAnimation"
    )
    
    // Animation for winning or losing
    val resultScale by animateFloatAsState(
        targetValue = if (gameState.showAnimation) 1.2f else 0f,
        animationSpec = spring(
            dampingRatio = 0.4f,
            stiffness = 300f
        ),
        label = "resultAnimation"
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Hangman Game",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Hangman figure with animated appearance
        Text(
            text = """
                |--------
                |    ${if (gameState.incorrectGuesses > 0) "O" else " "}
                |   ${if (gameState.incorrectGuesses > 1) "/" else " "}${if (gameState.incorrectGuesses > 2) "|" else " "}${if (gameState.incorrectGuesses > 3) "\\" else " "}
                |    ${if (gameState.incorrectGuesses > 4) "|" else " "}
                |   ${if (gameState.incorrectGuesses > 5) "/ \\" else "   "}
            """.trimIndent(),
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.alpha(hangmanAlpha)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Animated display of word with blanks
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            gameState.hiddenWord.forEachIndexed { index, char ->
                val isRevealed = char != '_'
                val letterAlpha by animateFloatAsState(
                    targetValue = if (isRevealed) 1f else 0.6f,
                    animationSpec = tween(
                        durationMillis = 500,
                        delayMillis = if (isRevealed) index * 100 else 0
                    ),
                    label = "letterAlpha_$index"
                )
                
                val letterScale by animateFloatAsState(
                    targetValue = if (isRevealed) 1.2f else 1f,
                    animationSpec = spring(
                        dampingRatio = 0.5f,
                        stiffness = 300f
                    ),
                    label = "letterScale_$index"
                )
                
                Text(
                    text = if (char == '_') "_" else char.toString(),
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .alpha(letterAlpha)
                        .scale(letterScale),
                    color = if (isRevealed) Color(0xFF4CAF50) else Color.Gray
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Game status with animation for incorrect guesses
        val incorrectGuessesAnimation by animateIntAsState(
            targetValue = gameState.incorrectGuesses,
            animationSpec = tween(500),
            label = "incorrectGuessesAnimation"
        )
        
        Text(
            text = "Incorrect guesses: $incorrectGuessesAnimation/${gameState.maxIncorrectGuesses}",
            style = MaterialTheme.typography.bodyLarge
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Win/Lose message with animation
        if (gameState.isGameOver) {
            Text(
                text = if (gameState.isWon) "You Won! 🎉" else "Game Over 😢",
                style = MaterialTheme.typography.headlineMedium,
                color = if (gameState.isWon) 
                            Color.Green 
                        else 
                            Color.Red,
                modifier = Modifier
                    .scale(resultScale)
                    .alpha(resultScale)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Animated reveal of the word
            val wordRevealProgress by animateFloatAsState(
                targetValue = if (gameState.isGameOver) 1f else 0f,
                animationSpec = tween(1000),
                label = "wordRevealAnimation"
            )
            
            Text(
                text = "The word was: ${gameState.word}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.alpha(wordRevealProgress)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { viewModel.newGame() },
                modifier = Modifier
                    .alpha(resultScale)
                    .scale(if (resultScale > 1f) 1f else resultScale)
            ) {
                Text("Play Again")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Real keyboard instruction
        Text(
            text = "You can also type using your keyboard!",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Keyboard for letter input
        if (!isGameOver) {
            VirtualKeyboard(
                onKeyClick = { letter -> viewModel.guessLetter(letter) },
                guessedLetters = gameState.guessedLetters,
                wordLetters = gameState.word.toSet()
            )
        }
    }
}

@Composable
fun VirtualKeyboard(
    onKeyClick: (Char) -> Unit,
    guessedLetters: Set<Char>,
    wordLetters: Set<Char>
) {
    val rows = listOf(
        "QWERTYUIOP",
        "ASDFGHJKL",
        "ZXCVBNM"
    )
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(4.dp) // Add padding to ensure visibility on some devices
    ) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                row.forEach { letter ->
                    val isGuessed = letter in guessedLetters
                    val isCorrect = letter in wordLetters && isGuessed
                    
                    // Add animation for key press feedback
                    var isPressed by remember { mutableStateOf(false) }
                    val keyScale by animateFloatAsState(
                        targetValue = if (isPressed) 0.8f else 1f,
                        animationSpec = spring(), // Add explicit animation spec
                        label = "keyAnimation_$letter"
                    )
                    
                    // Use a coroutineScope that's properly scoped to the composable
                    val scope = rememberCoroutineScope()
                    
                    OutlinedButton(
                        onClick = { 
                            if (!isGuessed) {
                                isPressed = true
                                onKeyClick(letter)
                                // Reset press state after animation using the scoped coroutine
                                scope.launch {
                                    kotlinx.coroutines.delay(100)
                                    isPressed = false
                                }
                            } 
                        },
                        enabled = !isGuessed,
                        modifier = Modifier
                            .size(40.dp)
                            .scale(keyScale),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = when {
                                isCorrect -> Color(0xFFE8F5E9) // Light green for correct guesses
                                isGuessed -> Color(0xFFFFEBEE) // Light red for incorrect guesses
                                else -> MaterialTheme.colorScheme.surface
                            }
                        )
                    ) {
                        Text(text = letter.toString())
                    }
                }
            }
        }
    }
}

/*========================================================================
  Preview
 */
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeMasterTheme {
        ArchitectureApp()
    }
}

