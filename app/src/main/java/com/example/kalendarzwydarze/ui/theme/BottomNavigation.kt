package com.example.kalendarzwydarze.ui.theme

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.kalendarzwydarze.ui.theme.screens.E1
import com.example.kalendarzwydarze.ui.theme.screens.E2
import com.example.kalendarzwydarze.ui.theme.screens.E3
import com.example.kalendarzwydarze.ui.theme.screens.E4
import com.example.kalendarzwydarze.data.EventViewModel
import com.example.kalendarzwydarze.ui.theme.screens.AddEventScreen
import com.example.kalendarzwydarze.ui.theme.screens.EditEventScreen

@Composable
fun BottomNavigation(navController: NavHostController, eventViewModel: EventViewModel)
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
        composable("addEvent") { // New route for adding events
            AddEventScreen(navController = navController, viewModel = eventViewModel) // Pass the ViewModel
        }
        composable("EditEvent/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")?.toInt() ?: return@composable
            EditEventScreen(navController, eventViewModel, eventId)
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