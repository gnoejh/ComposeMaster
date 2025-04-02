package com.example.lifecycle

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.lifecycle.ui.theme.ComposeMasterTheme
import kotlinx.coroutines.delay

/**
 * The main activity of the application.
 * This activity serves as the entry point for the app and handles the initial setup of the UI.
 */
class MainActivity : ComponentActivity() {
    /**
     * Called when the activity is starting.
     * This is where most initialization should go.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in onSaveInstanceState.  Note: Otherwise it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enables edge-to-edge display, allowing content to draw behind system bars.
        // This makes the app look more modern by extending the content under the status and navigation bars.
        enableEdgeToEdge()
        // Sets the Compose content for this activity.
        // Compose is a declarative UI toolkit for building Android UIs.
        setContent {
            // Sets up the Compose UI with a custom theme defined in the ComposeMasterTheme.
            // This ensures that all components within this scope follow the same visual styling.
            ComposeMasterTheme {
                // Calls the main composable function that builds the app's UI.
                // This is the starting point for the entire UI.
                LifecycleApp()
            }
        }
    }
}

/**
 * Enum class representing different topics related to lifecycles.
 * Each topic has a title that can be displayed in the UI.
 * This helps in organizing the content of the app and makes it easier to navigate.
 */
enum class LifecycleTopic(val title: String) {
    // Represents the Android Activity/Fragment lifecycle.
    // This topic will cover the standard Android lifecycle methods like onCreate, onStart, onResume, etc.
    ANDROID_LIFECYCLE("Android Lifecycle"),

    // Represents the Jetpack Compose composition lifecycle.
    // This topic will explain how composables are composed, recomposed, and disposed of.
    COMPOSE_LIFECYCLE("Compose Lifecycle"),

    // Represents observing Android lifecycle events.
    // This topic will demonstrate how to listen for changes in the Android lifecycle and react to them.
    OBSERVING_LIFECYCLE("Observing Lifecycle Events"),

    // Represents the Compose lifecycle-aware APIs.
    // This topic will cover the effect APIs like LaunchedEffect, DisposableEffect, etc., and how they relate to lifecycle.
    LIFECYCLE_AWARE_APIS("Lifecycle-Aware APIs"),

