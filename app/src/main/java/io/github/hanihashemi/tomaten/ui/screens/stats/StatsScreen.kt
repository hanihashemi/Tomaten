package io.github.hanihashemi.tomaten.ui.screens.stats

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.hanihashemi.tomaten.data.model.Tag
import io.github.hanihashemi.tomaten.data.model.TagColor
import io.github.hanihashemi.tomaten.theme.Dimens
import io.github.hanihashemi.tomaten.theme.TomatenTheme
import kotlin.math.min

enum class StatsRange(val displayName: String) {
    Day("Day"),
    Week("Week"),
    Month("Month"),
    Year("Year"),
    All("All"),
}

data class StatsData(
    val totalSessionsFinished: Int = 0,
    val totalActiveDays: Int = 0,
    val totalFocusTimeMinutes: Int = 0,
    val tagBreakdown: List<TagStats> = emptyList(),
    val finishedSessions: Int = 0,
    val abandonedSessions: Int = 0,
)

data class TagStats(
    val tag: Tag,
    val durationMinutes: Int,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    statsData: StatsData = StatsData(),
    selectedRange: StatsRange = StatsRange.Day,
    onTabSelected: (StatsRange) -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Statistics",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackPressed,
                        modifier =
                            Modifier.semantics {
                                contentDescription = "Navigate back to main screen"
                            },
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(Dimens.PaddingNormal),
            verticalArrangement = Arrangement.spacedBy(Dimens.PaddingNormal),
        ) {
            item {
                StatsTabRow(
                    selectedRange = selectedRange,
                    onTabSelected = onTabSelected,
                )
            }

            item {
                TotalsCard(
                    sessionsFinished = statsData.totalSessionsFinished,
                    activeDays = statsData.totalActiveDays,
                )
            }

            item {
                FocusTimeCard(
                    totalFocusMinutes = statsData.totalFocusTimeMinutes,
                    tagBreakdown = statsData.tagBreakdown,
                )
            }

            item {
                OutcomesCard(
                    finished = statsData.finishedSessions,
                    abandoned = statsData.abandonedSessions,
                )
            }
        }
    }
}

@Composable
private fun StatsTabRow(
    selectedRange: StatsRange,
    onTabSelected: (StatsRange) -> Unit,
) {
    // Container for the segmented control
    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Stats time range selector" }
                .padding(horizontal = Dimens.PaddingNormal),
        color = Color(0xFFF4F5F7),
        shape = RoundedCornerShape(9999.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)),
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            StatsRange.entries.forEach { range ->
                val isSelected = selectedRange == range

                Box(
                    modifier =
                        Modifier
                            .weight(1f)
                            .height(36.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication =
                                    ripple(
                                        bounded = true,
                                        radius = 18.dp,
                                        color = if (isSelected) Color(0xFF111827) else Color(0xFF374151),
                                    ),
                            ) { onTabSelected(range) }
                            .background(
                                color =
                                    if (isSelected) {
                                        Color.White
                                    } else {
                                        Color.Transparent
                                    },
                                shape = RoundedCornerShape(9999.dp),
                            )
                            .then(
                                if (isSelected) {
                                    Modifier.border(
                                        1.dp,
                                        Color(0xFFD1D5DB),
                                        RoundedCornerShape(9999.dp),
                                    )
                                } else {
                                    Modifier
                                },
                            )
                            .semantics {
                                contentDescription = "${range.displayName} stats tab"
                            },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = range.displayName,
                        modifier = Modifier.padding(horizontal = 14.dp),
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color =
                            if (isSelected) {
                                Color(0xFF111827)
                            } else {
                                Color(0xFF374151)
                            },
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }
    }
}

@Composable
private fun TotalsCard(
    sessionsFinished: Int,
    activeDays: Int,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Stats totals summary" },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(Dimens.PaddingNormal),
        ) {
            Text(
                text = "Totals",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = Dimens.PaddingSmall),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                KpiBlock(
                    title = "Sessions Finished",
                    value = sessionsFinished.toString(),
                    modifier = Modifier.weight(1f),
                )

                Spacer(modifier = Modifier.width(Dimens.PaddingNormal))

                KpiBlock(
                    title = "Active Days",
                    value = activeDays.toString(),
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun KpiBlock(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun FocusTimeCard(
    totalFocusMinutes: Int,
    tagBreakdown: List<TagStats>,
) {
    val filteredBreakdown =
        tagBreakdown.filter { it.durationMinutes > 0 }
            .sortedByDescending { it.durationMinutes }

    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Focus time breakdown by tags" },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(Dimens.PaddingNormal),
        ) {
            Text(
                text = "Focus Time",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = Dimens.PaddingSmall),
            )

            Text(
                text = formatDuration(totalFocusMinutes),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = Dimens.PaddingNormal),
            )

            filteredBreakdown.forEach { tagStat ->
                TagProgressRow(
                    tagStat = tagStat,
                    totalMinutes = totalFocusMinutes,
                )
                Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
            }
        }
    }
}

