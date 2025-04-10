// E2.kt
package com.example.kalendarzwydarze.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.kalendarzwydarze.data.EventProvider
//import com.example.kalendarzwydarze.data.model.event
import java.time.LocalDate
import com.example.kalendarzwydarze.data.EventViewModel
import com.example.kalendarzwydarze.data.Event

// Lista wydarzeń
@Composable
fun E2(navController: NavHostController, viewModel: EventViewModel = viewModel()) {
    val currentDate = LocalDate.now()
    val cday = currentDate.dayOfMonth
    val cmonth = currentDate.monthValue
    val monthNames = listOf(
        "January", "February", "March", "April", "May", "June", "July",
        "August", "September", "October", "November", "December"
    )
    // Sort events
    val events = viewModel.eventList.sortedWith(compareBy({ it.month < cmonth || (it.month == cmonth && it.day < cday) }, { it.month }, { it.day }))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            text = "Events",
            modifier = Modifier.padding(10.dp).align(Alignment.CenterHorizontally),
            fontSize = 26.sp
        )
        Button(
            onClick = {
                navController.navigate("addEvent")
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp) // Adds padding below the button
        ) {
            Text(text = "Add New Event")
        }
        // Display events
        LazyColumn(modifier = Modifier.fillMaxWidth().height(600.dp)) {
            items(events.size) { index ->
                Box(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                        .background(Color.Yellow)
                        .height(80.dp)
                        .clickable { navController.navigate("EditEvent/${events[index].id}") }
                ) {
                    Text(
                        text = events[index].content,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp),
                        fontSize = 26.sp
                    )
                    Text(
                        text = monthNames[events[index].month-1],
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                    )
                    Text(
                        text = "Day ${events[index].day}",
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(8.dp)
                    )
                }
            }
        }


    }
}
