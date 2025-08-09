package io.github.hanihashemi.tomaten

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import io.github.hanihashemi.tomaten.data.repository.TimerSessionRepository
import io.github.hanihashemi.tomaten.extensions.launchSafely
import io.github.hanihashemi.tomaten.ui.actions.Actions
import io.github.hanihashemi.tomaten.ui.events.UiEvents
import io.github.hanihashemi.tomaten.ui.states.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import java.util.Date

open class MainViewModel(
    private val timerSessionRepository: TimerSessionRepository? = null,
    shouldFetchCurrentUser: Boolean = true,
) : ViewModel() {
    private val _uiEvents = MutableSharedFlow<UiEvents>(replay = 0)
    val uiEvents = _uiEvents.asSharedFlow()
    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState
    val actions: Actions by lazy { Actions(this) }

    // Timer session tracking
    private var sessionStartTime: Date? = null

    init {
        if (shouldFetchCurrentUser) {
            fetchCurrentUser()
        }
    }

    private fun fetchCurrentUser() {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            val savedUser =
                User(
                    name = user.displayName,
                    email = user.email,
                    photoUrl = user.photoUrl.toString(),
                    uid = user.uid,
                )
            updateState {
                it.copy(login = it.login.copy(user = savedUser))
            }
        }
    }

    fun updateState(transform: (UIState) -> UIState) {
        _uiState.update(transform)
    }

    fun sendEvent(event: UiEvents) {
        viewModelScope.launchSafely(Dispatchers.Main) {
            _uiEvents.emit(event)
        }
    }

    fun startTimerSession() {
        sessionStartTime = Date()
        Timber.d("Timer session started at: $sessionStartTime")
    }

    fun stopTimerSession(completed: Boolean = false) {
        val startTime = sessionStartTime
        if (startTime == null) {
            Timber.w("Cannot stop timer session - no start time recorded")
            return
        }

        val currentState = _uiState.value.timer
        val endTime = Date()
        val actualDuration = (endTime.time - startTime.time) / 1000 // Convert to seconds
        val targetDuration = currentState.timeLimit

        viewModelScope.launchSafely(Dispatchers.IO) {
            timerSessionRepository?.saveTimerSession(
                startTime = startTime,
                duration = actualDuration,
                targetDuration = targetDuration,
                completed = completed,
            )?.onSuccess { sessionId ->
                if (TimerSessionRepository.isSkippedOperation(sessionId)) {
                    val reason = TimerSessionRepository.getSkipReason(sessionId)
                    Timber.d("Timer session not saved: $reason")
                } else {
                    Timber.d("Timer session saved successfully with ID: $sessionId")
                }
            }?.onFailure { error ->
                Timber.e(error, "Failed to save timer session")
            }
        }

        // Reset session tracking
        sessionStartTime = null
    }
}

data class User(
    val name: String? = null,
    val email: String? = null,
    val photoUrl: String? = null,
    val uid: String,
)
