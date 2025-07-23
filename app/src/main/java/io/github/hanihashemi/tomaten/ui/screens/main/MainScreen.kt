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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import io.github.hanihashemi.tomaten.theme.Dimens
import io.github.hanihashemi.tomaten.ui.components.TimePickerDialog
import io.github.hanihashemi.tomaten.ui.dialogs.login.LoginDialog
import io.github.hanihashemi.tomaten.ui.screens.main.components.Tomi
import io.github.hanihashemi.tomaten.ui.screens.main.components.TomiEmotes
import io.github.hanihashemi.tomaten.ui.screens.main.components.TopBar
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen() {
    val viewModel: MainViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val actions = viewModel.actions

    var emote by remember { mutableStateOf<TomiEmotes>(TomiEmotes.Smile) }
    var isZoomed by remember { mutableStateOf(false) }

    // Timer state
    var timerDurationMinutes by remember { mutableIntStateOf(15) } // Timer duration in minutes
    var timeRemaining by remember { mutableIntStateOf(15 * 60) } // 15 minutes in seconds
    var isTimerRunning by remember { mutableStateOf(false) }
    var showTimePickerDialog by remember { mutableStateOf(false) }

    // Timer countdown effect
    LaunchedEffect(isTimerRunning) {
        if (isTimerRunning) {
            while (timeRemaining > 0 && isTimerRunning) {
                delay(1000) // Wait 1 second
                timeRemaining--
            }
            if (timeRemaining == 0) {
                isTimerRunning = false
            }
        }
    }

    // Format time display (MM:SS)
    fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return "%02d:%02d".format(minutes, remainingSeconds)
    }

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
                    text = formatTime(timeRemaining),
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
                                if (!isTimerRunning) {
                                    showTimePickerDialog = true
                                }
                            },
                )

                Button(
                    text = if (isTimerRunning) "Stop" else "Start",
                    modifier = Modifier.widthIn(180.dp),
                ) {
                    if (isTimerRunning) {
                        isTimerRunning = false
                    } else {
                        timeRemaining = timerDurationMinutes * 60 // Reset to selected duration
                        isTimerRunning = true
                    }
                }
                Button("Focus", style = ButtonStyles.Secondary) { }

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

        // Time Picker Dialog
        TimePickerDialog(
            isVisible = showTimePickerDialog,
            currentTimeMinutes = timerDurationMinutes,
            onTimeSelected = { selectedMinutes: Int ->
                timerDurationMinutes = selectedMinutes
                timeRemaining = selectedMinutes * 60
            },
            onDismiss = {
                showTimePickerDialog = false
            },
        )
    }
}
