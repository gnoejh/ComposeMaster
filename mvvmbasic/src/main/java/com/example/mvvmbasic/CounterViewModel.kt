package com.example.mvvmbasic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * ViewModel for counter operations without using Hilt
 */
class CounterViewModel(private val repository: CounterRepository) : ViewModel() {
    
    private val _counterState = MutableStateFlow(repository.getCount())
    val counterState: StateFlow<Int> = _counterState
    
    fun increment() {
        repository.increment()
        _counterState.value = repository.getCount()
    }
    
    fun decrement() {
        repository.decrement()
        _counterState.value = repository.getCount()
    }
    
    fun reset() {
        repository.reset()
        _counterState.value = repository.getCount()
    }
    
    /**
     * Factory to create CounterViewModel with dependencies
     */
    class Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CounterViewModel::class.java)) {
                // Manual dependency injection
                val repository = CounterRepository()
                @Suppress("UNCHECKED_CAST")
                return CounterViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
