package io.github.hanihashemi.tomaten.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.github.hanihashemi.tomaten.Button
import io.github.hanihashemi.tomaten.ButtonStyles
import io.github.hanihashemi.tomaten.MainViewModel
import io.github.hanihashemi.tomaten.theme.Dimens
import io.github.hanihashemi.tomaten.ui.dialogs.login.LoginDialog
import io.github.hanihashemi.tomaten.ui.screens.main.components.TomiEmotes
import io.github.hanihashemi.tomaten.ui.screens.main.components.Tomi
import io.github.hanihashemi.tomaten.ui.screens.main.components.TopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(

) {
    val context = LocalContext.current
    val viewModel: MainViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val actions = viewModel.actions

    var emote by remember { mutableStateOf<TomiEmotes>(TomiEmotes.Smile) }
    var isZoomed by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopBar(actions, uiState) },
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

                Tomi(
                    modifier = Modifier,
                    emote = emote,
                    isZoomed = isZoomed,
                )

                Row {
                    Button("Toggle Zoom") {
                        isZoomed = !isZoomed
                    }
                    Button("Smile") {
                        emote = TomiEmotes.Smile
                    }
                    Button("Neutral") {
                        emote = TomiEmotes.Neutral
                    }
                    Button("Sad") {
                        emote = TomiEmotes.Sad
                    }
                    Button("Surprise") { emote = TomiEmotes.Surprise }
                }
            }
        }

        LoginDialog(
            uiState = uiState,
            actions = actions,
        )
    }
}

