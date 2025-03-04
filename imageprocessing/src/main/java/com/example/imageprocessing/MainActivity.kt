package com.example.imageprocessing

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.Effect
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.effect.GlEffect
import androidx.media3.effect.RgbFilter
import androidx.media3.effect.SingleColorLut
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            com.example.imageprocessing.ui.theme.ComposeMasterTheme {
                DemoApp()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoApp(modifier: Modifier = Modifier) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf(
        "Image Display",
        "Image Transform",
        "Canvas Basics",
        "Canvas Advanced",
        "Video Playback",
        "Video Effects",
        "Advanced Filters"
    )
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
                0 -> ImageDisplayFunction()      // will be implemented.
                1 -> ImageTransformFunction()  // will be implemented.
                2 -> CanvasBasicsFunction()    // will be implemented.
                3 -> CanvasAdvancedFunction()  // will be implemented.
                4 -> VideoPlaybackFunction()   // will be implemented.
                5 -> VideoEffectsFunction()    // will be implemented.
                6 -> AdvancedFiltersFunction() // will be implemented.
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

/* ============================================================
   1. ImageDisplayFunction
   ============================================================ */

@Composable
fun ImageDisplayFunction() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Image Loading, Display, and Management",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        // 1. Displaying an Image from Drawables
        Text(text = "1. From Drawables:", fontWeight = FontWeight.Bold)
        DrawableImageExample(R.drawable.compose_logo, "Compose Logo")

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Displaying an Image from Network (using Coil)
        Text(text = "2. From Network (Coil):", fontWeight = FontWeight.Bold)
        NetworkImageExample(
            "data:image/jpeg;base64,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",
            "Compose Logo from Network"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Displaying an Image from Local Storage (Simulated)
        Text(text = "3. From Local Storage (Bitmap):", fontWeight = FontWeight.Bold)
        LocalStorageImageExample()

        Spacer(modifier = Modifier.height(16.dp))

        // 4. ContentScale Examples
        Text(text = "4. ContentScale Examples:", fontWeight = FontWeight.Bold)
        ContentScaleExamples()
    }
}

// Helper Functions

@Composable
fun DrawableImageExample(drawableId: Int, description: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = drawableId),
            contentDescription = description,
            modifier = Modifier.size(150.dp)
        )
        Text(text = description)
    }
}

@Composable
fun NetworkImageExample(imageUrl: String, description: String) {
    val context = LocalContext.current

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(imageUrl)
//            .size(Size.ORIGINAL) // Use Size.ORIGINAL here.
//            .allowHardware(false)
            .build()
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(
            model = imageUrl,
            contentDescription = description,
            modifier = Modifier.size(150.dp),
            placeholder = painterResource(R.drawable.compose_logo),
            error = painterResource(R.drawable.compose_logo),
            contentScale = ContentScale.Fit
        )
        Text(text = description)
    }
}


fun Drawable.toBitmap(): Bitmap {
    val bitmap = createBitmap(intrinsicWidth, intrinsicHeight)
    val canvas = android.graphics.Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)
    return bitmap
}

@Composable
fun LocalStorageImageExample() {
    // Simulate loading from local storage (replace with actual logic if needed)
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            // Simulate loading from local storage
            val drawable: Drawable? = ContextCompat.getDrawable(context, R.drawable.compose_logo)
            bitmap = drawable?.toBitmap()
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap!!.asImageBitmap(),
                contentDescription = "Compose Logo from Local Storage",
                modifier = Modifier.size(150.dp)
            )
        } else {
            CircularProgressIndicator()
        }
        Text(text = "Compose Logo from Local Storage")
    }
}

