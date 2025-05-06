package com.example.kalendarzwydarze.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomElement (
    val route: String,
    val title: String,
    val icon: ImageVector
)
{
    object E1 : BottomElement(
        route = "r1",
        title = "Months",
        icon = Icons.Default.DateRange
    )

    object E2 : BottomElement(
        route = "r2",
        title = "Events",
        icon = Icons.Default.Star
    )

    object EG : BottomElement(
        route = "r3",
        title = "Goals",
        icon = Icons.Default.Build
    )

}