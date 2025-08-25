package io.github.hanihashemi.tomaten.ui.screens.main

import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.hanihashemi.tomaten.Button
import io.github.hanihashemi.tomaten.ButtonStyles
import io.github.hanihashemi.tomaten.MainViewModel
import io.github.hanihashemi.tomaten.extensions.toSeconds
import io.github.hanihashemi.tomaten.ui.components.SelectTagDialog
import io.github.hanihashemi.tomaten.ui.components.TimePickerDialog
import io.github.hanihashemi.tomaten.ui.dialogs.login.LoginDialog
import io.github.hanihashemi.tomaten.ui.screens.main.components.Tomi
import io.github.hanihashemi.tomaten.ui.screens.main.components.TomiEmotes
import io.github.hanihashemi.tomaten.ui.screens.main.components.TopBar
import org.koin.androidx.compose.koinViewModel

@Suppress("FunctionName")
@Composable
fun MainScreen(
    onNavigateToStats: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    viewModel: MainViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val actions = viewModel.actions

    SharedMainScreen(
        timeRemaining = uiState.timer.timeRemaining,
        selectedTag = uiState.tag.selectedTag?.name,
        isTimerRunning = uiState.timer.isRunning,
        isDialogVisible = uiState.timer.isDialogVisible,
        isSelectDialogVisible = uiState.tag.isSelectDialogVisible,
        onTimerClick = { actions.timer.displayDialog(true) },
        onDisplayDialog = actions.timer::displayDialog,
        onStartOrStop = actions.timer::startOrStop,
        onSetTimeLimit = actions.timer::setTimeLimit,
        onShowSelectDialog = actions.tag::showSelectDialog,
        onNavigateToStats = onNavigateToStats,
        onNavigateToSettings = onNavigateToSettings,
        topBarContent = { TopBar(actions, uiState) },
        buttonContent = { text, onClick ->
            if (text == "Start") {
                Button(
                    text = text,
                    modifier = Modifier.widthIn(180.dp),
                    onClick = onClick,
                )
            } else {
                Button(
                    text = text,
                    style = ButtonStyles.Secondary,
                    onClick = onClick,
                )
            }
        },
        tomiContent = { isZoomed, onPress, onRelease ->
            Tomi(
                modifier = Modifier,
                // Simplified for now
                emote = TomiEmotes.Smile,
                isZoomed = isZoomed,
                onPress = onPress,
                onRelease = onRelease,
            )
        },
        loginDialogContent = {
            LoginDialog(
                uiState = uiState,
                actions = actions,
            )
        },
        timePickerDialogContent = {
            TimePickerDialog(
                isVisible = uiState.timer.isDialogVisible,
                currentTimeMinutes = uiState.timer.timeRemaining.toInt(),
                onTimeSelected = { selectedMinutes: Int ->
                    actions.timer.setTimeLimit(selectedMinutes.toSeconds())
                },
                onDismiss = {
                    actions.timer.displayDialog(false)
                },
            )
        },
        selectTagDialogContent = {
            if (uiState.tag.isSelectDialogVisible) {
                SelectTagDialog(
                    tagState = uiState.tag,
                    tagAction = actions.tag,
                    timerRunning = uiState.timer.isRunning,
                    onDismiss = { actions.tag.hideSelectDialog() },
                )
            }
        },
    )
}
