# **UI Elements, Lists, Grids, and Advanced Layouts**

This guide covers essential **UI elements, lists, grids, and advanced layouts** in Jetpack Compose, helping you build dynamic and scalable UI structures.

---
## **1. Common UI Elements**

Jetpack Compose provides core UI elements for handling user input and interactions.

### **Core UI Components**
| Component | Purpose |
|-----------|---------|
| **TextField** | Accepts user input text |
| **Checkbox** | Binary selection (checked/unchecked) |
| **RadioButton** | Allows selection from multiple options |
| **Switch** | Toggles a setting ON/OFF |

### **Example: Handling User Input with Form Elements**
The following example demonstrates how to use `TextField`, `Checkbox`, `RadioButton`, and `Switch` in a form.

```kotlin
@Composable
fun FormElementsDemo() {
    var text by remember { mutableStateOf("") }
    var checked by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Option A") }
    var isSwitched by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = text,
            onValueChange = { newValue -> text = newValue },
            label = { Text("Enter text") }
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = checked,
                onCheckedChange = { checked = it }
            )
            Text("Check me!")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = (selectedOption == "Option A"),
                onClick = { selectedOption = "Option A" }
            )
            Text("Option A")
            Spacer(modifier = Modifier.width(8.dp))
            RadioButton(
                selected = (selectedOption == "Option B"),
                onClick = { selectedOption = "Option B" }
            )
            Text("Option B")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(
                checked = isSwitched,
                onCheckedChange = { isSwitched = it }
            )
            Text("Switch me!")
        }
    }
}
```

---
## **2. Lists – LazyColumn & LazyRow**

Lists efficiently display large datasets by rendering only visible items.

### **Key Features:**
✅ **Lazy Loading** – Improves performance by rendering only necessary items  
✅ **Dynamic Data Handling** – Easily updates with new items  
✅ **Efficient Scrolling** – Reduces memory consumption

### **Example: Creating a Scrollable List with LazyColumn**
The `LazyColumn` composable efficiently renders a vertical list.

```kotlin
@Composable
fun SimpleList() {
    val items = listOf("Item 1", "Item 2", "Item 3", "Item 4")
    LazyColumn {
        items(items) { item ->
            Text(
                text = item,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}
```

---
## **3. Grids – LazyVerticalGrid**

Grids are useful for displaying structured items, such as image galleries or product lists.

### **Example: Creating a Simple Grid Layout**
The `LazyVerticalGrid` composable efficiently arranges items in a grid.

```kotlin
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SimpleGrid() {
    val items = (1..10).map { "Grid Item $it" }
    LazyVerticalGrid(
        cells = GridCells.Fixed(2),
        modifier = Modifier.padding(8.dp)
    ) {
        items(items.size) { index ->
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color.LightGray)
                    .fillMaxSize()
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(items[index])
            }
        }
    }
}
```

---
## **4. Advanced Layouts with ConstraintLayout**

`ConstraintLayout` allows flexible positioning, similar to XML-based ConstraintLayout in traditional Android Views.

### **Example: Positioning UI Elements with ConstraintLayout**
This example demonstrates how to **align** UI components using constraints.

```kotlin
@Composable
fun ConstraintLayoutExample() {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (title, button) = createRefs()

        Text(
            text = "ConstraintLayout Title",
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        Button(
            onClick = { /* TODO */ },
            modifier = Modifier.constrainAs(button) {
                bottom.linkTo(parent.bottom, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
            }
        ) {
            Text("Click Me")
        }
    }
}
```

---
## **5. Scaffold for Common App Structures**

`Scaffold` provides a structured layout with built-in support for **TopAppBar, BottomNavigation, FloatingActionButton, and Drawer**.

### **Example: Using Scaffold to Structure an App Layout**
```kotlin
@Composable
fun ScaffoldExample() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("My App") })
        },
        bottomBar = {
            BottomNavigation {
                /* Bottom nav items here */
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Action */ }) {
                Text("+")
            }
        }
    ) { innerPadding ->
        Text(
            "Scaffold Content",
            modifier = Modifier.padding(innerPadding)
        )
    }
}
```
---
### Sturucture of Composables
**Jetpack Compose Composables: Structure and Properties**

