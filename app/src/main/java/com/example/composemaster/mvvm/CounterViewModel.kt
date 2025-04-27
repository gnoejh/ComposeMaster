package com.example.composemaster.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * ViewModel for counter operations without using Hilt
 */
class CounterViewModel(private val repository: CounterRepository) : ViewModel() {
    
    private val _counterState = MutableLiveData(repository.getCount())
    val counterState: LiveData<Int> = _counterState
    
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
