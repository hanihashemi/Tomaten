package io.github.hanihashemi.tomaten.viewmodel

import io.github.hanihashemi.tomaten.data.model.SharedTag
import io.github.hanihashemi.tomaten.data.model.SharedUser
import io.github.hanihashemi.tomaten.ui.states.SharedUIState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Platform-agnostic ViewModel for MainScreen
 * Contains all the shared logic that both Android and iOS can use
 */
open class SharedMainViewModel {
    // Create a coroutine scope for the ViewModel
    protected open val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    
    private val _uiState = MutableStateFlow(SharedUIState())
    val uiState: StateFlow<SharedUIState> = _uiState
    
    val actions: SharedMainActions by lazy { SharedMainActions(this) }

    init {
        initializeDefaultTimer()
    }

    private fun initializeDefaultTimer() {
        // Set default timer to 25 minutes (Pomodoro)
        updateState { state ->
            state.copy(
                timer = state.timer.copy(
                    timeRemaining = 25 * 60, // 25 minutes
                    timeLimit = 25 * 60
                )
            )
        }
    }

    fun updateState(transform: (SharedUIState) -> SharedUIState) {
        _uiState.update(transform)
    }

    // Timer Actions
    fun startOrStopTimer() {
        val currentState = _uiState.value.timer
        if (currentState.isRunning) {
            stopTimer()
        } else {
            startTimer()
        }
    }

    private fun startTimer() {
        updateState { state ->
            state.copy(
                timer = state.timer.copy(
                    isRunning = true,
                    startTime = getCurrentTimeMillis()
                )
            )
        }
        startTimerCountdown()
    }

    private fun stopTimer() {
        updateState { state ->
            state.copy(
                timer = state.timer.copy(
                    isRunning = false,
                    startTime = null
                )
            )
        }
    }

    private fun startTimerCountdown() {
        viewModelScope.launch {
            while (_uiState.value.timer.isRunning && _uiState.value.timer.timeRemaining > 0) {
                kotlinx.coroutines.delay(1000) // Wait 1 second
                updateState { state ->
                    val newTimeRemaining = (state.timer.timeRemaining - 1).coerceAtLeast(0)
                    state.copy(
                        timer = state.timer.copy(
                            timeRemaining = newTimeRemaining,
                            isRunning = newTimeRemaining > 0 && state.timer.isRunning
                        )
                    )
                }
            }
        }
    }

    fun setTimeLimit(timeInSeconds: Long) {
        updateState { state ->
            state.copy(
                timer = state.timer.copy(
                    timeLimit = timeInSeconds,
                    timeRemaining = timeInSeconds
                )
            )
        }
    }

    fun displayTimerDialog(visible: Boolean) {
        updateState { state ->
            state.copy(
                timer = state.timer.copy(isDialogVisible = visible)
            )
        }
    }

    // Tag Actions
    fun showTagSelectDialog() {
        updateState { state ->
            state.copy(
                tag = state.tag.copy(isSelectDialogVisible = true)
            )
        }
    }

    fun hideTagSelectDialog() {
        updateState { state ->
            state.copy(
                tag = state.tag.copy(isSelectDialogVisible = false)
            )
        }
    }

    fun selectTag(tagId: String?) {
        updateState { state ->
            state.copy(
                tag = state.tag.copy(selectedTagId = tagId)
            )
        }
    }

    // Login Actions
    fun showLoginDialog() {
        updateState { state ->
            state.copy(
                login = state.login.copy(isDialogVisible = true)
            )
        }
    }

    fun hideLoginDialog() {
        updateState { state ->
            state.copy(
                login = state.login.copy(isDialogVisible = false)
            )
        }
    }

    fun setUser(user: SharedUser?) {
        updateState { state ->
            state.copy(
                login = state.login.copy(user = user)
            )
        }
    }

    fun setTags(tags: List<SharedTag>) {
        updateState { state ->
            state.copy(
                tag = state.tag.copy(tags = tags)
            )
        }
    }

    // Platform-specific functions to be implemented by each platform
    protected open fun getCurrentTimeMillis(): Long = 0L

    // Clean up resources when the ViewModel is no longer needed
    open fun onCleared() {
        // Platform-specific cleanup can be implemented in subclasses
    }
}

/**
 * Actions class that provides a clean interface for UI interactions
 */
class SharedMainActions(private val viewModel: SharedMainViewModel) {
    val timer = TimerActions()
    val tag = TagActions()
    val login = LoginActions()

    inner class TimerActions {
        fun startOrStop() = viewModel.startOrStopTimer()
        fun setTimeLimit(timeInSeconds: Long) = viewModel.setTimeLimit(timeInSeconds)
        fun displayDialog(visible: Boolean) = viewModel.displayTimerDialog(visible)
    }

    inner class TagActions {
        fun showSelectDialog() = viewModel.showTagSelectDialog()
        fun hideSelectDialog() = viewModel.hideTagSelectDialog()
        fun selectTag(tagId: String?) = viewModel.selectTag(tagId)
    }

    inner class LoginActions {
        fun showDialog() = viewModel.showLoginDialog()
        fun hideDialog() = viewModel.hideLoginDialog()
        fun setUser(user: SharedUser?) = viewModel.setUser(user)
    }
}