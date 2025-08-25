package io.github.hanihashemi.tomaten.data.model

import kotlin.random.Random

data class SharedTag(
    val id: String = generateId(),
    val name: String,
    val color: SharedTagColor,
    val createdAt: Long = getCurrentTimeMillis(),
) {
    companion object {
        const val MAX_NAME_LENGTH = 20

        fun isValidName(name: String): Boolean {
            val trimmed = name.trim()
            return trimmed.isNotEmpty() && trimmed.length <= MAX_NAME_LENGTH
        }

        private fun generateId(): String {
            // Simple UUID generation for multiplatform
            return "${Random.nextLong().toString(16)}-${Random.nextLong().toString(16)}"
        }

        private fun getCurrentTimeMillis(): Long {
            // This will need platform-specific implementation
            return 0L // Placeholder
        }
    }
}

enum class SharedTagColor(val colorValue: Long, val displayName: String) {
    RED(0xFFE57373, "Red"),
    ORANGE(0xFFFFB74D, "Orange"),
    GREEN(0xFF81C784, "Green"),
    BLUE(0xFF64B5F6, "Blue"),
    PURPLE(0xFFBA68C8, "Purple"),
    TEAL(0xFF4DB6AC, "Teal"),
    ;

    companion object {
        val DEFAULT = TEAL

        fun fromOrdinal(ordinal: Int): SharedTagColor {
            return entries.getOrElse(ordinal) { DEFAULT }
        }
    }
}