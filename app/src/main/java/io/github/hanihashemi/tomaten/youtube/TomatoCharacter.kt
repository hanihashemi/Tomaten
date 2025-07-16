package io.github.hanihashemi.tomaten.youtube

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Tomato Character Version 1
 *
 * Here I have send the design and asked ChatGpt to implement it.
 * As you see the result isn't good :D.
 */
@Suppress("DEPRECATION")
@Composable
fun TomatoCharacterChatGptV1() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val center = Offset(x = canvasWidth / 2, y = canvasHeight / 2)
        val tomatoRadius = canvasWidth.coerceAtMost(canvasHeight) / 4

        // Draw tomato body
        drawCircle(
            color = Color(0xFFFF4F64), // Tomato red
            radius = tomatoRadius,
            center = center
        )

        // Draw green leafy top
        val leafPath = Path().apply {
            moveTo(center.x, center.y - tomatoRadius)
            relativeCubicTo(-30f, -60f, -100f, -30f, -60f, 0f)
            relativeCubicTo(-40f, 10f, -40f, 50f, -10f, 30f)
            relativeCubicTo(10f, 10f, 30f, 0f, 40f, -10f)
            relativeCubicTo(20f, 30f, 60f, 20f, 40f, -10f)
            relativeCubicTo(20f, 0f, 30f, -30f, -10f, -40f)
            close()
        }
        drawPath(
            path = leafPath,
            color = Color(0xFF4CAF50) // Green
        )

        // Draw eyes
        val eyeOffsetX = tomatoRadius / 2
        val eyeOffsetY = tomatoRadius / 4
        val eyeRadius = 12f

        drawCircle(
            color = Color.Black,
            radius = eyeRadius,
            center = Offset(center.x - eyeOffsetX / 2, center.y - eyeOffsetY)
        )

        drawCircle(
            color = Color.Black,
            radius = eyeRadius,
            center = Offset(center.x + eyeOffsetX / 2, center.y - eyeOffsetY)
        )

        // Draw smiling mouth
        val mouthWidth = tomatoRadius / 2
        val mouthHeight = 20f

        val mouthPath = Path().apply {
            moveTo(center.x - mouthWidth / 2, center.y + eyeOffsetY)
            quadraticBezierTo(
                center.x,
                center.y + eyeOffsetY + mouthHeight,
                center.x + mouthWidth / 2,
                center.y + eyeOffsetY
            )
        }
        drawPath(
            path = mouthPath,
            color = Color.Black,
            style = Stroke(width = 8f)
        )
    }
}

/**
 * Tomato Character Version 2
 *
 * Here I have send the vector code which I got from AndroidStudio and asked ChatGpt to implement it.
 * Result is acceptable but not the best because it has used drawPath instead of drawCircle.
 */
