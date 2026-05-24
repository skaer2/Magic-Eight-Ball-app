package org.example.project.main.ui

import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import magiceight_ball.composeapp.generated.resources.Res
import magiceight_ball.composeapp.generated.resources.ball_image
import org.example.project.main.presentation.Ball
import org.jetbrains.compose.resources.painterResource
import kotlin.random.Random

private val SHAKE_POSITION_TOP = Offset(-50f, -100f)
private val SHAKE_POSITION_BOTTOM = Offset(50f, 100f)

@Composable
fun Ball(ball: Ball, onShakingEnd: () -> Unit) {
    when (ball) {
        is Ball.Showing -> TextBall(ball.text)

        Ball.Shaking -> ShakingBall(onShakingEnd)
    }
}

@Composable
private fun ShakingBall(onShakingEnd: () -> Unit) {
    var shakeOffset by remember { mutableStateOf(Offset.Zero) }
    val shakeAnimateOffsetState by animateOffsetAsState(
        shakeOffset, tween(80)
    )
    BaseBall(
        modifier = Modifier.graphicsLayer {
            translationX = shakeAnimateOffsetState.x
            translationY = shakeAnimateOffsetState.y
        }
    )

    LaunchedEffect(Unit) {
        repeat(5) {
            val shakeStrength = Random.nextInt(0, 30)
            shakeOffset = SHAKE_POSITION_TOP.copy(
                x = SHAKE_POSITION_TOP.x - shakeStrength, y = SHAKE_POSITION_TOP.y - shakeStrength
            )
            delay(100)
            shakeOffset = SHAKE_POSITION_BOTTOM.copy(
                x = SHAKE_POSITION_BOTTOM.x + shakeStrength, y = SHAKE_POSITION_BOTTOM.y + shakeStrength
            )
            delay(100)
        }
        shakeOffset = Offset.Zero
        delay(10)
        onShakingEnd()
    }
}

@Composable
private fun TextBall(text: String) {
    Box(contentAlignment = Alignment.Center) {
        BaseBall()
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 140.dp)
                .size(height = 110.dp, width = 130.dp),
        ){
            Text(
                text = text,
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun BaseBall(modifier: Modifier = Modifier) {
    // Отрисовка базового изображения шара с помощью картинки взятой из ресурсов приложения
    Image(
        painter = painterResource(Res.drawable.ball_image),
        contentDescription = null,
        modifier = modifier.size(350.dp)
    )
}

