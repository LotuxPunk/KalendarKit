package be.vandeas.kalendar.kit

expect class CalendarEventManager() {

    /**
     * Creates and saves a new calendar event.
     * Returns true if the event was successfully created.
     */
    suspend fun createEvent(
        value: Event
    ): Boolean
}
