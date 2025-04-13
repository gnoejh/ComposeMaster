package com.example.mlkit


import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mlkit.ui.theme.ComposeMasterTheme
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeMasterTheme {
                MLKitApp()
            }
        }
    }
}


// Enum class defining ML feature types
enum class MLFeature(val title: String) {
    IMAGE_LABELING("Image Labeling"),
    OBJECT_DETECTION("Object Detection"),
    FACE_DETECTION("Face Detection"),
    TEXT_RECOGNITION("Text Recognition"),
    BARCODE_SCANNING("Barcode Scanning"),
    POSE_DETECTION("Pose Detection"),
    SELFIE_SEGMENTATION("Selfie Segmentation"),
    DIGITAL_INK_RECOGNITION("Digital Ink Recognition"),
    ENTITY_EXTRACTION("Entity Extraction"),
    TRANSLATION("Translation"),
    NONE("Home")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MLKitApp() {
    var currentFeature by remember { mutableStateOf(MLFeature.NONE) }
    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "ML Kit Tasks") },
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
                MLFeature.IMAGE_LABELING -> ImageLabelingScreen()
                MLFeature.OBJECT_DETECTION -> ObjectDetectionScreen()
                MLFeature.FACE_DETECTION -> FaceDetectionScreen()
                MLFeature.TEXT_RECOGNITION -> TextRecognitionScreen()
                MLFeature.BARCODE_SCANNING -> BarcodeScanningScreen()
                MLFeature.POSE_DETECTION -> PoseDetectionScreen()
                MLFeature.SELFIE_SEGMENTATION -> SelfieSegmentationScreen()
                MLFeature.DIGITAL_INK_RECOGNITION -> DigitalInkRecognitionScreen()
                MLFeature.ENTITY_EXTRACTION -> EntityExtractionScreen()
                MLFeature.TRANSLATION -> TranslationScreen()
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
            text = "ML Kit App with Jetpack Compose\n\n" +
                    "Select a feature from the top menu",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


@Composable
fun ImageLabelingScreen() {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var labels by remember { mutableStateOf<List<Pair<String, Float>>>(emptyList()) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
        if (uri != null) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            imageBitmap = ImageDecoder.decodeBitmap(source)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { launcher.launch("image/*") }) {
            Text("Select Image")
        }
        Spacer(modifier = Modifier.height(16.dp))
        imageBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Selected Image",
                modifier = Modifier.size(250.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                val image = InputImage.fromBitmap(bitmap, 0)
                val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
                labeler.process(image)
                    .addOnSuccessListener { imageLabels ->
                        labels = imageLabels.map {
                            Pair(it.text, it.confidence)
                        }
                    }
                    .addOnFailureListener { e ->
                        println("Error labeling image: $e")
                    }
            }) {
                Text("Label Image")
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (labels.isNotEmpty()) {
                LazyColumn {
                    items(labels) { label ->
                        Text(
                            text = "${label.first}: ${label.second}",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ObjectDetectionScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Object Detection Screen")
    }
}


@Composable
fun FaceDetectionScreen() {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var faces by remember { mutableStateOf<List<Face>>(emptyList()) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
        if (uri != null) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            imageBitmap = ImageDecoder.decodeBitmap(source)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { launcher.launch("image/*") }) {
            Text("Select Image")
        }
        Spacer(modifier = Modifier.height(16.dp))
        imageBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Selected Image",
                modifier = Modifier.size(250.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                val image = InputImage.fromBitmap(bitmap, 0)
                val options = FaceDetectorOptions.Builder()
                    .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                    .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                    .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                    .build()
                val detector = FaceDetection.getClient(options)
                detector.process(image)
                    .addOnSuccessListener { detectedFaces ->
                        faces = detectedFaces as List<Face>
                    }
                    .addOnFailureListener { e ->
                        println("Error detecting faces: $e")
                    }
            }) {
                Text("Detect Faces")
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (faces.isNotEmpty()) {
                LazyColumn {
                    items(faces) { face ->
                        Text(
                            text = "Face: ${face.boundingBox}",
                            modifier = Modifier.padding(8.dp)
                        )
                        face.leftEyeOpenProbability?.let { probability ->
                            Text(
                                text = "Left eye open probability: $probability",
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                        face.rightEyeOpenProbability?.let { probability ->
                            Text(
                                text = "Right eye open probability: $probability",
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun TextRecognitionScreen() {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var recognizedText by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
        if (uri != null) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            imageBitmap = ImageDecoder.decodeBitmap(source)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { launcher.launch("image/*") }) {
            Text("Select Image")
        }
        Spacer(modifier = Modifier.height(16.dp))
        imageBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Selected Image",
                modifier = Modifier.size(250.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                val image = InputImage.fromBitmap(bitmap, 0)
                val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                recognizer.process(image)
                    .addOnSuccessListener { visionText ->
                        recognizedText = visionText.text
                    }
                    .addOnFailureListener { e ->
                        println("Error recognizing text: $e")
                    }
            }) {
                Text("Recognize Text")
            }
            Spacer(modifier = Modifier.height(16.dp))
            recognizedText?.let { text ->
                Text(
                    text = "Recognized Text:\n$text",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}


@Composable
fun BarcodeScanningScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Barcode Scanning Screen")
    }
}

@Composable
fun PoseDetectionScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Pose Detection Screen")
    }
}

@Composable
fun SelfieSegmentationScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Selfie Segmentation Screen")
    }
}

@Composable
fun DigitalInkRecognitionScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Digital Ink Recognition Screen")
    }
}

@Composable
fun EntityExtractionScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Entity Extraction Screen")
    }
}

@Composable
fun TranslationScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Translation Screen")
    }
}