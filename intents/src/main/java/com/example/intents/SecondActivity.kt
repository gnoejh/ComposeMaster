package com.example.intents

import android.content.Context
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

class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Get extras from intent
        val message = intent.getStringExtra(EXTRA_MESSAGE) ?: "No message"
        
        setContent {
            ComposeMasterTheme {
                SecondActivityScreen(message)
            }
        }
    }
    
    companion object {
        private const val EXTRA_MESSAGE = "extra_message"
        
        fun createIntent(context: Context, message: String): Intent {
            return Intent(context, SecondActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, message)
            }
        }
    }
}

@Composable
fun SecondActivityScreen(message: String) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Explicit Intent Received\n\nMessage: $message",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}
