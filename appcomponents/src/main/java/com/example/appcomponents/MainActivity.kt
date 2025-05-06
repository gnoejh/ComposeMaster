package com.example.appcomponents

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.appcomponents.ui.theme.ComposeMasterTheme

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(this, "Contacts permission granted", Toast.LENGTH_SHORT).show()
            queryContacts()
        } else {
            Toast.makeText(this, "Contacts permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun queryContacts() {
        try {
            val contentUri = android.provider.ContactsContract.Contacts.CONTENT_URI
            val cursor = contentResolver.query(
                contentUri,
                null,
                null,
                null,
                null
            )
            
            val contactCount = cursor?.count ?: 0
            cursor?.close()
            
            Toast.makeText(
                this,
                "Found $contactCount contacts in your device",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: SecurityException) {
            Toast.makeText(
                this,
                "Permission denied. Please grant contacts permission.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun checkContactPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED -> {
                queryContacts()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                Toast.makeText(
                    this,
                    "Contact permission is needed to access your contacts",
                    Toast.LENGTH_LONG
                ).show()
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeMasterTheme {
                AppComponentsApp(this)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppComponentsApp(activity: MainActivity? = null) {
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
                "Content Provider" -> ContentProviderContent(activity)
            }
        }
    }
}

@Composable
fun ActivityContent() {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
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
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = {
                val intent = Intent(context, SecondActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Launch Second Activity")
        }
    }
}

@Composable
fun ServiceContent() {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var serviceRunning by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
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
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = {
                if (!serviceRunning) {
                    val intent = Intent(context, ExampleService::class.java)
                    context.startService(intent)
                    Toast.makeText(context, "Service started", Toast.LENGTH_SHORT).show()
                    serviceRunning = true
                } else {
                    val intent = Intent(context, ExampleService::class.java)
                    context.stopService(intent)
                    Toast.makeText(context, "Service stopped", Toast.LENGTH_SHORT).show()
                    serviceRunning = false
                }
            },
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text(if (serviceRunning) "Stop Service" else "Start Service")
        }
    }
}

@Composable
fun BroadcastReceiverContent() {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var receiverRegistered by remember { mutableStateOf(false) }
    
    val CUSTOM_ACTION = "com.example.components.CUSTOM_BROADCAST"
    
    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Toast.makeText(
                context, 
                "Broadcast received: ${intent?.action ?: "unknown"}", 
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    
    DisposableEffect(receiverRegistered) {
        if (receiverRegistered) {
            val filter = IntentFilter(CUSTOM_ACTION)
            ContextCompat.registerReceiver(
                context,
                receiver,
                filter,
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
        }
        
        onDispose {
            if (receiverRegistered) {
                try {
                    context.unregisterReceiver(receiver)
                } catch (e: Exception) {
                }
            }
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
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
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = {
                receiverRegistered = !receiverRegistered
                if (receiverRegistered) {
                    val filter = IntentFilter(CUSTOM_ACTION)
                    ContextCompat.registerReceiver(
                        context,
                        receiver,
                        filter,
                        ContextCompat.RECEIVER_NOT_EXPORTED
                    )
                    Toast.makeText(context, "Receiver registered", Toast.LENGTH_SHORT).show()
                } else {
                    try {
                        context.unregisterReceiver(receiver)
                        Toast.makeText(context, "Receiver unregistered", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text(if (receiverRegistered) "Unregister Receiver" else "Register Receiver")
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Button(
            onClick = {
                val intent = Intent(CUSTOM_ACTION)
                context.sendBroadcast(intent)
                Toast.makeText(context, "Broadcast sent", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth(0.8f),
            enabled = receiverRegistered
        ) {
            Text("Send Broadcast")
        }
    }
}

@Composable
fun ContentProviderContent(activity: MainActivity? = null) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
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
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = {
                if (activity != null) {
                    activity.checkContactPermission()
                } else {
                    Toast.makeText(
                        context,
                        "Cannot access contacts in this context",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Query Contacts")
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Note: This example requires READ_CONTACTS permission",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.8f)
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
