package io.github.hanihashemi.tomaten.ui.actions

import com.google.firebase.auth.FirebaseAuth
import io.github.hanihashemi.tomaten.MainViewModel
import io.github.hanihashemi.tomaten.User
import io.github.hanihashemi.tomaten.ui.events.UiEvents

class LoginAction(private val viewModel: MainViewModel) {
    fun displayDialog(visible: Boolean) {
        val isLoading = viewModel.uiState.value.login.isLoading
        if (isLoading) return

        viewModel.updateState {
            it.copy(
                login =
                    it.login.copy(
                        isDialogVisible = visible,
                        errorMessage = null,
                    ),
            )
        }
    }

    fun login() {
        viewModel.updateState {
            it.copy(
                login =
                    it.login.copy(
                        isLoading = true,
                        errorMessage = null,
                    ),
            )
        }
        viewModel.sendEvent(UiEvents.Login)
    }

    fun setErrorMessage(errorMessage: String) {
        viewModel.updateState {
            it.copy(
                login =
                    it.login.copy(
                        errorMessage = errorMessage,
                        isLoading = false,
                    ),
            )
        }
    }

    fun setUser(user: User) {
        viewModel.updateState {
            it.copy(
                login =
                    it.login.copy(
                        user = user,
                        isDialogVisible = false,
                        isLoading = false,
                        errorMessage = null,
                    ),
            )
        }
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
        viewModel.updateState {
            it.copy(
                login =
                    it.login.copy(
                        user = null,
                    ),
            )
        }
    }
}
