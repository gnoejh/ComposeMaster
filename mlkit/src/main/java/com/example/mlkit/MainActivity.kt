package com.example.mlkit


import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.data.position
import androidx.compose.ui.unit.dp
import androidx.xr.runtime.math.Pose
//import androidx.privacysandbox.tools.core.generator.build
import com.example.mlkit.ui.theme.ComposeMasterTheme
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
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
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var detectedObjects by remember { mutableStateOf<List<com.google.mlkit.vision.objects.DetectedObject>>(emptyList()) }
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
    val textMeasurer = rememberTextMeasurer() // Initialize textMeasurer here

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
            Box(modifier = Modifier.size(250.dp)) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Selected Image",
                    modifier = Modifier.fillMaxSize()
                )
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val scaleX = size.width / bitmap.width
                    val scaleY = size.height / bitmap.height
                    detectedObjects.forEach { detectedObject ->
                        val rect = detectedObject.boundingBox
                        drawRect(
                            color = Color.Red,
                            topLeft = Offset(rect.left * scaleX, rect.top * scaleY),
                            size = Size(rect.width() * scaleX, rect.height() * scaleY),
                            style = Stroke(width = 2.dp.toPx())
                        )
                        detectedObject.labels.forEach { label ->
                            drawText(
                                textMeasurer = textMeasurer,
                                text = "${label.text}: ${label.confidence.format(2)}",
                                topLeft = Offset(rect.left * scaleX, (rect.top * scaleY) - 20f),
                                style = TextStyle(color = Color.White)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                val image = InputImage.fromBitmap(bitmap, 0)
                val options = ObjectDetectorOptions.Builder()
                    .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
                    .enableClassification()
                    .build()
                val objectDetector = ObjectDetection.getClient(options)
                objectDetector.process(image)
                    .addOnSuccessListener { detectedObjectsList ->
                        detectedObjects = detectedObjectsList
                    }
                    .addOnFailureListener { e ->
                        println("Error detecting objects: $e")
                    }
            }) {
                Text("Detect Objects")
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (detectedObjects.isNotEmpty()) {
                LazyColumn {
                    items(detectedObjects) { detectedObject ->
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(text = "Object Bounding Box: ${detectedObject.boundingBox}")
                            detectedObject.labels.forEach { label ->
                                Text(text = "Label: ${label.text}, Confidence: ${label.confidence}")
                            }
                        }
                    }
                }
            }
        }
    }
}

fun Float.format(digits: Int) = "%.${digits}f".format(this)

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
                        face.smilingProbability?.let { probability ->
                            Text(
                                text = "Smiling probability: $probability",
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
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var poseLandmarks by remember { mutableStateOf<List<PoseLandmark>>(emptyList()) }
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
    val textMeasurer = rememberTextMeasurer()

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
            Box(modifier = Modifier.size(250.dp)) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Selected Image",
                    modifier = Modifier.fillMaxSize()
                )
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val scaleX = size.width / bitmap.width
                    val scaleY = size.height / bitmap.height

                    poseLandmarks.forEach { landmark ->
                        val x = landmark.position.x
                        val y = landmark.position.y
                        drawCircle(
                            color = Color.Red,
                            center = Offset(x * scaleX, y * scaleY),
                            radius = 5.dp.toPx()
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                val image = InputImage.fromBitmap(bitmap, 0)

                // For accurate model
                val options = AccuratePoseDetectorOptions.Builder()
                    .setDetectorMode(AccuratePoseDetectorOptions.SINGLE_IMAGE_MODE)
                    .build()
                // For fast model
                // val options = PoseDetectorOptions.Builder()
                //       .setDetectorMode(PoseDetectorOptions.SINGLE_IMAGE_MODE)
                //       .build()

                val poseDetector = PoseDetection.getClient(options)
                poseDetector.process(image)
                    .addOnSuccessListener { pose ->
                        poseLandmarks = pose.allPoseLandmarks
                    }
                    .addOnFailureListener { e ->
                        Log.e("PoseDetectionScreen", "Error detecting pose: ${e.message}")
                    }
            }) {
                Text("Detect Pose")
            }
        }
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
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var recognizedText by remember { mutableStateOf<String?>(null) }
    var translatedText by remember { mutableStateOf<String?>(null) }
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
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
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
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val image = InputImage.fromBitmap(bitmap, 0)
                val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                recognizer.process(image)
                    .addOnSuccessListener { visionText ->
                        recognizedText = visionText.text
                        Log.d("TranslationScreen", "Recognized Text: ${recognizedText}") // Log the recognized text
                    }
                    .addOnFailureListener { e ->
                        Log.e("TranslationScreen", "Error recognizing text: ${e.message}") // Log error
                    }
            }) {
                Text("Recognize Text")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        recognizedText?.let { text ->
            Box(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Recognized Text:\n$text",
                    modifier = Modifier.padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                // Translate text to Spanish
                val options = TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.ENGLISH)
                    .setTargetLanguage(TranslateLanguage.RUSSIAN)
                    .build()
                val translator = Translation.getClient(options)

                // Download the translation model if necessary
                val conditions = DownloadConditions.Builder()
                    .requireWifi()
                    .build()
                translator.downloadModelIfNeeded(conditions)
                    .addOnSuccessListener {
                        Log.d("TranslationScreen", "Translation model downloaded successfully")
                        translator.translate(text)
                            .addOnSuccessListener { translated ->
                                translatedText = translated
                                Log.d("TranslationScreen", "Translated Text: $translated") // Log the translated text
                            }
                            .addOnFailureListener { e ->
                                Log.e("TranslationScreen", "Error translating text: ${e.message}") // Log error
                            }
                    }
                    .addOnFailureListener { e ->
                        Log.e("TranslationScreen", "Error downloading translation model: ${e.message}") // Log error
                    }
            }) {
                Text("Translate to Spanish")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        translatedText?.let{ translated ->
            Text(text = "Translated Text: \n $translated")
        }
    }
}