@Composable
fun TomatoCharacterChatGptV2() {
    Canvas(modifier = Modifier.size(100.dp, 100.dp)) {
        // Red tomato body
        val tomatoPath = Path().apply {
            moveTo(108.5f, 43f)
            cubicTo(167.7f, 43f, 215f, 82.52f, 215f, 130.5f)
            cubicTo(215f, 178.48f, 167.7f, 218f, 108.5f, 218f)
            cubicTo(49.3f, 218f, 2f, 178.48f, 2f, 130.5f)
            cubicTo(2f, 82.52f, 49.3f, 43f, 108.5f, 43f)
            close()
        }
        drawPath(
            path = tomatoPath,
            color = Color(0xFFFF4B60),
            style = Stroke(width = 4f)
        )
        drawPath(
            path = tomatoPath,
            color = Color(0xFFFF4B60)
        )

        // Green leafy top
        val leafPath = Path().apply {
            moveTo(113.02f, 32.26f)
            cubicTo(114.89f, 24f, 116.2f, 11.31f, 108.59f, 5.67f)
            cubicTo(96.83f, -3.06f, 85.2f, 5.49f, 85.2f, 13.58f)
            cubicTo(85.2f, 18.48f, 86.95f, 25.81f, 88.33f, 30.82f)
            cubicTo(88.83f, 32.65f, 86.7f, 34.27f, 85.07f, 33.3f)
            cubicTo(74.62f, 27.03f, 50.5f, 14.21f, 43.45f, 24.56f)
            cubicTo(36.78f, 34.36f, 46.04f, 43.6f, 51.44f, 47.82f)
            cubicTo(52.76f, 48.85f, 52.52f, 51.1f, 50.97f, 51.72f)
            cubicTo(37.58f, 57.12f, -5.1f, 75.89f, 10.31f, 90.23f)
            cubicTo(20.34f, 99.58f, 41.75f, 85.43f, 49.59f, 79.67f)
            cubicTo(50.87f, 78.72f, 52.79f, 79.51f, 53.03f, 81.09f)
            cubicTo(54.4f, 89.98f, 58.98f, 111.75f, 70.72f, 109.73f)
            cubicTo(78.17f, 108.44f, 83.67f, 98.38f, 86.02f, 93.24f)
            cubicTo(86.65f, 91.88f, 88.52f, 91.5f, 89.64f, 92.5f)
            cubicTo(92.94f, 95.45f, 99.13f, 100.17f, 104.4f, 99.47f)
            cubicTo(110.27f, 98.68f, 114.65f, 91.18f, 116.8f, 86.6f)
            cubicTo(117.49f, 85.13f, 119.62f, 84.83f, 120.65f, 86.09f)
            cubicTo(125.57f, 92.13f, 137.16f, 104.64f, 145.03f, 99.47f)
            cubicTo(148.65f, 97.08f, 150.92f, 91.31f, 151.91f, 88.19f)
            cubicTo(152.27f, 87.09f, 153.44f, 86.42f, 154.56f, 86.73f)
            cubicTo(162.16f, 88.87f, 188.55f, 95.28f, 194.21f, 83.05f)
            cubicTo(201.33f, 67.68f, 167.72f, 55.15f, 156.24f, 51.38f)
            cubicTo(154.69f, 50.87f, 154.28f, 48.77f, 155.49f, 47.67f)
            cubicTo(161.23f, 42.52f, 172.9f, 30.55f, 169.08f, 21.48f)
            cubicTo(161.18f, 2.72f, 130.3f, 24.34f, 116.98f, 34.85f)
            cubicTo(115.35f, 36.15f, 112.56f, 34.29f, 113.02f, 32.26f)
            close()
        }
        drawPath(
            path = leafPath,
            color = Color(0xFF5AAA82),
            style = Stroke(width = 4f)
        )
        drawPath(
            path = leafPath,
            color = Color(0xFF5AAA82)
        )

        // Eyes
        drawCircle(
            color = Color(0xFF191713),
            radius = 10f,
            center = Offset(73f, 148.29f)
        )
        drawCircle(
            color = Color(0xFF191713),
            radius = 10f,
            center = Offset(145f, 148.29f)
        )

        // Smiling mouth
        val mouthPath = Path().apply {
            moveTo(92.88f, 165.08f)
            cubicTo(97.51f, 168.68f, 103.22f, 170.66f, 109.14f, 170.66f)
            cubicTo(115.07f, 170.66f, 120.78f, 168.68f, 125.41f, 165.08f)
        }
        drawPath(
            path = mouthPath,
            color = Color(0xFF191713),
            style = Stroke(width = 8f)
        )
    }
}

/**
 * Tomato Character Version 3
 *
 * Asked chat-gpt to use drawCircle, drawOval instead of drawPath to make it easier to add animation.
 */
