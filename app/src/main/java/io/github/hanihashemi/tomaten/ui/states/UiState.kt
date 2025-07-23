package io.github.hanihashemi.tomaten.ui.states

import io.github.hanihashemi.tomaten.User

data class UIState(
    val login: LoginUiState = LoginUiState(),
)

data class LoginUiState(
    val isLoading: Boolean = false,
    val isDialogVisible: Boolean = false,
    val errorMessage: String? = null,
    val user: User? = null,
) {
    val userDisplayName: String?
        get() = user?.name

    val isLoggedIn: Boolean
        get() = user != null
}