    // Represents common pitfalls and best practices related to lifecycle.
    // This topic will give advice on what to avoid and how to manage lifecycle events effectively.
    PITFALLS_BEST_PRACTICES("Pitfalls and Best Practices"),
    GAME("Game")
}

/**
 * Main composable function for the Lifecycle guide app.
 * This sets up the Scaffold, TopAppBar, and DropdownMenu for topic selection.
 * It represents the main screen of the app.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LifecycleApp() {
    // State to keep track of the currently selected topic.
    // `mutableStateOf` creates a mutable state that can be updated.
    // `remember` ensures that the state is retained across recompositions.
    var selectedTopic by remember { mutableStateOf(LifecycleTopic.ANDROID_LIFECYCLE) }
    // State to control whether the dropdown menu is expanded.
    // This controls the visibility of the menu.
    var isDropdownExpanded by remember { mutableStateOf(false) }

    // Scaffold provides a basic layout structure with a TopAppBar and content area.
    // It's a common container for building material design layouts.
    Scaffold(
        // Modifiers to fill the max size of the view
        modifier = Modifier.fillMaxSize(),
        // TopAppBar for displaying the app title and dropdown menu.
        // It is placed at the top of the screen.
        topBar = {
            TopAppBar(
                // The main title of the app displayed in the TopAppBar.
                title = { Text(text = "Lifecycle Guide") },
                // Configures the colors of the TopAppBar to match the material 3 design.
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                // Actions placed at the end of the TopAppBar, in this case, a menu.
                // It allows to show action buttons, like a menu.
                actions = {
                    // IconButton to open the dropdown menu.
                    // When pressed the dropdown menu is shown.
                    IconButton(onClick = { isDropdownExpanded = true }) {
                        // Icon for the "More" button.
                        // The icon represents that there is more options.
                        Icon(Icons.Filled.MoreVert, contentDescription = "More")
                    }
                    // DropdownMenu to display the available lifecycle topics.
                    // It is a menu that appears when the iconbutton is clicked.
                    DropdownMenu(
                        //If the value is true, the menu is shown
                        expanded = isDropdownExpanded,
                        // Dismiss the menu when user click outside.
                        // The menu is closed when the user clicks outside the menu.
                        onDismissRequest = { isDropdownExpanded = false }
                    ) {
                        // Loop to create a MenuItem for each enum element.
                        // The loop generate a list of menu items.
                        LifecycleTopic.entries.forEach { topic ->
                            // Each DropdownMenuItem represents a lifecycle topic.
                            // Each item has a text and a click action.
                            DropdownMenuItem(
                                //The text of the item
                                text = { Text(text = topic.title) },
                                // Update the state when a topic is selected.
                                // When an item is clicked, it update the state.
                                onClick = {
                                    selectedTopic = topic
                                    isDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        // Main content area of the app, padded to not overlap with the TopAppBar.
        // The padding is the space that separates the content from the topbar.
        Column(modifier = Modifier.padding(innerPadding)) {
            // Call the composable that renders content for the selected topic.
            // This composable render the content.
            LifecycleTopicContent(selectedTopic)
        }
    }
}

/**
 * Composable function that renders content based on the selected lifecycle topic.
 * It uses a when statement to display the appropriate content.
 * It decides what to display according to the selected topic.
 */
@Composable
fun LifecycleTopicContent(topic: LifecycleTopic) {
    // Conditional rendering based on the selected topic.
    // The when statement is like a switch, used to select one composable based in the topic.
    when (topic) {
        LifecycleTopic.ANDROID_LIFECYCLE -> AndroidLifecycleContent()
        LifecycleTopic.COMPOSE_LIFECYCLE -> ComposeLifecycleContent()
        LifecycleTopic.OBSERVING_LIFECYCLE -> ObservingLifecycleContent()
        LifecycleTopic.LIFECYCLE_AWARE_APIS -> LifecycleAwareApisContent()
        LifecycleTopic.PITFALLS_BEST_PRACTICES -> PitfallsBestPracticesContent()
        LifecycleTopic.GAME -> GameContent()
    }
}

/**
 * Composable function that displays information about the Android lifecycle.
 * It provides a brief description of the Android lifecycle stages.
 */
@Composable
fun AndroidLifecycleContent() {
    Text(
        text = "Android Lifecycle: \n onCreate, \n onStart, \n onResume, \n onPause, \n onStop, \n onDestroy",
        style = MaterialTheme.typography.headlineLarge,
        textAlign = TextAlign.Center
    )
}

/**
 * Composable function that displays information about the Compose lifecycle.
 * It explains the different stages of the Compose lifecycle.
 */
@Composable
fun ComposeLifecycleContent() {
    Text(
        text = "Compose Lifecycle: \n Composition, \n Recomposition, \n Decomposition",
        style = MaterialTheme.typography.headlineLarge,
        textAlign = TextAlign.Center
    )
}

/**
 * Composable function that demonstrates observing Android lifecycle events.
 * It logs each event to the console.
 * This function shows how to track Android lifecycle events using a LifecycleEventObserver.
 * 1.LocalLifecycleOwner.current Provides Context: Inside a composable,
 * you use LocalLifecycleOwner.current to get the LifecycleOwner that's associated with the current composition.
 * 2.LifecycleOwner Provides the Lifecycle: The LifecycleOwner has a lifecycle property which is a Lifecycle object.
 * 3.Lifecycle Manages Events: The Lifecycle object tracks the lifecycle state and dispatches events.
 * 4.LifecycleEventObserver Listens: You create a LifecycleEventObserver and add it to the Lifecycle using addObserver().
 * 5.Events are received: When a lifecycle event happens (e.g., ON_START, ON_RESUME, ON_PAUSE),
 * the Lifecycle notifies all registered LifecycleEventObserver instances.
 * 6.Actions are taken: Your LifecycleEventObserver's onStateChanged() method is called, and you can execute the necessary code based on the event.
 * 7.Clean up: When the Composable is removed from the composition,
 * you should remove your observer using removeObserver() to avoid memory leaks.
 * The DisposableEffect is useful to perform this clean up.
 */
@Composable
fun ObservingLifecycleContent(lifecycleOwner: LifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current) {
    // Observe lifecycle events safely within DisposableEffect
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            Log.d("LifecycleObserver", "Observed event: $event")
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Text(
        text = "Observing Android Lifecycle events. Check Logcat for event output, LifecycleObserver.",
        style = MaterialTheme.typography.headlineLarge,
        textAlign = TextAlign.Center
    )
}

/**
 * Composable function that explains Compose lifecycle-aware APIs.
 * This function provides a list of Compose lifecycle-aware APIs.
 */
@Composable
fun LifecycleAwareApisContent() {
    Column {
        Text(
            text = "Lifecycle-Aware APIs: \n LaunchedEffect, \n DisposableEffect, \n SideEffect, \n rememberUpdatedState",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(64.dp))
        Text(
            text = "LaunchedEffect: Used for launching coroutines in a composable. It runs when the key changes.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Left
        )
        LaunchedEffectExample()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "DisposableEffect: Used for side effects that need to be cleaned up. It runs when the key changes.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Left
        )
        DisposableEffectExample()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "SideEffect: Used for side effects that should run after the composition. It runs on every recomposition.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Left
        )
        SideEffectExample()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "rememberUpdatedState: Used to remember the latest value of a state variable.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Left
        )
        RememberUpdatedStateExample(0)
    }
}

