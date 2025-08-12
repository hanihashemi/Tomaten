package io.github.hanihashemi.tomaten.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class TimerSession(
    @DocumentId
    val id: String = "",
    val startTime: Date = Date(),
    // Duration in seconds
    val duration: Long = 0L,
    // Planned duration in seconds
    val targetDuration: Long = 0L,
    // Whether the timer was completed or stopped early
    val completed: Boolean = false,
    // Tag ID associated with this timer session
    val tagId: String? = null,
    @ServerTimestamp
    val createdAt: Date? = null,
)
