package io.github.hanihashemi.tomaten.screens.main

import android.media.MediaPlayer
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import io.github.hanihashemi.tomaten.Button
import io.github.hanihashemi.tomaten.ButtonStyles
import io.github.hanihashemi.tomaten.R
import io.github.hanihashemi.tomaten.screens.main.components.TopBar
import io.github.hanihashemi.tomaten.theme.Dimens
import io.github.hanihashemi.tomaten.theme.TomatenTheme
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {
    val context = LocalContext.current

    var emote by remember { mutableStateOf<TomatoCharacterEmotes>(TomatoCharacterEmotes.Smile) }

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

                TomatoCharacterChatGptV6(
                    modifier = Modifier,
                    emote = emote,
                )

                Row {
                    Button("Smile") {
                        emote = TomatoCharacterEmotes.Smile
                    }
                    Button("Neutral") {
                        emote = TomatoCharacterEmotes.Neutral
                    }
                    Button("Sad") {
                        emote = TomatoCharacterEmotes.Sad
                    }
                    Button("Surprise") { emote = TomatoCharacterEmotes.Surprise }
                }
            }
        }
    }
}

sealed class TomatoCharacterEmotes(
    val mouthTargetValue: Float,
) {
    data object Smile : TomatoCharacterEmotes(mouthTargetValue = 0f)
    data object Neutral : TomatoCharacterEmotes(mouthTargetValue = 0.5f)
    data object Sad : TomatoCharacterEmotes(mouthTargetValue = 1f)
    data object Surprise : TomatoCharacterEmotes(mouthTargetValue = 1.5f)
}


