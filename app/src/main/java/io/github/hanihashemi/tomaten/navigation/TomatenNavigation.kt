package io.github.hanihashemi.tomaten.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.hanihashemi.tomaten.MainViewModel
import io.github.hanihashemi.tomaten.data.model.Tag
import io.github.hanihashemi.tomaten.ui.screens.main.MainScreen
import io.github.hanihashemi.tomaten.ui.screens.stats.StatsData
import io.github.hanihashemi.tomaten.ui.screens.stats.StatsRange
import io.github.hanihashemi.tomaten.ui.screens.stats.StatsScreen
import io.github.hanihashemi.tomaten.ui.screens.stats.TagStats

sealed class TomatenDestination(val route: String) {
    data object Main : TomatenDestination("main")

    data object Stats : TomatenDestination("stats")
}

@Composable
fun TomatenNavigation(
    navController: NavHostController = rememberNavController(),
    viewModel: MainViewModel,
) {
    NavHost(
        navController = navController,
        startDestination = TomatenDestination.Main.route,
    ) {
        composable(TomatenDestination.Main.route) {
            MainScreen(
                onNavigateToStats = {
                    navController.navigate(TomatenDestination.Stats.route)
                },
            )
        }

        composable(TomatenDestination.Stats.route) {
            val uiState by viewModel.uiState.collectAsState()
            var selectedRange by remember { mutableStateOf(StatsRange.Day) }

            // Generate data based on selected range
            val statsData = generateSampleStatsDataForRange(uiState.tag.tags, selectedRange)

            StatsScreen(
                statsData = statsData,
                selectedRange = selectedRange,
                onTabSelected = { newRange ->
                    selectedRange = newRange
                },
                onBackPressed = {
                    navController.popBackStack()
                },
            )
        }
    }
}

// Temporary sample data generator - replace with real repository data
private fun generateSampleStatsDataForRange(
    tags: List<Tag>,
    range: StatsRange,
): StatsData {
    return when (range) {
        StatsRange.Day -> {
            val tagStats =
                tags.take(2).mapIndexed { index, tag ->
                    TagStats(
                        tag = tag,
                        durationMinutes =
                            when (index) {
                                0 -> 120 // 2h
                                1 -> 60 // 1h
                                else -> 30
                            },
                    )
                }
            StatsData(
                totalSessionsFinished = 6,
                totalActiveDays = 1,
                totalFocusTimeMinutes = tagStats.sumOf { it.durationMinutes },
                tagBreakdown = tagStats,
                finishedSessions = 5,
                abandonedSessions = 1,
            )
        }
        StatsRange.Week -> {
            val tagStats =
                tags.take(3).mapIndexed { index, tag ->
                    TagStats(
                        tag = tag,
                        durationMinutes =
                            when (index) {
                                0 -> 480 // 8h
                                1 -> 240 // 4h
                                2 -> 120 // 2h
                                else -> 60
                            },
                    )
                }
            StatsData(
                totalSessionsFinished = 28,
                totalActiveDays = 5,
                totalFocusTimeMinutes = tagStats.sumOf { it.durationMinutes },
                tagBreakdown = tagStats,
                finishedSessions = 25,
                abandonedSessions = 3,
            )
        }
        StatsRange.Month -> {
            val tagStats =
                tags.take(4).mapIndexed { index, tag ->
                    TagStats(
                        tag = tag,
                        durationMinutes =
                            when (index) {
                                0 -> 1440 // 24h
                                1 -> 720 // 12h
                                2 -> 420 // 7h
                                3 -> 270 // 4h 30m
                                else -> 120
                            },
                    )
                }
            StatsData(
                totalSessionsFinished = 95,
                totalActiveDays = 18,
                totalFocusTimeMinutes = tagStats.sumOf { it.durationMinutes },
                tagBreakdown = tagStats,
                finishedSessions = 82,
                abandonedSessions = 13,
            )
        }
        StatsRange.Year -> {
            val tagStats =
                tags.take(5).mapIndexed { index, tag ->
                    TagStats(
                        tag = tag,
                        durationMinutes =
                            when (index) {
                                0 -> 6000 // 100h
                                1 -> 3600 // 60h
                                2 -> 2400 // 40h
                                3 -> 1200 // 20h
                                4 -> 300 // 5h
                                else -> 180
                            },
                    )
                }
            StatsData(
                totalSessionsFinished = 450,
                totalActiveDays = 125,
                totalFocusTimeMinutes = tagStats.sumOf { it.durationMinutes },
                tagBreakdown = tagStats,
                finishedSessions = 390,
                abandonedSessions = 60,
            )
        }
        StatsRange.All -> {
            val tagStats =
                tags.take(5).mapIndexed { index, tag ->
                    TagStats(
                        tag = tag,
                        durationMinutes =
                            when (index) {
                                0 -> 18000 // 300h
                                1 -> 9000 // 150h
                                2 -> 6000 // 100h
                                3 -> 3000 // 50h
                                4 -> 1500 // 25h
                                else -> 600
                            },
                    )
                }
            StatsData(
                totalSessionsFinished = 1250,
                totalActiveDays = 365,
                totalFocusTimeMinutes = tagStats.sumOf { it.durationMinutes },
                tagBreakdown = tagStats,
                finishedSessions = 1050,
                abandonedSessions = 200,
            )
        }
    }
}
