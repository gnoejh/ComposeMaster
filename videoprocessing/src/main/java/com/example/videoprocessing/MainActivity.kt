package com.example.videoprocessing

import android.graphics.Bitmap
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
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
            com.example.videoprocessing.ui.theme.ComposeMasterTheme {
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
        "Video Playback",
        "Video Effects",
    )
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Image Processing and Graphics") },
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
                0 -> VideoPlaybackFunction()
                1 -> VideoEffectsFunction()
            }
        }
    }
}


/* ============================================================
   0. VideoPlaybackFunction
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
            "android.resource://${context.packageName}/${com.example.videoprocessing.R.raw.video_test}".toUri()
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
   1. VideoEffectsFunction
   ============================================================ */
@UnstableApi
fun shaderInverted(): Effect {
    return RgbFilter.createInvertedFilter()
}

@UnstableApi
fun shaderCustom(): GlEffect {
    val lutSize = 64  // The size of the LUT cube
    val bitmapWidth = lutSize
    val bitmapHeight = lutSize * lutSize  // Ensure N × N² format

    val grayscaleBitmap = createBitmap(bitmapWidth, bitmapHeight).apply {
        for (blue in 0 until lutSize) {
            for (green in 0 until lutSize) {
                for (red in 0 until lutSize) {
                    val luminance =
                        ((red + green + blue).toFloat() / (3 * (lutSize - 1)) * 255).toInt()
                    val color = android.graphics.Color.rgb(luminance, luminance, luminance)
                    this[red, green * lutSize + blue] = color
                }
            }
        }
    }
    return SingleColorLut.createFromBitmap(grayscaleBitmap)
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun VideoEffectsFunction() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var selectedEffect by remember { mutableStateOf<GlEffect?>(null) }

    val effects = listOf(
        "No Effect" to null,
        "Inverted" to shaderInverted(),
        "Grayscale LUT" to shaderCustom()
    )

    val videoUri = remember {
        "android.resource://${context.packageName}/${com.example.videoprocessing.R.raw.video_test}".toUri()
    }

    val player = remember(context) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUri))
            prepare()
            playWhenReady = true
            setVideoEffects(listOfNotNull(selectedEffect))
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

    LaunchedEffect (selectedEffect) {
        player.setVideoEffects(listOfNotNull(selectedEffect))
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
                .aspectRatio(9f / 12f),
            factory = { ctx ->
                PlayerView(ctx).apply {
                    this.player = player
                    useController = true
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(effects.size) { index ->
                val (effectName, effect) = effects[index]
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    onClick = { selectedEffect = effect as GlEffect? }
                ) {
                    Text(effectName)
                }
            }
        }
    }
}

/* ============================================================
   7. CameraEffectsFunction
   ============================================================ */

@Composable
fun CameraEffectsFunction() {

}

/* ============================================================
   8. AudioPlaybackFunction
   ============================================================ */

/* ============================================================
   9. AudioEffectsFunction
   ============================================================ */

/* ============================================================
10. AudioRecordingFunction
 */



@Composable
@Preview(showSystemUi = true, showBackground = true)
fun DemoAppPreview() {
    MaterialTheme {
        DemoApp()
    }
}