package com.example.videoprocessing

//import androidx.camera.core.Preview
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.Effect
import androidx.media3.common.MediaItem
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.effect.GlEffect
import androidx.media3.effect.RgbFilter
import androidx.media3.effect.SingleColorLut
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
//import androidx.privacysandbox.tools.core.generator.build
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.Executors
import android.Manifest



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
        "Camera Preview",
        "Camera Effects",
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
                2 -> CameraPreview()
                3 -> CameraEffectsFunction()
            }
        }
    }
}


/* ============================================================
   VideoPlaybackFunction
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
                playWhenReady = false
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
   VideoEffectsFunction
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
            playWhenReady = false
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

    LaunchedEffect(selectedEffect) {
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
   CameraPreview
   ============================================================ */
@Composable
fun CameraPreview() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // State to manage camera permission
    var hasCameraPermission by remember { mutableStateOf(false) }

    // Request camera permission launcher
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            hasCameraPermission = isGranted
        }
    )

    // Check if permission is already granted
    LaunchedEffect(key1 = true) {
        val cameraPermission = Manifest.permission.CAMERA
        hasCameraPermission = ContextCompat.checkSelfPermission(
            context,
            cameraPermission
        ) == PackageManager.PERMISSION_GRANTED

        // If permission not granted, launch request
        if (!hasCameraPermission) {
            requestPermissionLauncher.launch(cameraPermission)
        }
    }

    // Camera Provider Future
    val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
        remember { ProcessCameraProvider.getInstance(context) }

    // Preview View
    val previewView = remember {
        PreviewView(context).apply {
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        }
    }

    // Camera Preview
    DisposableEffect(cameraProviderFuture, hasCameraPermission) {
        val cameraProvider = cameraProviderFuture.get()
        val cameraExecutor = Executors.newSingleThreadExecutor() // Add this line

        if (hasCameraPermission) {
            val preview = androidx.camera.core.Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview
                )
            } catch (exc: Exception) {
                Log.e("TestFunction", "Use case binding failed", exc)
            }
        }
        onDispose {
//            cameraProvider.shutdown()
            cameraExecutor.shutdown()
            cameraProvider.unbindAll()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (hasCameraPermission) {
            // Display Camera Preview
            Text(text = "Camera Preview")
            Spacer(modifier = Modifier.height(16.dp))
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(9f / 12f),
                factory = { previewView }
            )
        } else {
            // Display Permission Request
            Text(text = "Camera Permission Required")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { requestPermissionLauncher.launch(Manifest.permission.CAMERA) }) {
                Text("Request Camera Permission")
            }
        }
    }
}


/* ============================================================
   3. CameraEffectsFunction
   ============================================================ */

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun CameraEffectsFunction() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var selectedEffect by remember { mutableStateOf<GlEffect?>(null) }

    val effects = listOf(
        "No Effect" to null,
        "Inverted" to shaderInverted(),
        "Grayscale LUT" to shaderCustom()
    )

    val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> = remember {
        ProcessCameraProvider.getInstance(context)
    }

    val previewView = remember {
        PreviewView(context).apply {
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        }
    }

    DisposableEffect(cameraProviderFuture) {
        val cameraProvider = cameraProviderFuture.get()


        val preview = androidx.camera.core.Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview
            )
        } catch (exc: Exception) {
            exc.printStackTrace()
        }

        onDispose {
            cameraProvider.unbindAll()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Camera Effects",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(9f / 12f),
            factory = { previewView }
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
   Preview
   ============================================================ */


@Composable
@Preview(showSystemUi = true, showBackground = true)
fun DemoAppPreview() {
    MaterialTheme {
        DemoApp()
    }
}