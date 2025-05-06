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
import com.example.kalendarzwydarze.data.SubGoal
import com.example.kalendarzwydarze.data.Goal
import com.example.kalendarzwydarze.data.GoalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGoalScreen(navController: NavHostController, viewModel: GoalViewModel) {
    val monthNames = listOf(
        "January", "February", "March", "April", "May", "June", "July",
        "August", "September", "October", "November", "December"
    )

    var selectedMonthIndex by remember { mutableStateOf(0) }
    var day by remember { mutableStateOf(1) }
    var content by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val subGoals = remember { mutableStateListOf<SubGoal>() }
    var newSubGoalText by remember { mutableStateOf("") }
    var noDeadline by remember { mutableStateOf(false) }

    val currentYear = java.time.LocalDate.now().year
    val daysInMonth = if (selectedMonthIndex == 1 && currentYear % 4 == 0 && (currentYear % 100 != 0 || currentYear % 400 == 0)) 29
    else listOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)[selectedMonthIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Add Goal", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        // No deadline checkbox
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Checkbox(
                checked = noDeadline,
                onCheckedChange = { noDeadline = it }
            )
            Text("No deadline")
        }

        // Deadline input (hidden if noDeadline is true)
        if (!noDeadline) {
            // Month dropdown
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                TextField(
                    readOnly = true,
                    value = monthNames[selectedMonthIndex],
                    onValueChange = {},
                    label = { Text("Month") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
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

            // Day input
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
        }

        // Goal content input
        TextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Goal Content") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        // Subgoal input
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = newSubGoalText,
                onValueChange = { newSubGoalText = it },
                label = { Text("New Subgoal") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (newSubGoalText.isNotBlank()) {
                    subGoals.add(SubGoal(content = newSubGoalText))
                    newSubGoalText = ""
                }
            }) {
                Text("Add")
            }
        }

        Spacer(Modifier.height(12.dp))

        // Display list of subgoals
        subGoals.forEach { sub ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Checkbox(checked = sub.isCompleted, onCheckedChange = null)
                Text(text = sub.content)
            }
        }

        Spacer(Modifier.height(24.dp))

        // Save button
        Button(onClick = {
            viewModel.addGoal(
                Goal(
                    content = content,
                    month = if (noDeadline) null else selectedMonthIndex + 1,
                    day = if (noDeadline) null else day,
                    subGoals = subGoals.toMutableList()
                )
            )
            navController.popBackStack()
        }) {
            Text("Save Goal")
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGoalScreen(
    navController: NavHostController,
    viewModel: GoalViewModel,
    goalId: Int
) {
    val goal = viewModel.goalList.find { it.id == goalId } ?: return

    var content by remember { mutableStateOf(goal.content) }
    var day by remember { mutableStateOf(goal.day ?: 1) }
    var month by remember { mutableStateOf(goal.month ?: 1) }
    var subGoals by remember { mutableStateOf(goal.subGoals.toList()) }
    var newSubGoalText by remember { mutableStateOf("") }
    var noDeadline by remember { mutableStateOf(goal.day == null || goal.month == null) }

    val monthNames = listOf(
        "January", "February", "March", "April", "May", "June", "July",
        "August", "September", "October", "November", "December"
    )
    val selectedMonthIndex = month - 1
    val currentYear = java.time.LocalDate.now().year
    val daysInMonth = if (selectedMonthIndex == 1 && currentYear % 4 == 0 && (currentYear % 100 != 0 || currentYear % 400 == 0)) 29
    else listOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)[selectedMonthIndex]

    var monthDropdownExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        Text("Edit Goal", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(12.dp))

        // No deadline checkbox
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = noDeadline,
                onCheckedChange = { noDeadline = it }
            )
            Text("No deadline")
        }

        if (!noDeadline) {
            Spacer(modifier = Modifier.height(12.dp))

            // Month dropdown
            ExposedDropdownMenuBox(expanded = monthDropdownExpanded, onExpandedChange = {
                monthDropdownExpanded = !monthDropdownExpanded
            }) {
                TextField(
                    readOnly = true,
                    value = monthNames[month - 1],
                    onValueChange = {},
                    label = { Text("Month") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = monthDropdownExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = monthDropdownExpanded, onDismissRequest = { monthDropdownExpanded = false }) {
                    monthNames.forEachIndexed { index, name ->
                        DropdownMenuItem(
                            text = { Text(name) },
                            onClick = {
                                month = index + 1
                                if (day > daysInMonth) day = daysInMonth
                                monthDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Day input
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

            Spacer(modifier = Modifier.height(12.dp))
        }

        TextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Goal") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Subgoals with toggles
        subGoals.forEachIndexed { index, subGoal ->
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Checkbox(
                    checked = subGoal.isCompleted,
                    onCheckedChange = {
                        subGoals = subGoals.toMutableList().also {
                            it[index] = it[index].copy(isCompleted = !it[index].isCompleted)
                        }
                    }
                )
                Text(subGoal.content)
            }
        }

        // Add new subgoal
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = newSubGoalText,
                onValueChange = { newSubGoalText = it },
                label = { Text("New Subgoal") },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                if (newSubGoalText.isNotBlank()) {
                    subGoals = subGoals + SubGoal(content = newSubGoalText)
                    newSubGoalText = ""
                }
            }) {
                Text("Add")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Save and delete buttons
        Row {
            Button(onClick = {
                viewModel.updateGoal(
                    goal.copy(
                        content = content,
                        month = if (noDeadline) null else month,
                        day = if (noDeadline) null else day,
                        subGoals = subGoals.toMutableList()
                    )
                )
                navController.popBackStack()
            }) {
                Text("Save Changes")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(onClick = {
                viewModel.deleteGoal(goalId)
                navController.popBackStack()
            }) {
                Text("Delete")
            }
        }
    }
}


