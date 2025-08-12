package io.github.hanihashemi.tomaten.ui.screens.stats

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
    var selectedTabIndex by remember { mutableIntStateOf(StatsRange.Day.ordinal) }

    LaunchedEffect(selectedRange) {
        selectedTabIndex = selectedRange.ordinal
    }

    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        modifier =
            Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Stats time range selector" },
        edgePadding = 0.dp,
    ) {
        StatsRange.entries.forEachIndexed { index, range ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = {
                    selectedTabIndex = index
                    onTabSelected(range)
                },
                modifier =
                    Modifier.semantics {
                        contentDescription = "${range.displayName} stats tab"
                    },
            ) {
                Text(
                    text = range.displayName,
                    modifier = Modifier.padding(Dimens.PaddingSmall),
                    style = MaterialTheme.typography.labelLarge,
                )
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

@Preview(showBackground = true)
@Composable
private fun StatsScreenPreview() {
    TomatenTheme {
        val sampleTags =
            listOf(
                Tag(name = "Focus", color = TagColor.TEAL),
                Tag(name = "Work", color = TagColor.BLUE),
                Tag(name = "Study", color = TagColor.GREEN),
                Tag(name = "Exercise", color = TagColor.ORANGE),
            )

        val sampleData =
            StatsData(
                totalSessionsFinished = 42,
                totalActiveDays = 7,
                // 18h 49m
                totalFocusTimeMinutes = 1129,
                tagBreakdown =
                    listOf(
                        // 18h 13m
                        TagStats(sampleTags[0], 1093),
                        // 4h
                        TagStats(sampleTags[1], 240),
                        // 2h
                        TagStats(sampleTags[2], 120),
                        // 1h
                        TagStats(sampleTags[3], 60),
                    ),
                finishedSessions = 38,
                abandonedSessions = 4,
            )

        StatsScreen(statsData = sampleData)
    }
}

@Preview(showBackground = true, name = "Large Numbers")
@Composable
private fun StatsScreenLargeNumbersPreview() {
    TomatenTheme {
        val sampleTags =
            listOf(
                Tag(name = "Focus", color = TagColor.TEAL),
                Tag(name = "Work", color = TagColor.BLUE),
            )

        val sampleData =
            StatsData(
                totalSessionsFinished = 150,
                totalActiveDays = 45,
                // 60h
                totalFocusTimeMinutes = 3600,
                tagBreakdown =
                    listOf(
                        // 40h
                        TagStats(sampleTags[0], 2400),
                        // 20h
                        TagStats(sampleTags[1], 1200),
                    ),
                finishedSessions = 120,
                abandonedSessions = 30,
            )

        StatsScreen(statsData = sampleData)
    }
}