@Composable
private fun TagProgressRow(
    tagStat: TagStats,
    totalMinutes: Int,
) {
    val progress = if (totalMinutes > 0) tagStat.durationMinutes.toFloat() / totalMinutes else 0f
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(progress) {
        animatedProgress.animateTo(
            targetValue = progress,
            animationSpec = tween(durationMillis = 1000),
        )
    }

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = "${tagStat.tag.name} tag used for ${formatDuration(tagStat.durationMinutes)}"
                },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Tag chip
        Surface(
            modifier =
                Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .semantics { contentDescription = tagStat.tag.name },
            color = tagStat.tag.color.displayColor,
            contentColor = Color.White,
        ) {
            Text(
                text = tagStat.tag.name,
                modifier =
                    Modifier.padding(
                        horizontal = Dimens.PaddingSmall,
                        vertical = Dimens.PaddingXXXSmall,
                    ),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
            )
        }

        Spacer(modifier = Modifier.width(Dimens.PaddingSmall))

        // Progress bar
        LinearProgressIndicator(
            progress = { animatedProgress.value },
            modifier =
                Modifier
                    .weight(1f)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
            color = tagStat.tag.color.displayColor,
        )

        Spacer(modifier = Modifier.width(Dimens.PaddingSmall))

        // Duration text
        Text(
            text = formatDuration(tagStat.durationMinutes),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun OutcomesCard(
    finished: Int,
    abandoned: Int,
) {
    val total = finished + abandoned
    val maxGridIcons = 60
    val showGrid = total <= maxGridIcons
    val gridCount = if (showGrid) total else maxGridIcons
    val remainingCount = if (showGrid) 0 else total - maxGridIcons

    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = "Session outcomes: $finished finished, $abandoned abandoned"
                },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(Dimens.PaddingNormal),
        ) {
            Text(
                text = "Outcomes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = Dimens.PaddingSmall),
            )

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = Dimens.PaddingNormal),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                OutcomeCounter(
                    label = "Finished",
                    count = finished,
                    color = MaterialTheme.colorScheme.primary,
                )

                OutcomeCounter(
                    label = "Abandoned",
                    count = abandoned,
                    color = MaterialTheme.colorScheme.error,
                )
            }

            if (total > 0) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    // Show finished sessions first
                    repeat(min(finished, gridCount)) {
                        OutcomeIcon(
                            isFinished = true,
                            modifier =
                                Modifier.semantics {
                                    contentDescription = "Finished session"
                                },
                        )
                    }

                    // Show abandoned sessions
                    val remainingInGrid = gridCount - min(finished, gridCount)
                    repeat(min(abandoned, remainingInGrid)) {
                        OutcomeIcon(
                            isFinished = false,
                            modifier =
                                Modifier.semantics {
                                    contentDescription = "Abandoned session"
                                },
                        )
                    }
                }

                if (remainingCount > 0) {
                    Text(
                        text = "+$remainingCount more",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = Dimens.PaddingXSmall),
                    )
                }
            }
        }
    }
}

@Composable
private fun OutcomeCounter(
    label: String,
    count: Int,
    color: Color,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = color,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun OutcomeIcon(
    isFinished: Boolean,
    modifier: Modifier = Modifier,
) {
    val color =
        if (isFinished) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.error
        }

    Box(
        modifier =
            modifier
                .size(24.dp)
                .clip(CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        if (isFinished) {
            Canvas(modifier = Modifier.size(24.dp)) {
                drawCircle(
                    color = color,
                    radius = size.minDimension / 2,
                )
            }
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp),
            )
        } else {
            Canvas(modifier = Modifier.size(24.dp)) {
                drawCircle(
                    color = color,
                    radius = size.minDimension / 2,
                    style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round),
                )
            }
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(12.dp),
            )
        }
    }
}

private fun formatDuration(minutes: Int): String {
    val hours = minutes / 60
    val mins = minutes % 60

    return when {
        hours > 0 && mins > 0 -> "${hours}h ${mins}m"
        hours > 0 -> "${hours}h"
        mins > 0 -> "${mins}m"
        else -> "0m"
    }
}

