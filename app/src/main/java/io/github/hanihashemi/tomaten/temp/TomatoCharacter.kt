package io.github.hanihashemi.tomaten.temp

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke

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

@Composable
fun TomatoCharacterChatGptV2() {
    Canvas(modifier = Modifier.fillMaxSize()) {
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

@Composable
fun TomatoCharacterChatGptV3() {
    Canvas(modifier = Modifier.fillMaxSize()) {

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