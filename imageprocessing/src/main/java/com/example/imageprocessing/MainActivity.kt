package com.example.imageprocessing

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.math.abs


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
        "LabColor",
        "LabPuzzle"
    )
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Image Processing") },
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
                0 -> ImageDisplayFunction()
                1 -> ImageTransformFunction()
                2 -> CanvasBasicsFunction()
                3 -> CanvasAdvancedFunction()
                4 -> LabColor()
                5 -> LabPuzzle()
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
   5. LabColor
   ============================================================ */
// Function to generate a random color with RGB values from 0 to 255
fun generateRandomColor(): Color {
    val random = java.util.Random()
    val red = random.nextInt(256) // 0 to 255
    val green = random.nextInt(256) // 0 to 255
    val blue = random.nextInt(256) // 0 to 255
    return Color(red / 255f, green / 255f, blue / 255f) // Convert to 0.0 - 1.0 range
}

@Composable
fun LabColor() {
    val context = LocalContext.current
    val drawable = ContextCompat.getDrawable(context, R.drawable.compose_logo) as BitmapDrawable
    val bitmap = drawable.bitmap

    var targetColor by remember { mutableStateOf(generateRandomColor()) }
    var red by remember { mutableFloatStateOf(0f) }
    var green by remember { mutableFloatStateOf(0f) }
    var blue by remember { mutableFloatStateOf(0f) }
    var score by remember { mutableStateOf(100) }
    var timeLeft by remember { mutableStateOf(30) }
    var gameOver by remember { mutableStateOf(false) }

    LaunchedEffect(timeLeft) {
        while (timeLeft > 0) {
            delay(1000L)
            timeLeft--
        }
        gameOver = true
    }

    val selectedColor = Color(red / 255f, green / 255f, blue / 255f)

    // Calculate similarity score
    LaunchedEffect(red, green, blue) {
        if (!gameOver) {
            val diffR = abs(targetColor.red * 255 - red)
            val diffG = abs(targetColor.green * 255 - green)
            val diffB = abs(targetColor.blue * 255 - blue)
            score = (100 - (diffR + diffG + diffB) / 3).toInt()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Color Matching Game", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        // Display target color
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(targetColor)
                .border(2.dp, Color.Black)
        )
        Text("Match this color!", fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        // Display player's selected color
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(selectedColor)
                .border(2.dp, Color.Black)
        )
        Text("Your Selected Color", fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        // Sliders for RGB
        SliderWithLabel("Red", red, { if (!gameOver) red = it }, Color.Red, gameOver)
        SliderWithLabel("Green", green, { if (!gameOver) green = it }, Color.Green, gameOver)
        SliderWithLabel("Blue", blue, { if (!gameOver) blue = it }, Color.Blue, gameOver)

        Spacer(modifier = Modifier.height(16.dp))

        // Display Score
        Text("Score: $score", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        // Display Timer
        Text(
            text = if (gameOver) "Game Over!" else "Time Left: $timeLeft sec",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (gameOver) Color.Gray else Color.Red
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Reset Button
        Button(onClick = {
            targetColor = generateRandomColor()
            red = 0f
            green = 0f
            blue = 0f
            score = 100
            timeLeft = 30
            gameOver = false
        }) {
            Text("New Challenge")
        }
    }
}


// Custom Composable for Sliders
@Composable
fun SliderWithLabel(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    color: Color,
    gameOver: Boolean
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("$label: ${value.toInt()}", fontWeight = FontWeight.Bold, color = color)
        Slider(
            value = value,
            onValueChange = { if (!gameOver) onValueChange(it) },
            valueRange = 0f..255f,
            modifier = Modifier.fillMaxWidth(0.8f),
            enabled = !gameOver
        )
    }
}

/* ============================================================
   LabPuzzle
   ============================================================ */
@Composable
fun LabPuzzle() {
    val context = LocalContext.current
    val drawable = ContextCompat.getDrawable(context, R.drawable.compose_logo) as BitmapDrawable
    val originalBitmap = drawable.bitmap

    val gridSize = 3 // Adjustable grid size
    var tileGrid by remember { mutableStateOf(shuffleTiles(originalBitmap, gridSize)) }
    val originalTiles = remember { mutableStateOf(originalTileSet(originalBitmap, gridSize)) }
    var moves by remember { mutableStateOf(0) }
    var timeLeft by remember { mutableStateOf(60) }
    var gameOver by remember { mutableStateOf(false) }

    LaunchedEffect(timeLeft) {
        while (timeLeft > 0 && !gameOver) {
            delay(10000L)
            timeLeft--
        }
        if (timeLeft == 0) {
            gameOver = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Image Puzzle Game", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        // Puzzle Grid
        PuzzleGrid(tileGrid, gridSize, onSwap = { i ->
            if (!gameOver) {
                tileGrid = swapTiles(tileGrid, i)
                moves++
                gameOver = isSolved(tileGrid, originalTiles.value)
            }
        })

        Spacer(modifier = Modifier.height(16.dp))

        // Display Timer
        Text(
            text = if (gameOver) "Game Over!" else "Time Left: $timeLeft sec",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (gameOver) Color.Gray else Color.Red
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Restart Button
        Button(onClick = {
            tileGrid = shuffleTiles(originalBitmap, gridSize)
            moves = 0
            timeLeft = 60
            gameOver = false
        }) {
            Text("New Puzzle")
        }
    }
}

// Function to create the original tile order with one empty space
fun originalTileSet(bitmap: Bitmap, gridSize: Int): List<Bitmap> {
    val tiles = splitImage(bitmap, gridSize).toMutableList()
    tiles[tiles.lastIndex] = createEmptyTile(tiles[0].width, tiles[0].height) // Use transparent tile
    return tiles
}

// Function to create a transparent empty tile
fun createEmptyTile(width: Int, height: Int): Bitmap {
    return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
        eraseColor(android.graphics.Color.RED) // Makes the tile empty
    }
}

// Function to split an image into grid tiles
fun splitImage(bitmap: Bitmap, gridSize: Int): List<Bitmap> {
    val tileWidth = bitmap.width / gridSize
    val tileHeight = bitmap.height / gridSize
    val tiles = mutableListOf<Bitmap>()

    for (y in 0 until gridSize) {
        for (x in 0 until gridSize) {
            val tile = Bitmap.createBitmap(bitmap, x * tileWidth, y * tileHeight, tileWidth, tileHeight)
            tiles.add(tile)
        }
    }
    return tiles
}

// Function to shuffle tiles while ensuring solvability
fun shuffleTiles(bitmap: Bitmap, gridSize: Int): List<Bitmap> {
    val tiles = originalTileSet(bitmap, gridSize).toMutableList()
    val gridSizeSquared = gridSize * gridSize
    var emptyIndex = tiles.indexOfFirst { it.sameAs(createEmptyTile(it.width, it.height)) }

    if (emptyIndex == -1) {
        Log.e("PuzzleGame", "Error: Empty tile not found, setting last tile as empty.")
        emptyIndex = tiles.lastIndex
        tiles[emptyIndex] = createEmptyTile(tiles[0].width, tiles[0].height)
    }

    val moves = listOf(-1, 1, -gridSize, gridSize) // Left, Right, Up, Down

    Log.d("PuzzleGame", "Starting tile shuffle, emptyIndex = $emptyIndex")

    repeat(100) { // Shuffle using 100 valid moves
        val validMoves = moves.filter { move ->
            val newIndex = emptyIndex + move
            newIndex in 0 until gridSizeSquared &&
                    !(move == -1 && emptyIndex % gridSize == 0) && // Prevent left wrap
                    !(move == 1 && emptyIndex % gridSize == gridSize - 1) // Prevent right wrap
        }

        if (validMoves.isNotEmpty()) {
            val swapIndex = emptyIndex + validMoves.random()
            if (swapIndex in 0 until gridSizeSquared) { // Final check before swapping
                Log.d("PuzzleGame", "Swapping empty tile at $emptyIndex with tile at $swapIndex")
                tiles[emptyIndex] = tiles[swapIndex]
                tiles[swapIndex] = createEmptyTile(tiles[0].width, tiles[0].height)
                emptyIndex = swapIndex
            } else {
                Log.e("PuzzleGame", "Invalid swap detected: emptyIndex=$emptyIndex, swapIndex=$swapIndex")
            }
        }
    }

    Log.d("PuzzleGame", "Shuffling complete, final emptyIndex = $emptyIndex")
    return tiles
}

// Function to check if tiles are in the correct order
fun isSolved(tiles: List<Bitmap>, originalTiles: List<Bitmap>): Boolean {
    return tiles == originalTiles
}

// Function to swap only adjacent tiles with the empty space
fun swapTiles(tiles: List<Bitmap>, index: Int): List<Bitmap> {
    val emptyIndex = tiles.indexOfFirst { it.sameAs(createEmptyTile(it.width, it.height)) }
    val gridSize = Math.sqrt(tiles.size.toDouble()).toInt()

    val validMoves = listOf(emptyIndex - 1, emptyIndex + 1, emptyIndex - gridSize, emptyIndex + gridSize)
    if (index !in validMoves) return tiles

    Log.d("PuzzleGame", "Swapping tiles: empty at $emptyIndex <-> tile at $index")
    val newTiles = tiles.toMutableList()
    newTiles[emptyIndex] = newTiles[index]
    newTiles[index] = createEmptyTile(newTiles[0].width, newTiles[0].height)
    return newTiles
}

// Puzzle Grid UI
@Composable
fun PuzzleGrid(tiles: List<Bitmap>, gridSize: Int, onSwap: (Int) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        for (row in 0 until gridSize) {
            Row {
                for (col in 0 until gridSize) {
                    val index = row * gridSize + col
                    val tile = tiles[index]
                    Image(
                        bitmap = tile.asImageBitmap(),
                        contentDescription = "Tile $index",
                        modifier = Modifier
                            .size(100.dp)
                            .border(1.dp, Color.Black)
                            .clickable { onSwap(index) }
                    )
                }
            }
        }
    }
}


/* ============================================================
   Preview
   ============================================================ */

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun DemoAppPreview() {
    MaterialTheme {
        DemoApp()
    }
}