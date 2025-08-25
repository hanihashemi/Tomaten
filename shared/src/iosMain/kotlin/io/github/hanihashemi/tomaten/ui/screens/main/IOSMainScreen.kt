package io.github.hanihashemi.tomaten.ui.screens.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import io.github.hanihashemi.tomaten.viewmodel.IOSMainViewModel
@Composable
fun IOSMainScreen(
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
) {
    SharedMainScreen(
        timeRemaining = timeRemaining,
        selectedTag = selectedTag,
        isTimerRunning = isTimerRunning,
        isDialogVisible = isDialogVisible,
        isSelectDialogVisible = isSelectDialogVisible,
        onTimerClick = onTimerClick,
        onDisplayDialog = onDisplayDialog,
        onStartOrStop = onStartOrStop,
        onSetTimeLimit = onSetTimeLimit,
        onShowSelectDialog = onShowSelectDialog,
        onNavigateToStats = onNavigateToStats,
        onNavigateToSettings = onNavigateToSettings,
        topBarContent = {
            // iOS-style top bar - simplified for now
            Text(
                text = "Tomaten",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
        },
        buttonContent = { text, onClick ->
            if (text == "Start") {
                // iOS-style primary button
                Button(
                    onClick = onClick,
                    modifier = Modifier
                        .widthIn(180.dp)
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                // iOS-style secondary button
                OutlinedButton(
                    onClick = onClick,
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        },
        tomiContent = { isZoomed, onPress, onRelease ->
            // Simplified Tomi for iOS - just a circle for now
            Box(
                modifier = Modifier
                    .padding(32.dp)
            ) {
                Canvas(
                    modifier = Modifier
                        .size(if (isZoomed) 120.dp else 80.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onPress()
                        }
                ) {
                    // Simple circle representing Tomi
                    drawCircle(
                        color = if (isZoomed) Color.Red else Color.Green,
                        radius = size.minDimension / 2
                    )
                }
            }
        },
        loginDialogContent = {
            // iOS login dialog - placeholder for now
        },
        timePickerDialogContent = {
            // iOS time picker dialog - placeholder for now  
        },
        selectTagDialogContent = {
            // iOS tag selection dialog - placeholder for now
        }
    )
}

@Composable
fun IOSMainScreenWithViewModel(
    viewModel: IOSMainViewModel = IOSMainViewModel(),
    onNavigateToStats: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    val actions = viewModel.actions

    IOSMainScreen(
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
    )
}

