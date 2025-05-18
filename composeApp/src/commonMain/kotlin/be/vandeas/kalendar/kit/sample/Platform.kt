package be.vandeas.kalendar.kit.sample

import be.vandeas.kalendar.kit.CalendarEventManager

expect object Platform {
    val calendarEventManager: CalendarEventManager
}