private fun getSampleDataForRange(range: StatsRange): StatsData {
    val sampleTags =
        listOf(
            Tag(name = "Focus", color = TagColor.TEAL),
            Tag(name = "Work", color = TagColor.BLUE),
            Tag(name = "Study", color = TagColor.GREEN),
            Tag(name = "Exercise", color = TagColor.ORANGE),
            Tag(name = "Reading", color = TagColor.PURPLE),
        )

    return when (range) {
        StatsRange.Day ->
            StatsData(
                totalSessionsFinished = 6,
                totalActiveDays = 1,
                totalFocusTimeMinutes = 180,
                tagBreakdown =
                    listOf(
                        TagStats(sampleTags[0], 120),
                        TagStats(sampleTags[1], 60),
                    ),
                finishedSessions = 5,
                abandonedSessions = 1,
            )
        StatsRange.Week ->
            StatsData(
                totalSessionsFinished = 28,
                totalActiveDays = 5,
                totalFocusTimeMinutes = 840,
                tagBreakdown =
                    listOf(
                        TagStats(sampleTags[0], 480),
                        TagStats(sampleTags[1], 240),
                        TagStats(sampleTags[2], 120),
                    ),
                finishedSessions = 25,
                abandonedSessions = 3,
            )
        StatsRange.Month ->
            StatsData(
                totalSessionsFinished = 95,
                totalActiveDays = 18,
                totalFocusTimeMinutes = 2850,
                tagBreakdown =
                    listOf(
                        TagStats(sampleTags[0], 1440),
                        TagStats(sampleTags[1], 720),
                        TagStats(sampleTags[2], 420),
                        TagStats(sampleTags[3], 270),
                    ),
                finishedSessions = 82,
                abandonedSessions = 13,
            )
        StatsRange.Year ->
            StatsData(
                totalSessionsFinished = 450,
                totalActiveDays = 125,
                totalFocusTimeMinutes = 13500,
                tagBreakdown =
                    listOf(
                        TagStats(sampleTags[0], 6000),
                        TagStats(sampleTags[1], 3600),
                        TagStats(sampleTags[2], 2400),
                        TagStats(sampleTags[3], 1200),
                        TagStats(sampleTags[4], 300),
                    ),
                finishedSessions = 390,
                abandonedSessions = 60,
            )
        StatsRange.All ->
            StatsData(
                totalSessionsFinished = 1250,
                totalActiveDays = 365,
                totalFocusTimeMinutes = 37500,
                tagBreakdown =
                    listOf(
                        TagStats(sampleTags[0], 18000),
                        TagStats(sampleTags[1], 9000),
                        TagStats(sampleTags[2], 6000),
                        TagStats(sampleTags[3], 3000),
                        TagStats(sampleTags[4], 1500),
                    ),
                finishedSessions = 1050,
                abandonedSessions = 200,
            )
    }
}

@Preview(showBackground = true)
@Composable
private fun StatsScreenPreview() {
    TomatenTheme {
        var selectedRange by remember { mutableStateOf(StatsRange.Week) }
        val statsData = getSampleDataForRange(selectedRange)

        StatsScreen(
            statsData = statsData,
            selectedRange = selectedRange,
            onTabSelected = { selectedRange = it },
        )
    }
}

@Preview(showBackground = true, name = "Large Numbers")
@Composable
private fun StatsScreenLargeNumbersPreview() {
    TomatenTheme {
        var selectedRange by remember { mutableStateOf(StatsRange.All) }
        val statsData = getSampleDataForRange(selectedRange)

        StatsScreen(
            statsData = statsData,
            selectedRange = selectedRange,
            onTabSelected = { selectedRange = it },
        )
    }
}

@Preview(showBackground = true, name = "Tab Test")
@Composable
private fun TabTestPreview() {
    TomatenTheme {
        var selectedRange by remember { mutableStateOf(StatsRange.Day) }

        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = "🔥 CLICK TEST - TAP TABS TO CHANGE DATA",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            // Simple color indicator that changes based on selection
            val indicatorColor =
                when (selectedRange) {
                    StatsRange.Day -> Color(0xFF007AFF) // Same vibrant blue as active tab
                    StatsRange.Week -> Color(0xFF007AFF)
                    StatsRange.Month -> Color(0xFF007AFF)
                    StatsRange.Year -> Color(0xFF007AFF)
                    StatsRange.All -> Color(0xFF007AFF)
                }

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(indicatorColor, RoundedCornerShape(8.dp))
                        .padding(16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "SELECTED: ${selectedRange.displayName}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            StatsTabRow(
                selectedRange = selectedRange,
                onTabSelected = { newRange ->
                    selectedRange = newRange
                },
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Simple numeric data that's very different for each range
            val testData =
                when (selectedRange) {
                    StatsRange.Day -> "DAY: 1 session, 25 minutes"
                    StatsRange.Week -> "WEEK: 10 sessions, 5 hours"
                    StatsRange.Month -> "MONTH: 50 sessions, 25 hours"
                    StatsRange.Year -> "YEAR: 200 sessions, 100 hours"
                    StatsRange.All -> "ALL TIME: 1000 sessions, 500 hours"
                }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors =
                    CardDefaults.cardColors(
                        containerColor = indicatorColor.copy(alpha = 0.1f),
                    ),
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "📊 DATA CHANGES HERE:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                    Text(
                        text = testData,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = indicatorColor,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}