@Composable
fun ContentScaleExamples() {
    val painter = painterResource(id = R.drawable.compose_logo)
    val contentScales = listOf(
        ContentScale.Crop,
        ContentScale.Fit,
        ContentScale.FillBounds,
        ContentScale.FillWidth,
        ContentScale.FillHeight,
        ContentScale.Inside
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        contentScales.forEach { scale ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = scale.toString().substringAfterLast("."),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Image(
                    painter = painter,
                    contentDescription = "Content Scale Example",
                    contentScale = scale,
                    modifier = Modifier
                        .size(150.dp, 100.dp)
                        .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                        .background(Color.Yellow.copy(alpha = 0.2f))
                )
            }
        }
    }
}

/* ============================================================
   2. ImageTransformFunction
   ============================================================ */

@Composable
fun ImageTransformFunction() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Image Manipulation and Transformation",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        // 1. Resizing (using Modifier.size)
        Text(text = "1. Resizing (using Modifier.size):", fontWeight = FontWeight.Bold)
        ResizingExample()
        Spacer(modifier = Modifier.height(16.dp))

        // 2. Cropping (using Modifier.clip)
        Text(text = "2. Cropping (using Modifier.clip):", fontWeight = FontWeight.Bold)
        CroppingExample()
        Spacer(modifier = Modifier.height(16.dp))

        // 3. Rotating (using Modifier.rotate)
        Text(text = "3. Rotating (using Modifier.rotate):", fontWeight = FontWeight.Bold)
        RotatingExample()
        Spacer(modifier = Modifier.height(16.dp))

        // 4. Scaling (using Modifier.scale)
        Text(text = "4. Scaling (using Modifier.scale):", fontWeight = FontWeight.Bold)
        ScalingExample()
        Spacer(modifier = Modifier.height(16.dp))

        // 5. Color Adjustments (using graphicsLayer)
        Text(text = "5. Color Adjustments (using graphicsLayer):", fontWeight = FontWeight.Bold)
        ColorAdjustmentExample()

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "6. Bitmap usage:", fontWeight = FontWeight.Bold)
        BitmapExample()
    }
}

// Helper Functions

@Composable
fun ResizingExample() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Original Size", fontSize = 16.sp)
        Image(
            painter = painterResource(id = R.drawable.compose_logo),
            contentDescription = "Compose Logo",
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Resized", fontSize = 16.sp)
        Image(
            painter = painterResource(id = R.drawable.compose_logo),
            contentDescription = "Compose Logo",
            modifier = Modifier.size(50.dp) // Smaller size
        )
    }
}

@Composable
fun CroppingExample() {
    val context = LocalContext.current
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Circle Crop", fontSize = 16.sp)
        Image(
            painter = painterResource(id = R.drawable.compose_logo),
            contentDescription = "Compose Logo Circle Crop",
            modifier = Modifier
                .size(100.dp)
                .clip(androidx.compose.foundation.shape.CircleShape) // Circle crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Rounded Rect", fontSize = 16.sp)
        Image(
            painter = painterResource(id = R.drawable.compose_logo),
            contentDescription = "Compose Logo Rounded Rect",
            modifier = Modifier
                .size(100.dp)
                .clip(androidx.compose.foundation.shape.RoundedCornerShape(16.dp))
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RotatingExample() {
    var rotationAngle by remember { mutableFloatStateOf(0f) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.compose_logo),
            contentDescription = "Compose Logo Rotating",
            modifier = Modifier
                .size(100.dp)
                .rotate(rotationAngle)
        )
        Slider(
            value = rotationAngle,
            onValueChange = { rotationAngle = it },
            valueRange = 0f..360f
        )
        Text(text = "Rotation Angle: ${rotationAngle.toInt()} degrees")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScalingExample() {
    var scaleFactor by remember { mutableFloatStateOf(1f) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.compose_logo),
            contentDescription = "Compose Logo Scaling",
            modifier = Modifier
                .size(100.dp)
                .scale(scaleFactor)
        )
        Slider(
            value = scaleFactor,
            onValueChange = { scaleFactor = it },
            valueRange = 0.1f..2f
        )
        Text(text = "Scale Factor: ${String.format("%.1f", scaleFactor)}")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorAdjustmentExample() {
    var brightness by remember { mutableFloatStateOf(1f) } // Default: No change
    var saturation by remember { mutableFloatStateOf(1f) } // Default: No change

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.compose_logo),
            contentDescription = "Compose Logo Color Adjustment",
            modifier = Modifier
                .size(100.dp)
                .graphicsLayer {
                    this.alpha = brightness
                }
        )

        Text(text = "Brightness")
        Slider(
            value = brightness,
            onValueChange = { brightness = it },
            valueRange = 0f..1f
        )
        Text(text = "Brightness: ${String.format("%.1f", brightness)}")

    }
}


