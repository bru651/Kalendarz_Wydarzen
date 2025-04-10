package com.example.kalendarzwydarze.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
//import com.example.kalendarzwydarze.data.EventProvider
import com.example.kalendarzwydarze.data.EventViewModel

// Szczegóły dnia

@Composable
fun E4(navController: NavHostController, indexPassed: String, indexPassedM: String, eventViewModel: EventViewModel)
{
    val indexint = indexPassed.toInt()
    val indexint2 = indexPassedM.toInt()
    val events = eventViewModel.eventList.filter { it.day == (indexint) && it.month == (indexint2+1)}

    val monthNames = listOf(
        "January", "February", "March", "April", "May", "June", "July",
        "August", "September", "October", "November", "December"
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(text = monthNames[indexint2]+" "+(indexint), modifier = Modifier.padding(10.dp).align(Alignment.CenterHorizontally),fontSize=26.sp)
        LazyColumn(
            modifier = Modifier.fillMaxWidth().height(600.dp)
        ) {
            items(events.size) { index ->
                Box(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                        .background(Color.Yellow)
                        .height(80.dp)
                        .clickable { navController.navigate("EditEvent/${events[index].id}") }
                ) {


                    //Text
                    Text(
                        text = events[index].content,
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