@Composable
fun TomatoCharacterChatGptV3() {
    Canvas(modifier = Modifier.size(100.dp, 100.dp)) {

        // Red tomato body
        drawOval(
            color = Color(0xFFFF4B60),
            topLeft = Offset(2f, 43f),
            size = androidx.compose.ui.geometry.Size(213f, 175f)
        )

        drawOval(
            color = Color(0xFF000000),
            topLeft = Offset(2f, 43f),
            size = androidx.compose.ui.geometry.Size(213f, 175f),
            style = Stroke(width = 3f)
        )

        // Green leafy top
        val leafPath = Path().apply {
            moveTo(113.02f, 32.26f)
            cubicTo(114.89f, 24f, 116.2f, 11.31f, 108.59f, 5.67f)
            cubicTo(96.83f, -3.06f, 85.2f, 5.49f, 85.2f, 13.58f)
            cubicTo(85.2f, 18.48f, 86.95f, 25.81f, 88.33f, 30.82f)
            cubicTo(88.83f, 32.65f, 86.7f, 34.27f, 85.07f, 33.3f)
            cubicTo(74.62f, 27.03f, 50.5f, 14.21f, 43.45f, 24.56f)
            cubicTo(36.78f, 34.36f, 46.04f, 43.6f, 51.44f, 47.82f)
            cubicTo(52.76f, 48.85f, 52.52f, 51.1f, 50.97f, 51.72f)
            cubicTo(37.58f, 57.12f, -5.1f, 75.89f, 10.31f, 90.23f)
            cubicTo(20.34f, 99.58f, 41.75f, 85.43f, 49.59f, 79.67f)
            cubicTo(50.87f, 78.72f, 52.79f, 79.51f, 53.03f, 81.09f)
            cubicTo(54.4f, 89.98f, 58.98f, 111.75f, 70.72f, 109.73f)
            cubicTo(78.17f, 108.44f, 83.67f, 98.38f, 86.02f, 93.24f)
            cubicTo(86.65f, 91.88f, 88.52f, 91.5f, 89.64f, 92.5f)
            cubicTo(92.94f, 95.45f, 99.13f, 100.17f, 104.4f, 99.47f)
            cubicTo(110.27f, 98.68f, 114.65f, 91.18f, 116.8f, 86.6f)
            cubicTo(117.49f, 85.13f, 119.62f, 84.83f, 120.65f, 86.09f)
            cubicTo(125.57f, 92.13f, 137.16f, 104.64f, 145.03f, 99.47f)
            cubicTo(148.65f, 97.08f, 150.92f, 91.31f, 151.91f, 88.19f)
            cubicTo(152.27f, 87.09f, 153.44f, 86.42f, 154.56f, 86.73f)
            cubicTo(162.16f, 88.87f, 188.55f, 95.28f, 194.21f, 83.05f)
            cubicTo(201.33f, 67.68f, 167.72f, 55.15f, 156.24f, 51.38f)
            cubicTo(154.69f, 50.87f, 154.28f, 48.77f, 155.49f, 47.67f)
            cubicTo(161.23f, 42.52f, 172.9f, 30.55f, 169.08f, 21.48f)
            cubicTo(161.18f, 2.72f, 130.3f, 24.34f, 116.98f, 34.85f)
            cubicTo(115.35f, 36.15f, 112.56f, 34.29f, 113.02f, 32.26f)
            close()
        }
        drawPath(
            path = leafPath,
            color = Color(0xFF000000),
            style = Stroke(width = 6f)
        )
        drawPath(
            path = leafPath,
            color = Color(0xFF5AAA82)
        )

        // Eyes
        drawCircle(
            color = Color(0xFF191713),
            radius = 10f,
            center = Offset(73f, 148.29f)
        )
        drawCircle(
            color = Color(0xFF191713),
            radius = 10f,
            center = Offset(145f, 148.29f)
        )

        // Smiling mouth
        val mouthPath = Path().apply {
            moveTo(130.24f, 165.24f)
            cubicTo(131.53f, 166.53f, 131.54f, 168.64f, 130.12f, 169.8f)
            cubicTo(124.23f, 174.62f, 116.82f, 177.29f, 109.14f, 177.29f)
            cubicTo(101.46f, 177.29f, 94.06f, 174.62f, 88.16f, 169.8f)
            cubicTo(86.75f, 168.64f, 86.76f, 166.53f, 88.05f, 165.24f)
            cubicTo(89.35f, 163.94f, 91.43f, 163.96f, 92.88f, 165.08f)
            cubicTo(97.51f, 168.68f, 103.22f, 170.66f, 109.14f, 170.66f)
            cubicTo(115.07f, 170.66f, 120.78f, 168.68f, 125.41f, 165.08f)
            cubicTo(126.86f, 163.96f, 128.94f, 163.94f, 130.24f, 165.24f)
            close()
        }

        drawPath(
            path = mouthPath,
            color = Color(0xFF191713) // black
        )
    }
}

/**
 * Tomato Character Version 4
 *
 * Here I have replaced the sizes to dp, so we can change it size with the container easier.
 */
@Composable
fun TomatoCharacterChatGptV4(modifier: Modifier) {
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

        // Mouth
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

@Preview
@Composable
private fun TomatoPreview_V1() {
    TomatoCharacterChatGptV1()
}

@Preview
@Composable
private fun TomatoPreview_V2() {
    TomatoCharacterChatGptV2()
}

@Preview
@Composable
private fun TomatoPreview_V3() {
    TomatoCharacterChatGptV3()
}

@Preview
@Composable
private fun TomatoPreview_V4() {
    TomatoCharacterChatGptV4(modifier = Modifier)
}