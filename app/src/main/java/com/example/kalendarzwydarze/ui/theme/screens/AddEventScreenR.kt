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
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.kalendarzwydarze.data.EventViewModelR
import com.example.kalendarzwydarze.data.EventR
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventScreenR(navController: NavHostController, viewModel: EventViewModelR) {
    val monthNames = listOf(
        "January", "February", "March", "April", "May", "June", "July",
        "August", "September", "October", "November", "December"
    )

    val currentYear = LocalDate.now().year
    var selectedMonthIndex by remember { mutableStateOf(0) }
    var day by remember { mutableStateOf(1) }
    var content by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val daysInMonth = if (selectedMonthIndex == 1 && currentYear % 4 == 0 &&
        (currentYear % 100 != 0 || currentYear % 400 == 0)) 29
    else listOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)[selectedMonthIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Add Event", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            TextField(
                readOnly = true,
                value = monthNames[selectedMonthIndex],
                onValueChange = {},
                label = { Text("Month") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
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
                val num = it.toIntOrNull()
                if (num != null && num in 1..daysInMonth) {
                    day = num
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
            val event = EventR(
                content = content,
                month = selectedMonthIndex + 1,
                day = day
            )
            viewModel.viewModelScope.launch {
                viewModel.addEvent(event)
                navController.popBackStack()
            }
        }) {
            Text("Save Event")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEventScreenR(navController: NavHostController, viewModel: EventViewModelR) {
    val event = viewModel.selectedEvent ?: return

    val monthNames = listOf(
        "January", "February", "March", "April", "May", "June", "July",
        "August", "September", "October", "November", "December"
    )

    val currentYear = LocalDate.now().year
    var selectedMonthIndex by remember { mutableStateOf(event.month - 1) }
    var day by remember { mutableStateOf(event.day) }
    var content by remember { mutableStateOf(event.content) }
    var expanded by remember { mutableStateOf(false) }

    val daysInMonth = if (selectedMonthIndex == 1 && currentYear % 4 == 0 &&
        (currentYear % 100 != 0 || currentYear % 400 == 0)) 29
    else listOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)[selectedMonthIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Edit Event", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            TextField(
                readOnly = true,
                value = monthNames[selectedMonthIndex],
                onValueChange = {},
                label = { Text("Month") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
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
                val num = it.toIntOrNull()
                if (num != null && num in 1..daysInMonth) {
                    day = num
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
            val updatedEvent = event.copy(
                content = content,
                month = selectedMonthIndex + 1,
                day = day
            )
            viewModel.viewModelScope.launch {
                viewModel.updateEvent(updatedEvent)
                navController.popBackStack()
            }
        }) {
            Text("Save Changes")
        }

        Spacer(Modifier.height(12.dp))

        Button(onClick = {
            viewModel.viewModelScope.launch {
                viewModel.deleteEvent(event)
                navController.popBackStack()
            }
        }) {
            Text("Delete Event")
        }
    }
}