### **1. What is a Composable?**
A **Composable** is a function annotated with `@Composable` that defines a piece of UI in Jetpack Compose. Unlike traditional Views in Android, Composables are declarative and do not modify UI elements directly but instead describe how UI should look based on state.

Example:
```kotlin
@Composable
fun Greeting(name: String) {
    Text(text = "Hello, $name!")
}
```

---

### **2. General Structure of a Composable**
A basic Composable function follows this structure:
```kotlin
@Composable
fun FunctionName(
    parameter1: Type,  // Required data input
    modifier: Modifier = Modifier,  // Optional styling and layout adjustments
    property: Type = DefaultValue,  // UI behavior properties
    eventHandler: () -> Unit = {}  // User interaction (clicks, gestures, etc.)
) {
    // UI Elements inside this function
}
```
- Must be annotated with `@Composable`.
- Accepts parameters for flexibility.
- Uses other Composables to construct the UI.

---

### **3. Types of Composables**
#### **a. Basic UI Composables**
These are fundamental building blocks:
- `Text()` – Displays text.
- `Image()` – Displays images.
- `Button()` – Represents a clickable button.
- `Icon()` – Displays an icon.

#### **b. Layout Composables**
These help organize UI elements:
- `Row()` – Arranges items horizontally.
- `Column()` – Arranges items vertically.
- `Box()` – Overlaps elements.
- `LazyColumn()` – Optimized scrolling list.

---

### **4. Components Inside `()` of a Composable**
Most Composables accept these elements inside `()`:

1. **General Parameters** (Data required by the Composable)
   ```kotlin
   Text(text = "Hello")
   ```

2. **Modifier** (Styling and layout adjustments)
   ```kotlin
   Button(modifier = Modifier.padding(16.dp)) { Text("Click Me") }
   ```

3. **Properties** (Behavior and appearance customization)
   ```kotlin
   Button(enabled = false) { Text("Disabled") }
   ```

4. **Event Listeners** (User interactions such as clicks)
   ```kotlin
   Button(onClick = { /* Handle click */ }) { Text("Click Me") }
   ```

---

### **5. Example: Custom Composable with Parameters**
```kotlin
@Composable
fun CustomCard(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .padding(16.dp)
            .clickable(onClick = onClick),
        elevation = 8.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = description, fontSize = 16.sp)
        }
    }
}
```

---

This document provides a structured guide to **Compose Composables, their structure, properties, lifecycle, and best practices** for optimization and reuse.



----
### Major Jetpack Compose Composables and Layouts

| **Category**         | **Component**              | **Description**                                                                 |
|-----------------------|----------------------------|---------------------------------------------------------------------------------|
| **Composables**       | Text                      | Displays text in the UI.                                                       |
|                       | Image                     | Displays images in the UI.                                                     |
|                       | Button                    | Represents a clickable button.                                                 |
|                       | Icon                      | Displays material icons.                                                       |
|                       | Checkbox                  | Provides a toggleable checkbox.                                                |
|                       | RadioButton               | Provides single-selection radio buttons.                                       |
|                       | Switch                    | Provides toggle switches.                                                      |
|                       | TextField                 | Enables user input fields.                                                     |
| **Layouts**           | Column                   | Arranges children vertically.                                                  |
|                       | Row                       | Arranges children horizontally.                                                |
|                       | Box                       | Stacks children on top of each other.                                          |
|                       | ConstraintLayout          | Defines complex relationships between UI elements.                             |
|                       | LazyColumn                | Handles vertical scrolling lists.                                              |
|                       | LazyRow                   | Handles horizontal scrolling lists.                                            |
|                       | FlowRow/FlowColumn        | Wraps children when space is constrained (from Accompanist library).           |
| **Specialized Layouts** | Scaffold                | Creates structured screens with bars, FABs, etc.                               |
|                       | Card                      | Displays content with rounded corners and shadow.                              |
|                       | Dialog                    | Creates modal dialogs.                                                         |
|                       | Surface                   | Provides customizable containers following material design.                    |
|                       | Divider                   | Adds visual separators between UI elements.                                    |