/**
 * Composable function that explains common pitfalls and best practices.
 * It gives advice to avoid common mistakes.
 */
@Composable
fun PitfallsBestPracticesContent() {
    Text(
        text = "Pitfalls: Triggering suspend functions outside effects, not cleaning up, using GlobalScope, expensive work in recomposition.",
        style = MaterialTheme.typography.headlineLarge,
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(32.dp))
    Text(
        text = "Best Practices: LaunchedEffect for coroutines, onDispose, rememberCoroutineScope or viewModelScope, move side effects to effect blocks.",
        style = MaterialTheme.typography.headlineLarge,
        textAlign = TextAlign.Center
    )
}

/**
 * Composable function that demonstrates the usage of LaunchedEffect.
 * This function contains a launchedEffect that will update a counter every second.
 */
@Composable
fun LaunchedEffectExample() {
    var timerValue by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000) // Wait for 1 second
            timerValue++
        }
    }

    Text(
        text = "LaunchedEffect Example: Timer: $timerValue",
        style = MaterialTheme.typography.bodyLarge,
        color = Color.Red,
        textAlign = TextAlign.Left
    )
}

/**
 * Composable function that demonstrates the usage of DisposableEffect.
 * This function contains a disposableEffect that listen to lifecycle events.
 */
@Composable
fun DisposableEffectExample() {
    //The LocalLifecycleOwner.current is inside the composable
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            Log.d("DisposableEffect", "Observed event: $event")
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            Log.d("DisposableEffect", "Removed observer")
        }
    }
    Text(
        text = "DisposableEffect Example: Check Logcat for event output.",
        style = MaterialTheme.typography.bodyLarge,
        color = Color.Red,
        textAlign = TextAlign.Left
    )
}

/**
 * Composable function that demonstrates the usage of SideEffect.
 * This function contains a side effect that increment a counter with every recomposition.
 */
@Composable
fun SideEffectExample() {
    var counter by remember { mutableStateOf(0) }

    SideEffect {
        counter++
        Log.d("SideEffect", "SideEffect ran. Counter: $counter")
    }

    Text(
        text = "SideEffect Example: Counter: $counter. Check Logcat for recompositions.",
        style = MaterialTheme.typography.bodyLarge,
        color = Color.Red,
        textAlign = TextAlign.Left
    )
}

