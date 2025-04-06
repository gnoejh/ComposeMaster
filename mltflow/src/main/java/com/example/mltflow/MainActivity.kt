package com.example.mltflow

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Paint
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview as CameraPreview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.mltflow.ui.theme.ComposeMasterTheme
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import java.io.File
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.text.forEach
import kotlin.text.trim

// Enum class defining ML feature types
enum class MLFeature(val title: String) {
    IMAGE_CLASSIFICATION("Image Classification"),
    OBJECT_DETECTION("Real-time Object Detection"),
    FACE_RECOGNITION("Face Recognition"),
    NONE("Home")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeMasterTheme {
                MLTensorFlowApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MLTensorFlowApp() {
    var currentFeature by remember { mutableStateOf(MLFeature.NONE) }
    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "TensorFlow Lite + Compose") },
                actions = {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                    
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        MLFeature.entries.forEach { feature ->
                            DropdownMenuItem(
                                text = { Text(feature.title) },
                                onClick = { 
                                    currentFeature = feature
                                    menuExpanded = false
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            when (currentFeature) {
                MLFeature.IMAGE_CLASSIFICATION -> ImageClassificationScreen()
                MLFeature.OBJECT_DETECTION -> ObjectDetectionScreen()
                MLFeature.FACE_RECOGNITION -> FaceRecognitionScreen()
                MLFeature.NONE -> WelcomeScreen()
            }
        }
    }
}

@Composable
fun WelcomeScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "ML App using TensorFlow Lite and Jetpack Compose\n\n" +
                   "Select a feature from the top menu",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

// Data class for image classification
data class Classification(val label: String, val confidence: Float)

// Data class for object detection
data class Detection(val label: String, val confidence: Float, val boundingBox: Rect)

@Composable
fun ImageClassificationScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val labelsFilename = "labels.txt" // Your labels file
    var labels by remember { mutableStateOf<List<String>>(emptyList()) }

    // 라벨 로드를 화면 시작 시 수행
    LaunchedEffect(Unit) {
        labels = loadLabels(context, labelsFilename)
        Log.d("ImageClassification", "Labels loaded: ${labels.size} labels")
    }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var classifications by remember { mutableStateOf<List<Classification>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            imageUri = it
            isLoading = true
            errorMessage = null

            coroutineScope.launch {
                try {
                    val bitmap = withContext(Dispatchers.IO) {
                        val source = ImageDecoder.createSource(context.contentResolver, it)
                        ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                            decoder.isMutableRequired = true
                        }
                    }

                    val modelPath = "mobilenet_v1_1.0_224_quant.tflite"

                    try {
                        // 직접 TensorFlow Lite 분류기 생성
                        val options = ImageClassifier.ImageClassifierOptions.builder()
                            .setMaxResults(5)
                            .build()

                        val classifier = withContext(Dispatchers.IO) {
                            try {
                                // assets에서 바로 모델을 로드
                                ImageClassifier.createFromFileAndOptions(
                                    context,
                                    modelPath, // assets 경로에서 직접 로드
                                    options
                                )
                            } catch (e: Exception) {
                                Log.e("ImageClassification", "Error creating classifier", e)
                                errorMessage = "분류기 생성 오류: ${e.message}"
                                null
                            }
                        }

                        if (classifier != null) {
                            // 이미지 분류 수행
                            val tensorImage = org.tensorflow.lite.support.image.TensorImage.fromBitmap(bitmap)
                            val results = classifier.classify(tensorImage)
                            Log.d("ImageClassification", "Classification results: $results")
                            
                            // 디버깅용 로그 추가
                            if (results.isNotEmpty() && results[0].categories.isNotEmpty()) {
                                val firstCategory = results[0].categories[0]
                                Log.d("ImageClassification", "First result: index=${firstCategory.index}, label=${firstCategory.label}, score=${firstCategory.score}")
                            }

                            classifications = processClassificationResults(results, labels)
                            classifier.close() // 사용 후 분류기 닫기
                        } else {
                            errorMessage = "분류기를 로드할 수 없습니다. 예제 데이터를 표시합니다."
                            classifications = listOf(
                                Classification("Cat", 0.95f),
                                Classification("Dog", 0.03f),
                                Classification("Rabbit", 0.01f),
                                Classification("Hamster", 0.005f),
                                Classification("Guinea Pig", 0.002f)
                            )
                        }
                    } catch (e: Exception) {
                        errorMessage = "분류 중 오류 발생: ${e.message}"
                        Log.e("ImageClassification", "Classification error", e)
                    }

                    isLoading = false
                } catch (e: Exception) {
                    errorMessage = "이미지 처리 오류: ${e.message}"
                    isLoading = false
                    Log.e("ImageClassification", "Error processing image", e)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { launcher.launch("image/*") },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("Select Image")
        }

        imageUri?.let { uri ->
            AsyncImage(
                model = uri,
                contentDescription = "Selected image",
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Text(
                    text = "Classification Results:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                if (classifications.isEmpty()) {
                    Text(
                        text = "No objects recognized",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                } else {
                    LazyColumn {
                        items(classifications) { classification ->
                            ClassificationItem(classification)
                        }
                    }
                }
            }
        } ?: run {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Select an image to classify with TensorFlow Lite",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

//Load the labels from the provided file.
private fun loadLabels(context: Context, filename: String): List<String> {
    val labels = mutableListOf<String>()
    try {
        context.assets.open(filename).bufferedReader().useLines { lines ->
            lines.forEach { 
                if (it.isNotBlank()) {
                    labels.add(it.trim())
                }
            }
        }
        Log.i("ImageClassification", "Labels loaded correctly. Count: ${labels.size}")
        
        // 처음 10개 라벨 로깅 (디버깅 목적)
        if (labels.isNotEmpty()) {
            val previewLabels = labels.take(10).joinToString(", ")
            Log.d("ImageClassification", "First 10 labels: $previewLabels")
        }
    } catch (e: IOException) {
        Log.e("ImageClassification", "Error loading labels: ${e.message}", e)
    }
    return labels
}

//New Classifications process, using the list of label to map the names.
private fun processClassificationResults(results: List<Classifications>, labels: List<String>): List<Classification> {
    if (results.isEmpty() || results[0].categories.isEmpty()) {
        Log.w("ImageClassification", "No classification results returned")
        return emptyList()
    }
    
    // 디버깅 목적으로 전체 결과 로깅
    Log.d("ImageClassification", "Processing ${results[0].categories.size} categories")
    Log.d("ImageClassification", "Labels available: ${labels.size}")
    
    return results[0].categories.map {
        // 인덱스 기반 라벨 매핑을 강제로 적용
        val label = if (it.index >= 0 && it.index < labels.size && labels.isNotEmpty()) {
            labels[it.index]
        } else if (!it.label.isNullOrBlank() && it.label != "???") {
            // TFLite 모델이 직접 라벨을 반환하는 경우(보조 로직)
            it.label
        } else {
            // 두 가지 방법 모두 실패한 경우
            Log.w("ImageClassification", "Could not determine label for index: ${it.index}")
            "Unknown (${it.index})"
        }
        
        Log.d("ImageClassification", "Mapped label: index=${it.index}, final label=$label, score=${it.score}")
        Classification(label, it.score)
    }
}

@Composable
fun ClassificationItem(classification: Classification) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = classification.label)
        Text(
            text = "${(classification.confidence * 100).toInt()}%",
            fontWeight = FontWeight.Bold
        )
    }
}

@androidx.annotation.OptIn(ExperimentalGetImage::class)
@Composable
fun ObjectDetectionScreen() {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    var detections by remember { mutableStateOf<List<Detection>>(emptyList()) }
    var cameraPermissionGranted by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        cameraPermissionGranted = isGranted
        if (!isGranted) {
            errorMessage = "Camera permission is required"
        }
    }

    LaunchedEffect(key1 = Unit) {
        requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (cameraPermissionGranted) {
            val objectDetector = remember {
                ObjectDetection.getClient(
                    ObjectDetectorOptions.Builder()
                        .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
                        .enableMultipleObjects()
                        .enableClassification()
                        .build()
                )
            }

            val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
            var cameraExecutor: ExecutorService? = remember { Executors.newSingleThreadExecutor() }

            DisposableEffect(key1 = Unit) {
                onDispose {
                    cameraExecutor?.shutdown()
                }
            }

            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx).apply {
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    }

                    val executor = ContextCompat.getMainExecutor(ctx)

                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()

                        val preview = CameraPreview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }

                        val imageAnalysis = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()

                        imageAnalysis.setAnalyzer(executor) { imageProxy ->
                            try {
                                val mediaImage = imageProxy.image
                                if (mediaImage != null) {
                                    val image = InputImage.fromMediaImage(
                                        mediaImage,
                                        imageProxy.imageInfo.rotationDegrees
                                    )

                                    objectDetector.process(image)
                                        .addOnSuccessListener { detectedObjects ->
                                            detections = detectedObjects.map { obj ->
                                                val label = if (obj.labels.isNotEmpty()) {
                                                    obj.labels[0].text
                                                } else "Unknown"

                                                val confidence = if (obj.labels.isNotEmpty()) {
                                                    obj.labels[0].confidence
                                                } else 0f

                                                Detection(
                                                    label = label,
                                                    confidence = confidence,
                                                    boundingBox = obj.boundingBox
                                                )
                                            }
                                        }
                                        .addOnCompleteListener {
                                            imageProxy.close()
                                        }
                                } else {
                                    imageProxy.close()
                                }
                            } catch (e: Exception) {
                                Log.e("ObjectDetection", "Error: ${e.message}")
                                imageProxy.close()
                            }
                        }

                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                CameraSelector.DEFAULT_BACK_CAMERA,
                                preview,
                                imageAnalysis
                            )
                        } catch (e: Exception) {
                            Log.e("CameraX", "Error binding camera: ${e.message}")
                            errorMessage = "Camera initialization error: ${e.message}"
                        }
                    }, executor)

                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )

            Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasWidth = size.width
                val canvasHeight = size.height

                detections.forEach { detection ->
                    val boundingBox = detection.boundingBox

                    val left = boundingBox.left * canvasWidth / 1000
                    val top = boundingBox.top * canvasHeight / 1000
                    val right = boundingBox.right * canvasWidth / 1000
                    val bottom = boundingBox.bottom * canvasHeight / 1000

                    drawRect(
                        color = Color.Red,
                        topLeft = Offset(left, top),
                        size = Size(right - left, bottom - top),
                        style = Stroke(width = 3f)
                    )

                    drawRect(
                        color = Color.Red.copy(alpha = 0.4f),
                        topLeft = Offset(left, top - 30f),
                        size = Size(right - left, 30f)
                    )

                    val nativeCanvas = drawContext.canvas.nativeCanvas
                    nativeCanvas.drawText(
                        "${detection.label} ${(detection.confidence * 100).toInt()}%",
                        left + 10f,
                        top - 10f,
                        Paint().apply {
                            color = Color.White.toArgb()
                            textSize = 30f
                        }
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.TopCenter)
            ) {
                Text(
                    text = "Point camera at objects",
                    color = Color.White,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(8.dp)
                        .background(Color.Black.copy(alpha = 0.6f))
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Text(
                    text = "Detected objects: ${detections.size}",
                    color = Color.White,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(8.dp)
                        .background(Color.Black.copy(alpha = 0.6f))
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = errorMessage ?: "Camera permission is required for real-time object detection",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { requestPermissionLauncher.launch(android.Manifest.permission.CAMERA) }) {
                    Text("Request Permission")
                }
            }
        }
    }
}

