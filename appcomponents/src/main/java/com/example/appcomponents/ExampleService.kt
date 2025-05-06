package com.example.appcomponents

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ExampleService : Service() {
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Default + serviceJob)
    private var isRunning = false
    
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "Service starting...", Toast.LENGTH_SHORT).show()
        
        if (!isRunning) {
            isRunning = true
            startBackgroundWork()
        }
        
        return START_STICKY
    }
    
    private fun startBackgroundWork() {
        serviceScope.launch {
            var counter = 0
            while (isRunning) {
                delay(2000) // Delay for 2 seconds
                counter++
                
                // Log or show a toast every few iterations
                if (counter % 5 == 0) {
                    showMessage("Service running for ${counter * 2} seconds")
                }
            }
        }
    }
    
    private fun showMessage(message: String) {
        val mainScope = CoroutineScope(Dispatchers.Main)
        mainScope.launch {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        serviceJob.cancel()
        Toast.makeText(this, "Service stopping...", Toast.LENGTH_SHORT).show()
    }
}
