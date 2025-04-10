package com.example.kalendarzwydarze.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.kalendarzwydarze.data.Event
import com.example.kalendarzwydarze.data.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventScreen(navController: NavHostController, viewModel: EventViewModel) {
    val monthNames = listOf(
        "January", "February", "March", "April", "May", "June", "July",
        "August", "September", "October", "November", "December"
    )

    var selectedMonthIndex by remember { mutableStateOf(0) }
    var day by remember { mutableStateOf(1) }
    var content by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    // Calculate max days for the selected month (leap year check for February)
    val currentYear = java.time.LocalDate.now().year
    val daysInMonth = if (selectedMonthIndex == 1 && // February
        currentYear % 4 == 0 && (currentYear % 100 != 0 || currentYear % 400 == 0)
    ) 29 else listOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)[selectedMonthIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Add Event", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        // Dropdown for selecting month
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                readOnly = true,
                value = monthNames[selectedMonthIndex],
                onValueChange = {},
                label = { Text("Month") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                monthNames.forEachIndexed { index, name ->
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = {
                            selectedMonthIndex = index
                            if (day > daysInMonth) day = daysInMonth // clamp day if needed
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Numeric input for day
        TextField(
            value = day.toString(),
            onValueChange = {
                val value = it.toIntOrNull()
                if (value != null && value in 1..daysInMonth) {
                    day = value
                }
            },
            label = { Text("Day (1–$daysInMonth)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        // Event content
        TextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Event Content") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        Button(onClick = {
            viewModel.addEvent(Event(content = content, month = selectedMonthIndex + 1, day = day))
            navController.popBackStack()
        }) {
            Text("Save")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEventScreen(
    navController: NavHostController,
    viewModel: EventViewModel,
    eventId: Int // Pass this via nav arguments
) {
    val event = viewModel.eventList.find { it.id == eventId } ?: return
    val monthNames = listOf(
        "January", "February", "March", "April", "May", "June", "July",
        "August", "September", "October", "November", "December"
    )

    var selectedMonthIndex by remember { mutableStateOf(event.month - 1) }
    var day by remember { mutableStateOf(event.day) }
    var content by remember { mutableStateOf(event.content) }
    var expanded by remember { mutableStateOf(false) }

    val currentYear = java.time.LocalDate.now().year
    val daysInMonth = if (selectedMonthIndex == 1 &&
        currentYear % 4 == 0 && (currentYear % 100 != 0 || currentYear % 400 == 0)
    ) 29 else listOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)[selectedMonthIndex]

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Edit Event", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        // [Dropdown, Day Input, Content Field – same as AddEventScreen]

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                readOnly = true,
                value = monthNames[selectedMonthIndex],
                onValueChange = {},
                label = { Text("Month") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                monthNames.forEachIndexed { index, name ->
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = {
                            selectedMonthIndex = index
                            if (day > daysInMonth) day = daysInMonth
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        TextField(
            value = day.toString(),
            onValueChange = {
                val value = it.toIntOrNull()
                if (value != null && value in 1..daysInMonth) {
                    day = value
                }
            },
            label = { Text("Day (1–$daysInMonth)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        TextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Event Content") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        Button(onClick = {
            viewModel.updateEvent(
                event.copy(
                    content = content,
                    month = selectedMonthIndex + 1,
                    day = day
                )
            )
            navController.popBackStack()
        }) {
            Text("Save Changes")
        }
        Button(
            onClick = {
                viewModel.deleteEvent(eventId)
                navController.popBackStack()
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Delete")
        }
    }
}
