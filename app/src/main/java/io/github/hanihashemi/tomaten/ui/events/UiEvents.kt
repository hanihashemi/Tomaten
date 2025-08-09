package io.github.hanihashemi.tomaten.ui.events

sealed interface UiEvents {
    data object Login : UiEvents

    data object StopTimer : UiEvents

    data class StartTimer(
        val timeLimit: Long,
    ) : UiEvents
}
