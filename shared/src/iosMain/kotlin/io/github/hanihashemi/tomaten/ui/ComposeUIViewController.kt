package io.github.hanihashemi.tomaten

import androidx.compose.ui.window.ComposeUIViewController
import io.github.hanihashemi.tomaten.ui.screens.main.IOSMainScreenWithViewModel
import platform.UIKit.UIViewController

fun createMainViewController(): UIViewController {
    return ComposeUIViewController {
        IOSMainScreenWithViewModel()
    }
}