package be.vandeas.kalendar.kit

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.sync.Mutex
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toNSDate
import platform.darwin.NSObject
import platform.EventKit.EKEventStore
import platform.EventKit.EKEvent
import platform.EventKit.EKEntityType
import platform.EventKitUI.EKEventEditViewController
import platform.EventKitUI.EKEventEditViewDelegateProtocol
import platform.Foundation.NSURL
import platform.UIKit.UIViewController
import kotlin.apply
import kotlin.let
import kotlin.takeIf
import kotlin.time.ExperimentalTime

actual class CalendarEventManager {
    private val eventStore = EKEventStore()
    private lateinit var presentingViewController: UIViewController

    @OptIn(ExperimentalForeignApi::class)
    private suspend fun requestCalendarAccess(): Boolean {
        // Request access from the user.
        var granted = false
        val semaphore = Mutex(locked = true)
        eventStore.requestAccessToEntityType(EKEntityType.EKEntityTypeEvent) { accessGranted, _ ->
            granted = accessGranted
            semaphore.unlock()
        }
        semaphore.lock() // In production you’d want to use a proper suspend mechanism.
        return granted
    }

    /**
     * Sets the UIViewController that will present the modal.
     *
     * @param viewController The UIViewController that will present the modal.
     */
    fun setPresentingViewController(viewController: UIViewController) {
        this.presentingViewController = viewController
    }

    /**
     * Presents a modal EKEventEditViewController that allows the user to review and possibly edit the
     * event details before deciding to save it.
     *
     * @param value The event to be created.
     * @param presentingViewController The UIViewController that will present the modal.
     *
     * @return true if the modal was presented; false otherwise.
     */
    @OptIn(ExperimentalForeignApi::class, ExperimentalTime::class)
    actual suspend fun createEvent(
        value: Event,
    ): Boolean = requestCalendarAccess().takeIf { it }?.let {
        val startTime = value.startDate.toInstant(TimeZone.currentSystemDefault()).toNSDate()
        val endTime = (value.endDate.toInstant(TimeZone.currentSystemDefault())).toNSDate()

        val ekEvent = EKEvent.eventWithEventStore(eventStore).apply {
            title = value.title
            startDate = startTime
            endDate = endTime
            location = value.location
            notes = value.notes
            calendar = eventStore.defaultCalendarForNewEvents
            URL = value.url?.let { NSURL(string = it) }
        }

        val eventEditVC = EKEventEditViewController().apply {
            event = ekEvent
            eventStore = this@CalendarEventManager.eventStore
            editViewDelegate = object : NSObject(), EKEventEditViewDelegateProtocol {
                override fun eventEditViewController(
                    controller: EKEventEditViewController,
                    didCompleteWithAction: Long
                ) {
                    controller.dismissViewControllerAnimated(true, null)
                }
            }
        }

        presentingViewController.presentViewController(eventEditVC, true, null)
        true
    } == true
}
