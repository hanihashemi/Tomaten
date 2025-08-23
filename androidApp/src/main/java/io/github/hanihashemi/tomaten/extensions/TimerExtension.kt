package io.github.hanihashemi.tomaten.extensions

import java.util.Locale

fun Long.formatTime(): String {
    val minutes = this / 60
    val remainingSeconds = this % 60
    return String.format(Locale.getDefault(), "%d:%02d", minutes, remainingSeconds)
}

fun Int.toSeconds(): Long = (this * 60).toLong()
