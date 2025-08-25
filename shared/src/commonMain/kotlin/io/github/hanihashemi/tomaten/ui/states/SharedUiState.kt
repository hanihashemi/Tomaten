package io.github.hanihashemi.tomaten.ui.states

import io.github.hanihashemi.tomaten.data.model.SharedTag
import io.github.hanihashemi.tomaten.data.model.SharedTagColor
import io.github.hanihashemi.tomaten.data.model.SharedUser

data class SharedUIState(
    val login: SharedLoginUiState = SharedLoginUiState(),
    val timer: SharedTimerUiState = SharedTimerUiState(),
    val tag: SharedTagUiState = SharedTagUiState(),
)

data class SharedLoginUiState(
    val isLoading: Boolean = false,
    val isDialogVisible: Boolean = false,
    val errorMessage: String? = null,
    val user: SharedUser? = null,
) {
    val userDisplayName: String?
        get() = user?.name

    val isLoggedIn: Boolean
        get() = user != null
}

data class SharedTimerUiState(
    val isRunning: Boolean = false,
    val timeRemaining: Long = 15 * 60,
    val timeLimit: Long = 15 * 60,
    val isDialogVisible: Boolean = false,
    val startTime: Long? = null, // Using Long instead of Date for multiplatform compatibility
)

data class SharedTagUiState(
    val tags: List<SharedTag> = emptyList(),
    val selectedTagId: String? = null,
    val isLoading: Boolean = false,
    val isSelectDialogVisible: Boolean = false,
    val isCreateDialogVisible: Boolean = false,
    val createTagName: String = "",
    val createTagColor: SharedTagColor = SharedTagColor.DEFAULT,
    val createTagNameError: String? = null,
) {
    val selectedTag: SharedTag? get() = tags.find { it.id == selectedTagId }
    val isConfirmEnabled: Boolean get() = selectedTagId != null

    val isCreateTagNameValid: Boolean get() = SharedTag.isValidName(createTagName)
    val isCreateConfirmEnabled: Boolean get() =
        isCreateTagNameValid && !tags.any { it.name.equals(createTagName.trim(), ignoreCase = true) }
}