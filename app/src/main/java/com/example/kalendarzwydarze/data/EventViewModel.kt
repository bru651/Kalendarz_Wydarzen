package com.example.kalendarzwydarze.data

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
//import com.example.kalendarzwydarze.data.model.event
import kotlin.random.Random

data class Event(

    var content: String,
    var month: Int,
    var day: Int,
    val id: Int = Random.nextInt()
)

class EventViewModel : ViewModel() {
    // Mutable list to manage events dynamically
    private val _eventList = mutableStateListOf<Event>()
    val eventList: List<Event> get() = _eventList

    init {
        /// New Year
        _eventList.add(Event("New Year", 1, 1))

        // Valentine's Day
        _eventList.add(Event("Valentine's Day", 2, 14))

        // International Women's Day
        _eventList.add(Event("International Women's Day", 3, 8))

        // Easter Sunday (date may vary)
        _eventList.add(Event("Easter Sunday", 4, 17)) // Adjust the date as needed

        // Labor Day (May 1st)
        _eventList.add(Event("Labor Day", 5, 1))

        // Corpus Christi (date may vary)
        _eventList.add(Event("Corpus Christi", 6, 16)) // Adjust the date as needed

        // Polish Independence Day (November 11th)
        _eventList.add(Event("Independence Day (Poland)", 11, 11))

        // Christmas Eve
        _eventList.add(Event("Christmas Eve", 12, 24))

        // Christmas
        _eventList.add(Event("Christmas", 12, 25))

        // Second Christmas Day (December 26th, Polish holiday)
        _eventList.add(Event("Second Christmas Day", 12, 26))

        // International Men's Day
        _eventList.add(Event("International Men's Day", 11, 19))

        // Thanksgiving (fourth Thursday in November, US)
        _eventList.add(Event("Thanksgiving", 11, 24)) // Adjust the date as needed

        // Hanukkah (date may vary)
        _eventList.add(Event("Hanukkah", 12, 18)) // Adjust the date as needed

        // Kwanzaa (December 26th to January 1st)
        _eventList.add(Event("Kwanzaa", 12, 26))

        // April Fools' Day
        _eventList.add(Event("April Fools' Day", 4, 1))

        // Earth Day
        _eventList.add(Event("Earth Day", 4, 22))

        // Star Wars Day (May the 4th)
        _eventList.add(Event("Star Wars Day", 5, 4))

        // International Nurses Day
        _eventList.add(Event("International Nurses Day", 5, 12))

        // World Environment Day
        _eventList.add(Event("World Environment Day", 6, 5))

        // World Emoji Day
        _eventList.add(Event("World Emoji Day", 7, 17))

        // International Cat Day
        _eventList.add(Event("International Cat Day", 8, 8))

        // International Dog Day
        _eventList.add(Event("International Dog Day", 8, 26))

        // International Literacy Day
        _eventList.add(Event("International Literacy Day", 9, 8))

        // World Smile Day (first Friday in October)
        _eventList.add(Event("World Smile Day", 10, 7)) // Adjust the date as needed

        // Halloween
        _eventList.add(Event("Halloween", 10, 31))

        // World Kindness Day
        _eventList.add(Event("World Kindness Day", 11, 13))

        // Cyber Monday (first Monday after Thanksgiving, US)
        _eventList.add(Event("Cyber Monday", 11, 28)) // Adjust the date as needed

        // Giving Tuesday (first Tuesday after Thanksgiving, US)
        _eventList.add(Event("Giving Tuesday", 11, 29)) // Adjust the date as needed

        // Human Rights Day
        _eventList.add(Event("Human Rights Day", 12, 10))

        // Test
        //eventlist.add(Event("TEST", 1, 1))

        // Test
        _eventList.add(Event("Wielki Piątek", 3, 25))

        // Zielone Świątki (another year)
        _eventList.add(Event("Zielone Świątki", 5, 24))

        // Wszystkich Świętych
        _eventList.add(Event("Wszystkich Świętych", 11, 1))

        // Wniebowzięcie Najświętszej Maryi Panny
        _eventList.add(Event("Wniebowzięcie Najświętszej Maryi Panny", 8, 15))

        // Święto Konstytucji Trzeciego Maja
        _eventList.add(Event("Święto Konstytucji Trzeciego Maja", 5, 3))
    }

    // Function to add a new event
    fun addEvent(newEvent: Event) {
        _eventList.add(newEvent)
    }

    // Function to remove an event (if needed)
    fun removeEvent(event: Event) {
        _eventList.remove(event)
    }

    fun updateEvent(updatedEvent: Event) {
        val index = _eventList.indexOfFirst { it.id == updatedEvent.id }
        if (index != -1) {
            _eventList[index] = updatedEvent
        }
    }

    fun deleteEvent(id: Int) {
        _eventList.removeAll { it.id == id }
    }
}
