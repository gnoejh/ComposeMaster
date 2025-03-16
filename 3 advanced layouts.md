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
