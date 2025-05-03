package io.github.hanihashemi.tomaten.screens.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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

                TomatoCharacterChatGptV5(
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
fun TomatoCharacterChatGptV5(modifier: Modifier) {
    Canvas(
        modifier = modifier
            .size(218.dp, 220.dp)
            .border(width = 1.dp, color = Color.Red)
            .background(Color.Green)
    ) {
        // Use this to convert dp to px inside the Canvas
        val dpToPx = { dp: Dp -> dp.toPx() }

        // Red tomato body
        drawOval(
            color = Color(0xFFFF4B60),
            topLeft = Offset(dpToPx(2.dp), dpToPx(43.dp)),
            size = Size(dpToPx(213.dp), dpToPx(175.dp))
        )

        drawOval(
            color = Color.Black,
            topLeft = Offset(dpToPx(2.dp), dpToPx(43.dp)),
            size = Size(dpToPx(213.dp), dpToPx(175.dp)),
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
        drawPath(path = leafPath, color = Color.Black, style = Stroke(width = dpToPx(6.dp)))
        drawPath(path = leafPath, color = Color(0xFF5AAA82))

        // Eyes
        drawCircle(
            color = Color(0xFF191713),
            radius = dpToPx(10.dp),
            center = Offset(dpToPx(73.dp), dpToPx(148.29.dp))
        )
        drawCircle(
            color = Color(0xFF191713),
            radius = dpToPx(10.dp),
            center = Offset(dpToPx(145.dp), dpToPx(148.29.dp))
        )

        // Mouth smile
        val mouthPath = Path().apply {
            moveTo(dpToPx(130.24.dp), dpToPx(165.24.dp))
            cubicTo(
                dpToPx(131.53.dp),
                dpToPx(166.53.dp),
                dpToPx(131.54.dp),
                dpToPx(168.64.dp),
                dpToPx(130.12.dp),
                dpToPx(169.8.dp)
            )
            cubicTo(
                dpToPx(124.23.dp),
                dpToPx(174.62.dp),
                dpToPx(116.82.dp),
                dpToPx(177.29.dp),
                dpToPx(109.14.dp),
                dpToPx(177.29.dp)
            )
            cubicTo(
                dpToPx(101.46.dp),
                dpToPx(177.29.dp),
                dpToPx(94.06.dp),
                dpToPx(174.62.dp),
                dpToPx(88.16.dp),
                dpToPx(169.8.dp)
            )
            cubicTo(
                dpToPx(86.75.dp),
                dpToPx(168.64.dp),
                dpToPx(86.76.dp),
                dpToPx(166.53.dp),
                dpToPx(88.05.dp),
                dpToPx(165.24.dp)
            )
            cubicTo(
                dpToPx(89.35.dp),
                dpToPx(163.94.dp),
                dpToPx(91.43.dp),
                dpToPx(163.96.dp),
                dpToPx(92.88.dp),
                dpToPx(165.08.dp)
            )
            cubicTo(
                dpToPx(97.51.dp),
                dpToPx(168.68.dp),
                dpToPx(103.22.dp),
                dpToPx(170.66.dp),
                dpToPx(109.14.dp),
                dpToPx(170.66.dp)
            )
            cubicTo(
                dpToPx(115.07.dp),
                dpToPx(170.66.dp),
                dpToPx(120.78.dp),
                dpToPx(168.68.dp),
                dpToPx(125.41.dp),
                dpToPx(165.08.dp)
            )
            cubicTo(
                dpToPx(126.86.dp),
                dpToPx(163.96.dp),
                dpToPx(128.94.dp),
                dpToPx(163.94.dp),
                dpToPx(130.24.dp),
                dpToPx(165.24.dp)
            )
            close()
        }
        drawPath(path = mouthPath, color = Color(0xFF191713))
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