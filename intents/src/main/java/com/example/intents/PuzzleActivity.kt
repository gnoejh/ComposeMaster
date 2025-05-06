package com.example.intents

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.intents.ui.theme.ComposeMasterTheme

class PuzzleActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Extract data from the intent
        val action = intent.action
        val data = intent.data
        val extras = intent.extras
        
        // Determine which puzzle was triggered
        val puzzleType = when {
            action == "com.example.intents.START_PUZZLE" -> "Start Puzzle"
            action == "com.example.intents.PUZZLE_ACTION" -> "Action Puzzle"
            action == "com.example.intents.CATEGORY_PUZZLE" -> "Category Puzzle"
            action == "com.example.intents.EXTRAS_PUZZLE" -> "Extras Puzzle"
            action == "com.example.intents.FINAL_CHALLENGE" -> "Final Challenge"
            action == Intent.ACTION_VIEW && data?.scheme == "puzzle" -> "Data Puzzle"
            else -> "Unknown Puzzle"
        }
        
        setContent {
            ComposeMasterTheme {
                val context = LocalContext.current
                
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Intent Puzzle Lab") }
                        )
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Display puzzle information
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = puzzleType,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                
                                Divider()
                                
                                Text(
                                    text = "Intent Action:",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = action ?: "No action specified",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Text(
                                    text = "Intent Data:",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = data?.toString() ?: "No data specified",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                
                                if (extras != null) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    
                                    Text(
                                        text = "Intent Extras:",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    
                                    // Display all extras
                                    extras.keySet()?.forEach { key ->
                                        val value = extras.get(key)
                                        Text(
                                            text = "$key: $value",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                        
                        // Display puzzle instructions
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Puzzle Instructions",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                
                                val instructions = when (puzzleType) {
                                    "Start Puzzle" -> "Welcome to the Intent Relay Puzzle! This is the starting point of the puzzle sequence. To proceed, you'll need to create an intent with the proper action."
                                    "Action Puzzle" -> "This puzzle checks if you can send an intent with a specific action. Make sure your action matches the expected pattern."
                                    "Data Puzzle" -> "For this puzzle, you need to provide a URI with the correct structure in your intent's data field."
                                    "Category Puzzle" -> "Categories help filter intents. For this puzzle, add the right category to your intent."
                                    "Extras Puzzle" -> "Intent extras are key-value pairs that carry additional data. This puzzle requires you to include specific extras with the right values."
                                    "Final Challenge" -> "This is the final challenge! You need to chain multiple intents together and handle broadcast receivers to complete the puzzle."
                                    else -> "Unknown puzzle type. Check your intent configuration."
                                }
                                
                                Text(
                                    text = instructions,
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Justify
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Button(onClick = { finish() }) {
                            Text("Return to Main Menu")
                        }
                    }
                }
            }
        }
    }
}
