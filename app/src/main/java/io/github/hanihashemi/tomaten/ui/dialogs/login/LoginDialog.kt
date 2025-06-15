package io.github.hanihashemi.tomaten.ui.dialogs.login

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import io.github.hanihashemi.tomaten.Actions
import io.github.hanihashemi.tomaten.Button
import io.github.hanihashemi.tomaten.UIState

@Composable
fun LoginDialog(uiState: UIState, actions: Actions) {
    if (uiState.loggingDialogVisible) {
        AlertDialog(
            onDismissRequest = { actions.login.dismissDialog() },
            title = {
                Text(text = "Login")
            },
            text = {
                Text("Here you can show login instructions or progress.")
            },
            confirmButton = {
                Button(
                    text = "Proceed",
                    onClick = {

                    }
                )
            },
            dismissButton = {
                Button(
                    text = "Cancel",
                    onClick = {
                        actions.login.dismissDialog()
                    }
                )
            }
        )
    }
}