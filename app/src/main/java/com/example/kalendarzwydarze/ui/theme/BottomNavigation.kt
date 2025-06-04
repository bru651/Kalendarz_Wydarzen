package com.example.kalendarzwydarze.ui.theme

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.kalendarzwydarze.ui.theme.screens.E1
import com.example.kalendarzwydarze.ui.theme.screens.E3
import com.example.kalendarzwydarze.ui.theme.screens.E4
import com.example.kalendarzwydarze.data.EventViewModel
import com.example.kalendarzwydarze.data.EventViewModelR
import com.example.kalendarzwydarze.data.GoalViewModelR
import com.example.kalendarzwydarze.ui.theme.screens.AddEventScreenR
import com.example.kalendarzwydarze.ui.theme.screens.AddGoalScreenR
import com.example.kalendarzwydarze.ui.theme.screens.E2R
import com.example.kalendarzwydarze.ui.theme.screens.EGR
import com.example.kalendarzwydarze.ui.theme.screens.EditEventScreenR
import com.example.kalendarzwydarze.ui.theme.screens.EditGoalScreenR

@Composable
fun BottomNavigation(navController: NavHostController, eventViewModel: EventViewModel, eventViewModelR: EventViewModelR, goalViewModelR: GoalViewModelR)
{
    NavHost(navController = navController, startDestination = BottomElement.E1.route )
    {
        composable(route= BottomElement.E1.route)
        {
            E1(navController)// Wybór miesięcy
        }
        composable(route= BottomElement.E2.route)
        {
            //E2(navController = navController,viewModel = eventViewModel)// Lista wydarzeń
            E2R(navController = navController,viewModel = eventViewModelR)// Lista wydarzeń
        }
        composable(route= BottomElement.EG.route)
        {
            //EG(navController = navController,viewModel = goalViewModel)
            EGR(navController = navController,viewModel = goalViewModelR)// Lista celów
        }
        composable("addEvent") { // Route for adding events
            //AddEventScreen(navController = navController, viewModel = eventViewModel) // Pass the ViewModel
            AddEventScreenR(navController = navController, viewModel = eventViewModelR)
        }
        composable("editEventR") {
            val selectedEvent = eventViewModelR.selectedEvent
            if (selectedEvent != null) {
                EditEventScreenR(navController, eventViewModelR)
            }
        }
        composable("editGoalR") {
            val selectedGoal = goalViewModelR.selectedGoalWithSubGoals
            if (selectedGoal != null) {
                EditGoalScreenR(navController, goalViewModelR, selectedGoal)
            }
        }

        composable("addGoal") { // Route for adding goals
            //AddGoalScreen(navController = navController, viewModel = goalViewModel) // Pass the ViewModel
            AddGoalScreenR(navController = navController, viewModel = goalViewModelR) // Pass the ViewModel
        }
        composable("editGoalR") {
            val selectedGoal = goalViewModelR.selectedGoalWithSubGoals
            if (selectedGoal != null) {
                EditGoalScreenR(navController, goalViewModelR, selectedGoal)
            }
        }

        composable("E3/{index}") { backStackEntry ->
            val index = backStackEntry.arguments?.getString("index")
            if (index != null) {
                E3(index,navController,eventViewModelR)// Wydarzenia miesiąca
            }
        }
        composable("E4/{index}/{indexm}") { backStackEntry ->
            val index = backStackEntry.arguments?.getString("index")
            val indexm = backStackEntry.arguments?.getString("indexm")
            if (index != null && indexm != null) {
                E4(navController,index,indexm,eventViewModelR)// Szczegóły dnia
            }
        }
    }
}