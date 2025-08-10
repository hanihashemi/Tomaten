package io.github.hanihashemi.tomaten.ui.events

import io.github.hanihashemi.tomaten.data.model.TagColor

sealed class TagEvent {
    data object ShowSelectDialog : TagEvent()

    data object HideSelectDialog : TagEvent()

    data class SelectTag(val tagId: String?) : TagEvent()

    data object ConfirmTagSelection : TagEvent()

    data object ShowCreateDialog : TagEvent()

    data object HideCreateDialog : TagEvent()

    data class UpdateCreateTagName(val name: String) : TagEvent()

    data class UpdateCreateTagColor(val color: TagColor) : TagEvent()

    data object CreateTag : TagEvent()

    data object ManageTags : TagEvent()
}