@Composable
fun BitmapExample() {
    val context = LocalContext.current
    val originalDrawable = context.getDrawable(R.drawable.compose_logo) as BitmapDrawable
    val originalBitmap = originalDrawable.bitmap

    // Example 1: Rotating the Bitmap
    val rotatedBitmap: Bitmap = rotateBitmap(originalBitmap, 45f)
    Spacer(modifier = Modifier.height(8.dp))
    Text(text = "Rotated Bitmap")
    Image(
        bitmap = rotatedBitmap.asImageBitmap(),
        contentDescription = "Rotated Bitmap",
        modifier = Modifier.size(100.dp)
    )
    Spacer(modifier = Modifier.height(16.dp))

    // Example 2: Scaling the Bitmap
    val scaledBitmap: Bitmap = scaleBitmap(originalBitmap, 0.5f, 0.5f)
    Spacer(modifier = Modifier.height(8.dp))
    Text(text = "Scaled Bitmap")
    Image(
        bitmap = scaledBitmap.asImageBitmap(),
        contentDescription = "Scaled Bitmap",
        modifier = Modifier.size(100.dp)
    )
    Spacer(modifier = Modifier.height(16.dp))

    // Example 3: Display original Bitmap
    Spacer(modifier = Modifier.height(8.dp))
    Text(text = "Original Bitmap")
    Image(
        bitmap = originalBitmap.asImageBitmap(),
        contentDescription = "Original Bitmap",
        modifier = Modifier.size(100.dp)
    )
    Spacer(modifier = Modifier.height(16.dp))
}

// Helper function to rotate a Bitmap
fun rotateBitmap(source: Bitmap, angle: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(angle) }
    return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
}

// Helper function to scale a Bitmap
fun scaleBitmap(source: Bitmap, scaleX: Float, scaleY: Float): Bitmap {
    val matrix = Matrix().apply { postScale(scaleX, scaleY) }
    return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
}

/* ============================================================
   3. CanvasBasicsFunction
   ============================================================ */
