package com.example.mvvmbasic

/**
 * Repository class that manages counter data
 */
class CounterRepository {
    private var counter = 0
    
    fun getCount(): Int = counter
    
    fun increment() {
        counter++
    }
    
    fun decrement() {
        if (counter > 0) counter--
    }
    
    fun reset() {
        counter = 0
    }
}
