package io.github.hanihashemi.tomaten.ui.screens.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.hanihashemi.tomaten.Button
import io.github.hanihashemi.tomaten.ButtonStyles
import io.github.hanihashemi.tomaten.theme.Dimens
import io.github.hanihashemi.tomaten.theme.TomatenTheme
import io.github.hanihashemi.tomaten.theme.Typography
import io.github.hanihashemi.tomaten.ui.actions.Actions
import io.github.hanihashemi.tomaten.ui.actions.previewActions
import io.github.hanihashemi.tomaten.ui.states.UIState

@Composable
fun TopBar(actions: Actions, uiState: UIState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(horizontal = Dimens.PaddingNormal),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Tomaten",
            style = Typography.titleLarge,
        )
        Button(
            text = uiState.login.userDisplayName ?: "Login",
            style = ButtonStyles.Secondary,
            modifier = Modifier.offset(x = Dimens.PaddingNormal)
        ) {
            if (uiState.login.isLoggedIn) return@Button

            actions.login.displayDialog(true)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TopBarPreview() {
    TomatenTheme {
        TopBar(actions = previewActions, uiState = UIState())
    }
}