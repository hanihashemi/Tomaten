package io.github.hanihashemi.tomaten.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.github.hanihashemi.tomaten.data.model.TimerSession
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.Date

class TimerSessionRepository {
    private val firestore: FirebaseFirestore? by lazy {
        try {
            FirebaseFirestore.getInstance()
        } catch (e: Exception) {
            Timber.w("Firebase Firestore not available: ${e.message}")
            null
        }
    }

    private val auth: FirebaseAuth? by lazy {
        try {
            FirebaseAuth.getInstance()
        } catch (e: Exception) {
            Timber.w("Firebase Auth not available: ${e.message}")
            null
        }
    }

    private fun isFirebaseAvailable(): Boolean {
        return try {
            // Simply check if we can access Firebase instances
            firestore != null && auth != null
        } catch (e: Exception) {
            Timber.w("Firebase not available: ${e.message}")
            false
        }
    }

    companion object {
        private const val COLLECTION_USERS = "users"
        private const val COLLECTION_TIMER_SESSIONS = "timer_sessions"

        // Skip reasons for better consistency and maintainability
        const val SKIP_NO_FIREBASE = "skipped-firebase-unavailable"
        const val SKIP_NO_AUTH = "skipped-user-anonymous"
        const val SKIP_NO_FIRESTORE = "skipped-firestore-unavailable"

        fun isSkippedOperation(sessionId: String): Boolean {
            return sessionId.startsWith("skipped-")
        }

        fun getSkipReason(sessionId: String): String? {
            return when (sessionId) {
                SKIP_NO_FIREBASE -> "Firebase not initialized"
                SKIP_NO_AUTH -> "User not logged in"
                SKIP_NO_FIRESTORE -> "Firestore not available"
                else -> if (isSkippedOperation(sessionId)) "Unknown skip reason" else null
            }
        }
    }

    suspend fun saveTimerSession(
        startTime: Date,
        duration: Long,
        targetDuration: Long,
        completed: Boolean,
    ): Result<String> {
        return try {
            // Early returns for cases where we can't save
            if (!isFirebaseAvailable()) {
                Timber.d("Firebase not available - skipping timer session save")
                return Result.success(SKIP_NO_FIREBASE)
            }

            val firestoreInstance =
                firestore ?: run {
                    Timber.d("Firestore instance not available - skipping timer session save")
                    return Result.success(SKIP_NO_FIRESTORE)
                }

            val userId =
                auth?.currentUser?.uid ?: run {
                    Timber.d("User not logged in - skipping timer session save")
                    return Result.success(SKIP_NO_AUTH)
                }

            val session =
                TimerSession(
                    startTime = startTime,
                    duration = duration,
                    targetDuration = targetDuration,
                    completed = completed,
                )

            val documentRef =
                firestoreInstance
                    .collection(COLLECTION_USERS)
                    .document(userId)
                    .collection(COLLECTION_TIMER_SESSIONS)
                    .add(session)
                    .await()

            Timber.d("Timer session saved with ID: ${documentRef.id}")
            Result.success(documentRef.id)
        } catch (e: Exception) {
            Timber.e(e, "Failed to save timer session")
            Result.failure(e)
        }
    }

    suspend fun getUserTimerSessions(limit: Int = 50): Result<List<TimerSession>> {
        return try {
            if (!isFirebaseAvailable()) {
                Timber.d("Firebase not available - returning empty timer sessions list")
                return Result.success(emptyList())
            }

            val userId = auth?.currentUser?.uid
            if (userId == null) {
                Timber.d("User not logged in - returning empty timer sessions list")
                return Result.success(emptyList())
            }

            val firestoreInstance = firestore
            if (firestoreInstance == null) {
                Timber.d("Firestore instance not available - returning empty timer sessions list")
                return Result.success(emptyList())
            }

            val querySnapshot =
                firestoreInstance
                    .collection(COLLECTION_USERS)
                    .document(userId)
                    .collection(COLLECTION_TIMER_SESSIONS)
                    .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
                    .limit(limit.toLong())
                    .get()
                    .await()

            val sessions =
                querySnapshot.documents.mapNotNull { document ->
                    try {
                        document.toObject(TimerSession::class.java)
                    } catch (e: Exception) {
                        Timber.e(e, "Failed to parse timer session: ${document.id}")
                        null
                    }
                }

            Result.success(sessions)
        } catch (e: Exception) {
            Timber.e(e, "Failed to fetch user timer sessions")
            Result.failure(e)
        }
    }

    suspend fun getTotalUserStats(): Result<UserTimerStats> {
        return try {
            if (!isFirebaseAvailable()) {
                Timber.d("Firebase not available - returning empty timer stats")
                return Result.success(UserTimerStats())
            }

            val userId = auth?.currentUser?.uid
            if (userId == null) {
                Timber.d("User not logged in - returning empty timer stats")
                return Result.success(UserTimerStats())
            }

            val firestoreInstance = firestore
            if (firestoreInstance == null) {
                Timber.d("Firestore instance not available - returning empty timer stats")
                return Result.success(UserTimerStats())
            }

            val querySnapshot =
                firestoreInstance
                    .collection(COLLECTION_USERS)
                    .document(userId)
                    .collection(COLLECTION_TIMER_SESSIONS)
                    .get()
                    .await()

            val sessions =
                querySnapshot.documents.mapNotNull { document ->
                    try {
                        document.toObject(TimerSession::class.java)
                    } catch (e: Exception) {
                        null
                    }
                }

            val totalSessions = sessions.size
            val completedSessions = sessions.count { it.completed }
            val totalDuration = sessions.sumOf { it.duration }
            val averageDuration = if (sessions.isNotEmpty()) totalDuration / sessions.size else 0L

            val stats =
                UserTimerStats(
                    totalSessions = totalSessions,
                    completedSessions = completedSessions,
                    totalDuration = totalDuration,
                    averageDuration = averageDuration,
                )

            Result.success(stats)
        } catch (e: Exception) {
            Timber.e(e, "Failed to fetch user timer stats")
            Result.failure(e)
        }
    }
}

data class UserTimerStats(
    val totalSessions: Int = 0,
    val completedSessions: Int = 0,
    // Total duration in seconds
    val totalDuration: Long = 0,
    // Average duration in seconds
    val averageDuration: Long = 0,
) {
    val completionRate: Float
        get() = if (totalSessions > 0) completedSessions.toFloat() / totalSessions else 0f
}
