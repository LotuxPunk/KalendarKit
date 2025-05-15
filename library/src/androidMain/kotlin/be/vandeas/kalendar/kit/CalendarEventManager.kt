package be.vandeas.kalendar.kit

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
     */
    actual suspend fun createEvent(
        value: Event
    ): Boolean {
        // Compute event start and end times in milliseconds.
        val startTime = value.startDate.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        val endTime = value.endDate.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()

        val notes = buildString {
            value.url?.let {
                append("URL: $it\n")
            }
            value.notes?.let {
                append(it)
            }
        }

        // Create an intent to insert a new calendar event.
        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, value.title)
            putExtra(CalendarContract.Events.DESCRIPTION, notes)
            putExtra(CalendarContract.Events.EVENT_LOCATION, value.location)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime)
            // You can add more extras here as needed.
        }

        // Make sure there's an app to handle this intent.
        return if (intent.resolveActivity(context.packageManager) != null) {
            // If you're calling this from an Activity, you can directly invoke startActivity.
            // If you’re using a non-Activity context, consider adding FLAG_ACTIVITY_NEW_TASK.
            context.startActivity(intent)
            true
        } else {
            // No calendar application found to handle the intent.
            false
        }
    }
}
