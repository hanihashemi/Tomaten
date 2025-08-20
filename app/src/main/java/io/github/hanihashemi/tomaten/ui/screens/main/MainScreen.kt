package io.github.hanihashemi.tomaten.ui.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
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
import kotlinx.coroutines.delay
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
    val isZoomed = uiState.timer.isRunning
    var hasTimerStarted by remember { mutableStateOf(false) }
    var holdProgress by remember { mutableFloatStateOf(0f) }
    var isHolding by remember { mutableStateOf(false) }
    var lastTimeRemaining by remember { mutableLongStateOf(0L) }

    // Handle emote changes based on timer state
    LaunchedEffect(uiState.timer.isRunning) {
        if (uiState.timer.isRunning) {
            hasTimerStarted = true
            lastTimeRemaining = uiState.timer.timeRemaining
            emote = TomiEmotes.Smile
        } else if (hasTimerStarted) {
            // Timer stopped - check if it was completed or cancelled
            if (lastTimeRemaining > 0L && uiState.timer.timeRemaining == 0L) {
                // Timer completed successfully
                emote = TomiEmotes.Smile // Keep happy emote for completion
                delay(3000) // Show success for 3 seconds
                emote = TomiEmotes.Neutral
            } else if (uiState.timer.timeRemaining > 0L) {
                // Timer stopped but not completed (cancelled/incomplete)
                emote = TomiEmotes.Sad
                delay(5000) // Wait 5 seconds
                emote = TomiEmotes.Neutral
            }
        }
    }

    // Handle hold progress animation
    LaunchedEffect(isHolding) {
        if (isHolding && uiState.timer.isRunning) {
            val startTime = System.currentTimeMillis()
            while (isHolding && System.currentTimeMillis() - startTime < 2000) {
                val elapsed = System.currentTimeMillis() - startTime
                holdProgress = (elapsed / 2000f).coerceIn(0f, 1f)
                delay(16) // ~60fps updates
            }
            // Check if we completed the full 2 seconds while still holding
            if (isHolding && System.currentTimeMillis() - startTime >= 2000) {
                // Stop timer after 2 seconds of holding
                actions.timer.startOrStop()
                isHolding = false
                holdProgress = 0f
            }
        } else {
            holdProgress = 0f
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopBar(actions, uiState) },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .let { modifier ->
                        if (uiState.timer.isRunning) {
                            modifier.pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = {
                                        isHolding = true
                                        tryAwaitRelease()
                                        isHolding = false
                                        holdProgress = 0f
                                    },
                                )
                            }
                        } else {
                            modifier
                        }
                    },
        ) {
            Column(
                modifier =
                    Modifier
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

                    // Only show start button when timer is not running
                    if (!uiState.timer.isRunning) {
                        Button(
                            text = "Start",
                            modifier = Modifier.widthIn(180.dp),
                        ) {
                            viewModel.actions.timer.startOrStop()
                        }
                    }
                    Button(
                        text = uiState.tag.selectedTag?.name ?: "Focus",
                        style = ButtonStyles.Secondary,
                    ) {
                        actions.tag.showSelectDialog()
                    }

                    Tomi(
                        modifier = Modifier,
                        emote = emote,
                        isZoomed = isZoomed,
                        onPress = { isHolding = true },
                        onRelease = { isHolding = false },
                    )
                }
            }

            // Progress bar for hold-to-stop (positioned at top of screen)
            if (uiState.timer.isRunning && isHolding && holdProgress > 0f) {
                Column(
                    modifier = Modifier.align(Alignment.TopCenter),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Hold for 2 seconds to cancel focus time",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    )
                    LinearProgressIndicator(
                        progress = { holdProgress },
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .padding(horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            // Settings and Stats buttons in bottom right corner (slide to right when hiding)
            AnimatedVisibility(
                visible = !uiState.timer.isRunning,
                enter =
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(300),
                    ),
                exit =
                    slideOutHorizontally(
                        targetOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(300),
                    ),
                modifier = Modifier.align(Alignment.BottomEnd),
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    // Stats button
                    FloatingActionButton(
                        onClick = { onNavigateToStats() },
                        modifier = Modifier.size(48.dp),
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                        contentColor = MaterialTheme.colorScheme.onSurface,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "Stats",
                        )
                    }
                    // Settings button
                    FloatingActionButton(
                        onClick = { /* TODO: Add settings navigation */ },
                        modifier = Modifier.size(48.dp),
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                        contentColor = MaterialTheme.colorScheme.onSurface,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings",
                        )
                    }
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
