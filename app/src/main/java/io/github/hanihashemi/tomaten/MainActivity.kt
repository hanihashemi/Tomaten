package io.github.hanihashemi.tomaten

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import io.github.hanihashemi.tomaten.screens.main.MainScreen
import io.github.hanihashemi.tomaten.theme.TomatenTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TomatenTheme {
                MainScreen()
            }
        }
    }
}
