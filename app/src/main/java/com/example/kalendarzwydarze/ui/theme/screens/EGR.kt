
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
import com.example.kalendarzwydarze.data.GoalViewModelR
import com.example.kalendarzwydarze.data.GoalWithSubGoals
import java.time.LocalDate

@Composable
fun EGR(navController: NavHostController, viewModel: GoalViewModelR) {
    val currentDate = LocalDate.now()
    val cday = currentDate.dayOfMonth
    val cmonth = currentDate.monthValue
    val monthNames = listOf(
        "January", "February", "March", "April", "May", "June", "July",
        "August", "September", "October", "November", "December"
    )

    LaunchedEffect(Unit) {
        viewModel.loadGoals()
    }

    val goals by viewModel.goals.observeAsState(emptyList())

    val sortedGoals = goals.sortedWith(
        compareBy<GoalWithSubGoals>(
            { it.goal.month == null || it.goal.day == null }, // No-deadline last
            {
                if (it.goal.month != null && it.goal.day != null &&
                    (it.goal.month!! < cmonth || (it.goal.month == cmonth && it.goal.day!! < cday))
                ) 1 else 0
            },
            { it.goal.month ?: 13 },
            { it.goal.day ?: 32 }
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            text = "Goals",
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterHorizontally),
            fontSize = 26.sp
        )
        Button(
            onClick = {
                navController.navigate("addGoal")
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        ) {
            Text(text = "Add New Goal")
        }

        LazyColumn(modifier = Modifier.fillMaxWidth().height(600.dp)) {
            items(sortedGoals) { goalWithSubGoals ->
                val goal = goalWithSubGoals.goal
                Box(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                        .background(Color.Yellow)
                        .height(80.dp)
                        .clickable { viewModel.selectedGoalWithSubGoals = goalWithSubGoals
                            navController.navigate("editGoalR") }
                ) {
                    Text(
                        text = goal.content,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp),
                        fontSize = 26.sp
                    )
                    Text(
                        text = if (goal.month != null && goal.day != null)
                            "${monthNames[goal.month!! - 1]} ${goal.day}"
                        else
                            "No deadline",
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}
