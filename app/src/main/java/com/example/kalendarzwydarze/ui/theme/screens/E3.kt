package com.example.kalendarzwydarze.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
//import com.example.kalendarzwydarze.data.EventProvider
import com.example.kalendarzwydarze.data.EventViewModel
import com.example.kalendarzwydarze.data.Event
import com.example.kalendarzwydarze.data.EventViewModelR
import java.time.LocalDate

// Wydarzenia miesiąca
@Composable
fun E3(indexPassed: String, navController: NavHostController,eventViewModel: EventViewModelR) {
    val indexint = indexPassed.toInt()
    val currentDate = LocalDate.now()
    val cday = currentDate.dayOfMonth
    val cmonth = currentDate.monthValue
    val cyear = currentDate.year
    val eventsT by eventViewModel.events.observeAsState(emptyList())
    val events = eventsT.filter { it.month == (indexint + 1) }
    val monthDays = if (cyear % 4 == 0 && (cyear % 100 != 0 || cyear % 400 == 0)) {
        // February in a leap year
        listOf(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    } else {
        // Regular month days
        listOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    }
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
            text = monthNames[indexint],
            modifier = Modifier.padding(10.dp).align(Alignment.CenterHorizontally),
            fontSize = 26.sp
        )

        for (rowIndex in 0 until monthDays[indexint]) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp)
            ) {
                for (colIndex in 0 until 5) {
                    val index = rowIndex * 5 + colIndex + 1
                    if (index <= monthDays[indexint]) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(1.dp)
                                .background(
                                    if (index < cday && indexint + 1 == cmonth) {
                                        Color.Gray
                                    } else {
                                        Color.Yellow
                                    }
                                )
                                .height(80.dp)
                                .clickable { navController.navigate("E4/$index/$indexint") }
                        ) {
                            // Upper-left
                            Text(
                                text = "$index",
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(8.dp),
                            )
                            // Bottom-left
                            Text(
                                text = "Events ${events.filter { it.day == index }.size}",
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
