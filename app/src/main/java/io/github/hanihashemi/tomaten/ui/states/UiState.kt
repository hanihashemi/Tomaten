package io.github.hanihashemi.tomaten.ui.states

import io.github.hanihashemi.tomaten.User
import java.util.Date

data class UIState(
    val login: LoginUiState = LoginUiState(),
    val timer: TimerUiState = TimerUiState(),
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

data class TimerUiState(
    val isRunning: Boolean = false,
    val timeRemaining: Long = 15 * 60,
    val timeLimit: Long = 15 * 60,
    val isDialogVisible: Boolean = false,
    val startTime: Date? = null,
)
