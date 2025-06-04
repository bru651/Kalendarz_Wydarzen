package com.example.kalendarzwydarze.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.*
import kotlinx.coroutines.launch

// ——— Entities ———

@Entity
data class GoalR(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val content: String,
    val month: Int?,         // nullable for no deadline
    val day: Int?            // nullable for no deadline
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = GoalR::class,
        parentColumns = ["id"],
        childColumns = ["goalId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("goalId")]
)
data class SubGoalR(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val goalId: Int,
    val content: String,
    val isCompleted: Boolean = false
)

data class GoalWithSubGoals(
    @Embedded val goal: GoalR,
    @Relation(
        parentColumn = "id",
        entityColumn = "goalId"
    )
    val subGoals: List<SubGoalR>
)

// ——— DAO ———

@Dao
interface GoalDao {
    @Transaction
    @Query("SELECT * FROM GoalR")
    suspend fun getAllGoalsWithSubGoals(): List<GoalWithSubGoals>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: GoalR): Long

    @Insert
    suspend fun insertSubGoals(subGoals: List<SubGoalR>)

    @Delete
    suspend fun deleteGoal(goal: GoalR)

    @Delete
    suspend fun deleteSubGoal(subGoal: SubGoalR)

    @Update
    suspend fun updateSubGoal(subGoal: SubGoalR)

    @Update
    suspend fun updateGoal(goal: GoalR)
}

// ——— Database ———

@Database(entities = [GoalR::class, SubGoalR::class, EventR::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun goalDao(): GoalDao
    abstract fun eventDao(): EventDao
}


// ——— Repository ———

class GoalRepository(private val dao: GoalDao) {
    suspend fun insertGoal(goal: GoalR): Long = dao.insertGoal(goal)
    suspend fun insertSubGoals(subGoals: List<SubGoalR>) = dao.insertSubGoals(subGoals)
    suspend fun deleteGoal(goal: GoalR) = dao.deleteGoal(goal)
    suspend fun deleteSubGoal(subGoal: SubGoalR) = dao.deleteSubGoal(subGoal)
    suspend fun updateGoal(goal: GoalR) = dao.updateGoal(goal)
    suspend fun updateSubGoal(subGoal: SubGoalR) = dao.updateSubGoal(subGoal)
    suspend fun getAllGoalsWithSubGoals(): List<GoalWithSubGoals> = dao.getAllGoalsWithSubGoals()
}

// ——— ViewModel (Room-backed) ———

class GoalViewModelR(val repository: GoalRepository) : ViewModel() {
    private val _goals = MutableLiveData<List<GoalWithSubGoals>>()
    val goals: LiveData<List<GoalWithSubGoals>> = _goals
    var selectedGoalWithSubGoals: GoalWithSubGoals? = null

    fun loadGoals() {
        viewModelScope.launch {
            _goals.value = repository.getAllGoalsWithSubGoals()
        }
    }

    fun addGoal(goal: GoalR) {
        viewModelScope.launch {
            repository.insertGoal(goal)
            loadGoals()
        }
    }

    /*fun addGoalWithSubGoals(goal: GoalR, subGoals: List<SubGoalR>) {
        viewModelScope.launch {
            val goalId = repository.insertGoal(goal).toInt()
            val subGoalsWithGoalId = subGoals.map { it.copy(goalId = goalId) }
            repository.insertSubGoals(subGoalsWithGoalId)
            loadGoals()
        }
    }*/
    fun addGoalWithSubGoals(goal: GoalR, subGoals: List<SubGoalR>) {
        viewModelScope.launch {
            val goalId = repository.insertGoal(goal)
            val updatedSubGoals = subGoals.map { it.copy(goalId = goalId.toInt()) }
            repository.insertSubGoals(updatedSubGoals)
        }
    }


    fun updateSubGoal(subGoal: SubGoalR) {
        viewModelScope.launch {
            repository.updateSubGoal(subGoal)
            loadGoals()
        }
    }

    fun updateGoal(goal: GoalR) {
        viewModelScope.launch {
            repository.updateGoal(goal)
            loadGoals()
        }
    }

    fun updateGoalWithSubGoals(goal: GoalR, subGoals: List<SubGoalR>) {
        viewModelScope.launch {
            repository.updateGoal(goal)
            subGoals.forEach { repository.updateSubGoal(it) }
            loadGoals()
        }
    }

    fun deleteGoal(goal: GoalR) {
        viewModelScope.launch {
            repository.deleteGoal(goal)
            loadGoals()
        }
    }
}

// ——— Factory ———

class GoalViewModelFactory(private val repository: GoalRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GoalViewModelR::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GoalViewModelR(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
