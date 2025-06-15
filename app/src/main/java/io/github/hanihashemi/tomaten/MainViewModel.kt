package io.github.hanihashemi.tomaten

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {

    val uiState = MutableStateFlow(UIState())
    val actions = Actions(uiState)
}

data class UIState(
    val loggingDialogVisible: Boolean = false,
)


class Actions(private val uiState: MutableStateFlow<UIState>) {

    val login = Login()

    inner class Login{
        fun openDialog() {
            update {
                it.copy(loggingDialogVisible = true)
            }
        }

        fun dismissDialog() {
            update {
                it.copy(loggingDialogVisible = false)
            }
        }
    }

    private fun update(block: (UIState) -> UIState) {
        uiState.update(block)
    }
}