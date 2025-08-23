package io.github.hanihashemi.tomaten.ui.states

import io.github.hanihashemi.tomaten.data.model.Tag
import io.github.hanihashemi.tomaten.data.model.TagColor

data class TagUiState(
    val tags: List<Tag> = emptyList(),
    val selectedTagId: String? = null,
    val isLoading: Boolean = false,
    val isSelectDialogVisible: Boolean = false,
    val isCreateDialogVisible: Boolean = false,
    val createTagName: String = "",
    val createTagColor: TagColor = TagColor.DEFAULT,
    val createTagNameError: String? = null,
) {
    val selectedTag: Tag? get() = tags.find { it.id == selectedTagId }
    val isConfirmEnabled: Boolean get() = selectedTagId != null

    val isCreateTagNameValid: Boolean get() = Tag.isValidName(createTagName)
    val isCreateConfirmEnabled: Boolean get() =
        isCreateTagNameValid && !tags.any { it.name.equals(createTagName.trim(), ignoreCase = true) }
}
