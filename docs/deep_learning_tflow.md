# AI Coding in Jetpack Compose: A Comprehensive Tutorial

## Introduction

Jetpack Compose, Android's modern toolkit for building native UI, can be seamlessly integrated with machine learning capabilities using TensorFlow Lite. This tutorial will guide you through implementing AI features in your Compose applications.

## Prerequisites

- Basic knowledge of Kotlin and Jetpack Compose
- Android Studio Arctic Fox or newer
- Understanding of basic machine learning concepts

## Useful Sites
- [mobilnet](https://www.tensorflow.org/api_docs/python/tf/keras/applications/MobileNet) 
- [kaggle](https://www.kaggle.com/code/gaur128/creating-tflite-model)
- [TensorFlow Lite](https://ai.google.dev/edge/litert)
- [ML Kit](https://developers.google.com/ml-kit)

# TFLite and LiteRT

This section explains the roles and official sites for **TensorFlow Lite (TFLite)** and **LiteRT**—two key components for integrating machine learning features into mobile and edge applications.

---

## TensorFlow Lite (TFLite)

**What It Is:**  
TensorFlow Lite is the lightweight version of TensorFlow designed specifically for mobile and embedded devices. It enables on-device machine learning inference without relying on a server, thereby reducing latency and preserving privacy. It comes with tools to optimize models, such as quantization and pruning, and supports hardware accelerators like GPUs or specialized AI chips.

**Key Features:**

- **Optimized for Mobile and Embedded Devices:**  
  Designed to work efficiently on low-resource devices, ensuring minimal latency for tasks such as image classification, object detection, and more.

- **Model Optimization:**  
  Allows developers to convert full TensorFlow models into a compact, efficient format, which is crucial for running deep learning models on smartphones and IoT devices.

- **Hardware Acceleration:**  
  Supports hardware accelerators (e.g., GPUs and NPUs) to speed up model inference.

- **Robust Ecosystem:**  
  Offers extensive documentation, community support, and regular updates, making it a popular choice for deploying AI models on mobile platforms.

**Official Website:**  
Visit [TensorFlow Lite](https://www.tensorflow.org/lite) for comprehensive guides, documentation, and updates.

---

## LiteRT

**What It Is:**  
LiteRT (Lite Runtime) is a runtime module focused on providing optimized execution of machine learning models on edge devices. While similar in concept to TFLite—targeting efficient on-device inference—LiteRT is part of a broader edge AI toolset that incorporates additional low-level optimizations and integrations with edge-specific software.

**Key Features:**

- **Edge Optimization:**  
  Specifically designed to enhance performance for real-time tasks on edge devices with a strong focus on low-level runtime improvements, ideal for applications where performance and power efficiency are critical.

- **Specialized Use Cases:**  
  While TensorFlow Lite is popular for mobile app development, LiteRT might be more suitable for specialized edge scenarios, such as industrial IoT or advanced robotics, where every millisecond of inference matters.

- **Integration with Google’s Edge AI Solutions:**  
  Positioned within Google's suite of edge AI tools, offering tight integration with other edge modules and hardware accelerators.

**Official Website:**  
For detailed documentation, examples, and updates, check out the dedicated page at [LiteRT](https://ai.google.dev/edge/litert).

---

## Summary

- **TensorFlow Lite (TFLite):**
   - A framework for deploying machine learning models on mobile and embedded devices.
   - Official website: [https://www.tensorflow.org/lite](https://www.tensorflow.org/lite)

- **LiteRT:**
   - An optimized runtime for executing ML models on edge devices with a focus on real-time performance and low-level hardware integration.
   - Official website: [https://ai.google.dev/edge/litert](https://ai.google.dev/edge/litert)

By understanding these distinctions, developers can choose the right tool based on their specific application requirements, whether it’s for a general-purpose mobile solution (TFLite) or a highly optimized runtime for edge computing (LiteRT).


## Setting Up Your Project

First, add the necessary dependencies to your `build.gradle` file:

```kotlin
dependencies {
    // Jetpack Compose
    implementation "androidx.compose.ui:ui:1.5.0"
    implementation "androidx.compose.material:material:1.5.0"
    implementation "androidx.compose.ui:ui-tooling-preview:1.5.0"
    
    // TensorFlow Lite
    implementation "org.tensorflow:tensorflow-lite:2.12.0"
    implementation "org.tensorflow:tensorflow-lite-support:0.4.2"
    implementation "org.tensorflow:tensorflow-lite-metadata:0.4.2"
    implementation "org.tensorflow:tensorflow-lite-gpu:2.12.0" // Optional GPU acceleration
}
```

## Creating a Basic Image Classifier

### Step 1: Add a Pre-trained Model

1. Create an `assets` folder in your Android project
2. Download a TensorFlow Lite model (e.g., MobileNet)
3. Place the `.tflite` file in the assets folder

### Step 2: Define the Classifier Class

Create a class that handles the TensorFlow Lite operations:

```kotlin
class ImageClassifier(
    private val context: Context,
    private val modelName: String = "mobilenet_v1.tflite",
    private val labels: List<String>
) {
    private var interpreter: Interpreter? = null
    
    init {
        val model = loadModelFile(context, modelName)
        val options = Interpreter.Options()
        interpreter = Interpreter(model, options)
    }
    
    private fun loadModelFile(context: Context, modelName: String): ByteBuffer {
        val assetManager = context.assets
        val fileDescriptor = assetManager.openFd(modelName)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }
    
    fun classify(bitmap: Bitmap): List<Classification> {
        // Preprocess the image
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, true)
        val byteBuffer = convertBitmapToByteBuffer(resizedBitmap)
        
        // Run inference
        val result = Array(1) { FloatArray(labels.size) }
        interpreter?.run(byteBuffer, result)
        
        // Process results
        return processResults(result[0])
    }
    
    // Additional helper methods for image preprocessing and result processing
    // ...
    
    companion object {
        private const val INPUT_SIZE = 224
    }
}

data class Classification(val label: String, val confidence: Float)
```

### Step 3: Implement the UI with Jetpack Compose

Create a composable function to capture and classify images:

```kotlin
@Composable
fun ImageClassifierScreen(
    classifier: ImageClassifier,
    modifier: Modifier = Modifier
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var classifications by remember { mutableStateOf<List<Classification>>(emptyList()) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri = uri
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            classifications = classifier.classify(bitmap)
        }
    }
    
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Button(
            onClick = { launcher.launch("image/*") },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Select Image")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
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
            
            Text(
                text = "Classifications:",
                style = MaterialTheme.typography.h6
            )
            
            LazyColumn {
                items(classifications) { classification ->
                    ClassificationItem(classification)
                }
            }
        }
    }
}

@Composable
fun ClassificationItem(classification: Classification) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = classification.label)
        Text(
            text = "${(classification.confidence * 100).toInt()}%",
            fontWeight = FontWeight.Bold
        )
    }
}
```

## Advanced AI Integration: Real-time Object Detection

For real-time object detection using your device's camera:

```kotlin
@Composable
fun CameraObjectDetection(
    objectDetector: ObjectDetector,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    
    var detections by remember { mutableStateOf<List<Detection>>(emptyList()) }
    
    Box(modifier = modifier) {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                val executor = ContextCompat.getMainExecutor(ctx)
                
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    
                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                    
                    imageAnalysis.setAnalyzer(executor) { imageProxy ->
                        // Process frame and run ML model
                        val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                        // Convert ImageProxy to Bitmap
                        val bitmap = imageProxyToBitmap(imageProxy)
                        // Run detection
                        detections = objectDetector.detect(bitmap)
                        imageProxy.close()
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
                        Log.e("Camera", "Use case binding failed", e)
                    }
                }, executor)
                
                previewView
            },
            modifier = Modifier.fillMaxSize()
        )
        
        // Draw bounding boxes over detected objects
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            
            detections.forEach { detection ->
                val rect = detection.boundingBox
                val scaledRect = Rect(
                    left = rect.left * width,
                    top = rect.top * height,
                    right = rect.right * width,
                    bottom = rect.bottom * height
                )
                
                drawRect(
                    color = Color.Red,
                    topLeft = Offset(scaledRect.left, scaledRect.top),
                    size = Size(scaledRect.width(), scaledRect.height()),
                    style = Stroke(width = 4f)
                )
                
                drawContext.canvas.nativeCanvas.drawText(
                    "${detection.label} ${(detection.confidence * 100).toInt()}%",
                    scaledRect.left,
                    scaledRect.top - 10,
                    Paint().apply {
                        color = android.graphics.Color.RED
                        textSize = 42f
                        strokeWidth = 4f
                    }
                )
            }
        }
    }
}
```

## Implementing Face Detection

Another useful AI feature is face detection:

```kotlin
@Composable
fun FaceDetectionScreen() {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var faces by remember { mutableStateOf<List<Face>>(emptyList()) }
    val context = LocalContext.current
    
    val faceDetector = remember {
        FaceDetection.getClient(
            FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .enableTracking()
                .build()
        )
    }
    
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri = uri
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            val image = InputImage.fromBitmap(bitmap, 0)
            faceDetector.process(image)
                .addOnSuccessListener { detectedFaces ->
                    faces = detectedFaces
                }
                .addOnFailureListener { e ->
                    Log.e("FaceDetection", "Face detection failed", e)
                }
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = { launcher.launch("image/*") },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Select Image for Face Detection")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        imageUri?.let { uri ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                // Display the image
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
                
                // Draw face bounding boxes
                Canvas(modifier = Modifier.fillMaxSize()) {
                    // Implementation to draw face boxes
                    // ...
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Detected ${faces.size} face(s)",
                style = MaterialTheme.typography.h6
            )
            
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(faces) { face ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = 4.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Face ID: ${face.trackingId ?: "Unknown"}")
                            Text("Smiling Probability: ${face.smilingProbability?.times(100)?.toInt()}%")
                            Text("Left Eye Open Probability: ${face.leftEyeOpenProbability?.times(100)?.toInt()}%")
                            Text("Right Eye Open Probability: ${face.rightEyeOpenProbability?.times(100)?.toInt()}%")
                        }
                    }
                }
            }
        }
    }
}
```

## Optimizing ML Performance in Compose

When working with ML models in Compose, consider these optimization tips:

1. **Use Coroutines for ML Operations**: 
   
   ```kotlin
   var results by remember { mutableStateOf<List<Result>>(emptyList()) }
   val scope = rememberCoroutineScope()
   
   Button(onClick = {
       scope.launch {
           val bitmap = // Get bitmap
           withContext(Dispatchers.Default) {
               // Run ML model
               classifier.classify(bitmap)
           }.let { classification ->
               results = classification
           }
       }
   }) {
       Text("Classify")
   }
   ```

2. **Model Optimization**:
   - Convert models to TensorFlow Lite format
   - Quantize models to reduce size
   - Consider using ML Kit for common tasks

3. **UI Responsiveness**:
   - Use State and remember for efficient recompositions
   - Implement proper loading states during ML processing

## Conclusion

Integrating AI capabilities into your Jetpack Compose applications opens up numerous possibilities for creating intelligent and responsive user experiences. With TensorFlow Lite and ML Kit, you can add powerful machine learning features while maintaining a modern, declarative UI through Compose.

## Resources

- [TensorFlow Lite Documentation](https://www.tensorflow.org/lite)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [ML Kit for Firebase](https://developers.google.com/ml-kit)
- [CameraX Documentation](https://developer.android.com/training/camerax)

## Next Steps

- Explore custom model training with TensorFlow
- Implement on-device training with TensorFlow Lite
- Explore federated learning concepts for privacy-preserving ML
