package com.example.sideeffects

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.sideeffects.ui.theme.ComposeMasterTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enable edge-to-edge display
        setContent {
            ComposeMasterTheme { // Apply custom theme
                SideEffectDemo() // Call the main composable function
            }
        }
    }
}

/**
 * Enum class to represent different side effect topics.
 */
enum class SideEffectTopic(val label: String) {
    LaunchedEffect("LaunchedEffect"),
    RememberUpdatedState("rememberUpdatedState"),
    DisposableEffect("DisposableEffect"),
    SideEffect("SideEffect"),
    RememberCoroutineScope("rememberCoroutineScope")
}

/**
 * Main composable function that demonstrates different side effect APIs.
 */
@OptIn(ExperimentalMaterial3Api::class) // Indicate the usage of experimental Material 3 APIs
@Composable
fun SideEffectDemo() {
    // State variable to track the selected side effect topic.
    var selectedTopic by remember { mutableStateOf(SideEffectTopic.LaunchedEffect) }
    // State variable to track the expanded/collapsed state of the dropdown menu.
    var expanded by remember { mutableStateOf(false) }

    Scaffold { innerPadding -> // Material 3 Scaffold
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Apply padding from Scaffold
                .padding(16.dp), // Add additional padding
            horizontalAlignment = Alignment.CenterHorizontally, // Center items horizontally
            verticalArrangement = Arrangement.spacedBy(16.dp) // Add space between items vertically
        ) {
            ExposedDropdownMenuBox( // Material 3 exposed dropdown menu
                expanded = expanded, // Control expansion state
                onExpandedChange = { expanded = !expanded } // Toggle expansion state on click
            ) {
                TextField( // Text field to display the selected topic
                    value = selectedTopic.label, // Display the selected topic's label
                    onValueChange = {}, // Do nothing on text change (read-only)
                    readOnly = true, // Make the text field read-only
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },// Show trailing icon.
                    modifier = Modifier.menuAnchor() // Modifier to make the TextField a menu anchor
                )
                DropdownMenu( // The dropdown menu
                    expanded = expanded, // Control the visibility of the dropdown
                    onDismissRequest = { expanded = false }, // Close the dropdown on dismiss
                    modifier = Modifier.exposedDropdownSize() // Modifier to control the size of the dropdown
                ) {
                    SideEffectTopic.entries.forEach { topic -> // Iterate through the enum entries
                        DropdownMenuItem( // Each item in the dropdown menu
                            text = { Text(topic.label) }, // Display the label of the topic
                            onClick = {
                                selectedTopic = topic // Update selected topic
                                expanded = false // Close the dropdown
                            }
                        )
                    }
                }
            }

            // Display the selected side effect example
            when (selectedTopic) { // Switch on the selected topic
                SideEffectTopic.LaunchedEffect -> LaunchedEffectExample()
                SideEffectTopic.RememberUpdatedState -> RememberUpdatedStateExample()
                SideEffectTopic.DisposableEffect -> DisposableEffectExample()
                SideEffectTopic.SideEffect -> SideEffectExample()
                SideEffectTopic.RememberCoroutineScope -> RememberCoroutineScopeExample()
            }
        }
    }
}

/**
 * Example of using LaunchedEffect.
 */
@Composable
fun LaunchedEffectExample() {
    val context = LocalContext.current // Get current context
    /**
     * LaunchedEffect:
     * - **Purpose**: LaunchedEffect is used to run coroutines that need to be executed when the composable
     *   is launched (enters the composition) and during its lifecycle.
     * - **Key Use Cases**:
     *   - Performing asynchronous tasks such as network requests.
     *   - Starting animations or timers.
     *   - Triggering actions based on state changes.
     * - **Key(s)**:
     *   - LaunchedEffect takes one or more keys. If any of these keys change, the coroutine is canceled
     *     and restarted with the new key value.
     *   - If no keys are provided or if `true` is provided as a key, the effect runs once when the
     *     composable enters the composition and is not restarted.
     * - **Lifecycle**:
     *   - The coroutine starts when the composable enters the composition.
     *   - The coroutine is canceled when the composable leaves the composition.
     *   - If any of the keys change, the current coroutine is canceled and a new one is launched.
     */
    LaunchedEffect(key1 = true) { // LaunchedEffect block
        delay(5000) // Delay for 3 seconds
        Toast.makeText(context, "LaunchedEffect: Toast after 3 seconds", Toast.LENGTH_SHORT).show() // Show a Toast
    }
    Text("LaunchedEffect: This composable will display a Toast after 5 seconds.") // Inform the user
}

/**
 * Example of using rememberUpdatedState.
 */
