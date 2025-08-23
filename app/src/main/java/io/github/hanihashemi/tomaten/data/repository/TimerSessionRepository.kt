package io.github.hanihashemi.tomaten.data.repository

import co.touchlab.kermit.Logger
import io.github.hanihashemi.tomaten.data.model.TimerSession
import kotlinx.coroutines.tasks.await
import java.util.Date

/**
 * Repository for managing timer sessions in Firestore.
 * Handles saving timer session data with associated tag information.
 */
class TimerSessionRepository : BaseFirebaseRepository() {
    /**
     * Saves a timer session to Firestore.
     * Only saves for authenticated non-anonymous users.
     *
     * @param startTime When the timer was started
     * @param duration Actual duration the timer ran (in seconds)
     * @param targetDuration Planned duration for the timer (in seconds)
     * @param completed Whether the timer was completed or stopped early
     * @param tagId ID of the tag associated with this session (optional)
     * @return Result with session ID or skip reason
     */
    suspend fun saveTimerSession(
        startTime: Date,
        duration: Long,
        targetDuration: Long,
        completed: Boolean,
        tagId: String? = null,
    ): Result<String> {
        return try {
            executeIfAuthenticated(
                operation = { userId, firestore ->
                    val session =
                        TimerSession(
                            startTime = startTime,
                            duration = duration,
                            targetDuration = targetDuration,
                            completed = completed,
                            tagId = tagId,
                        )

                    val documentRef =
                        firestore
                            .collection(COLLECTION_USERS)
                            .document(userId)
                            .collection(COLLECTION_TIMER_SESSIONS)
                            .add(session)
                            .run { await() }

                    Logger.d("Timer session saved with ID: ${documentRef.id}")
                    Result.success(documentRef.id)
                },
                fallback = { skipReason ->
                    Logger.d("Timer session save skipped: ${getSkipReason(skipReason)}")
                    Result.success(skipReason)
                },
            )
        } catch (e: Exception) {
            Logger.e("Failed to save timer session", e)
            Result.failure(e)
        }
    }
}