@Composable
fun CanvasBasicsFunction() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Canvas Basics",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 1. Drawing Rectangles
        Text(text = "1. Rectangles", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Canvas(modifier = Modifier.size(200.dp)) {
            drawRect(
                color = Color.Blue,
                size = Size(100.dp.toPx(), 50.dp.toPx())
            ) // Filled rectangle
            drawRect(
                color = Color.Red,
                topLeft = Offset(50.dp.toPx(), 75.dp.toPx()),
                size = Size(100.dp.toPx(), 50.dp.toPx()),
                style = Stroke(width = 5.dp.toPx())
            ) // Outlined rectangle
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Drawing Circles
        Text(text = "2. Circles", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Canvas(modifier = Modifier.size(200.dp)) {
            drawCircle(color = Color.Green, radius = 50.dp.toPx()) // Filled circle
            drawCircle(
                color = Color.Magenta,
                radius = 30.dp.toPx(),
                center = Offset(100.dp.toPx(), 100.dp.toPx()),
                style = Stroke(width = 5.dp.toPx())
            ) // Outlined circle
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Drawing Lines
        Text(text = "3. Lines", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Canvas(modifier = Modifier.size(200.dp)) {
            drawLine(
                color = Color.Black,
                start = Offset(0.dp.toPx(), 0.dp.toPx()),
                end = Offset(200.dp.toPx(), 200.dp.toPx()),
                strokeWidth = 5.dp.toPx() // Line width
            )
            drawLine(
                color = Color.Cyan,
                start = Offset(200.dp.toPx(), 0.dp.toPx()),
                end = Offset(0.dp.toPx(), 200.dp.toPx()),
                strokeWidth = 5.dp.toPx(),
                cap = StrokeCap.Round
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 4. Drawing order.
        Text(text = "4. Drawing Order", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Canvas(modifier = Modifier.size(200.dp)) {
            drawCircle(color = Color.Gray, radius = 50.dp.toPx())
            drawRect(
                color = Color.Red,
                size = Size(100.dp.toPx(), 100.dp.toPx()),
                topLeft = Offset(50.dp.toPx(), 50.dp.toPx())
            )
            drawCircle(
                color = Color.Yellow,
                radius = 30.dp.toPx(),
                center = Offset(100.dp.toPx(), 100.dp.toPx())
            )
        }
    }
}

/* ============================================================
   4. CanvusAdvancedFunctions
   ============================================================ */
@Composable
fun CanvasAdvancedFunction() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Advanced Canvas Drawing",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        // 1. Drawing Shapes
        Text(text = "1. Shapes", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Canvas(modifier = Modifier.size(200.dp)) {
            drawRect(color = Color.Blue, size = Size(100.dp.toPx(), 100.dp.toPx()))
            drawCircle(
                color = Color.Red,
                radius = 50.dp.toPx(),
                center = Offset(150.dp.toPx(), 50.dp.toPx())
            )
            drawRoundRect(
                color = Color.Green,
                topLeft = Offset(0.dp.toPx(), 100.dp.toPx()),
                size = Size(100.dp.toPx(), 50.dp.toPx()),
                cornerRadius = CornerRadius(10.dp.toPx())
            )
            drawRoundRect(
                color = Color.Green,
                topLeft = Offset(100.dp.toPx(), 100.dp.toPx()),
                size = Size(100.dp.toPx(), 50.dp.toPx()),
                cornerRadius = CornerRadius(30.dp.toPx())
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Drawing Paths
        Text(text = "2. Paths", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Canvas(modifier = Modifier.size(200.dp)) {
            val path = Path().apply {
                moveTo(50.dp.toPx(), 50.dp.toPx())
                lineTo(150.dp.toPx(), 50.dp.toPx())
                lineTo(100.dp.toPx(), 150.dp.toPx())
                close()
            }
            drawPath(
                path,
                color = Color.Magenta,
                style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
            val path2 = Path().apply {
                moveTo(50.dp.toPx(), 150.dp.toPx())
                cubicTo(
                    0.dp.toPx(),
                    0.dp.toPx(),
                    200.dp.toPx(),
                    0.dp.toPx(),
                    150.dp.toPx(),
                    150.dp.toPx()
                )
            }
            drawPath(
                path2,
                color = Color.Black,
                style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Drawing Text
        Text(text = "3. Text", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Canvas(modifier = Modifier.size(200.dp)) {
            drawContext.canvas.nativeCanvas.apply {
                drawText("Hello Canvas!", 50.dp.toPx(), 100.dp.toPx(), Paint().apply {
                    color = android.graphics.Color.CYAN
                    textSize = 40.sp.toPx()
                })
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 4. Drawing Image
        Text(text = "4. Image", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Canvas(modifier = Modifier.size(200.dp)) {

            val drawable = ContextCompat.getDrawable(context, R.drawable.compose_logo)
            val bitmap =
                (drawable as android.graphics.drawable.BitmapDrawable).bitmap.asImageBitmap()
            drawImage(bitmap, topLeft = Offset(50.dp.toPx(), 50.dp.toPx()))
        }
        Spacer(modifier = Modifier.height(16.dp))


        // 5. Transformations
        Text(text = "5. Transformations", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Canvas(modifier = Modifier.size(200.dp)) {
            translate(left = 50.dp.toPx(), top = 50.dp.toPx()) {
                drawRect(color = Color.Cyan, size = Size(50.dp.toPx(), 50.dp.toPx()))
            }

            rotate(degrees = 45f, pivot = Offset(100.dp.toPx(), 100.dp.toPx())) {
                drawRect(
                    color = Color.Blue,
                    topLeft = Offset(75.dp.toPx(), 75.dp.toPx()),
                    size = Size(50.dp.toPx(), 50.dp.toPx())
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 6. Gradients
        Text(text = "6. Gradients", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Canvas(modifier = Modifier.size(200.dp)) {
            drawRect(
                brush = Brush.linearGradient(
                    colors = listOf(Color.Red, Color.Yellow, Color.Green),
                    start = Offset(0.dp.toPx(), 0.dp.toPx()),
                    end = Offset(200.dp.toPx(), 200.dp.toPx())
                ),
                size = Size(200.dp.toPx(), 200.dp.toPx())
            )
        }
    }
}

/* ============================================================
   5. VideoPlaybackFunction
   ============================================================ */
@Composable
fun VideoPlaybackFunction() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .background(Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Advanced Video Playback",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        val videoUri = remember {
            "android.resource://${context.packageName}/${R.raw.video_test}".toUri()
        }

        val exoPlayer = remember {
            ExoPlayer.Builder(context).build().apply {
                setMediaItem(MediaItem.fromUri(videoUri))
                prepare()
                playWhenReady = true
            }
        }

        DisposableEffect(exoPlayer) {
            onDispose {
                exoPlayer.release()
            }
        }

        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(9 / 16f)
        )
    }
}

/* ============================================================
   6. VideoEffectsFunction
   ============================================================ */
@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun VideoEffectsFunction() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var applyEffect by remember { mutableStateOf(false) }
    val currentEffect = remember(applyEffect) {
        if (applyEffect) ShaderProgram() else null
    }

    val videoUri = remember {
        "android.resource://${context.packageName}/${R.raw.video_test}".toUri()
    }

    val player = remember(context) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUri))
            prepare()
            playWhenReady = true
            setVideoEffects(listOfNotNull(currentEffect))
        }
    }

    DisposableEffect(player, lifecycleOwner) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> player.play()
                Lifecycle.Event.ON_PAUSE -> player.pause()
                Lifecycle.Event.ON_STOP -> {
                    player.pause()
                    player.seekTo(0)
                }

                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
            player.release()
        }
    }

    SideEffect {
        player.setVideoEffects(listOfNotNull(currentEffect))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Advanced Video Effects",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(9f / 16f),
            factory = { ctx ->
                PlayerView(ctx).apply {
                    this.player = player
                    useController = true
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { applyEffect = !applyEffect }) {
            Text(if (applyEffect) "Remove Filter" else "Apply Filter")
        }
    }
}

@UnstableApi
fun ShaderProgram(): Effect {
//    return RgbFilter.createInvertedFilter()
    return RgbFilter.createGrayscaleFilter()
}

@UnstableApi
fun ShaderProgram1(): GlEffect {
    val size = 2
    val grayscaleBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888).apply {
        for (y in 0 until size) {
            for (x in 0 until size) {
                val luminance = ((x + y).toFloat() / (size * 2 - 2))
                val gray: Color = Color.hsv(hue = 0f, saturation = 0f, value = luminance)
                setPixel(x, y, gray.toArgb()) // Convert Compose Color to Int
            }
        }
    }

    return SingleColorLut.createFromBitmap(grayscaleBitmap)
}

/* ============================================================
   7. AdvancedFiltersFunction
   ============================================================ */

//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.aspectRatio
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.Button
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.DisposableEffect
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.toArgb
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalLifecycleOwner
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.viewinterop.AndroidView
//import androidx.core.net.toUri
//import androidx.lifecycle.Lifecycle
//import androidx.lifecycle.LifecycleEventObserver
//import androidx.media3.common.Effect
//import androidx.media3.common.MediaItem
//import androidx.media3.common.util.UnstableApi
//import androidx.media3.effect.SingleColorLut
//import androidx.media3.exoplayer.ExoPlayer
//import androidx.media3.ui.PlayerView
//import com.example.imageprocessing.R

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun AdvancedFiltersFunction() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var selectedEffect by remember { mutableStateOf<GlEffect?>(null) }

    val videoUri = remember {
        "android.resource://${context.packageName}/${R.raw.video_test}".toUri()
    }

    val player = remember(context) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUri))
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(player, lifecycleOwner) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> player.play()
                Lifecycle.Event.ON_PAUSE -> player.pause()
                Lifecycle.Event.ON_STOP -> {
                    player.pause()
                    player.seekTo(0)
                }

                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
            player.release()
        }
    }

    LaunchedEffect(selectedEffect) {
        player.setVideoEffects(listOfNotNull(selectedEffect) as List<Effect>)
    }

    val filters = listOf(
        "No Filter" to null,
        "Grayscale" to createGrayscaleLut(),
        "Inverted" to createInvertedLut(),
        "Red Tone" to createRedToneLut(context),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Advanced Video Filters",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(9f / 16f),
            factory = { ctx ->
                PlayerView(ctx).apply {
                    this.player = player
                    useController = true
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(filters.size) { index ->
                val (filterName, filterEffect) = filters[index]
                Button(onClick = { selectedEffect = filterEffect }) {
                    Text(filterName)
                }
            }
        }
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
fun createGrayscaleLut(): GlEffect {
    val size = 2
    val grayscaleBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888).apply {
        for (y in 0 until size) {
            for (x in 0 until size) {
                val luminance = ((x + y).toFloat() / (size * 2 - 2))
                val gray: Color = Color.hsv(hue = 0f, saturation = 0f, value = luminance)
                setPixel(x, y, gray.toArgb())
            }
        }
    }
    return SingleColorLut.createFromBitmap(grayscaleBitmap)
}

@androidx.annotation.OptIn(UnstableApi::class)
fun createInvertedLut(): GlEffect {
    val size = 2
    val invertedBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888).apply {
        for (y in 0 until size) {
            for (x in 0 until size) {
                val r = 1f - (x.toFloat() / (size - 1))
                val g = 1f - (y.toFloat() / (size - 1))
                val b = 1f - ((x + y).toFloat() / (size * 2 - 2))

                val invertedColor = Color(r, g, b)
                setPixel(x, y, invertedColor.toArgb())
            }
        }
    }
    return SingleColorLut.createFromBitmap(invertedBitmap)
}

@androidx.annotation.OptIn(UnstableApi::class)
fun createRedToneLut(context: android.content.Context): GlEffect {
//    val red_tone: GlEffect = RgbFilter(1f, 0f, 0f)
//    val red_tone: GlEffect = SingleColorLut(Color.Red.toArgb())

//    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.red_tone)
//    return SingleColorLut.createFromBitmap(bitmap)

    //Instead, create a custom bitmap
    val size = 2
    val redBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888).apply {
        for (y in 0 until size) {
            for (x in 0 until size) {
                val r = 1f - (x.toFloat() / (size - 1))
                val g = 0f
                val b = 0f
                val redColor = Color(r, g, b)
                setPixel(x, y, redColor.toArgb())
            }
        }
    }
    return SingleColorLut.createFromBitmap(redBitmap)

//    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.red_tone)
//    return SingleColorLut.createFromBitmap(bitmap)
}












@Composable
@Preview(showSystemUi = true, showBackground = true)
fun DemoAppPreview() {
    MaterialTheme {
        DemoApp()
    }
}