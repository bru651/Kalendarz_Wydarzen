package com.example.kalendarzwydarze

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kalendarzwydarze.ui.theme.BottomElement
import com.example.kalendarzwydarze.ui.theme.BottomNavigation
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material3.Text
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.kalendarzwydarze.data.EventViewModel
import com.example.kalendarzwydarze.data.EventViewModelR
import com.example.kalendarzwydarze.data.GoalViewModelR


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(eventViewModel: EventViewModel, eventViewModelR: EventViewModelR, goalViewModelR: GoalViewModelR) {
    val navController = rememberNavController()
    Scaffold (
        bottomBar = { BottomBar(navController = navController)}
    ){
        BottomNavigation(navController = navController, eventViewModel = eventViewModel, eventViewModelR = eventViewModelR, goalViewModelR = goalViewModelR)
    }
}
@Composable
fun BottomBar(navController: NavHostController)
{
    val screens = listOf(BottomElement.E1, BottomElement.E2, BottomElement.EG)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation(){
        screens.forEach {
            screen ->AddItem(screen = screen, currentDestination = currentDestination, navController = navController)
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomElement,
    currentDestination: NavDestination?,
    navController: NavHostController
)
{
    BottomNavigationItem(
        label = { Text(text = screen.title)},
        icon = {
            Icon(imageVector = screen.icon, contentDescription = "Navigation Icon")
        },
        selected = currentDestination?.hierarchy?.any
        {
            it.route == screen.route
        } == true,
        unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
        onClick = {
            navController.navigate(screen.route)
            {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}
