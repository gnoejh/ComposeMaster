package com.example.animations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.animations.ui.theme.ComposeMasterTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeMasterTheme {
                DemoApp()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoApp(modifier: Modifier = Modifier) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Float", "Color", "Dp", "Visibility", "Draggable", "Infinite", "Lab", "Lab2")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Jetpack Compose Animations") }
            )
        },
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, label ->
                    NavigationBarItem(
                        selected = (selectedTab == index),
                        onClick = { selectedTab = index },
                        label = { Text(label) },
                        icon = {}
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (selectedTab) {
                0 -> FloatAnimationScreen()
                1 -> ColorAnimationScreen()
                2 -> DpAnimationScreen()
                3 -> VisibilityAnimationScreen()
                4 -> DraggableAnimationScreen()
                5 -> InfiniteAnimationScreen()
                6 -> Lab()
                7 -> Lab2()
            }
        }
    }
}

/* ============================================================
   1. FloatAnimationScreen - animateFloatAsState
   ============================================================ */
@Composable
fun FloatAnimationScreen() {
    var expanded by remember { mutableStateOf(false) }

    val size by animateFloatAsState(
        targetValue = if (expanded) 200f else 50f,
        animationSpec = tween(durationMillis = 1000)
    )

    Box(
        modifier = Modifier
            .background(color = Color.Gray)
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(size.dp)
                .background(Color.Blue)
                .clickable { expanded = !expanded }
        )
    }
}

/* ============================================================
   2. ColorAnimationScreen - animateColorAsState
   ============================================================ */
@Composable
fun ColorAnimationScreen() {
    var isRed by remember { mutableStateOf(true) }

    val color by animateColorAsState(
        targetValue = if (isRed) Color.Red else Color.Green,
        animationSpec = tween(durationMillis = 800)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .clickable { isRed = !isRed },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Tap to change color",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
    }
}

/* ============================================================
   3. DpAnimationScreen - animateDpAsState
   ============================================================ */
@Composable
fun DpAnimationScreen() {
    var expanded by remember { mutableStateOf(false) }

    val padding by animateDpAsState(
        targetValue = if (expanded) 32.dp else 8.dp,
        animationSpec = spring(stiffness = Spring.StiffnessMedium)
    )

    Column(
        modifier = Modifier
            .background(color = Color.LightGray)
            .fillMaxSize()
            .padding(padding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.Magenta)
                .clickable { expanded = !expanded }
        )
        Spacer(Modifier.height(8.dp))
        Text("Tap the box to animate padding", textAlign = TextAlign.Center)
    }
}

/* ============================================================
   4. VisibilityAnimationScreen - AnimatedVisibility
   ============================================================ */
@Composable
fun VisibilityAnimationScreen() {
    var visible by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.LightGray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { visible = !visible }) {
            Text("Toggle Visibility")
        }
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Cyan)
            )
        }
    }
}

/* ============================================================
   5. DraggableAnimationScreen - pointerInput gestures
   ============================================================ */
@Composable
fun DraggableAnimationScreen() {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier.fillMaxSize().background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.toInt(), offsetY.toInt()) }
                .size(100.dp)
                .background(Color.Blue)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                }
        )
    }
}

/* ============================================================
   6. InfiniteAnimationScreen - rememberInfiniteTransition
   ============================================================ */
@Composable
fun InfiniteAnimationScreen() {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.Blue.copy(alpha = alpha))
        )
        Text(
            text = "Pulsing Animation",
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}


/* ============================================================
   7. LabGameScreen - Simple "Bouncing Ball" Game
   ============================================================ */
@Composable
fun Lab() {
    // Container dimensions
    var containerWidth by remember { mutableStateOf(0) }
    var containerHeight by remember { mutableStateOf(0) }

    // Ball properties
    var x by remember { mutableStateOf(100f) }
    var y by remember { mutableStateOf(100f) }
    var dx by remember { mutableStateOf(5f) }
    var dy by remember { mutableStateOf(5f) }
    val radius = 50f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF222222))
            .onGloballyPositioned { coordinates ->
                containerWidth = coordinates.size.width
                containerHeight = coordinates.size.height
            }
    ) {
        // Launch a coroutine to update ball position repeatedly
        val coroutineScope = rememberCoroutineScope()
        LaunchedEffect(Unit) {
            coroutineScope.launch {
                while (true) {
                    // Update position
                    x += dx
                    y += dy

                    // Check horizontal bounds
                    if (x - radius < 0) {
                        x = radius
                        dx = -dx
                    } else if (x + radius > containerWidth) {
                        x = (containerWidth - radius)
                        dx = -dx
                    }

                    // Check vertical bounds
                    if (y - radius < 0) {
                        y = radius
                        dy = -dy
                    } else if (y + radius > containerHeight) {
                        y = (containerHeight - radius)
                        dy = -dy
                    }

                    delay(16) // ~60fps
                }
            }
        }

        // Draw the ball
        Box(
            modifier = Modifier
                .offset { IntOffset((x - radius).toInt(), (y - radius).toInt()) }
                .size((radius * 2).dp)
                .background(Color.Cyan)
        )
        Text(
            text = "Bouncing Ball Game",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        )
    }
}

/* ============================================================
   8. Lab2 (Breakout Game)
      -- Uses containerWidth/Height from the content area.
         The ball and paddle won't move behind the top bar
         or bottom bar because we measure the Box below.
   ============================================================ */

data class Brick(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float,
    var destroyed: Boolean = false
)