/**
 * Composable function that demonstrates the usage of rememberUpdatedState.
 * This function contains a launchedEffect that respect the latest value of the count variable.
 */
@Composable
fun RememberUpdatedStateExample(initialCount: Int) {
    var count by remember { mutableStateOf(initialCount) }

    val currentCount by rememberUpdatedState(newValue = count)

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000) // Wait for 1 second
            count++ // Update the state value. This will trigger recomposition.
            Log.d("RememberUpdatedState", "LaunchedEffect: $currentCount")
        }
    }

    Text(
        text = "RememberUpdatedState Example: Count: $count. Check Logcat for latest values.",
        style = MaterialTheme.typography.bodyLarge,
        color = Color.Red,
        textAlign = TextAlign.Left
    )
}

/**
 * Composable function that displays content for the "Game" topic.
 * This code will display a Hangman game in the screen.
 * Each time the user change the state of the lifecycle, using the back button, the lives will be updated.
 */
//@Composable
//fun GameContent() {
//    // State variables for the game
//    var wordToGuess by remember { mutableStateOf("") }
//    var guessedLetters = remember { mutableStateListOf<Char>() }
//    var lives by remember { mutableStateOf(6) }
//    var gameMessage by remember { mutableStateOf("Guess a letter!") }
//
//    // List of words to choose from
//    val wordList = remember {
//        listOf(
//            "ANDROID",
//            "KOTLIN",
//            "COMPOSE",
//            "DEVELOPER",
//            "JETPACK",
//            "GOOGLE",
//            "MOBILE",
//            "STUDIO",
//            "APPLICATION",
//            "HONGJEONG"
//        )
//    }
//
//    //Launched effect to select a new word when the view is created.
//    LaunchedEffect(Unit) {
//        wordToGuess = wordList.random()
//    }
//
//    // Get the current lifecycle owner (Activity or Fragment).
//    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
//
//    // DisposableEffect to observe lifecycle events.
//    DisposableEffect(lifecycleOwner) {
//        // Create a LifecycleEventObserver to listen for lifecycle events.
//        val observer = LifecycleEventObserver { _, event ->
//            // Check if the lifecycle event is ON_STOP.
//            if (event == Lifecycle.Event.ON_STOP) {
//                //If the state is stop the value of lives will be updated.
//                lives = 6
//                guessedLetters.clear()
//                gameMessage = "Guess a letter!"
//                wordToGuess = wordList.random()
//                Log.d("Game", "Observed event: $event, lives: $lives")
//            }
//            // Log all the events.
//            Log.d("Game", "Observed event: $event")
//        }
//        // Add the observer to the lifecycle.
//        lifecycleOwner.lifecycle.addObserver(observer)
//
//        // When the DisposableEffect is disposed, remove the observer to avoid memory leaks.
//        onDispose {
//            lifecycleOwner.lifecycle.removeObserver(observer)
//        }
//    }
//
//    // Function to handle letter guessing
//    fun guessLetter(letter: Char) {
//        if (letter !in guessedLetters) {
//            guessedLetters.add(letter)
//            if (letter !in wordToGuess) {
//                lives--
//            }
//            if (lives == 0) {
//                gameMessage =
//                    "You lost! The word was $wordToGuess. Click the back button and try again"
//            } else if (wordToGuess.all { it in guessedLetters }) {
//                gameMessage = "You won! Click the back button and try again"
//            } else {
//                gameMessage = "Guess a letter!"
//            }
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        // Display the word with underscores
//        val displayWord = wordToGuess.map { if (it in guessedLetters) it else '_' }
//        Text(
//            text = displayWord.joinToString(" "),
//            style = MaterialTheme.typography.headlineLarge,
//            textAlign = TextAlign.Center
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Display lives
//        Text(text = "Lives: $lives")
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Display game message
//        Text(text = gameMessage, textAlign = TextAlign.Center)
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Display guessed letters
//        Text(text = "Guessed letters: ${guessedLetters.joinToString(", ")}")
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Input for guessing a letter
//        var inputLetter by remember { mutableStateOf("") }
//        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
//            TextField(
//                value = inputLetter,
//                onValueChange = {
//                    if (it.length <= 1) {
//                        inputLetter = it.uppercase()
//                    }
//                },
//                label = { Text("Enter a letter") }
//            )
//
//            Spacer(modifier = Modifier.padding(4.dp))
//
//            Button(onClick = {
//                if (inputLetter.isNotEmpty()) {
//                    guessLetter(inputLetter[0])
//                    inputLetter = ""
//                }
//            }) {
//                Text("Guess")
//            }
//        }
//    }
//}

