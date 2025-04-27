package com.example.mvvmbasic

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CounterScreen(
    viewModel: CounterViewModel = viewModel(factory = CounterViewModel.Factory())
) {
    val counterState by viewModel.counterState.collectAsState(0)

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "MVVM Counter Example",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Counter Value: $counterState",
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Button(onClick = { viewModel.decrement() }) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Decrement")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Decrement")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(onClick = { viewModel.increment() }) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Increment")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Increment")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.reset() }) {
            Icon(Icons.Default.Delete, contentDescription = "Reset")
            Text("Reset")
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "This is a MVVM implementation without Hilt dependency injection",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
