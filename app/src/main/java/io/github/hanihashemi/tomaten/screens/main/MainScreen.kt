package io.github.hanihashemi.tomaten.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.hanihashemi.tomaten.Button
import io.github.hanihashemi.tomaten.ButtonStyles
import io.github.hanihashemi.tomaten.screens.main.components.TopBar
import io.github.hanihashemi.tomaten.theme.Dimens
import io.github.hanihashemi.tomaten.theme.TomatenTheme

@Composable
fun MainScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopBar() },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(Dimens.PaddingNormal),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button("Start", modifier = Modifier.widthIn(180.dp)) { }
                Button("Focus", style = ButtonStyles.Secondary) { }
            }
        }
    }
}

@Composable
private fun MainScreenPreview() {
    TomatenTheme {
        MainScreen()
    }
}

@Preview(device = "id:small_phone")
@Composable
private fun SmallPreview() = MainScreenPreview()

@Preview(device = "id:medium_phone")
@Composable
private fun MediumPreview() = MainScreenPreview()

@Preview(device = "id:pixel_9_pro_xl")
@Composable
private fun PixelProPreview() = MainScreenPreview()