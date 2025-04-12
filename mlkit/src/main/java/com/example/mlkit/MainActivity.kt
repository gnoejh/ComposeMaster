package com.example.mlkit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import com.example.mlkit.ui.theme.ComposeMasterTheme

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
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Image Labeling Screen")
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
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Face Detection Screen")
    }
}

@Composable
fun TextRecognitionScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Text Recognition Screen")
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