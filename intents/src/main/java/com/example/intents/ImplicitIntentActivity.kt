package com.example.intents

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.intents.ui.theme.ComposeMasterTheme

class ImplicitIntentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val action = intent.action ?: "Unknown Action"
        val data = intent.dataString ?: "No Data"
        val type = intent.type ?: "No Type"
        
        setContent {
            ComposeMasterTheme {
                ImplicitIntentScreen(action, data, type)
            }
        }
    }
}

@Composable
fun ImplicitIntentScreen(action: String, data: String, type: String) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Implicit Intent Received\n\n" +
                      "Action: $action\n" +
                      "Data: $data\n" +
                      "Type: $type",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