@Composable
fun FaceRecognitionScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var faces by remember { mutableStateOf<List<Face>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            imageUri = it
            isLoading = true
            errorMessage = null

            coroutineScope.launch {
                try {
                    val selectedBitmap = withContext(Dispatchers.IO) {
                        val source = ImageDecoder.createSource(context.contentResolver, it)
                        ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                            decoder.isMutableRequired = true
                        }
                    }
                    bitmap = selectedBitmap

                    val options = FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                        .setMinFaceSize(0.15f)
                        .enableTracking()
                        .build()

                    val faceDetector = FaceDetection.getClient(options)

                    val image = InputImage.fromBitmap(selectedBitmap, 0)

                    try {
                        faces = withContext(Dispatchers.Default) {
                            faceDetector.process(image).await()
                        }

                        if (faces.isEmpty()) {
                            errorMessage = "No faces detected"
                        }
                    } catch (e: Exception) {
                        errorMessage = "Face recognition error: ${e.message}"
                    }

                    isLoading = false
                } catch (e: Exception) {
                    errorMessage = "Image processing error: ${e.message}"
                    isLoading = false
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { launcher.launch("image/*") },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("Select Image")
        }

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(50.dp)
                    .padding(8.dp)
            )
        } else if (imageUri != null && bitmap != null) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Selected image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Fit
                )

                Canvas(modifier = Modifier.matchParentSize()) {
                    val imageWidth = bitmap!!.width.toFloat()
                    val imageHeight = bitmap!!.height.toFloat()

                    val scaleX = size.width / imageWidth
                    val scaleY = size.height / imageHeight
                    val minScale = minOf(scaleX, scaleY)

                    val offsetX = (size.width - (imageWidth * minScale)) / 2
                    val offsetY = (size.height - (imageHeight * minScale)) / 2

                    faces.forEach { face ->
                        val boundingBox = face.boundingBox

                        val left = boundingBox.left * minScale + offsetX
                        val top = boundingBox.top * minScale + offsetY
                        val right = boundingBox.right * minScale + offsetX
                        val bottom = boundingBox.bottom * minScale + offsetY

                        drawRect(
                            color = Color.Green,
                            topLeft = Offset(left, top),
                            size = Size(right - left, bottom - top),
                            style = Stroke(width = 3f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Detected faces: ${faces.size}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(faces) { face ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            face.trackingId?.let {
                                Text("Face ID: $it")
                            }

                            Text("Smile probability: ${(face.smilingProbability?.times(100) ?: 0f).toInt()}%")
                            Text("Left eye open probability: ${(face.leftEyeOpenProbability?.times(100) ?: 0f).toInt()}%")
                            Text("Right eye open probability: ${(face.rightEyeOpenProbability?.times(100) ?: 0f).toInt()}%")

                            Text("Face angle X: ${face.headEulerAngleX}°")
                            Text("Face angle Y: ${face.headEulerAngleY}°")
                            Text("Face angle Z: ${face.headEulerAngleZ}°")
                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Select an image to detect faces with ML Kit",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MLTensorFlowAppPreview() {
    ComposeMasterTheme {
        MLTensorFlowApp()
    }
}
