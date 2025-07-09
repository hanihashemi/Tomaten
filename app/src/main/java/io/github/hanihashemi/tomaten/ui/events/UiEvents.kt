package io.github.hanihashemi.tomaten.ui.events

sealed interface UiEvents {

    data object Login : UiEvents
}