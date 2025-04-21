package io.github.hanihashemi.tomaten.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.hanihashemi.tomaten.theme.TomatenTheme

@Composable
fun MainScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text(
                text = "Hello Hani!",
            )
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