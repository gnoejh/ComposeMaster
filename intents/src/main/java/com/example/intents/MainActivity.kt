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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    IntentTestingScreen(modifier = Modifier.padding(innerPadding))
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
fun IntentTestingScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var selectedIntentOption by remember { mutableStateOf<IntentOption?>(null) }
    
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        // Handle the returned bitmap if needed
        if (bitmap != null) {
            Toast.makeText(context, "Picture taken successfully", Toast.LENGTH_SHORT).show()
        }
    }
    
    val intentOptions = remember {
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
                        putExtra(Intent.EXTRA_EMAIL, arrayOf("example@example.com"))
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
            )
        )
    }
    
    val categories = intentOptions.map { it.category }.distinct()
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Intent Testing App",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Box {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    value = selectedIntentOption?.name ?: "Select Intent to Test",
                    onValueChange = {},
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Dropdown menu"
                        )
                    }
                )
                
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
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
                                        Column {
                                            Text(text = option.name)
                                            Text(
                                                text = option.description,
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    },
                                    onClick = {
                                        selectedIntentOption = option
                                        expanded = false
                                    }
                                )
                            }
                        
                        Divider()
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
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
        IntentTestingScreen()
    }
}
