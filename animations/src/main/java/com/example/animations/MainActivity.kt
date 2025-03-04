package com.example.animations

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.SizeTransform
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
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
    val tabs = listOf("Float", "Color", "Dp", "Visibility", "Draggable", "Infinite", "ContentAnim", "Crossfade", "Lab", "Lab2")
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Jetpack Compose Animations") },
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        tabs.forEachIndexed { index, label ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    selectedTab = index
                                    expanded = false // Close the menu after selection
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Filled.Check,
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                    }
                }
            )
        },

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
                6 -> ContentAnimation()
                7 -> Crossfade()
                8 -> Lab()
                9 -> Lab2()

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
        targetValue = if (expanded) 128.dp else 8.dp,
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
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
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
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
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


@Composable
fun Lab() {
    // Capture the density once at the start of the composable
    val density = LocalDensity.current

    // Container dimensions (in dp)
    var containerWidthDp by remember { mutableStateOf(0.dp) }
    var containerHeightDp by remember { mutableStateOf(0.dp) }

    // Ball properties (in dp)
    val radiusDp = 50.dp
    var ballCenterX by remember { mutableStateOf(100.dp) }
    var ballCenterY by remember { mutableStateOf(100.dp) }

    // Movement per frame (in dp)
    var dx by remember { mutableStateOf(3.dp) }
    var dy by remember { mutableStateOf(3.dp) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF222222))
            .onGloballyPositioned { layoutCoordinates ->
                // Convert container width/height from px to dp
                containerWidthDp = with(density) {
                    layoutCoordinates.size.width.toDp()
                }
                containerHeightDp = with(density) {
                    layoutCoordinates.size.height.toDp()
                }
            }
    ) {
        // Animation loop
        val coroutineScope = rememberCoroutineScope()
        LaunchedEffect(Unit) {
            coroutineScope.launch {
                while (true) {
                    // Update the ball's center by velocity
                    ballCenterX += dx
                    ballCenterY += dy

                    // Check horizontal bounds
                    if ((ballCenterX - radiusDp) < 0.dp) {
                        ballCenterX = radiusDp
                        dx = -dx
                    } else if ((ballCenterX + radiusDp) > containerWidthDp) {
                        ballCenterX = containerWidthDp - radiusDp
                        dx = -dx
                    }

                    // Check vertical bounds
                    if ((ballCenterY - radiusDp) < 0.dp) {
                        ballCenterY = radiusDp
                        dy = -dy
                    } else if ((ballCenterY + radiusDp) > containerHeightDp) {
                        ballCenterY = containerHeightDp - radiusDp
                        dy = -dy
                    }

                    delay(16) // ~60fps
                }
            }
        }

        // Draw the ball (offset in dp)
        Box(
            modifier = Modifier
                .offset(
                    x = ballCenterX - radiusDp,
                    y = ballCenterY - radiusDp
                )
                .size(radiusDp * 2)
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

@Composable
fun ContentAnimation() {
    var isFirstContent by remember { mutableStateOf(true) }
    Column {
        // Toggle content on click (e.g., from a button)
        Button(onClick = { isFirstContent = !isFirstContent }) {
            Text("Toggle Content")
        }
        AnimatedContent(
            targetState = isFirstContent,
            transitionSpec = {
                if (targetState) {
                    (slideInHorizontally { width -> -width } + fadeIn()).togetherWith(
                        slideOutHorizontally { width -> width } + fadeOut()
                    )
                } else {
                    (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                        slideOutHorizontally { width -> -width } + fadeOut()
                    )
                }.using(
                    SizeTransform(clip = false)
                )
            }, label = ""
        ) { targetIsFirstContent ->
            if (targetIsFirstContent) {
                Text("First Content")
            } else {
                Text("Second Content")
            }
        }
    }
}


@Composable
fun Crossfade() {
    var isFirstContent by remember { mutableStateOf(true) }
    Column {
        // Toggle content on click (e.g., from a button)
        Button(onClick = { isFirstContent = !isFirstContent }) {
            Text("Toggle Content")
        }
        Crossfade(targetState = isFirstContent, label = "") { targetIsFirstContent ->
            if (targetIsFirstContent) {
                Text("First Content")
            } else {
                Text("Second Content")
            }
        }
    }
}
/* ============================================================
   8. Lab2 (Breakout Game)
      -- Uses containerWidth/Height from the content area.
         The ball and paddle won't move behind the top bar
         or bottom bar because we measure the Box below.
   ============================================================ */

data class Brick(
    val id: Int,
    var x: Dp,
    var y: Dp,
    val width: Dp,
    val height: Dp,
    var isVisible: Boolean = true
)

@Composable
fun Lab2() {
    val density = LocalDensity.current

    // Container dimensions
    var containerWidthDp by remember { mutableStateOf(0.dp) }
    var containerHeightDp by remember { mutableStateOf(0.dp) }

    // Ball
    val ballRadiusDp = 12.dp
    var ballCenterX by remember { mutableStateOf(100.dp) }
    var ballCenterY by remember { mutableStateOf(200.dp) }
    var dx by remember { mutableStateOf(3.dp) }
    var dy by remember { mutableStateOf(-3.dp) }

    // Paddle
    val paddleWidth = 80.dp
    val paddleHeight = 12.dp
    var paddleX by remember { mutableStateOf(100.dp) }
    val paddleY by remember { mutableStateOf(500.dp) }  // distance from top

    // Bricks: create a small grid, for example 3 rows x 6 columns
    val brickWidth = 50.dp
    val brickHeight = 20.dp
    val brickPadding = 8.dp
    val bricks = remember {
        mutableStateListOf<Brick>().apply {
            var idCounter = 0
            val rows = 3
            val cols = 6
            repeat(rows) { row ->
                repeat(cols) { col ->
                    val x = (brickWidth + brickPadding) * col + 16.dp
                    val y = (brickHeight + brickPadding) * row + 60.dp
                    add(Brick(idCounter++, x, y, brickWidth, brickHeight, true))
                }
            }
        }
    }

    // For controlling the loop & any side effects
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF222222))
            .onGloballyPositioned { layoutCoordinates ->
                containerWidthDp = with(density) { layoutCoordinates.size.width.toDp() }
                containerHeightDp = with(density) { layoutCoordinates.size.height.toDp() }
            }
            // Paddle movement with drag. You can do a horizontal drag only:
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume() // mark the gesture as consumed
                    paddleX += with(density) { dragAmount.x.toDp() }
                }
            }
    ) {
        // Animation loop for the ball
        LaunchedEffect(Unit) {
            coroutineScope.launch {
                while (true) {
                    // Move ball
                    ballCenterX += dx
                    ballCenterY += dy

                    // Check horizontal walls
                    if (ballCenterX - ballRadiusDp < 0.dp) {
                        ballCenterX = ballRadiusDp
                        dx = -dx
                    } else if (ballCenterX + ballRadiusDp > containerWidthDp) {
                        ballCenterX = containerWidthDp - ballRadiusDp
                        dx = -dx
                    }

                    // Check top wall
                    if (ballCenterY - ballRadiusDp < 0.dp) {
                        ballCenterY = ballRadiusDp
                        dy = -dy
                    }

                    // Check bottom (Game Over if it hits bottom)
                    if (ballCenterY + ballRadiusDp > containerHeightDp) {
                        // Reset ball/paddle or handle "game over"
                        ballCenterX = 100.dp
                        ballCenterY = 200.dp
                        dx = 3.dp
                        dy = -3.dp
                    }

                    // Check paddle collision
                    val paddleLeft = paddleX
                    val paddleRight = paddleX + paddleWidth
                    val paddleTop = paddleY
                    val paddleBottom = paddleY + paddleHeight

                    if (ballCenterY + ballRadiusDp >= paddleTop &&
                        ballCenterY - ballRadiusDp <= paddleBottom &&
                        ballCenterX >= paddleLeft &&
                        ballCenterX <= paddleRight
                    ) {
                        // Collides with paddle
                        ballCenterY = paddleTop - ballRadiusDp
                        dy = -dy
                    }

                    // Check bricks
                    bricks.filter { it.isVisible }.forEach { brick ->
                        val brickLeft = brick.x
                        val brickRight = brick.x + brick.width
                        val brickTop = brick.y
                        val brickBottom = brick.y + brick.height

                        // If ball intersects with the brick
                        if (ballCenterX + ballRadiusDp >= brickLeft &&
                            ballCenterX - ballRadiusDp <= brickRight &&
                            ballCenterY + ballRadiusDp >= brickTop &&
                            ballCenterY - ballRadiusDp <= brickBottom
                        ) {
                            // Hide the brick
                            brick.isVisible = false

                            // Basic collision response: invert vertical direction
                            dy = -dy
                        }
                    }

                    delay(16) // ~60 fps
                }
            }
        }

        // Draw bricks
        bricks.forEach { brick ->
            if (brick.isVisible) {
                Box(
                    modifier = Modifier
                        .offset(x = brick.x, y = brick.y)
                        .size(brick.width, brick.height)
                        .background(Color.Red)
                )
            }
        }

        // Draw paddle
        Box(
            modifier = Modifier
                .offset(x = paddleX, y = paddleY)
                .size(paddleWidth, paddleHeight)
                .background(Color.Green)
        )

        // Draw ball
        Box(
            modifier = Modifier
                .offset(x = ballCenterX - ballRadiusDp, y = ballCenterY - ballRadiusDp)
                .size(ballRadiusDp * 2)
                .background(Color.Cyan)
        )

        // Title
        Text(
            text = "Breakout Game (Jetpack Compose)",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp),
            color = Color.White
        )
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun DemoAppPreview() {
    MaterialTheme {
        DemoApp()
    }
}