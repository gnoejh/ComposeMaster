package com.example.intents

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.AlarmClock
import android.provider.ContactsContract
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.intents.ui.theme.ComposeMasterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeMasterTheme {
                val context = LocalContext.current
                var expanded by remember { mutableStateOf(false) }
                var selectedIntentOption by remember { mutableStateOf<IntentOption?>(null) }
                
                val intentOptions = rememberIntentOptions(context)
                
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        IntentSelectionTopBar(
                            expanded = expanded,
                            onExpandedChange = { expanded = it },
                            selectedIntentOption = selectedIntentOption,
                            onIntentSelected = { selectedIntentOption = it },
                            intentOptions = intentOptions
                        )
                    }
                ) { innerPadding ->
                    IntentTestingScreen(
                        modifier = Modifier.padding(innerPadding),
                        selectedIntentOption = selectedIntentOption,
                        onExecuteIntent = {
                            selectedIntentOption?.let {
                                try {
                                    it.handler(context)
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        context,
                                        "Error: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } ?: run {
                                Toast.makeText(
                                    context,
                                    "Please select an intent option first",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    )
                }
            }
        }
    }
}

data class IntentOption(
    val name: String,
    val description: String,
    val category: String,
    val handler: (context: android.content.Context) -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntentSelectionTopBar(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    selectedIntentOption: IntentOption?,
    onIntentSelected: (IntentOption) -> Unit,
    intentOptions: List<IntentOption>
) {
    val categories = intentOptions.map { it.category }.distinct()
    
    TopAppBar(
        title = { Text("Intent Testing App") },
        actions = {
            Box {
                IconButton(
                    onClick = { onExpandedChange(!expanded) }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Intent menu"
                    )
                }
                
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { onExpandedChange(false) },
                    modifier = Modifier.width(320.dp)
                ) {
                    categories.forEach { category ->
                        // Add category header
                        DropdownMenuItem(
                            text = { 
                                Text(
                                    text = category,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            },
                            onClick = {},
                            enabled = false
                        )
                        
                        // Add intents in this category
                        intentOptions
                            .filter { it.category == category }
                            .forEach { option ->
                                DropdownMenuItem(
                                    text = { 
                                        Column(modifier = Modifier.fillMaxWidth()) {
                                            Text(text = option.name)
                                            Text(
                                                text = option.description,
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    },
                                    onClick = {
                                        onIntentSelected(option)
                                        onExpandedChange(false)
                                    }
                                )
                            }
                        
                        Divider()
                    }
                }
            }
        }
    )
}

@Composable
fun rememberIntentOptions(context: android.content.Context): List<IntentOption> {
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            Toast.makeText(context, "Picture taken successfully", Toast.LENGTH_SHORT).show()
        }
    }
    
    return remember {
        listOf(
            // Explicit Intents
            IntentOption(
                "Launch Second Activity",
                "Launches SecondActivity with explicit intent",
                "Explicit",
                { ctx -> ctx.startActivity(SecondActivity.createIntent(ctx, "Hello from MainActivity!")) }
            ),
            
            // Implicit Intents - Outgoing
            IntentOption(
                "Open URL",
                "Opens a URL in browser",
                "Implicit - Outgoing",
                { ctx ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://developer.android.com"))
                    ctx.startActivity(intent)
                }
            ),
            IntentOption(
                "Dial Number",
                "Opens dialer with number",
                "Implicit - Outgoing",
                { ctx ->
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:1234567890"))
                    ctx.startActivity(intent)
                }
            ),
            IntentOption(
                "Send Email",
                "Compose an email",
                "Implicit - Outgoing",
                { ctx ->
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_EMAIL, arrayOf("h.jeong@inha.uz"))
                        putExtra(Intent.EXTRA_SUBJECT, "Test Subject")
                        putExtra(Intent.EXTRA_TEXT, "This is a test email")
                    }
                    ctx.startActivity(Intent.createChooser(intent, "Send Email"))
                }
            ),
            IntentOption(
                "Share Text",
                "Share text with other apps",
                "Implicit - Outgoing",
                { ctx ->
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "Check out this cool Intent testing app!")
                    }
                    ctx.startActivity(Intent.createChooser(intent, "Share via"))
                }
            ),
            IntentOption(
                "Take Picture",
                "Open camera to take picture",
                "Implicit - Outgoing",
                { _ -> takePictureLauncher.launch(null) }
            ),
            IntentOption(
                "View Contacts",
                "Open contacts app",
                "Implicit - Outgoing",
                { ctx ->
                    val intent = Intent(Intent.ACTION_VIEW, ContactsContract.Contacts.CONTENT_URI)
                    ctx.startActivity(intent)
                }
            ),
            IntentOption(
                "Set Alarm",
                "Set an alarm for 10 minutes",
                "Implicit - Outgoing",
                { ctx ->
                    val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                        putExtra(AlarmClock.EXTRA_MESSAGE, "Test Alarm")
                        putExtra(AlarmClock.EXTRA_HOUR, 10)
                        putExtra(AlarmClock.EXTRA_MINUTES, 30)
                    }
                    ctx.startActivity(intent)
                }
            ),
            
            // Implicit Intents - Our custom handlers
            IntentOption(
                "Send Custom Action",
                "Send a custom action to our ImplicitIntentActivity",
                "Implicit - Custom",
                { ctx ->
                    val intent = Intent("com.example.intents.CUSTOM_ACTION").apply {
                        putExtra("extra_data", "Some test data")
                    }
                    ctx.startActivity(intent)
                }
            ),
            IntentOption(
                "View Custom Content",
                "View custom content with our handler",
                "Implicit - Custom",
                { ctx ->
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("intentsample://item/123")
                    }
                    ctx.startActivity(intent)
                }
            ),
            
            // Intent Relay Puzzle Game Lab
            IntentOption(
                "Start Puzzle Game",
                "Begin the intent relay puzzle sequence",
                "Lab",
                { ctx ->
                    val intent = Intent("com.example.intents.START_PUZZLE").apply {
                        putExtra("puzzle_level", 1)
                    }
                    ctx.startActivity(intent)
                }
            ),
            IntentOption(
                "Solve Action Puzzle",
                "Send an intent with the correct action to proceed",
                "Lab",
                { ctx ->
                    val intent = Intent("com.example.intents.PUZZLE_ACTION").apply {
                        putExtra("solution_key", "action_solution")
                    }
                    ctx.startActivity(intent)
                }
            ),
            IntentOption(
                "Solve Data Puzzle",
                "Send an intent with correct data URI format",
                "Lab",
                { ctx ->
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("puzzle://solve/data/123")
                    }
                    ctx.startActivity(intent)
                }
            ),
            IntentOption(
                "Solve Category Puzzle",
                "Send an intent with the correct category to match a filter",
                "Lab",
                { ctx ->
                    val intent = Intent("com.example.intents.CATEGORY_PUZZLE").apply {
                        addCategory("com.example.intents.category.PUZZLE_PIECE")
                        putExtra("piece_id", "center_piece")
                    }
                    ctx.startActivity(intent)
                }
            ),
            IntentOption(
                "Solve Extras Puzzle",
                "Send an intent with the required extra data",
                "Lab",
                { ctx ->
                    val intent = Intent("com.example.intents.EXTRAS_PUZZLE").apply {
                        putExtra("key1", "unlock")
                        putExtra("key2", 42)
                        putExtra("key3", true)
                    }
                    ctx.startActivity(intent)
                }
            ),
            IntentOption(
                "Final Challenge",
                "Chain multiple intents with broadcast receivers to complete the puzzle",
                "Lab",
                { ctx ->
                    val intent = Intent("com.example.intents.FINAL_CHALLENGE").apply {
                        putExtra("challenge_mode", "advanced")
                        putExtra("time_limit", 120) // 2 minutes
                        data = Uri.parse("puzzle://final/challenge")
                    }
                    ctx.startActivity(intent)
                }
            )
        )
    }
}

@Composable
fun IntentTestingScreen(
    modifier: Modifier = Modifier, 
    selectedIntentOption: IntentOption?,
    onExecuteIntent: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = onExecuteIntent
        ) {
            Text(text = "Execute Intent")
        }
        
        selectedIntentOption?.let {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = it.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Category: ${it.category}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeMasterTheme {
        IntentTestingScreen(
            selectedIntentOption = null,
            onExecuteIntent = {}
        )
    }
}

