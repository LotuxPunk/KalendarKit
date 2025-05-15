package be.vandeas.kalendar.kit

import kotlinx.datetime.LocalDateTime

data class Event(
    val title: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val notes: String? = null,
    val location: String? = null,
    val url: String? = null,
)
