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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(events: List<EventR>)

}

class EventRepository(private val dao: EventDao) {
    suspend fun getAllEvents(): List<EventR> = dao.getAllEvents()
    suspend fun insertEvent(event: EventR) = dao.insertEvent(event)
    suspend fun updateEvent(event: EventR) = dao.updateEvent(event)
    suspend fun deleteEvent(event: EventR) = dao.deleteEvent(event)
    suspend fun insertInitialEvents() {
        val predefined = listOf(
            EventR(content = "Nowy Rok", month = 1, day = 1),
            EventR(content = "Święto Trzech Króli", month = 1, day = 6),
            EventR(content = "Święto Pracy", month = 5, day = 1),
            EventR(content = "Święto Komuny", month = 5, day = 1),
            EventR(content = "Święto Konstytucji 3 Maja", month = 5, day = 3),
            EventR(content = "Wniebowzięcie NMP", month = 8, day = 15),
            EventR(content = "Wszystkich Świętych", month = 11, day = 1),
            EventR(content = "Święto Niepodległości", month = 11, day = 11),
            EventR(content = "Boże Narodzenie", month = 12, day = 25),
            EventR(content = "Drugi dzień Bożego Narodzenia", month = 12, day = 26)
        )
        dao.insertAll(predefined)
    }


}

class EventViewModelR(private val repository: EventRepository) : ViewModel() {
    private val _events = MutableLiveData<List<EventR>>()
    val events: LiveData<List<EventR>> = _events
    var selectedEvent: EventR? = null

    init {
        viewModelScope.launch {
            if (repository.getAllEvents().isEmpty()) {
                repository.insertInitialEvents()
            }
            loadEvents()
        }
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

