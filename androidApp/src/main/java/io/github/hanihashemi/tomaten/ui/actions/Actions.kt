package io.github.hanihashemi.tomaten.ui.actions

import io.github.hanihashemi.tomaten.MainViewModel
import io.github.hanihashemi.tomaten.ui.events.UiEvents
import java.util.Date

class Actions(viewModel: MainViewModel) {
    val login = LoginAction(viewModel)
    val timer = TimerAction(viewModel)
    val tag = TagAction(viewModel)
}

val previewActions = Actions(viewModel = FakeViewModel())

class FakeViewModel : MainViewModel(timerSessionRepository = null, tagRepository = null, shouldFetchCurrentUser = false)

class TimerAction(private val viewModel: MainViewModel) {
    fun startOrStop() {
        val timeLimit = viewModel.uiState.value.timer.timeLimit
        val isRunning = viewModel.uiState.value.timer.isRunning

        if (isRunning) {
            // Determine if timer was completed by checking remaining time
            val remainingTime = viewModel.uiState.value.timer.timeRemaining
            val completed = remainingTime <= 0

            // Save timer session before clearing startTime
            viewModel.saveTimerSession(completed = completed)

            viewModel.updateState {
                it.copy(
                    timer =
                        it.timer.copy(
                            isRunning = false,
                            startTime = null,
                        ),
                )
            }
            viewModel.sendEvent(UiEvents.StopTimer)
        } else {
            viewModel.updateState {
                it.copy(
                    timer =
                        it.timer.copy(
                            isRunning = true,
                            startTime = Date(),
                        ),
                )
            }
            viewModel.sendEvent(UiEvents.StartTimer(timeLimit))
        }
    }

    fun setTimeLimit(time: Long) {
        val isTimerRunning = viewModel.uiState.value.timer.isRunning
        if (isTimerRunning) return

        viewModel.updateState {
            it.copy(
                timer =
                    it.timer.copy(
                        timeLimit = time,
                        timeRemaining = time,
                    ),
            )
        }
    }

    fun displayDialog(visible: Boolean) {
        val isTimerRunning = viewModel.uiState.value.timer.isRunning
        if (isTimerRunning) return

        viewModel.updateState {
            it.copy(timer = it.timer.copy(isDialogVisible = visible))
        }
    }

    fun updateTimer(remainingTime: Long) {
        viewModel.updateState {
            it.copy(timer = it.timer.copy(timeRemaining = remainingTime))
        }

        // If timer reached 0 and is still running, complete it
        if (remainingTime <= 0 && viewModel.uiState.value.timer.isRunning) {
            completeTimer()
        }
    }

    private fun completeTimer() {
        // Save timer session as completed
        viewModel.saveTimerSession(completed = true)

        // Reset timer state
        viewModel.updateState {
            it.copy(
                timer =
                    it.timer.copy(
                        isRunning = false,
                        startTime = null,
                        // Reset to original time limit
                        timeRemaining = it.timer.timeLimit,
                    ),
            )
        }

        viewModel.sendEvent(UiEvents.StopTimer)
    }
}
