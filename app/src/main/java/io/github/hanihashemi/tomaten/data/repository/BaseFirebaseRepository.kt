package io.github.hanihashemi.tomaten.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber

/**
 * Base repository class that provides common Firebase functionality for all repositories.
 * Handles Firebase initialization, user authentication, and provides consistent skip operations.
 */
abstract class BaseFirebaseRepository {
    protected val firestore: FirebaseFirestore? by lazy {
        try {
            FirebaseFirestore.getInstance()
        } catch (e: Exception) {
            Timber.w("Firebase Firestore not available: ${e.message}")
            null
        }
    }

    protected val auth: FirebaseAuth? by lazy {
        try {
            FirebaseAuth.getInstance()
        } catch (e: Exception) {
            Timber.w("Firebase Auth not available: ${e.message}")
            null
        }
    }

    companion object {
        // Skip reasons for consistent error handling across repositories
        const val SKIP_NO_FIREBASE = "skipped-firebase-unavailable"
        const val SKIP_NO_AUTH = "skipped-user-anonymous"
        const val SKIP_NO_FIRESTORE = "skipped-firestore-unavailable"

        // Collection names
        const val COLLECTION_USERS = "users"
        const val COLLECTION_TAGS = "tags"
        const val COLLECTION_TIMER_SESSIONS = "timer_sessions"

        fun isSkippedOperation(result: String): Boolean {
            return result.startsWith("skipped-")
        }

        fun getSkipReason(result: String): String? {
            return when (result) {
                SKIP_NO_FIREBASE -> "Firebase not initialized"
                SKIP_NO_AUTH -> "User not logged in"
                SKIP_NO_FIRESTORE -> "Firestore not available"
                else -> if (isSkippedOperation(result)) "Unknown skip reason" else null
            }
        }
    }

    /**
     * Gets the current user ID if the user is logged in and not anonymous.
     * Returns a skip reason if the user cannot be authenticated.
     */
    protected fun getCurrentUserId(): String {
        if (firestore == null) return SKIP_NO_FIRESTORE

        val authInstance = auth ?: return SKIP_NO_FIREBASE

        val currentUser = authInstance.currentUser
        return if (currentUser?.isAnonymous == false) {
            currentUser.uid
        } else {
            SKIP_NO_AUTH
        }
    }

    /**
     * Executes an operation only if the user is properly authenticated.
     * Returns the skip reason if authentication fails.
     */
    protected inline fun <T> executeIfAuthenticated(
        operation: (userId: String, firestore: FirebaseFirestore) -> T,
        fallback: (skipReason: String) -> T,
    ): T {
        val userId = getCurrentUserId()
        if (userId.startsWith("skipped-")) {
            return fallback(userId)
        }

        val firestoreInstance = firestore ?: return fallback(SKIP_NO_FIRESTORE)
        return operation(userId, firestoreInstance)
    }
}
