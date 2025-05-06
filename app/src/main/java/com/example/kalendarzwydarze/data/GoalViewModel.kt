package com.example.kalendarzwydarze.data

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
//import com.example.kalendarzwydarze.data.model.event
import kotlin.random.Random

data class SubGoal(
    var content: String,
    var isCompleted: Boolean = false,
    val id: Int = Random.nextInt()
)

data class Goal(
    var content: String,
    var month: Int,
    var day: Int,
    val subGoals: MutableList<SubGoal> = mutableListOf(),
    val id: Int = Random.nextInt()
)


class GoalViewModel : ViewModel() {
    private val _goalList = mutableStateListOf<Goal>()
    val goalList: List<Goal> get() = _goalList

    fun addGoal(goal: Goal) {
        _goalList.add(goal)
    }

    fun deleteGoal(id: Int) {
        _goalList.removeAll { it.id == id }
    }

    fun updateGoal(updatedGoal: Goal) {
        val index = _goalList.indexOfFirst { it.id == updatedGoal.id }
        if (index != -1) {
            _goalList[index] = updatedGoal
        }
    }

    /*fun toggleSubGoalCompletion(goalId: Int, subGoalId: Int) {
        val goal = _goalList.find { it.id == goalId }
        val subGoal = goal?.subGoals?.find { it.id == subGoalId }
        subGoal?.let {
            it.isCompleted = !it.isCompleted
        }
    }*/
}

