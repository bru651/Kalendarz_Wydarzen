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
import com.example.kalendarzwydarze.data.GoalR
import com.example.kalendarzwydarze.data.GoalViewModel
import com.example.kalendarzwydarze.data.GoalViewModelR
import com.example.kalendarzwydarze.data.GoalWithSubGoals
import com.example.kalendarzwydarze.data.SubGoalR
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGoalScreenR(navController: NavHostController, viewModel: GoalViewModelR) {
    val monthNames = listOf(
        "January", "February", "March", "April", "May", "June", "July",
        "August", "September", "October", "November", "December"
    )

    val currentYear = LocalDate.now().year

    var selectedMonthIndex by remember { mutableStateOf(0) }
    var day by remember { mutableStateOf(1) }
    var content by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var noDeadline by remember { mutableStateOf(false) }

    val subGoals = remember { mutableStateListOf<String>() }
    var newSubGoalText by remember { mutableStateOf("") }

    val daysInMonth = if (selectedMonthIndex == 1 && currentYear % 4 == 0 &&
        (currentYear % 100 != 0 || currentYear % 400 == 0)) 29
    else listOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)[selectedMonthIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Add Goal", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Checkbox(checked = noDeadline, onCheckedChange = { noDeadline = it })
            Text("No deadline")
        }

        if (!noDeadline) {
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
        }

        Spacer(Modifier.height(12.dp))

        TextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Goal Content") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = newSubGoalText,
                onValueChange = { newSubGoalText = it },
                label = { Text("New Subgoal") },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                if (newSubGoalText.isNotBlank()) {
                    subGoals.add(newSubGoalText)
                    newSubGoalText = ""
                }
            }) {
                Text("Add")
            }
        }

        Spacer(Modifier.height(12.dp))

        Column {
            subGoals.forEach {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = false, onCheckedChange = null)
                    Text(it)
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(onClick = {
            val goal = GoalR(
                content = content,
                month = if (noDeadline) null else selectedMonthIndex + 1,
                day = if (noDeadline) null else day
            )

            val subGoalEntities = subGoals.map {
                SubGoalR(goalId = 0, content = it) // temp goalId; assigned in ViewModel
            }

            viewModel.viewModelScope.launch {
                viewModel.addGoalWithSubGoals(goal, subGoalEntities)
                navController.popBackStack()
            }
        }) {
            Text("Save Goal")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGoalScreenR(
    navController: NavHostController,
    viewModel: GoalViewModelR,
    goalWithSubGoals: GoalWithSubGoals
) {
    val monthNames = listOf(
        "January", "February", "March", "April", "May", "June", "July",
        "August", "September", "October", "November", "December"
    )

    val currentYear = LocalDate.now().year

    var selectedMonthIndex by remember { mutableStateOf((goalWithSubGoals.goal.month ?: 1) - 1) }
    var day by remember { mutableStateOf(goalWithSubGoals.goal.day ?: 1) }
    var content by remember { mutableStateOf(goalWithSubGoals.goal.content) }
    var expanded by remember { mutableStateOf(false) }
    var noDeadline by remember { mutableStateOf(goalWithSubGoals.goal.month == null) }

    val subGoals = remember { mutableStateListOf(*goalWithSubGoals.subGoals.toTypedArray()) }
    var newSubGoalText by remember { mutableStateOf("") }

    val daysInMonth = if (selectedMonthIndex == 1 && currentYear % 4 == 0 &&
        (currentYear % 100 != 0 || currentYear % 400 == 0)) 29
    else listOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)[selectedMonthIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Edit Goal", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Checkbox(checked = noDeadline, onCheckedChange = { noDeadline = it })
            Text("No deadline")
        }

        if (!noDeadline) {
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
        }

        Spacer(Modifier.height(12.dp))

        TextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Goal Content") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = newSubGoalText,
                onValueChange = { newSubGoalText = it },
                label = { Text("New Subgoal") },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                if (newSubGoalText.isNotBlank()) {
                    subGoals.add(SubGoalR(goalId = goalWithSubGoals.goal.id, content = newSubGoalText))
                    newSubGoalText = ""
                }
            }) {
                Text("Add")
            }
        }

        Spacer(Modifier.height(12.dp))

        Column {
            subGoals.forEachIndexed { index, subGoal ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = subGoal.isCompleted,
                        onCheckedChange = {
                            subGoals[index] = subGoal.copy(isCompleted = it)
                        }
                    )
                    Text(subGoal.content)
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(onClick = {
            val updatedGoal = goalWithSubGoals.goal.copy(
                content = content,
                month = if (noDeadline) null else selectedMonthIndex + 1,
                day = if (noDeadline) null else day
            )
            viewModel.viewModelScope.launch {
                viewModel.updateGoal(updatedGoal)
                subGoals.forEach { viewModel.updateSubGoal(it) }
                navController.popBackStack()
            }
        }) {
            Text("Save Changes")
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {
                viewModel.viewModelScope.launch {
                    viewModel.deleteGoal(goalWithSubGoals.goal)
                    navController.popBackStack()
                }
            }
        ) {
            Text("Delete Goal")
        }
    }
}

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGoalScreenR(
    navController: NavHostController,
    viewModel: GoalViewModelR,
    goalWithSubGoals: GoalWithSubGoals
) {
    val monthNames = listOf(
        "January", "February", "March", "April", "May", "June", "July",
        "August", "September", "October", "November", "December"
    )

    val currentYear = LocalDate.now().year

    var selectedMonthIndex by remember {
        mutableStateOf((goalWithSubGoals.goal.month ?: 1) - 1)
    }
    var day by remember {
        mutableStateOf(goalWithSubGoals.goal.day ?: 1)
    }
    var content by remember {
        mutableStateOf(goalWithSubGoals.goal.content)
    }
    var expanded by remember { mutableStateOf(false) }
    var noDeadline by remember {
        mutableStateOf(goalWithSubGoals.goal.month == null || goalWithSubGoals.goal.day == null)
    }

    val subGoals = remember {
        mutableStateListOf<String>().apply {
            addAll(goalWithSubGoals.subGoals.map { it.content })
        }
    }
    var newSubGoalText by remember { mutableStateOf("") }

    val daysInMonth = if (selectedMonthIndex == 1 && currentYear % 4 == 0 &&
        (currentYear % 100 != 0 || currentYear % 400 == 0)) 29
    else listOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)[selectedMonthIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Edit Goal", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Checkbox(checked = noDeadline, onCheckedChange = { noDeadline = it })
            Text("No deadline")
        }

        if (!noDeadline) {
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
        }

        Spacer(Modifier.height(12.dp))

        TextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Goal Content") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = newSubGoalText,
                onValueChange = { newSubGoalText = it },
                label = { Text("New Subgoal") },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                if (newSubGoalText.isNotBlank()) {
                    subGoals.add(newSubGoalText)
                    newSubGoalText = ""
                }
            }) {
                Text("Add")
            }
        }

        Spacer(Modifier.height(12.dp))

        Column {
            subGoals.forEach {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = false, onCheckedChange = null)
                    Text(it)
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(onClick = {
            val updatedGoal = goalWithSubGoals.goal.copy(
                content = content,
                month = if (noDeadline) null else selectedMonthIndex + 1,
                day = if (noDeadline) null else day
            )

            val updatedSubGoals = subGoals.map {
                SubGoalR(goalId = updatedGoal.id, content = it)
            }

            viewModel.viewModelScope.launch {
                viewModel.updateGoalWithSubGoals(updatedGoal, updatedSubGoals)
                navController.popBackStack()
            }
        }) {
            Text("Save Goal")
        }
    }
}
*/


