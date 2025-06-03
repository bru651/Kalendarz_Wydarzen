// MainActivity.kt
package com.example.kalendarzwydarze

import android.app.Application
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
import androidx.room.Room
import com.example.kalendarzwydarze.data.AppDatabase
import com.example.kalendarzwydarze.data.EventViewModel
import com.example.kalendarzwydarze.data.GoalRepository
import com.example.kalendarzwydarze.data.GoalViewModelFactory
import com.example.kalendarzwydarze.data.GoalViewModelR

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KalendarzWydarzeńTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val db = MyApp.database
                    val repository = GoalRepository(db.goalDao())
                    val goalViewModelR: GoalViewModelR = viewModel(
                        factory = GoalViewModelFactory(repository)
                    )

                    // Pass the eventViewModel to the navigation function
                    Navigation(eventViewModel = viewModel(),goalViewModel = viewModel(), goalViewModelR = goalViewModelR)
                }
            }
        }
    }
}

class MyApp : Application() {
    companion object {
        lateinit var database: AppDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "goal_database"
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }
}

