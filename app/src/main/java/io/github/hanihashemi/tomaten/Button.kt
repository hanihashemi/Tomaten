package io.github.hanihashemi.tomaten

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.hanihashemi.tomaten.theme.Dimens
import io.github.hanihashemi.tomaten.theme.OnSurfaceColor
import io.github.hanihashemi.tomaten.theme.Shapes
import io.github.hanihashemi.tomaten.theme.TomatenTheme
import io.github.hanihashemi.tomaten.theme.Typography
import io.github.hanihashemi.tomaten.util.VibrateUtil

@Composable
fun Button(
    text: String,
    modifier: Modifier = Modifier,
    style: ButtonStyle = ButtonStyles.Primary,
    onClick: () -> Unit,
) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Release -> {
                    VibrateUtil.vibrate(context)
                }
            }
        }
    }

    Box(
        modifier = modifier
            .clip(Shapes.large)
            .border(style.border, CircleShape)
            .background(color = style.backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(
                    color = style.rippleColor,
                )
            ) { onClick() }
            .padding(Dimens.PaddingNormal),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = style.textStyle,
        )
    }
}

data class ButtonStyle(
    val backgroundColor: Color = Color.Transparent,
    val textStyle: TextStyle = Typography.bodyMedium,
    val rippleColor: Color = OnSurfaceColor,
    val border: BorderStroke = BorderStroke(
        width = 1.dp,
        color = OnSurfaceColor,
    ),
)

object ButtonStyles {
    val Primary = ButtonStyle()

    val Secondary = ButtonStyle(
        border = BorderStroke(0.dp, Color.Transparent),
        rippleColor = Color.DarkGray
    )
}

@Preview(showBackground = true)
@Composable
private fun ButtonPrimaryPreview() {
    TomatenTheme {
        Button("Primary Button") { }
    }
}

@Preview(showBackground = true)
@Composable
private fun ButtonSecondaryPreview() {
    TomatenTheme {
        Button("Secondary Button", style = ButtonStyles.Secondary) { }
    }
}