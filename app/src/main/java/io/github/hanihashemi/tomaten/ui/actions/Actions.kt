package io.github.hanihashemi.tomaten.ui.actions

import io.github.hanihashemi.tomaten.MainViewModel
import io.github.hanihashemi.tomaten.ui.events.UiEvents

class Actions(viewModel: MainViewModel) {
    val login = LoginAction(viewModel)
    val timer = TimerAction(viewModel)
}

val previewActions = Actions(viewModel = FakeViewModel())

class FakeViewModel : MainViewModel()

class TimerAction(private val viewModel: MainViewModel) {
    fun startOrStop() {
        val timeLimit = viewModel.uiState.value.timer.timeLimit
        val isRunning = viewModel.uiState.value.timer.isRunning

        if (isRunning) {
            changeTimerState(false)
            viewModel.sendEvent(UiEvents.StopTimer)
        } else {
            changeTimerState(true)
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

    fun updateTimer(remainingTime: Int) {
        viewModel.updateState {
            it.copy(timer = it.timer.copy(timeRemaining = remainingTime.toLong()))
        }
    }

    private fun changeTimerState(isRunning: Boolean) {
        viewModel.updateState {
            it.copy(timer = it.timer.copy(isRunning = isRunning))
        }
    }
}
