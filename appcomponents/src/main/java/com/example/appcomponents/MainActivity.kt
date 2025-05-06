package com.example.appcomponents

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appcomponents.ui.theme.ComposeMasterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeMasterTheme {
                AppComponentsApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppComponentsApp() {
    var expanded by remember { mutableStateOf(false) }
    var selectedComponent by remember { mutableStateOf("Activity") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = selectedComponent) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Select Component"
                        )
                    }
                    
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Activity") },
                            onClick = { 
                                selectedComponent = "Activity"
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Service") },
                            onClick = { 
                                selectedComponent = "Service"
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Broadcast Receiver") },
                            onClick = { 
                                selectedComponent = "Broadcast Receiver"
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Content Provider") },
                            onClick = { 
                                selectedComponent = "Content Provider"
                                expanded = false
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
            when (selectedComponent) {
                "Activity" -> ActivityContent()
                "Service" -> ServiceContent()
                "Broadcast Receiver" -> BroadcastReceiverContent()
                "Content Provider" -> ContentProviderContent()
            }
        }
    }
}

@Composable
fun ActivityContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Activity",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        InfoSection(
            title = "What is an Activity?",
            content = "An activity is a single, focused thing that the user can do. Almost all activities interact with the user, so the Activity class takes care of creating a window for you in which you can place your UI."
        )
        
        InfoSection(
            title = "Activity Lifecycle",
            content = "Activities have a defined lifecycle with methods like onCreate(), onStart(), onResume(), onPause(), onStop(), and onDestroy() that you can override to handle transitions between states."
        )
        
        InfoSection(
            title = "Common Use Cases",
            content = "• User interface screens\n• Form inputs\n• Displaying data\n• Navigation between app sections\n• Starting other activities"
        )
        
        InfoSection(
            title = "Example",
            content = "class MainActivity : AppCompatActivity() {\n" +
                    "    override fun onCreate(savedInstanceState: Bundle?) {\n" +
                    "        super.onCreate(savedInstanceState)\n" +
                    "        setContentView(R.layout.activity_main)\n" +
                    "    }\n" +
                    "}"
        )
    }
}

@Composable
fun ServiceContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Service",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        InfoSection(
            title = "What is a Service?",
            content = "A service is a component that runs in the background to perform long-running operations or to perform work for remote processes. A service does not provide a user interface."
        )
        
        InfoSection(
            title = "Service Types",
            content = "• Started Services: Initiated by startService() and run until stopSelf() or stopService() is called\n" +
                    "• Bound Services: Initiated by bindService() and run as long as components are bound to it\n" +
                    "• Foreground Services: Visible to the user via a notification that cannot be dismissed"
        )
        
        InfoSection(
            title = "Common Use Cases",
            content = "• Playing music in the background\n• Downloading files\n• Performing network operations\n• Processing data\n• Scheduled tasks"
        )
        
        InfoSection(
            title = "Example",
            content = "class MyService : Service() {\n" +
                    "    override fun onBind(intent: Intent): IBinder? {\n" +
                    "        return null\n" +
                    "    }\n\n" +
                    "    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {\n" +
                    "        // Service work here\n" +
                    "        return START_STICKY\n" +
                    "    }\n" +
                    "}"
        )
    }
}

@Composable
fun BroadcastReceiverContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Broadcast Receiver",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        InfoSection(
            title = "What is a Broadcast Receiver?",
            content = "A broadcast receiver is a component that enables the system to deliver events to the app outside of a regular user flow, allowing the app to respond to system-wide broadcast announcements."
        )
        
        InfoSection(
            title = "Registration Types",
            content = "• Static Registration: Declared in the AndroidManifest.xml file\n" +
                    "• Dynamic Registration: Registered and unregistered programmatically using registerReceiver() and unregisterReceiver()"
        )
        
        InfoSection(
            title = "Common Use Cases",
            content = "• Responding to system events (boot completed, battery low)\n• Network connectivity changes\n• Device state changes\n• Custom app-to-app communications\n• Scheduled alarms"
        )
        
        InfoSection(
            title = "Example",
            content = "class MyReceiver : BroadcastReceiver() {\n" +
                    "    override fun onReceive(context: Context, intent: Intent) {\n" +
                    "        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {\n" +
                    "            // Handle boot completed\n" +
                    "        }\n" +
                    "    }\n" +
                    "}"
        )
    }
}

@Composable
fun ContentProviderContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Content Provider",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        InfoSection(
            title = "What is a Content Provider?",
            content = "A content provider manages a shared set of app data that you can store in the file system, in a SQLite database, on the web, or on any other persistent storage location that your app can access."
        )
        
        InfoSection(
            title = "Key Methods",
            content = "• query(): Retrieve data\n• insert(): Insert new data\n• update(): Update existing data\n• delete(): Remove data\n• getType(): Return the MIME type of data"
        )
        
        InfoSection(
            title = "Common Use Cases",
            content = "• Sharing data between applications\n• Accessing system data (contacts, calendar)\n• Abstracting data sources\n• Providing search suggestions\n• Sync adapter implementations"
        )
        
        InfoSection(
            title = "Example",
            content = "class MyContentProvider : ContentProvider() {\n" +
                    "    override fun query(uri: Uri, projection: Array<String>?, selection: String?, " +
                    "selectionArgs: Array<String>?, sortOrder: String?): Cursor? {\n" +
                    "        // Query implementation\n" +
                    "        return null\n" +
                    "    }\n" +
                    "    \n" +
                    "    // Other required overrides...\n" +
                    "}"
        )
    }
}

@Composable
fun InfoSection(title: String, content: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppComponentsPreview() {
    ComposeMasterTheme {
        AppComponentsApp()
    }
}
