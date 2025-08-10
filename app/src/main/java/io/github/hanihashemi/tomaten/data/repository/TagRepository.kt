package io.github.hanihashemi.tomaten.data.repository

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import io.github.hanihashemi.tomaten.data.model.Tag
import io.github.hanihashemi.tomaten.data.model.TagColor
import kotlinx.coroutines.tasks.await
import timber.log.Timber

/**
 * Firestore representation of a Tag for database operations.
 */
data class FirestoreTag(
    @DocumentId val id: String = "",
    val name: String = "",
    val color: String = "",
    val createdAt: Long = 0L,
)

/**
 * Repository for managing tags in Firestore.
 * Handles CRUD operations for user tags with default tag initialization.
 */
class TagRepository : BaseFirebaseRepository() {
    companion object {
        private val DEFAULT_TAGS =
            listOf(
                Tag(name = "Focus", color = TagColor.DEFAULT),
                Tag(name = "Work", color = TagColor.BLUE),
                Tag(name = "Study", color = TagColor.GREEN),
                Tag(name = "Exercise", color = TagColor.ORANGE),
                Tag(name = "Reading", color = TagColor.PURPLE),
                Tag(name = "Personal", color = TagColor.RED),
            )

        val DEFAULT_FOCUS_TAG_ID = DEFAULT_TAGS.first().id
    }

    /**
     * Retrieves all tags for the current user.
     * Returns default tags for unauthenticated users or if no user tags exist.
     *
     * @return List of tags sorted by creation time
     */
    suspend fun getTags(): List<Tag> {
        return try {
            executeIfAuthenticated(
                operation = { userId, firestore ->
                    val tagsCollection =
                        firestore
                            .collection(COLLECTION_USERS)
                            .document(userId)
                            .collection(COLLECTION_TAGS)

                    val snapshot = tagsCollection.get().await()
                    val tags =
                        snapshot.documents.mapNotNull { doc ->
                            doc.toObject(FirestoreTag::class.java)?.toTag()
                        }

                    // If user has no tags, initialize with defaults
                    if (tags.isEmpty()) {
                        initializeDefaultTags(userId, firestore)
                        getDefaultTags()
                    } else {
                        tags.sortedBy { it.createdAt }
                    }
                },
                fallback = { skipReason ->
                    Timber.d("getTags skipped: ${getSkipReason(skipReason)}")
                    getDefaultTags()
                },
            )
        } catch (e: Exception) {
            Timber.e(e, "Error getting tags")
            getDefaultTags()
        }
    }

    /**
     * Adds a new tag to the user's collection.
     * Returns the tag ID for both successful saves and fallback scenarios.
     *
     * @param tag The tag to add
     * @return The tag ID
     */
    suspend fun addTag(tag: Tag): String {
        return try {
            executeIfAuthenticated(
                operation = { userId, firestore ->
                    val firestoreTag = tag.toFirestoreTag()

                    firestore
                        .collection(COLLECTION_USERS)
                        .document(userId)
                        .collection(COLLECTION_TAGS)
                        .document(tag.id)
                        .set(firestoreTag)
                        .await()

                    Timber.d("Tag added successfully: ${tag.name}")
                    tag.id
                },
                fallback = { skipReason ->
                    Timber.d("addTag skipped: ${getSkipReason(skipReason)}")
                    tag.id
                },
            )
        } catch (e: Exception) {
            Timber.e(e, "Error adding tag: ${tag.name}")
            tag.id
        }
    }

    /**
     * Deletes a tag from the user's collection.
     * Always returns true for consistency, even in fallback scenarios.
     *
     * @param tagId The ID of the tag to delete
     * @return True if deletion succeeded or was skipped
     */
    suspend fun deleteTag(tagId: String): Boolean {
        return try {
            executeIfAuthenticated(
                operation = { userId, firestore ->
                    firestore
                        .collection(COLLECTION_USERS)
                        .document(userId)
                        .collection(COLLECTION_TAGS)
                        .document(tagId)
                        .delete()
                        .await()

                    Timber.d("Tag deleted successfully: $tagId")
                    true
                },
                fallback = { skipReason ->
                    Timber.d("deleteTag skipped: ${getSkipReason(skipReason)}")
                    true
                },
            )
        } catch (e: Exception) {
            Timber.e(e, "Error deleting tag: $tagId")
            false
        }
    }

    /**
     * Initializes default tags for a new user.
     * Uses batch write for better performance.
     *
     * @param userId The user ID to initialize tags for
     * @param firestore The Firestore instance to use
     */
    private suspend fun initializeDefaultTags(
        userId: String,
        firestore: FirebaseFirestore,
    ) {
        try {
            val batch = firestore.batch()
            val userTagsRef =
                firestore
                    .collection(COLLECTION_USERS)
                    .document(userId)
                    .collection(COLLECTION_TAGS)

            DEFAULT_TAGS.forEach { tag ->
                val firestoreTag = tag.toFirestoreTag()
                val tagRef = userTagsRef.document(tag.id)
                batch.set(tagRef, firestoreTag)
            }

            batch.commit().await()
            Timber.d("Default tags initialized for user: $userId")
        } catch (e: Exception) {
            Timber.e(e, "Error initializing default tags")
        }
    }

    /**
     * Returns the default tags list.
     * Used for unauthenticated users and as fallback.
     */
    private fun getDefaultTags(): List<Tag> = DEFAULT_TAGS

    /**
     * Gets the default Focus tag ID.
     * Used for pre-selecting the Focus tag on app startup.
     */
    fun getDefaultFocusTagId(): String = DEFAULT_FOCUS_TAG_ID

    /**
     * Converts a domain Tag to Firestore representation.
     */
    private fun Tag.toFirestoreTag() =
        FirestoreTag(
            id = id,
            name = name,
            color = color.name,
            createdAt = createdAt,
        )

    /**
     * Converts a Firestore tag to domain representation.
     * Falls back to DEFAULT color if the stored color is invalid.
     */
    private fun FirestoreTag.toTag() =
        Tag(
            id = id,
            name = name,
            color = TagColor.entries.find { it.name == color } ?: TagColor.DEFAULT,
            createdAt = createdAt,
        )
}
