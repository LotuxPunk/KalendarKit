package be.vandeas.kalendar.kit.sample

import be.vandeas.kalendar.kit.CalendarEventManager
import platform.UIKit.UIViewController

actual object Platform {
    actual val calendarEventManager: CalendarEventManager = CalendarEventManager()

    fun initializeCalendarEventManager(viewController: UIViewController) {
        calendarEventManager.setPresentingViewController(viewController)
    }
}
