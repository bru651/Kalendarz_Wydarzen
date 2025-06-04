package com.example.kalendarzwydarze.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.kalendarzwydarze.data.EventViewModelR
import com.example.kalendarzwydarze.data.EventR

@Composable
fun E4(
    navController: NavHostController,
    indexPassed: String,
    indexPassedM: String,
    eventViewModel: EventViewModelR
) {
    val indexint = indexPassed.toIntOrNull() ?: return
    val indexint2 = indexPassedM.toIntOrNull() ?: return

    val events by eventViewModel.events.observeAsState(emptyList())

    val eventsToday = events.filter { it.day == indexint && it.month == indexint2 + 1 }

    val monthNames = listOf(
        "January", "February", "March", "April", "May", "June", "July",
        "August", "September", "October", "November", "December"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            text = "${monthNames.getOrNull(indexint2) ?: "Unknown"} $indexint",
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterHorizontally),
            fontSize = 26.sp
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
        ) {
            items(eventsToday) { event ->
                Box(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                        .background(Color.Yellow)
                        .height(80.dp)
                        .clickable {
                            eventViewModel.selectedEvent = event
                            navController.navigate("editEventR")
                        }
                ) {
                    Text(
                        text = event.content,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(8.dp),
                        fontSize = 26.sp
                    )
                }
            }
        }
    }
}