@Composable
fun RememberUpdatedStateExample() {
    var message by remember { mutableStateOf("Initial message") } // Mutable state for a message
    /**
     * rememberUpdatedState:
     * - **Purpose**: Remember the most recently provided value. Useful for capturing a value in a
     *   LaunchedEffect or callback that should always be the latest version of the value.
     * - **Key Use Cases**:
     *   - Capturing the latest state or lambda value to use in a coroutine or a callback.
     *   - Handling state updates in long-running operations without canceling and restarting the
     *     operation when the state changes.
     * - **Behavior**:
     *   - Returns a State object that always holds the latest value.
     *   - The value is updated when the composable recomposes.
     *   - It does not cause the effect to restart, it only updates the captured state.
     */
    val updatedMessage by rememberUpdatedState(newValue = message) // Remember the updated state of the message

    LaunchedEffect(key1 = true) { // LaunchedEffect block
        delay(2000) // Delay for 2 seconds
        Log.d("RememberUpdatedState", "Message after 2 seconds: $updatedMessage") // Log the updated message
    }

    Button(onClick = { message = "New message at ${System.currentTimeMillis()}" }) { // Button to update the message
        Text("Change Message")
    }

    Text("rememberUpdatedState: Check the log after 2 seconds. The updated message should be logged.") // Inform the user
    Text("Current Message : $message")
}

/**
 * Example of using DisposableEffect.
 */
@Composable
fun DisposableEffectExample() {
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current // Get current lifecycle owner
    var lifecycleEvent by remember { mutableStateOf(Lifecycle.Event.ON_CREATE) } // Mutable state for lifecycle event
    /**
     * DisposableEffect:
     * - **Purpose**: DisposableEffect is used for side effects that need to be cleaned up or disposed
     *   of when the composable leaves the composition or when the keys change.
     * - **Key Use Cases**:
     *   - Managing resources like listeners or subscriptions.
     *   - Registering and unregistering callbacks.
     *   - Setting up and tearing down any external resource that needs handling.
     * - **Key(s)**:
     *   - Similar to LaunchedEffect, it takes keys. When the keys change, the `onDispose` block is
     *     executed before the effect is re-run.
     * - **Lifecycle**:
     *   - The effect starts when the composable enters the composition.
     *   - The `onDispose` block is executed when the composable leaves the composition or when the keys change.
     * - **onDispose**:
     *   - Provides a cleanup block. This block is called when the effect is disposed.
     */
    DisposableEffect(key1 = lifecycleOwner) { // DisposableEffect block
        val observer = LifecycleEventObserver { _, event -> // Create a lifecycle observer
            lifecycleEvent = event // Update the lifecycle event state
            Log.d("DisposableEffect", "Lifecycle Event: $event") // Log the lifecycle event
        }
        lifecycleOwner.lifecycle.addObserver(observer) // Add the observer to the lifecycle
        onDispose { // On dispose block
            lifecycleOwner.lifecycle.removeObserver(observer) // Remove the observer
            Log.d("DisposableEffect", "DisposableEffect removed the observer") // Log that the observer is removed
        }
    }

    Text("DisposableEffect: Check the log for lifecycle events.") // Inform the user
    Text("Current Event : $lifecycleEvent")
}

/**
 * Example of using SideEffect.
 */
@Composable
fun SideEffectExample() {
    var recompositionCount by remember { mutableIntStateOf(0) } // Mutable state for recomposition count
    /**
     * SideEffect:
     * - **Purpose**: SideEffect is used to run code that should execute after every successful recomposition.
     *   It's used to share state with non-compose code.
     * - **Key Use Cases**:
     *   - Sending analytics or events based on the current composition state.
     *   - Updating the state of non-compose objects or variables.
     *   - Triggering actions that must happen after composition.
     * - **Lifecycle**:
     *   - The effect runs after every successful recomposition.
     *   - It will not run if the composable is skipped due to optimization.
     */
    SideEffect { // SideEffect block
        recompositionCount++ // Increment the count on each recomposition
        Log.d("SideEffect", "Recomposition count: $recompositionCount") // Log the recomposition count
    }

    Text("SideEffect: This composable recomposes. Check log for recomposition count") // Inform the user
    Text("Recomposition Count : $recompositionCount")
}

/**
 * Example of using rememberCoroutineScope.
 */
@Composable
fun RememberCoroutineScopeExample() {
    /**
     * rememberCoroutineScope:
     * - **Purpose**: Creates a CoroutineScope that is tied to the composition. Used to launch
     *   coroutines outside of the context of other side-effect APIs.
     * - **Key Use Cases**:
     *   - Launching coroutines in response to UI events or actions (e.g., button clicks).
     *   - Performing asynchronous operations that don't necessarily depend on recomposition.
     * - **Lifecycle**:
     *   - The scope is canceled when the composable leaves the composition.
     *   - Any coroutines launched within this scope are also canceled.
     * - **Usage**:
     *   - `rememberCoroutineScope` returns a `CoroutineScope` instance.
     *   - You use the `launch` function on this scope to start new coroutines.
     */
    val scope = rememberCoroutineScope() // Get a coroutine scope tied to the composable
    val context = LocalContext.current // Get current context

    Button(onClick = { // Button to launch a coroutine
        scope.launch { // Launch a coroutine in the composable's scope
            delay(3000) // Delay for 1 second
            Toast.makeText(context, "Coroutine launched!", Toast.LENGTH_SHORT).show() // Show a Toast
        }
    }) {
        Text("Launch Coroutine")
    }
    Text("rememberCoroutineScope: Click button to launch a coroutine.") // Inform the user
}

/**
 * Preview function for SideEffectDemo.
 */
@Preview(showBackground = true)
@Composable
fun SideEffectDemoPreview() {
    ComposeMasterTheme {
        SideEffectDemo() // Call the main composable
    }
}
