package be.vandeas.kalendar.kit.sample

import android.content.Context
import be.vandeas.kalendar.kit.CalendarEventManager

actual object Platform {
    actual val calendarEventManager: CalendarEventManager = CalendarEventManager()

    fun initializeCalendarEventManager(context: Context) {
        calendarEventManager.setup(context)
    }
}
