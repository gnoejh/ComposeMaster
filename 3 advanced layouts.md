
# Week 3: UI Elements, Lists, Grids, and Advanced Layouts

## Slide 1: Overview of Week 3

- **Topics This Week:**
  - Common UI Elements
  - Lists and Grids
  - Advanced Layouts
  - Assignments

## Slide 2: Common UI Elements

- `TextField`, Checkbox, RadioButton, Switch
- Handling user input and events
- Callbacks like `onClick`, `onValueChange`
- Basic UI state management

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
                onCheckedChange = { isChecked -> checked = isChecked }
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
                onCheckedChange = { toggled -> isSwitched = toggled }
            )
            Text("Switch me!")
        }
    }
}
```

## Slide 3: Lists – LazyColumn & LazyRow

- Efficient list rendering
- Only renders visible items
- Great for scrollable lists of dynamic data

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

## Slide 4: Grids – LazyVerticalGrid

- Display items in a grid layout
- Useful for galleries or card layouts

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

## Slide 5: Advanced Layouts with ConstraintLayout

- Allows more flexible positioning
- Similar to XML ConstraintLayout
- Define relationships using constraints

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

## Slide 6: Scaffold for Common App Structures

- Provides layout structure: `TopAppBar`, `BottomNavigation`, `FloatingActionButton`, `Drawer`
- Handles layout scaffolding automatically

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

## Slide 7: Navigation Patterns

- Use `TopAppBar` for consistent brand and back navigation
- Bottom navigation for switching between main sections
- Drawer for additional or less-frequent navigation
- Each approach can integrate with the Navigation component

```kotlin
@Composable
fun NavigationSkeleton() {
    // Combine Scaffold with official Navigation libraries
    // or custom state management.
}
```

## Slide 8: Assignments

1. Create a form with `TextField`, Checkbox, RadioButton, and Switch.
2. Build a list of items using `LazyColumn` (or `LazyRow`) and design a grid with `LazyVerticalGrid`.
3. Create a complex layout using `ConstraintLayout`.
4. Develop an app structure using Scaffold, `TopAppBar`, and `BottomNavigation`.

---

*End of Week 3 Slides*
