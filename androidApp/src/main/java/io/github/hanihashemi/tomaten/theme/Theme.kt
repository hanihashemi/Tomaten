package io.github.hanihashemi.tomaten.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme =
    lightColorScheme(
        primary = PrimaryColor,
        surface = SurfaceColor,
        onSurface = OnSurfaceColor,
        onBackground = OnBackgroundColor,
        secondaryContainer = SecondaryContainerColor,
        onSurfaceVariant = OnSurfaceVariantColor,
    )

@Suppress("FunctionName")
@Composable
fun TomatenTheme(content: @Composable () -> Unit) {
//    val view = LocalView.current
//    if (!view.isInEditMode) {
//        SideEffect {
//            (view.context as Activity).window.statusBarColor = LightColorScheme.primary.toArgb()
//            (view.context as Activity).window.navigationBarColor =
//                AndroidNavigationBarColor.toArgb()
//            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = false
//        }
//    }

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content,
    )
}
