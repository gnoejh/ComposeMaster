package com.example.composelayouts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composelayouts.ui.theme.ComposeMasterTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

/**
 * Main Activity class that serves as the entry point of the application.
 * Demonstrates basic Compose concepts including layouts, state, and Material Design components.
 */
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enable edge-to-edge display
        enableEdgeToEdge()

        // Set the content of the activity using Compose
        setContent {
            // Apply the app theme to all composables
            ComposeMasterTheme {
                // Scaffold provides the basic material design layout structure
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    // TopAppBar demonstrates the use of Material3 components
                    topBar = {
                        TopAppBar(
                            title = { Text("Compose Layout Demo") },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }
                ) { padding ->
                    // Pass the padding from Scaffold to maintain proper layout
                    MainContent(modifier = Modifier.padding(padding))
                }
            }
        }
    }
}

/**
 * Main content composable that demonstrates various Compose layout concepts.
 *
 * @param modifier Modifier to be applied to the content
 */
@Composable
fun MainContent(modifier: Modifier = Modifier) {
    // State management example using remember and mutableStateOf
    var count by remember { mutableStateOf(0) }


    // Column is a vertical layout container
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        // Demonstrates spacing between items
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Text with multiple modifiers
        Text(
            text = "Welcome to Compose",
            // Demonstrates Material3 typography system
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                // Demonstrates background color from Material theme
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(8.dp),
            textAlign = TextAlign.Center
        )

        // Row demonstrates horizontal layout
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Box demonstrates overlay layout with click interaction
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    // Demonstrates click handling
                    .clickable { count++ },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Box 1",
                    color = Color.White
                )
            }

            // Second Box for comparison
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(MaterialTheme.colorScheme.secondary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Box 2",
                    color = Color.White
                )
            }
        }

        // Demonstrates state observation
        Text(
            text = "Count: $count",
            style = MaterialTheme.typography.headlineMedium
        )

        // Button demonstrates component with state interaction
        Button(
            onClick = { count = 0 },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reset Counter")
        }

        // Spacer demonstrates layout spacing
        Spacer(modifier = Modifier.height(16.dp))

        // Business Card Composable
        BusinessCard()

        Spacer(modifier = Modifier.height(16.dp))

        // Card demonstrates Material design container
        Card(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Profile Card",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Click the blue box to increase the counter",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

// Business Card Composable
@Composable
fun BusinessCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        // Fix the CardDefaults reference
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Image
            Image(
                painter = painterResource(id = R.drawable.photo),
                contentDescription = "Profile Photo",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Name
            Text(
                text = stringResource(id = R.string.name),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ID
            Text(
                text = stringResource(id = R.string.id),
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Additional Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                InfoItem(
                    icon = Icons.Default.Email,
                    text = stringResource(id = R.string.email)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                InfoItem(
                    icon = Icons.Default.Phone,
                    text = stringResource(id = R.string.phone)
                )
            }
        }
    }
}
@Composable
private fun InfoItem(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text)
    }
}

/**
 * Preview function for the MainContent composable.
 * Allows viewing the layout in Android Studio's preview pane.
 */
@Preview(showBackground = true)
@Composable
fun MainContentPreview() {
    ComposeMasterTheme {
        MainContent()
    }
}
