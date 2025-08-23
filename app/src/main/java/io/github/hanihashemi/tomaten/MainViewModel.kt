package io.github.hanihashemi.tomaten

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.google.firebase.auth.FirebaseAuth
import io.github.hanihashemi.tomaten.data.repository.BaseFirebaseRepository
import io.github.hanihashemi.tomaten.data.repository.TagRepository
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

open class MainViewModel(
    private val timerSessionRepository: TimerSessionRepository? = null,
    private val tagRepository: TagRepository? = null,
    shouldFetchCurrentUser: Boolean = true,
) : ViewModel() {
    private val _uiEvents = MutableSharedFlow<UiEvents>(replay = 1)
    val uiEvents = _uiEvents.asSharedFlow()
    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState
    val actions: Actions by lazy { Actions(this) }

    init {
        if (shouldFetchCurrentUser) {
            fetchCurrentUser()
            initializeDefaultFocusTag()
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

    fun saveTimerSession(completed: Boolean = false) {
        val timerState = uiState.value.timer
        val startTime = timerState.startTime
        if (startTime == null) {
            Logger.w("Cannot stop timer session - no start time recorded")
            return
        }

        // Calculate actual duration: how much time has elapsed since start
        val actualDuration = timerState.timeLimit - timerState.timeRemaining
        val targetDuration = timerState.timeLimit

        viewModelScope.launchSafely(Dispatchers.IO) {
            timerSessionRepository?.saveTimerSession(
                startTime = startTime,
                duration = actualDuration,
                targetDuration = targetDuration,
                completed = completed,
                tagId = uiState.value.tag.selectedTagId,
            )?.onSuccess { sessionId ->
                if (BaseFirebaseRepository.isSkippedOperation(sessionId)) {
                    val reason = BaseFirebaseRepository.getSkipReason(sessionId)
                    Logger.d("Timer session not saved: $reason")
                } else {
                    Logger.d("Timer session saved successfully with ID: $sessionId")
                }
            }?.onFailure { error ->
                Logger.e("Failed to save timer session", error)
            }
        }
    }

    suspend fun getTags() = tagRepository?.getTags() ?: emptyList()

    suspend fun addTag(tag: io.github.hanihashemi.tomaten.data.model.Tag) = tagRepository?.addTag(tag) ?: tag.id

    suspend fun deleteTag(tagId: String) = tagRepository?.deleteTag(tagId) ?: true

    fun getDefaultFocusTagId() = tagRepository?.getDefaultFocusTagId() ?: ""

    private fun initializeDefaultFocusTag() {
        viewModelScope.launchSafely(Dispatchers.IO) {
            // Initialize tags in database and get them
            val tags = tagRepository?.getTags() ?: emptyList()
            val defaultFocusTagId = getDefaultFocusTagId()

            if (defaultFocusTagId.isNotEmpty()) {
                updateState { state ->
                    state.copy(
                        tag =
                            state.tag.copy(
                                selectedTagId = defaultFocusTagId,
                                tags = tags,
                            ),
                    )
                }
            }
        }
    }
}

data class User(
    val name: String? = null,
    val email: String? = null,
    val photoUrl: String? = null,
    val uid: String,
)
