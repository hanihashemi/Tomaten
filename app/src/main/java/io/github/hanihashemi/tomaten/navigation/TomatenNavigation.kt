package io.github.hanihashemi.tomaten.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

            // TODO: Replace with real data from repository
            val sampleStatsData = generateSampleStatsData(uiState.tag.tags)

            StatsScreen(
                statsData = sampleStatsData,
                selectedRange = StatsRange.Day,
                onTabSelected = { /* TODO: Implement stats filtering */ },
                onBackPressed = {
                    navController.popBackStack()
                },
            )
        }
    }
}

// Temporary sample data generator - replace with real repository data
private fun generateSampleStatsData(tags: List<Tag>): StatsData {
    val tagStats =
        tags.take(4).mapIndexed { index, tag ->
            TagStats(
                tag = tag,
                durationMinutes =
                    when (index) {
                        0 -> 1093 // 18h 13m
                        1 -> 240 // 4h
                        2 -> 120 // 2h
                        3 -> 60 // 1h
                        else -> 30
                    },
            )
        }

    return StatsData(
        totalSessionsFinished = 42,
        totalActiveDays = 7,
        totalFocusTimeMinutes = tagStats.sumOf { it.durationMinutes },
        tagBreakdown = tagStats,
        finishedSessions = 38,
        abandonedSessions = 4,
    )
}
