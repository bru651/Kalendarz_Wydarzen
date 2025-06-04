// E2R.kt
package com.example.kalendarzwydarze.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.kalendarzwydarze.data.Event.*
import com.example.kalendarzwydarze.data.EventR
import com.example.kalendarzwydarze.data.EventViewModelR
import java.time.LocalDate

@Composable
fun E2R(navController: NavHostController, viewModel: EventViewModelR) {
    val currentDate = LocalDate.now()
    val cday = currentDate.dayOfMonth
    val cmonth = currentDate.monthValue
    val monthNames = listOf(
        "January", "February", "March", "April", "May", "June", "July",
        "August", "September", "October", "November", "December"
    )

    LaunchedEffect(Unit) {
        viewModel.loadEvents()
    }

    val events by viewModel.events.observeAsState(emptyList())

    val sortedEvents = events.sortedWith(
        compareBy<EventR>(
            { it.month < cmonth || (it.month == cmonth && it.day < cday) }, // Past events last
            { it.month },
            { it.day }
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            text = "Events",
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterHorizontally),
            fontSize = 26.sp
        )

        Button(
            onClick = { navController.navigate("addEvent") },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        ) {
            Text(text = "Add New Event")
        }

        LazyColumn(modifier = Modifier.fillMaxWidth().height(600.dp)) {
            items(sortedEvents) { event ->
                Box(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                        .background(Color.Yellow)
                        .height(80.dp)
                        .clickable {
                            viewModel.selectedEvent = event
                            navController.navigate("editEventR")
                        }
                ) {
                    Text(
                        text = event.content,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp),
                        fontSize = 24.sp
                    )
                    Text(
                        text = "${monthNames[event.month - 1]} ${event.day}",
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}
