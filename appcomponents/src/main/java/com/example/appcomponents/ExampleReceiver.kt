package com.example.appcomponents

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

/**
 * Example broadcast receiver that handles system events like boot completed
 * and demonstrates static registration via AndroidManifest.xml
 */
class ExampleReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                // This would be executed when the device has completed booting
                Toast.makeText(
                    context,
                    "Boot completed event received",
                    Toast.LENGTH_SHORT
                ).show()
                
                // In real applications, you might start a service or schedule work here
            }
            
            // You could handle other system broadcasts here
            Intent.ACTION_BATTERY_LOW -> {
                Toast.makeText(
                    context,
                    "Battery is running low",
                    Toast.LENGTH_SHORT
                ).show()
            }
            
            // Handle custom broadcasts
            "com.example.components.CUSTOM_BROADCAST" -> {
                Toast.makeText(
                    context,
                    "Static receiver got custom broadcast",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
