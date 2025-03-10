# **Image Graphics Processing with Jetpack Compose**

## **I. Core Image Handling in Compose**
These modules cover the foundational elements of working with images in **Jetpack Compose**.

### **1. Image Loading, Display, and Management**
Jetpack Compose provides an `Image` composable for displaying images efficiently.

#### **Using the `Image` Composable**
```kotlin
Image(
    painter = painterResource(id = R.drawable.sample_image),
    contentDescription = "Sample Image",
    contentScale = ContentScale.Crop,
    modifier = Modifier.fillMaxWidth()
)
```

#### **Loading Images from Different Sources**
- **From Resources**
  ```kotlin
  painterResource(id = R.drawable.image_example)
  ```
- **From Local Storage**
  ```kotlin
  rememberAsyncImagePainter(File("path_to_image"))
  ```
- **From a URL using Coil**
  ```kotlin
  AsyncImage(
      model = "https://example.com/image.jpg",
      contentDescription = "Network Image"
  )
  ```

#### **Handling Image Loading States (Coil Example)**
```kotlin
AsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
        .data("https://example.com/sample.jpg")
        .crossfade(true)
        .build(),
    contentDescription = "Loaded Image",
    placeholder = painterResource(R.drawable.placeholder),
    error = painterResource(R.drawable.error_image)
)
```

---

### **2. Image Manipulation and Transformation**
Jetpack Compose allows modifying and transforming images dynamically.

#### **Basic Transformations**
```kotlin
Image(
    painter = painterResource(id = R.drawable.sample_image),
    contentDescription = "Rotated Image",
    modifier = Modifier
        .fillMaxSize()
        .rotate(45f)
)
```

#### **Cropping an Image with a Shape**
```kotlin
Image(
    painter = painterResource(id = R.drawable.sample_image),
    contentDescription = "Circular Cropped Image",
    modifier = Modifier.clip(CircleShape)
)
```

#### **Brightness, Contrast, and Saturation Adjustments (With Canvas)**
```kotlin
Canvas(modifier = Modifier.fillMaxSize()) {
    drawRect(
        color = Color.Red.copy(alpha = 0.5f)
    )
}
```

---

## **II. Custom Graphics and Drawing with Canvas**
Compose’s `Canvas` API allows drawing custom graphics.

### **3. Canvas Fundamentals**
```kotlin
Canvas(modifier = Modifier.size(200.dp)) {
    drawCircle(
        color = Color.Blue,
        radius = size.minDimension / 3,
        center = center
    )
}
```

#### **Drawing Custom Paths**
```kotlin
Canvas(modifier = Modifier.size(200.dp)) {
    val path = Path().apply {
        moveTo(50f, 50f)
        lineTo(150f, 50f)
        lineTo(100f, 150f)
        close()
    }
    drawPath(path, color = Color.Magenta)
}
```

---

### **4. Advanced Canvas Techniques**
#### **Gradient Effects**
```kotlin
drawRect(
    brush = Brush.linearGradient(
        colors = listOf(Color.Red, Color.Blue),
        start = Offset(0f, 0f),
        end = Offset(size.width, size.height)
    )
)
```

#### **Transformations**
```kotlin
rotate(45f) {
    drawRect(color = Color.Green, size = Size(50f, 50f))
}
```

---
