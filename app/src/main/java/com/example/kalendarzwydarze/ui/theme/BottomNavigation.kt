package com.example.kalendarzwydarze.ui.theme

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.kalendarzwydarze.ui.theme.screens.E1
import com.example.kalendarzwydarze.ui.theme.screens.E2
import com.example.kalendarzwydarze.ui.theme.screens.EG
import com.example.kalendarzwydarze.ui.theme.screens.E3
import com.example.kalendarzwydarze.ui.theme.screens.E4
import com.example.kalendarzwydarze.data.EventViewModel
import com.example.kalendarzwydarze.data.GoalViewModel
import com.example.kalendarzwydarze.data.GoalViewModelR
import com.example.kalendarzwydarze.ui.theme.screens.AddEventScreen
import com.example.kalendarzwydarze.ui.theme.screens.AddGoalScreen
import com.example.kalendarzwydarze.ui.theme.screens.AddGoalScreenR
import com.example.kalendarzwydarze.ui.theme.screens.EGR
//import com.example.kalendarzwydarze.ui.theme.screens.EGR
import com.example.kalendarzwydarze.ui.theme.screens.EditEventScreen
import com.example.kalendarzwydarze.ui.theme.screens.EditGoalScreen
import com.example.kalendarzwydarze.ui.theme.screens.EditGoalScreenR

@Composable
fun BottomNavigation(navController: NavHostController, eventViewModel: EventViewModel, goalViewModel: GoalViewModel, goalViewModelR: GoalViewModelR)
{
    NavHost(navController = navController, startDestination = BottomElement.E1.route )
    {
        composable(route= BottomElement.E1.route)
        {
            E1(navController)// Wybór miesięcy
        }
        composable(route= BottomElement.E2.route)
        {
            E2(navController = navController,viewModel = eventViewModel)// Lista wydarzeń
        }
        composable(route= BottomElement.EG.route)
        {
            //EG(navController = navController,viewModel = goalViewModel)// Lista wydarzeń
            EGR(navController = navController,viewModel = goalViewModelR)// Lista celów
        }
        composable("addEvent") { // Route for adding events
            AddEventScreen(navController = navController, viewModel = eventViewModel) // Pass the ViewModel
        }
        composable("EditEvent/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")?.toInt() ?: return@composable
            EditEventScreen(navController, eventViewModel, eventId)
        }
        composable("addGoal") { // Route for adding goals
            //AddGoalScreen(navController = navController, viewModel = goalViewModel) // Pass the ViewModel
            AddGoalScreenR(navController = navController, viewModel = goalViewModelR) // Pass the ViewModel
        }
        composable("EditGoal/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")?.toInt() ?: return@composable
            EditGoalScreen(navController, goalViewModel, eventId)
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
                E3(index,navController,eventViewModel)// Wydarzenia miesiąca
            }
        }
        composable("E4/{index}/{indexm}") { backStackEntry ->
            val index = backStackEntry.arguments?.getString("index")
            val indexm = backStackEntry.arguments?.getString("indexm")
            if (index != null && indexm != null) {
                E4(navController,index,indexm,eventViewModel)// Szczegóły dnia
            }
        }
    }
}