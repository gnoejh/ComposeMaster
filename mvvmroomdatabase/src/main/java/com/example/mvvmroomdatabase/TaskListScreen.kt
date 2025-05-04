package com.example.mvvmroomdatabase

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun TaskListScreen(viewModel: TaskViewModel = hiltViewModel()) {
    val tasks by viewModel.allTasks.collectAsState(initial = emptyList())
    Column {
        Button(
            modifier = Modifier.padding(10.dp),
            onClick = {
                viewModel.insertTask(Task(name = "New Task", description = "New task description"))
            }) {
            Text(text = "Add Task")
        }
        LazyColumn {
            items(tasks) { task ->
                Text(text = task.name)
            }
        }
    }
}