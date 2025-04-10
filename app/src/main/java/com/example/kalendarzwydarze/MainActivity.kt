// MainActivity.kt
package com.example.kalendarzwydarze

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.kalendarzwydarze.ui.theme.KalendarzWydarzeńTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kalendarzwydarze.data.EventViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KalendarzWydarzeńTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Pass the eventViewModel to the navigation function
                    Navigation(eventViewModel = viewModel())
                }
            }
        }
    }
}
