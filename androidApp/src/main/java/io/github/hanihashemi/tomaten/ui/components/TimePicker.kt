package io.github.hanihashemi.tomaten.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.abs

@Suppress("FunctionName")
@Composable
fun TimePickerDialog(
    isVisible: Boolean,
    currentTimeMinutes: Int,
    onTimeSelected: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    if (isVisible) {
        var selectedTime by remember(currentTimeMinutes) {
            mutableIntStateOf(currentTimeMinutes)
        }

        AlertDialog(onDismissRequest = onDismiss, title = {
            Text(
                text = "Select Timer Duration",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }, text = {
            TimePickerWheel(
                currentTimeMinutes = currentTimeMinutes,
                onTimeChanged = { selectedTime = it },
            )
        }, confirmButton = {
            TextButton(
                onClick = {
                    onTimeSelected(selectedTime)
                    onDismiss()
                },
            ) {
                Text("OK")
            }
        }, dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        })
    }
}

@Suppress("FunctionName")
@Composable
private fun TimePickerWheel(
    currentTimeMinutes: Int,
    onTimeChanged: (Int) -> Unit,
) {
    // Generate time options from 5 to 45 minutes in 5-minute increments
    val timeOptions = (5..45 step 5).toList()
    val itemHeight = 50.dp
    val visibleItemsCount = 5
    val containerHeight = itemHeight * visibleItemsCount

    // Find initial index
    val initialIndex =
        remember(currentTimeMinutes) {
            timeOptions.indexOf(currentTimeMinutes).takeIf { it >= 0 } ?: 2 // Default to 15 minutes
        }

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val coroutineScope = rememberCoroutineScope()

    // Calculate the center item index based on scroll position
    val centerIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val centerY = layoutInfo.viewportStartOffset + layoutInfo.viewportSize.height / 2
            val centerItem =
                layoutInfo.visibleItemsInfo.minByOrNull { item ->
                    abs((item.offset + item.size / 2) - centerY)
                }
            centerItem?.index?.minus(2) ?: 0 // Subtract padding items
        }
    }

    // Update selection when center index changes
    LaunchedEffect(centerIndex) {
        val clampedIndex = centerIndex.coerceIn(0, timeOptions.size - 1)
        onTimeChanged(timeOptions[clampedIndex])
    }

    Box(
        modifier =
            Modifier
                .height(containerHeight)
                .fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        // Selection indicator overlay - appears behind the center item
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(itemHeight)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                        RoundedCornerShape(8.dp),
                    ),
        )

        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .semantics {
                        contentDescription = "Time picker wheel. Scroll to select duration."
                    },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Add padding items at the top (2 items to center the first real item)
            items(2) {
                Box(modifier = Modifier.height(itemHeight))
            }

            // Time options
            itemsIndexed(timeOptions) { index, timeMinutes ->
                TimePickerItem(
                    timeMinutes = timeMinutes,
                    distanceFromCenter = abs(index - centerIndex),
                    onClick = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(index + 2) // Add 2 for padding items
                        }
                    },
                )
            }

            // Add padding items at the bottom (2 items to center the last real item)
            items(2) {
                Box(modifier = Modifier.height(itemHeight))
            }
        }
    }
}

@Composable
@Suppress("FunctionName")
private fun TimePickerItem(
    timeMinutes: Int,
    distanceFromCenter: Int,
    onClick: () -> Unit,
) {
    val isCenter = distanceFromCenter == 0

    // Calculate alpha based on distance from center (iOS-style fade effect)
    val targetAlpha =
        when (distanceFromCenter) {
            0 -> 1.0f // Center item - fully opaque
            1 -> 0.7f // Adjacent items
            2 -> 0.4f // Further away
            else -> 0.2f // Far away items
        }

    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = spring(dampingRatio = 0.8f),
        label = "alpha",
    )

    // Calculate font size based on distance from center
    val fontSize =
        when (distanceFromCenter) {
            0 -> 22.sp // Center item - largest
            1 -> 18.sp // Adjacent items
            else -> 16.sp // Other items
        }

    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) { onClick() }
                .semantics {
                    contentDescription =
                        if (isCenter) {
                            "$timeMinutes minutes, selected"
                        } else {
                            "$timeMinutes minutes"
                        }
                },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "$timeMinutes min",
            fontSize = fontSize,
            fontWeight = if (isCenter) FontWeight.Bold else FontWeight.Normal,
            color =
                if (isCenter) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
            textAlign = TextAlign.Center,
            modifier = Modifier.alpha(alpha),
        )
    }
}
