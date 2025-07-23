package io.github.hanihashemi.tomaten.ui.dialogs.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.hanihashemi.tomaten.Button
import io.github.hanihashemi.tomaten.ButtonStyles
import io.github.hanihashemi.tomaten.R
import io.github.hanihashemi.tomaten.theme.Dimens
import io.github.hanihashemi.tomaten.theme.TomatenTheme
import io.github.hanihashemi.tomaten.ui.actions.Actions
import io.github.hanihashemi.tomaten.ui.actions.previewActions
import io.github.hanihashemi.tomaten.ui.states.LoginUiState
import io.github.hanihashemi.tomaten.ui.states.UIState

@Suppress("FunctionName")
@Composable
fun LoginDialog(
    uiState: UIState,
    actions: Actions,
) {
    if (!uiState.login.isDialogVisible) return

    AlertDialog(
        onDismissRequest = { actions.login.displayDialog(false) },
        title = {
            Text(text = "Sign in to continue")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(Dimens.PaddingXXXSmall),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("Please sign in with your Google account to sync your Pomodoro data.")
                Spacer(modifier = Modifier.height(Dimens.PaddingXLarge))

                when {
                    uiState.login.isLoading -> {
                        CircularProgressIndicator()
                    }

                    else -> {
                        LoginButtons(actions, uiState)
                    }
                }
            }
        },
        confirmButton = {},
    )
}

@Suppress("FunctionName")
@Composable
private fun LoginButtons(
    actions: Actions,
    uiState: UIState,
) {
    ErrorMessage(uiState)
    Button(
        modifier =
            Modifier
                .fillMaxWidth(),
        onClick = {
            actions.login.login()
        },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_google_logo),
                contentDescription = "Google logo",
                modifier = Modifier.size(24.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Sign in with Google")
        }
    }
    Button(
        text = "Dismiss",
        style = ButtonStyles.Secondary,
    ) { actions.login.displayDialog(false) }
}

@Suppress("FunctionName")
@Composable
private fun ErrorMessage(uiState: UIState) {
    if (uiState.login.errorMessage == null) return

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Error",
            tint = Color.Red,
            modifier = Modifier.size(Dimens.PaddingNormal),
        )
        Spacer(modifier = Modifier.width(Dimens.PaddingXXSmall))
        Text(
            text = uiState.login.errorMessage,
            color = Color.Red,
        )
    }

    Spacer(modifier = Modifier.height(Dimens.PaddingXSmall))
}

@Suppress("FunctionName")
@Preview
@Composable
private fun LoginDialogPreview() {
    TomatenTheme {
        LoginDialog(
            uiState = UIState(LoginUiState(isDialogVisible = true)),
            actions = previewActions,
        )
    }
}

@Suppress("FunctionName")
@Preview
@Composable
private fun LoginDialogLoadingPreview() {
    TomatenTheme {
        LoginDialog(
            uiState =
                UIState(
                    login = LoginUiState(isDialogVisible = true, isLoading = true),
                ),
            actions = previewActions,
        )
    }
}

@Suppress("FunctionName")
@Preview
@Composable
private fun LoginDialogErrorPreview() {
    TomatenTheme {
        LoginDialog(
            uiState =
                UIState(
                    login =
                        LoginUiState(
                            isDialogVisible = true,
                            errorMessage = "Something went wrong.",
                        ),
                ),
            actions = previewActions,
        )
    }
}