@Composable
fun Lab2() {
    // Container dimensions
    var containerWidth by remember { mutableStateOf(0) }
    var containerHeight by remember { mutableStateOf(0) }

    // Ball properties
    var ballX by remember { mutableStateOf(0f) }
    var ballY by remember { mutableStateOf(0f) }
    val ballRadius = 15f
    var dx by remember { mutableStateOf(4f) }
    var dy by remember { mutableStateOf(-4f) }

    // Paddle properties
    var paddleX by remember { mutableStateOf(0f) }
    val paddleWidth = 160f
    val paddleHeight = 20f

    // Game state
    var gameOver by remember { mutableStateOf(false) }
    var gameWon by remember { mutableStateOf(false) }

    // Bricks in a simple grid
    val brickWidth = 80f
    val brickHeight = 40f
    val rowCount = 2
    val colCount = 6

    val bricks = remember {
        mutableStateListOf<Brick>().apply {
            for (row in 0 until rowCount) {
                for (col in 0 until colCount) {
                    val left = col * brickWidth
                    val top = row * brickHeight + 50
                    val right = left + brickWidth - 5
                    val bottom = top + brickHeight - 5
                    add(Brick(left, top, right, bottom))
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF222222))
            .onGloballyPositioned { coordinates ->
                containerWidth = coordinates.size.width
                containerHeight = coordinates.size.height

                // Place the paddle horizontally centered at the bottom
                if (paddleX == 0f) {
                    paddleX = (containerWidth / 2f) - (paddleWidth / 2f)
                }
                // If ball hasn’t been placed yet, place it above the paddle
                if (ballX == 0f && ballY == 0f) {
                    ballX = (containerWidth / 2f)
                    ballY = containerHeight - paddleHeight - 100f
                }
            }
            // Drag horizontally to move the paddle
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    paddleX += dragAmount.x

                    // Keep the entire paddle within bounds
                    paddleX = paddleX.coerceIn(0f, (containerWidth - paddleWidth).toFloat())
                }
            }
    ) {
        val scope = rememberCoroutineScope()

        // Game update loop (~60fps)
        LaunchedEffect(Unit) {
            scope.launch {
                while (!gameOver && !gameWon) {
                    ballX += dx
                    ballY += dy

                    // Paddle collision
                    if (ballY + ballRadius >= containerHeight - paddleHeight - 10) {
                        if (ballX in paddleX..(paddleX + paddleWidth)) {
                            // Bounce up
                            ballY = containerHeight - paddleHeight - 10 - ballRadius
                            dy = -dy
                        } else if (ballY + ballRadius > containerHeight) {
                            // Missed the paddle -> game over
                            gameOver = true
                        }
                    }

                    // Left/right walls
                    if (ballX - ballRadius < 0) {
                        ballX = ballRadius
                        dx = -dx
                    } else if (ballX + ballRadius > containerWidth) {
                        ballX = containerWidth - ballRadius // Ball will be exactly at the edge
                        dx = -dx
                    }

                    // Top boundary
                    if (ballY - ballRadius < 0) {
                        ballY = ballRadius
                        dy = -dy
                    }

                    // Brick collisions
                    bricks.forEach { brick ->
                        if (!brick.destroyed && isBallCollidingWithBrick(
                                ballX, ballY, ballRadius,
                                brick.left, brick.top, brick.right, brick.bottom
                            )
                        ) {
                            brick.destroyed = true
                            // Reverse ball’s vertical direction
                            dy = -dy
                        }
                    }

                    // Win condition
                    if (bricks.all { it.destroyed }) {
                        gameWon = true
                    }

                    delay(16)
                }
            }
        }

        // Draw the paddle
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        paddleX.toInt(),
                        (containerHeight - paddleHeight - 10).toInt()
                    )
                }
                .size(paddleWidth.dp, paddleHeight.dp)
                .background(Color.Green)
        )

        // Draw the ball
        Box(
            modifier = Modifier
                .offset {
                    IntOffset((ballX - ballRadius).toInt(), (ballY - ballRadius).toInt())
                }
                .size((ballRadius * 2).dp)
                .background(Color.Red)
        )

        // Draw the bricks
        for (brick in bricks) {
            if (!brick.destroyed) {
                Box(
                    modifier = Modifier
                        .offset { IntOffset(brick.left.toInt(), brick.top.toInt()) }
                        .size((brick.right - brick.left).dp, (brick.bottom - brick.top).dp)
                        .background(Color.Cyan)
                )
            }
        }

        // GameOver text
        if (gameOver) {
            Text(
                text = "Game Over! You Lost",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        // Win text
        if (gameWon) {
            Text(
                text = "Congratulations! You Won!",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Title
        Text(
            text = "Breakout Game",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        )
    }
}
/** Simple bounding box collision for ball & brick overlap. */
fun isBallCollidingWithBrick(
    cx: Float, cy: Float, radius: Float,
    left: Float, top: Float, right: Float, bottom: Float
): Boolean {
    val closestX = cx.coerceIn(left, right)
    val closestY = cy.coerceIn(top, bottom)

    val distanceX = cx - closestX
    val distanceY = cy - closestY
    val distanceSquared = (distanceX * distanceX) + (distanceY * distanceY)

    return distanceSquared < (radius * radius)
}


@Composable
@Preview(showSystemUi = true, showBackground = true)
fun DemoAppPreview() {
    MaterialTheme {
        DemoApp()
    }
}