package io.github.hanihashemi.tomaten.ui.actions

import io.github.hanihashemi.tomaten.MainViewModel
import io.github.hanihashemi.tomaten.ui.events.UiEvents

class Actions(viewModel: MainViewModel) {
    val login = LoginAction(viewModel)
    val timer = TimerAction(viewModel)
}

val previewActions = Actions(viewModel = FakeViewModel())

class FakeViewModel : MainViewModel(timerSessionRepository = null, shouldFetchCurrentUser = false)

class TimerAction(private val viewModel: MainViewModel) {
    fun startOrStop() {
        val timeLimit = viewModel.uiState.value.timer.timeLimit
        val isRunning = viewModel.uiState.value.timer.isRunning

        if (isRunning) {
            // Determine if timer was completed by checking remaining time
            val remainingTime = viewModel.uiState.value.timer.timeRemaining
            val completed = remainingTime <= 0

            changeTimerState(false)
            viewModel.stopTimerSession(completed = completed)
            viewModel.sendEvent(UiEvents.StopTimer)
        } else {
            changeTimerState(true)
            viewModel.startTimerSession()
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
    }

    private fun changeTimerState(isRunning: Boolean) {
        viewModel.updateState {
            it.copy(timer = it.timer.copy(isRunning = isRunning))
        }
    }
}