/**
 * Composable function that displays content for the "Game" topic.
 * This code will display a Hangman game in the screen.
 * Each time the user change the state of the lifecycle, using the back button, the lives will be updated.
 */
@Composable
fun GameContent() {
    // State variables for the game
    var wordToGuess by remember { mutableStateOf("") }
    var guessedLetters = remember { mutableStateListOf<Char>() }
    var lives by remember { mutableStateOf(6) }
    var gameMessage by remember { mutableStateOf("Guess a letter!") }

    // List of words to choose from
    val wordList = remember {
        listOf(
            "ANDROID",
            "KOTLIN",
            "COMPOSE",
            "DEVELOPER",
            "JETPACK",
            "GOOGLE",
            "MOBILE",
            "STUDIO",
            "APPLICATION",
            "HONGJEONG"
        )
    }

    //Launched effect to select a new word when the view is created.
    LaunchedEffect(Unit) {
        wordToGuess = wordList.random()
    }

    // Get the current lifecycle owner (Activity or Fragment).
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    // DisposableEffect to observe lifecycle events.
    DisposableEffect(lifecycleOwner) {
        // Create a LifecycleEventObserver to listen for lifecycle events.
        val observer = LifecycleEventObserver { _, event ->
            // Check if the lifecycle event is ON_STOP.
            if (event == Lifecycle.Event.ON_STOP) {
                //If the state is stop the value of lives will be updated.
                lives = 6
                guessedLetters.clear()
                gameMessage = "Guess a letter!"
                wordToGuess = wordList.random()
                Log.d("Game", "Observed event: $event, lives: $lives")
            }
            // Log all the events.
            Log.d("Game", "Observed event: $event")
        }
        // Add the observer to the lifecycle.
        lifecycleOwner.lifecycle.addObserver(observer)

        // When the DisposableEffect is disposed, remove the observer to avoid memory leaks.
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Function to handle letter guessing
    fun guessLetter(letter: Char) {
        if (letter !in guessedLetters) {
            guessedLetters.add(letter)
            if (letter !in wordToGuess) {
                lives--
            }
            if (lives == 0) {
                gameMessage =
                    "You lost! The word was $wordToGuess. Click the back button and try again"
            } else if (wordToGuess.all { it in guessedLetters }) {
                gameMessage = "You won! Click the back button and try again"
            } else {
                gameMessage = "Guess a letter!"
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Display the hangman diagram
        HangmanDiagram(lives = lives)

        // Display the word with underscores
        val displayWord = wordToGuess.map { if (it in guessedLetters) it else '_' }
        Text(
            text = displayWord.joinToString(" "),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display lives
        Text(text = "Lives: $lives")

        Spacer(modifier = Modifier.height(16.dp))

        // Display game message
        Text(text = gameMessage, textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(16.dp))

        // Display guessed letters
        Text(text = "Guessed letters: ${guessedLetters.joinToString(", ")}")

        Spacer(modifier = Modifier.height(16.dp))

        // Input for guessing a letter
        var inputLetter by remember { mutableStateOf("") }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            TextField(
                value = inputLetter,
                onValueChange = {
                    if (it.length <= 1) {
                        inputLetter = it.uppercase()
                    }
                },
                label = { Text("Enter a letter") }
            )

            Spacer(modifier = Modifier.padding(4.dp))

            Button(onClick = {
                if (inputLetter.isNotEmpty()) {
                    guessLetter(inputLetter[0])
                    inputLetter = ""
                }
            }) {
                Text("Guess")
            }
        }
    }
}

/**
 * Composable function to display the Hangman diagram.
 * This function displays a Hangman drawing based on the number of lives left.
 *
 * @param lives The number of lives remaining in the game.
 */
@Composable
fun HangmanDiagram(lives: Int) {
    Canvas(modifier = Modifier.size(100.dp)) {
        // Define the drawing parameters
        val canvasWidth = size.width
        val canvasHeight = size.height
        val strokeWidth = 5.dp.toPx()
        val color = Color.Black

        // Draw the base
        drawLine(
            color = color,
            start = Offset(canvasWidth * 0.1f, canvasHeight * 0.9f),
            end = Offset(canvasWidth * 0.6f, canvasHeight * 0.9f),
            strokeWidth = strokeWidth
        )

        // Draw the vertical pole
        drawLine(
            color = color,
            start = Offset(canvasWidth * 0.2f, canvasHeight * 0.9f),
            end = Offset(canvasWidth * 0.2f, canvasHeight * 0.1f),
            strokeWidth = strokeWidth
        )

        // Draw the horizontal beam
        drawLine(
            color = color,
            start = Offset(canvasWidth * 0.2f, canvasHeight * 0.1f),
            end = Offset(canvasWidth * 0.5f, canvasHeight * 0.1f),
            strokeWidth = strokeWidth
        )

        // Draw the rope
        drawLine(
            color = color,
            start = Offset(canvasWidth * 0.5f, canvasHeight * 0.1f),
            end = Offset(canvasWidth * 0.5f, canvasHeight * 0.2f),
            strokeWidth = strokeWidth
        )

        // Draw the head
        if (lives < 6) {
            drawCircle(
                color = color,
                radius = canvasWidth * 0.1f,
                center = Offset(canvasWidth * 0.5f, canvasHeight * 0.3f),
                style = Stroke(strokeWidth)
            )
        }

        // Draw the body
        if (lives < 5) {
            drawLine(
                color = color,
                start = Offset(canvasWidth * 0.5f, canvasHeight * 0.4f),
                end = Offset(canvasWidth * 0.5f, canvasHeight * 0.6f),
                strokeWidth = strokeWidth
            )
        }

        // Draw the left arm
        if (lives < 4) {
            drawLine(
                color = color,
                start = Offset(canvasWidth * 0.5f, canvasHeight * 0.45f),
                end = Offset(canvasWidth * 0.3f, canvasHeight * 0.35f),
                strokeWidth = strokeWidth
            )
        }

        // Draw the right arm
        if (lives < 3) {
            drawLine(
                color = color,
                start = Offset(canvasWidth * 0.5f, canvasHeight * 0.45f),
                end = Offset(canvasWidth * 0.7f, canvasHeight * 0.35f),
                strokeWidth = strokeWidth
            )
        }

        // Draw the left leg
        if (lives < 2) {
            drawLine(
                color = color,
                start = Offset(canvasWidth * 0.5f, canvasHeight * 0.6f),
                end = Offset(canvasWidth * 0.3f, canvasHeight * 0.7f),
                strokeWidth = strokeWidth
            )
        }

        // Draw the right leg
        if (lives < 1) {
            drawLine(
                color = color,
                start = Offset(canvasWidth * 0.5f, canvasHeight * 0.6f),
                end = Offset(canvasWidth * 0.7f, canvasHeight * 0.7f),
                strokeWidth = strokeWidth
            )
        }
    }
}


/**
 * Preview function for the LifecycleApp composable.
 * This composable preview the UI without launching the app in a device.
 */
@Preview(showBackground = true)
@Composable
fun LifecycleAppPreview() {
    ComposeMasterTheme {
        LifecycleApp()
    }
}