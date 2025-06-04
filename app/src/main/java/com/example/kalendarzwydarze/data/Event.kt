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
data class EventR(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val content: String,
    val month: Int,
    val day: Int
)

@Dao
interface EventDao {
    @Query("SELECT * FROM EventR")
    suspend fun getAllEvents(): List<EventR>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventR): Long

    @Update
    suspend fun updateEvent(event: EventR)

    @Delete
    suspend fun deleteEvent(event: EventR)
}

class EventRepository(private val dao: EventDao) {
    suspend fun getAllEvents(): List<EventR> = dao.getAllEvents()
    suspend fun insertEvent(event: EventR) = dao.insertEvent(event)
    suspend fun updateEvent(event: EventR) = dao.updateEvent(event)
    suspend fun deleteEvent(event: EventR) = dao.deleteEvent(event)
}

class EventViewModelR(private val repository: EventRepository) : ViewModel() {
    private val _events = MutableLiveData<List<EventR>>()
    val events: LiveData<List<EventR>> = _events
    var selectedEvent: EventR? = null

    init {
        loadEvents()
    }

    fun loadEvents() {
        viewModelScope.launch {
            _events.value = repository.getAllEvents()
        }
    }

    fun addEvent(event: EventR) {
        viewModelScope.launch {
            repository.insertEvent(event)
            loadEvents()
        }
    }

    fun updateEvent(event: EventR) {
        viewModelScope.launch {
            repository.updateEvent(event)
            loadEvents()
        }
    }

    fun deleteEvent(event: EventR) {
        viewModelScope.launch {
            repository.deleteEvent(event)
            loadEvents()
        }
    }
}

class EventViewModelFactory(private val repository: EventRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventViewModelR::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EventViewModelR(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