@Composable
fun TomatoCharacterChatGptV6(modifier: Modifier, emote: TomatoCharacterEmotes) {
    // Initial sound effects
    val context = LocalContext.current
    val smilePlayer = remember {
        MediaPlayer.create(context, R.raw.smile)
    }
    val neutralPlayer = remember {
        MediaPlayer.create(context, R.raw.neutral)
    }
    val sadPlayer = remember {
        MediaPlayer.create(context, R.raw.sad)
    }
    val surprisePlayer = remember {
        MediaPlayer.create(context, R.raw.surprised)
    }

    DisposableEffect(Unit) {
        onDispose {
            smilePlayer.release()
            neutralPlayer.release()
            sadPlayer.release()
            surprisePlayer.release()
        }
    }

    var previousEmote by remember { mutableStateOf<TomatoCharacterEmotes?>(null) }
    val progress = remember { Animatable(0f) }
    val surpriseOffsetX = remember { Animatable(0f) }
    val surpriseOffsetY = remember { Animatable(0f) }
    val scale = remember { Animatable(1f) }
    val surpriseMouthScale = remember { Animatable(0.6f) }
    val surpriseMouthAlpha = remember { Animatable(0f) }
    val eyeYOffset = remember { Animatable(0f) }
    val eyeScale = remember { Animatable(1f) }

    LaunchedEffect(emote) {
        val animationDelay = if (previousEmote is TomatoCharacterEmotes.Surprise) 0 else 800


        // Reset eyes animation
        launch {
            eyeYOffset.animateTo(0f, tween(100))
            eyeScale.animateTo(1f, tween(200))
        }
        launch {
            while (true) {
                scale.animateTo(
                    targetValue = 1.02f, animationSpec = tween(durationMillis = 2000)
                )
                scale.animateTo(targetValue = 1f, animationSpec = tween(durationMillis = 2000))
            }
        }

        when (emote) {
            is TomatoCharacterEmotes.Smile -> {
                smilePlayer.start()
                progress.animateTo(emote.mouthTargetValue, animationSpec = tween(animationDelay))
            }

            is TomatoCharacterEmotes.Neutral -> {
                neutralPlayer.start()
                progress.animateTo(
                    emote.mouthTargetValue,
                    animationSpec = tween(animationDelay)
                )
            }

            is TomatoCharacterEmotes.Sad -> {
                sadPlayer.start()
                launch {
                    eyeScale.animateTo(0.75f, tween(durationMillis = 200))
                }
                launch {
                    eyeYOffset.animateTo(6f, tween(durationMillis = 400))
                }
                progress.animateTo(
                    emote.mouthTargetValue,
                    animationSpec = tween(animationDelay)
                )
            }

            is TomatoCharacterEmotes.Surprise -> {
                surprisePlayer.start()
                launch {
                    eyeScale.animateTo(1.25f, tween(durationMillis = 200))
                }
                launch {
                    surpriseMouthScale.snapTo(0.6f)
                    surpriseMouthScale.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
                    )
                }
                launch {
                    surpriseMouthAlpha.snapTo(0f)
                    surpriseMouthAlpha.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
                    )
                }
                launch {
                    // First jump up
                    surpriseOffsetY.snapTo(0f)
                    surpriseOffsetY.animateTo(-16f, tween(100, easing = FastOutSlowInEasing))
                    // Fall down
                    surpriseOffsetY.animateTo(8f, tween(100))
                    // Smaller bounce up
                    surpriseOffsetY.animateTo(-6f, tween(80))
                    // Fall again
                    surpriseOffsetY.animateTo(4f, tween(80))
                    // Settle
                    surpriseOffsetY.animateTo(0f, tween(60))
                }
                launch {
                    surpriseOffsetX.snapTo(2f)
                    surpriseOffsetX.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
                    )
                }
                progress.animateTo(
                    emote.mouthTargetValue,
                    animationSpec = tween(0)
                )
            }
        }
        previousEmote = emote
    }

    Canvas(
        modifier = modifier.size(218.dp, 220.dp)
    ) {
        withTransform({
            translate(left = surpriseOffsetX.value, top = surpriseOffsetY.value)
        }) {
            // Use this to convert dp to px inside the Canvas
            val dpToPx = { dp: Dp -> dp.toPx() }

            // Red tomato body
            val scaleFactor = scale.value

            val baseTopLeft = Offset(dpToPx(2.dp), dpToPx(43.dp))
            val baseSize = Size(dpToPx(213.dp), dpToPx(175.dp))

            // Centered scaling
            val centerOffset = Offset(
                baseTopLeft.x + baseSize.width / 2, baseTopLeft.y + baseSize.height / 2
            )

            val scaledTopLeft = Offset(
                centerOffset.x - (baseSize.width * scaleFactor) / 2,
                centerOffset.y - (baseSize.height * scaleFactor) / 2
            )
            val scaledSize = Size(
                baseSize.width * scaleFactor, baseSize.height * scaleFactor
            )

            drawOval(
                color = Color(0xFFFF4B60), topLeft = scaledTopLeft, size = scaledSize
            )

            drawOval(
                color = Color.Black,
                topLeft = scaledTopLeft,
                size = scaledSize,
                style = Stroke(width = dpToPx(3.dp))
            )

            // Leaf path
            val leafPath = Path().apply {
                moveTo(dpToPx(113.02.dp), dpToPx(32.26.dp))
                cubicTo(
                    dpToPx(114.89.dp),
                    dpToPx(24.dp),
                    dpToPx(116.2.dp),
                    dpToPx(11.31.dp),
                    dpToPx(108.59.dp),
                    dpToPx(5.67.dp)
                )
                cubicTo(
                    dpToPx(96.83.dp),
                    dpToPx((-3.06).dp),
                    dpToPx(85.2.dp),
                    dpToPx(5.49.dp),
                    dpToPx(85.2.dp),
                    dpToPx(13.58.dp)
                )
                cubicTo(
                    dpToPx(85.2.dp),
                    dpToPx(18.48.dp),
                    dpToPx(86.95.dp),
                    dpToPx(25.81.dp),
                    dpToPx(88.33.dp),
                    dpToPx(30.82.dp)
                )
                cubicTo(
                    dpToPx(88.83.dp),
                    dpToPx(32.65.dp),
                    dpToPx(86.7.dp),
                    dpToPx(34.27.dp),
                    dpToPx(85.07.dp),
                    dpToPx(33.3.dp)
                )
                cubicTo(
                    dpToPx(74.62.dp),
                    dpToPx(27.03.dp),
                    dpToPx(50.5.dp),
                    dpToPx(14.21.dp),
                    dpToPx(43.45.dp),
                    dpToPx(24.56.dp)
                )
                cubicTo(
                    dpToPx(36.78.dp),
                    dpToPx(34.36.dp),
                    dpToPx(46.04.dp),
                    dpToPx(43.6.dp),
                    dpToPx(51.44.dp),
                    dpToPx(47.82.dp)
                )
                cubicTo(
                    dpToPx(52.76.dp),
                    dpToPx(48.85.dp),
                    dpToPx(52.52.dp),
                    dpToPx(51.1.dp),
                    dpToPx(50.97.dp),
                    dpToPx(51.72.dp)
                )
                cubicTo(
                    dpToPx(37.58.dp),
                    dpToPx(57.12.dp),
                    dpToPx((-5.1).dp),
                    dpToPx(75.89.dp),
                    dpToPx(10.31.dp),
                    dpToPx(90.23.dp)
                )
                cubicTo(
                    dpToPx(20.34.dp),
                    dpToPx(99.58.dp),
                    dpToPx(41.75.dp),
                    dpToPx(85.43.dp),
                    dpToPx(49.59.dp),
                    dpToPx(79.67.dp)
                )
                cubicTo(
                    dpToPx(50.87.dp),
                    dpToPx(78.72.dp),
                    dpToPx(52.79.dp),
                    dpToPx(79.51.dp),
                    dpToPx(53.03.dp),
                    dpToPx(81.09.dp)
                )
                cubicTo(
                    dpToPx(54.4.dp),
                    dpToPx(89.98.dp),
                    dpToPx(58.98.dp),
                    dpToPx(111.75.dp),
                    dpToPx(70.72.dp),
                    dpToPx(109.73.dp)
                )
                cubicTo(
                    dpToPx(78.17.dp),
                    dpToPx(108.44.dp),
                    dpToPx(83.67.dp),
                    dpToPx(98.38.dp),
                    dpToPx(86.02.dp),
                    dpToPx(93.24.dp)
                )
                cubicTo(
                    dpToPx(86.65.dp),
                    dpToPx(91.88.dp),
                    dpToPx(88.52.dp),
                    dpToPx(91.5.dp),
                    dpToPx(89.64.dp),
                    dpToPx(92.5.dp)
                )
                cubicTo(
                    dpToPx(92.94.dp),
                    dpToPx(95.45.dp),
                    dpToPx(99.13.dp),
                    dpToPx(100.17.dp),
                    dpToPx(104.4.dp),
                    dpToPx(99.47.dp)
                )
                cubicTo(
                    dpToPx(110.27.dp),
                    dpToPx(98.68.dp),
                    dpToPx(114.65.dp),
                    dpToPx(91.18.dp),
                    dpToPx(116.8.dp),
                    dpToPx(86.6.dp)
                )
                cubicTo(
                    dpToPx(117.49.dp),
                    dpToPx(85.13.dp),
                    dpToPx(119.62.dp),
                    dpToPx(84.83.dp),
                    dpToPx(120.65.dp),
                    dpToPx(86.09.dp)
                )
                cubicTo(
                    dpToPx(125.57.dp),
                    dpToPx(92.13.dp),
                    dpToPx(137.16.dp),
                    dpToPx(104.64.dp),
                    dpToPx(145.03.dp),
                    dpToPx(99.47.dp)
                )
                cubicTo(
                    dpToPx(148.65.dp),
                    dpToPx(97.08.dp),
                    dpToPx(150.92.dp),
                    dpToPx(91.31.dp),
                    dpToPx(151.91.dp),
                    dpToPx(88.19.dp)
                )
                cubicTo(
                    dpToPx(152.27.dp),
                    dpToPx(87.09.dp),
                    dpToPx(153.44.dp),
                    dpToPx(86.42.dp),
                    dpToPx(154.56.dp),
                    dpToPx(86.73.dp)
                )
                cubicTo(
                    dpToPx(162.16.dp),
                    dpToPx(88.87.dp),
                    dpToPx(188.55.dp),
                    dpToPx(95.28.dp),
                    dpToPx(194.21.dp),
                    dpToPx(83.05.dp)
                )
                cubicTo(
                    dpToPx(201.33.dp),
                    dpToPx(67.68.dp),
                    dpToPx(167.72.dp),
                    dpToPx(55.15.dp),
                    dpToPx(156.24.dp),
                    dpToPx(51.38.dp)
                )
                cubicTo(
                    dpToPx(154.69.dp),
                    dpToPx(50.87.dp),
                    dpToPx(154.28.dp),
                    dpToPx(48.77.dp),
                    dpToPx(155.49.dp),
                    dpToPx(47.67.dp)
                )
                cubicTo(
                    dpToPx(161.23.dp),
                    dpToPx(42.52.dp),
                    dpToPx(172.9.dp),
                    dpToPx(30.55.dp),
                    dpToPx(169.08.dp),
                    dpToPx(21.48.dp)
                )
                cubicTo(
                    dpToPx(161.18.dp),
                    dpToPx(2.72.dp),
                    dpToPx(130.3.dp),
                    dpToPx(24.34.dp),
                    dpToPx(116.98.dp),
                    dpToPx(34.85.dp)
                )
                cubicTo(
                    dpToPx(115.35.dp),
                    dpToPx(36.15.dp),
                    dpToPx(112.56.dp),
                    dpToPx(34.29.dp),
                    dpToPx(113.02.dp),
                    dpToPx(32.26.dp)
                )
                close()
            }
            val leafCenter = Offset(dpToPx(110.dp), dpToPx(60.dp))

            withTransform({
                scale(scale.value, pivot = leafCenter)
            }) {
                drawPath(path = leafPath, color = Color.Black, style = Stroke(width = dpToPx(6.dp)))
                drawPath(path = leafPath, color = Color(0xFF5AAA82))
            }

            // Eyes
            val baseEyeRadius = dpToPx(11.dp)
            val leftEyeCenter =
                Offset(dpToPx(73.dp), dpToPx(148.29.dp) + eyeYOffset.value)
            val rightEyeCenter =
                Offset(dpToPx(145.dp), dpToPx(148.29.dp) + eyeYOffset.value)

            drawCircle(
                color = Color(0xFF191713),
                radius = baseEyeRadius * eyeScale.value,
                center = leftEyeCenter
            )
            drawCircle(
                color = Color(0xFF191713),
                radius = baseEyeRadius * eyeScale.value,
                center = rightEyeCenter
            )

            // Morph between smile, neutral, and sad mouths
            val p = progress.value

            if (p > 1f) {
                // Mouth surprised
                val xOffset = -6.dp.toPx() // Move 10dp to the left
                val yOffset = -2.dp.toPx()   // No vertical movement

                val lipPath = Path().apply {
                    moveTo(128.dp.toPx() + xOffset, 176.dp.toPx() + yOffset)
                    cubicTo(
                        134.dp.toPx() + xOffset,
                        183.dp.toPx() + yOffset,
                        134.dp.toPx() + xOffset,
                        194.dp.toPx() + yOffset,
                        125.dp.toPx() + xOffset,
                        196.dp.toPx() + yOffset
                    )
                    cubicTo(
                        122.dp.toPx() + xOffset,
                        197.dp.toPx() + yOffset,
                        119.dp.toPx() + xOffset,
                        198.dp.toPx() + yOffset,
                        116.dp.toPx() + xOffset,
                        198.dp.toPx() + yOffset
                    )
                    cubicTo(
                        113.dp.toPx() + xOffset,
                        198.dp.toPx() + yOffset,
                        110.dp.toPx() + xOffset,
                        197.dp.toPx() + yOffset,
                        107.dp.toPx() + xOffset,
                        196.dp.toPx() + yOffset
                    )
                    cubicTo(
                        98.dp.toPx() + xOffset,
                        194.dp.toPx() + yOffset,
                        98.dp.toPx() + xOffset,
                        183.dp.toPx() + yOffset,
                        104.dp.toPx() + xOffset,
                        176.dp.toPx() + yOffset
                    )
                    cubicTo(
                        111.dp.toPx() + xOffset,
                        170.dp.toPx() + yOffset,
                        121.dp.toPx() + xOffset,
                        170.dp.toPx() + yOffset,
                        128.dp.toPx() + xOffset,
                        176.dp.toPx() + yOffset
                    )
                    close()
                }

                withTransform({
                    scale(surpriseMouthScale.value, pivot = Offset(dpToPx(116.dp), dpToPx(185.dp)))
                }) {
                    drawPath(
                        path = lipPath,
                        color = Color(0xFF191713).copy(alpha = surpriseMouthAlpha.value)
                    )
                }
            } else {
                // Control points for a smile mouth curve
                val xMOffset = -6.dp.toPx() // Move left
                val yMOffset = 0.dp.toPx()   // No vertical move

                // Control points for a smile mouth curve
                val smileCP1 = Offset(111.dp.toPx() + xMOffset, 194.dp.toPx() + yMOffset)
                val smileCP2 = Offset(119.dp.toPx() + xMOffset, 194.dp.toPx() + yMOffset)

                // Control points for neutral (straight line) mouth
                val neutralCP1 = Offset(111.dp.toPx() + xMOffset, 187.dp.toPx() + yMOffset)
                val neutralCP2 = Offset(119.dp.toPx() + xMOffset, 187.dp.toPx() + yMOffset)

                // Control points for a sad mouth curve
                val sadCP1 = Offset(111.dp.toPx() + xMOffset, 179.dp.toPx() + yMOffset)
                val sadCP2 = Offset(119.dp.toPx() + xMOffset, 178.dp.toPx() + yMOffset)

                // If p is between 0 and 0.5, interpolate smile -> neutral
                // If p is between 0.5 and 1, interpolate neutral -> sad
                val control1 = if (p < 0.5f) lerp(smileCP1, neutralCP1, p * 2f)
                else lerp(neutralCP1, sadCP1, (p - 0.5f) * 2f)

                val control2 = if (p < 0.5f) lerp(smileCP2, neutralCP2, p * 2f)
                else lerp(neutralCP2, sadCP2, (p - 0.5f) * 2f)

                // Start and end points for the mouth path (vertical lerp included)
                val start = Offset(
                    97.dp.toPx() + xMOffset,
                    lerp(185.dp.toPx(), 190.dp.toPx(), p) + yMOffset
                )
                val end = Offset(
                    134.dp.toPx() + xMOffset,
                    lerp(185.dp.toPx(), 190.dp.toPx(), p) + yMOffset
                )

                val mouthPath = Path().apply {
                    moveTo(start.x, start.y)
                    cubicTo(control1.x, control1.y, control2.x, control2.y, end.x, end.y)
                }

                drawPath(
                    path = mouthPath,
                    color = Color.Black,
                    style = Stroke(width = 20f, cap = StrokeCap.Round)
                )
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