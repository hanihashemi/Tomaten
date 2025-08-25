package io.github.hanihashemi.tomaten.ui.actions

import androidx.lifecycle.viewModelScope
import io.github.hanihashemi.tomaten.MainViewModel
import io.github.hanihashemi.tomaten.data.model.Tag
import io.github.hanihashemi.tomaten.data.model.TagColor
import io.github.hanihashemi.tomaten.extensions.launchSafely
import kotlinx.coroutines.Dispatchers

class TagAction(private val viewModel: MainViewModel) {
    fun showSelectDialog() {
        // Set default Focus tag if no tag is currently selected
        val defaultTagId =
            if (viewModel.uiState.value.tag.selectedTagId == null) {
                viewModel.getDefaultFocusTagId()
            } else {
                viewModel.uiState.value.tag.selectedTagId
            }

        viewModel.updateState { state ->
            state.copy(
                tag =
                    state.tag.copy(
                        isSelectDialogVisible = true,
                        selectedTagId = defaultTagId,
                    ),
            )
        }
        // Load tags when dialog opens
        loadTags()
    }

    fun hideSelectDialog() {
        viewModel.updateState { state ->
            state.copy(
                tag =
                    state.tag.copy(
                        isSelectDialogVisible = false,
                        isCreateDialogVisible = false,
                    ),
            )
        }
    }

    fun selectTag(tagId: String?) {
        val currentSelectedId = viewModel.uiState.value.tag.selectedTagId
        val newSelectedId = if (currentSelectedId == tagId) null else tagId

        viewModel.updateState { state ->
            state.copy(
                tag = state.tag.copy(selectedTagId = newSelectedId),
            )
        }
    }

    fun confirmTagSelection() {
        val selectedTag = viewModel.uiState.value.tag.selectedTag
        if (selectedTag != null) {
            // TODO: Save selected tag to session/preferences
            hideSelectDialog()
        }
    }

    fun showCreateDialog() {
        viewModel.updateState { state ->
            state.copy(
                tag =
                    state.tag.copy(
                        isCreateDialogVisible = true,
                        createTagName = "",
                        createTagColor = TagColor.DEFAULT,
                        createTagNameError = null,
                    ),
            )
        }
    }

    fun hideCreateDialog() {
        viewModel.updateState { state ->
            state.copy(
                tag =
                    state.tag.copy(
                        isCreateDialogVisible = false,
                        createTagName = "",
                        createTagNameError = null,
                    ),
            )
        }
    }

    fun updateCreateTagName(name: String) {
        val trimmedName = name.take(Tag.MAX_NAME_LENGTH)
        val currentTags = viewModel.uiState.value.tag.tags

        val error =
            when {
                trimmedName.trim().isEmpty() -> "Tag name cannot be empty"
                !Tag.isValidName(trimmedName) -> "Tag name must be 1-${Tag.MAX_NAME_LENGTH} characters"
                currentTags.any { it.name.equals(trimmedName.trim(), ignoreCase = true) } ->
                    "A tag with this name already exists"
                else -> null
            }

        viewModel.updateState { state ->
            state.copy(
                tag =
                    state.tag.copy(
                        createTagName = trimmedName,
                        createTagNameError = error,
                    ),
            )
        }
    }

    fun updateCreateTagColor(color: TagColor) {
        viewModel.updateState { state ->
            state.copy(
                tag = state.tag.copy(createTagColor = color),
            )
        }
    }

    fun createTag() {
        val tagState = viewModel.uiState.value.tag
        if (!tagState.isCreateConfirmEnabled) return

        val newTag =
            Tag(
                name = tagState.createTagName.trim(),
                color = tagState.createTagColor,
            )

        viewModel.viewModelScope.launchSafely(Dispatchers.IO) {
            val savedTagId = viewModel.addTag(newTag)

            viewModel.updateState { state ->
                state.copy(
                    tag =
                        state.tag.copy(
                            tags = state.tag.tags + newTag.copy(id = savedTagId),
                            selectedTagId = savedTagId,
                            isCreateDialogVisible = false,
                            createTagName = "",
                            createTagNameError = null,
                        ),
                )
            }
        }
    }

    fun deleteTag(tagId: String) {
        viewModel.viewModelScope.launchSafely(Dispatchers.IO) {
            val success = viewModel.deleteTag(tagId)
            if (success) {
                viewModel.updateState { state ->
                    state.copy(
                        tag =
                            state.tag.copy(
                                tags = state.tag.tags.filter { it.id != tagId },
                                selectedTagId = if (state.tag.selectedTagId == tagId) null else state.tag.selectedTagId,
                            ),
                    )
                }
            }
        }
    }

    fun manageTags() {
        // TODO: Navigate to manage tags screen
        // This would emit an event to the parent or navigate to a new screen
    }

    private fun loadTags() {
        viewModel.viewModelScope.launchSafely(Dispatchers.IO) {
            val tags = viewModel.getTags()

            viewModel.updateState { state ->
                state.copy(
                    tag = state.tag.copy(tags = tags),
                )
            }
        }
    }
}
