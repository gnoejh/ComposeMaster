package com.example.mvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mvvm.ui.theme.ComposeMasterTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeMasterTheme {
                CounterApp()
            }
        }
    }
}

@Composable
fun CounterApp(viewModel: MainViewModel = viewModel()) {
    val count by viewModel.count

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Count: $count")
        Spacer(modifier = Modifier.padding(16.dp))
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = viewModel::increment) {
                Text("Increment")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = viewModel::decrement) {
                Text("Decrement")
            }
        }
        Spacer(modifier = Modifier.padding(16.dp))
        Button(onClick = viewModel::reset) {
            Text("Reset")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CounterPreview() {
    ComposeMasterTheme {
        CounterApp()
    }
}