package be.vandeas.kalendar.kit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

actual class CalendarEventManager {
    private lateinit var context: Context

    fun setup(context: Context) {
        this.context = context
    }

    /**
     * Opens the system calendar event editor with pre-populated event data,
     * allowing the user to review and save the event.
     *
     * @param value The event data to be added to the calendar.
     */
    actual suspend fun createEvent(value: Event): Boolean {
        val startTime = value.startDate.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        val endTime = value.endDate.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()

        val notes = buildString {
            value.url?.let { append("URL: $it\n") }
            value.notes?.let { append(it) }
        }

        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, value.title)
            putExtra(CalendarContract.Events.DESCRIPTION, notes)
            putExtra(CalendarContract.Events.EVENT_LOCATION, value.location)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime)
        }

        if (context !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        val activities = context.packageManager.queryIntentActivities(intent, 0)
        if (activities.isNotEmpty()) {
            context.startActivity(intent)
            return true
        }
        return false
    }
}
