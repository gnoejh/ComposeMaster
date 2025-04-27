package com.example.mvvm

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel


class MainViewModel  : ViewModel() {
    // Counter state
    var count = mutableStateOf(0)
        private set

    // Increment the counter
    fun increment() {
        count.value++
    }

    // Decrement the counter
    fun decrement() {
        count.value--
    }

    // Reset the counter
    fun reset() {
        count.value = 0
    }
}