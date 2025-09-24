package be.vandeas.kalendar.kit.sample

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import be.vandeas.kalendar.kit.Event
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        val scope = rememberCoroutineScope()

        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = {
                    Event(
                        title = "Hello world!",
                        notes = "This is a test event",
                        startDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                        endDate = Clock.System.now().plus(1.hours).toLocalDateTime(TimeZone.currentSystemDefault()),
                        location = "Brussels",
                        url = "https://www.example.com",
                    ).let {
                        scope.launch {
                            Platform.calendarEventManager.createEvent(
                                it
                            )
                        }
                    }
                }
            ) {
                Text("Add the event !")
            }
        }
    }
}
