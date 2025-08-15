package io.github.hanihashemi.tomaten.ui.screens.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.hanihashemi.tomaten.Button
import io.github.hanihashemi.tomaten.ButtonStyles
import io.github.hanihashemi.tomaten.MainViewModel
import io.github.hanihashemi.tomaten.extensions.formatTime
import io.github.hanihashemi.tomaten.extensions.toSeconds
import io.github.hanihashemi.tomaten.theme.Dimens
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
    viewModel: MainViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val actions = viewModel.actions

    var emote by remember { mutableStateOf<TomiEmotes>(TomiEmotes.Smile) }
    var isZoomed by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopBar(actions, uiState) },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(Dimens.PaddingNormal),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Timer Display - Clickable to open time picker
                Text(
                    text = uiState.timer.timeRemaining.formatTime(),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    modifier =
                        Modifier
                            .padding(16.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                            ) {
                                actions.timer.displayDialog(true)
                            },
                )

                Button(
                    text = if (uiState.timer.isRunning) "Stop" else "Start",
                    modifier = Modifier.widthIn(180.dp),
                ) {
                    viewModel.actions.timer.startOrStop()
                }
                Button(
                    text = uiState.tag.selectedTag?.name ?: "Focus",
                    style = ButtonStyles.Secondary,
                ) {
                    actions.tag.showSelectDialog()
                }
                Button(
                    text = "Stats",
                    style = ButtonStyles.Secondary,
                ) {
                    onNavigateToStats()
                }

                Tomi(
                    modifier = Modifier,
                    emote = emote,
                    isZoomed = isZoomed,
                )

                Row {
                    Button("Toggle Zoom") {
                        isZoomed = !isZoomed
                    }
                    Button("Smile") {
                        emote = TomiEmotes.Smile
                    }
                    Button("Neutral") {
                        emote = TomiEmotes.Neutral
                    }
                    Button("Sad") {
                        emote = TomiEmotes.Sad
                    }
                    Button("Surprise") { emote = TomiEmotes.Surprise }
                }
            }
        }

        LoginDialog(
            uiState = uiState,
            actions = actions,
        )

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

        if (uiState.tag.isSelectDialogVisible) {
            SelectTagDialog(
                tagState = uiState.tag,
                tagAction = actions.tag,
                timerRunning = uiState.timer.isRunning,
                onDismiss = { actions.tag.hideSelectDialog() },
            )
        }
    }
}
