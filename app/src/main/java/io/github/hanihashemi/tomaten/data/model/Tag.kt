package io.github.hanihashemi.tomaten.data.model

import androidx.compose.ui.graphics.Color
import java.util.UUID

data class Tag(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val color: TagColor,
    val createdAt: Long = System.currentTimeMillis(),
) {
    companion object {
        const val MAX_NAME_LENGTH = 20

        fun isValidName(name: String): Boolean {
            val trimmed = name.trim()
            return trimmed.isNotEmpty() && trimmed.length <= MAX_NAME_LENGTH
        }
    }
}

enum class TagColor(val displayColor: Color, val displayName: String) {
    RED(Color(0xFFE57373), "Red"),
    ORANGE(Color(0xFFFFB74D), "Orange"),
    GREEN(Color(0xFF81C784), "Green"),
    BLUE(Color(0xFF64B5F6), "Blue"),
    PURPLE(Color(0xFFBA68C8), "Purple"),
    TEAL(Color(0xFF4DB6AC), "Teal"),
    ;

    companion object {
        val DEFAULT = TEAL

        fun fromOrdinal(ordinal: Int): TagColor {
            return entries.getOrElse(ordinal) { DEFAULT }
        }
    }
}
