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
import kotlinx.coroutines.delay

@Suppress("FunctionName")
@Composable
fun SharedMainScreen(
    timeRemaining: Long,
    selectedTag: String?,
    isTimerRunning: Boolean,
    isDialogVisible: Boolean,
    isSelectDialogVisible: Boolean,
    onTimerClick: () -> Unit,
    onDisplayDialog: (Boolean) -> Unit,
    onStartOrStop: () -> Unit,
    onSetTimeLimit: (Long) -> Unit,
    onShowSelectDialog: () -> Unit,
    onNavigateToStats: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    // Platform-specific composables as parameters
    topBarContent: @Composable () -> Unit,
    buttonContent: @Composable (String, () -> Unit) -> Unit,
    tomiContent: @Composable (isZoomed: Boolean, onPress: () -> Unit, onRelease: () -> Unit) -> Unit,
    loginDialogContent: @Composable () -> Unit,
    timePickerDialogContent: @Composable () -> Unit,
    selectTagDialogContent: @Composable () -> Unit,
) {
    var emote by remember { mutableStateOf("Smile") }
    val isZoomed = isTimerRunning
    var hasTimerStarted by remember { mutableStateOf(false) }
    var holdProgress by remember { mutableFloatStateOf(0f) }
    var isHolding by remember { mutableStateOf(false) }
    var lastTimeRemaining by remember { mutableLongStateOf(0L) }

    // Format time for display
    fun formatTime(seconds: Long): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return "$minutes:${remainingSeconds.toString().padStart(2, '0')}"
    }

    // Handle emote changes based on timer state
    LaunchedEffect(isTimerRunning) {
        if (isTimerRunning) {
            hasTimerStarted = true
            lastTimeRemaining = timeRemaining
            emote = "Smile"
        } else if (hasTimerStarted) {
            // Timer stopped - check if it was completed or cancelled
            if (lastTimeRemaining > 0L && timeRemaining == 0L) {
                // Timer completed successfully
                emote = "Smile" // Keep happy emote for completion
                delay(3000) // Show success for 3 seconds
                emote = "Neutral"
            } else if (timeRemaining > 0L) {
                // Timer stopped but not completed (cancelled/incomplete)
                emote = "Sad"
                delay(5000) // Wait 5 seconds
                emote = "Neutral"
            }
        }
    }

    // Handle hold progress animation
    LaunchedEffect(isHolding) {
        if (isHolding && isTimerRunning) {
            var elapsed = 0L
            val totalTime = 2000L
            while (isHolding && elapsed < totalTime) {
                holdProgress = (elapsed.toFloat() / totalTime).coerceIn(0f, 1f)
                delay(16) // ~60fps updates
                elapsed += 16
            }
            // Check if we completed the full 2 seconds while still holding
            if (isHolding && elapsed >= totalTime) {
                // Stop timer after 2 seconds of holding
                onStartOrStop()
                isHolding = false
                holdProgress = 0f
            }
        } else {
            holdProgress = 0f
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { topBarContent() },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .let { modifier ->
                        if (isTimerRunning) {
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
                        .padding(16.dp), // Using hardcoded padding instead of Dimens
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
                                    onDisplayDialog(true)
                                },
                    )

                    // Only show start button when timer is not running
                    if (!isTimerRunning) {
                        buttonContent("Start") {
                            onStartOrStop()
                        }
                    }
                    
                    buttonContent(selectedTag ?: "Focus") {
                        onShowSelectDialog()
                    }

                    tomiContent(
                        isZoomed,
                        { isHolding = true },
                        { isHolding = false },
                    )
                }
            }

            // Progress bar for hold-to-stop (positioned at top of screen)
            if (isTimerRunning && isHolding && holdProgress > 0f) {
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
                visible = !isTimerRunning,
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
                        onClick = { onNavigateToSettings() },
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

        loginDialogContent()
        timePickerDialogContent()
        selectTagDialogContent()
    }
}