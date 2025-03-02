# Animations in Compose

This document provides an overview of various animation techniques available within the Compose_Master module, focusing on their characteristics and use cases.

Android Developer: <https://developer.android.com/develop/ui/compose/animation/introduction>

## Animation Types

Android Jetpack Compose offers several ways to create animations, ranging from simple transitions to complex, state-driven sequences. Here's a breakdown of common types:

### 1. `AnimatedVisibility`

-   **Description:** Controls the visibility of composables with smooth enter/exit transitions.
-   **Use Case:** Ideal for showing/hiding UI elements like dialogs, menus, or loading indicators.
- **Example**

```compose
AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(),
        exit = slideOutVertically()
    ) {
        Text("Now you see me!")
    }
```
### 2. `animateContentSize`

-   **Description:** Smoothly animates changes in a composable's size.
-   **Use Case:** Great for expanding/collapsing content areas, accordions, or list item growth.
- **Example**
```compose
Column(
        modifier = Modifier
            .animateContentSize()
            .background(Color.LightGray)
            .clickable { isExpanded = !isExpanded }
            .padding(16.dp)
    ) {
        Text("Expandable Content")
        if (isExpanded) {
            Text("Additional content displayed here.")
        }
    }
```
### 3. `Animated*AsState`

-   **Description:** Animates the transition between different states of a property, for instance, from size 1 to size 2.
-   **Use Case:** Versatile for animating numeric values, colors, or other types based on state changes.
    - **Example**
```compose
@Composable
fun ColorAnimation() {
     var isRed by remember { mutableStateOf(false) }
     val animatedColor by animateColorAsState(
         targetValue = if (isRed) Color.Red else Color.Blue,
         label = ""
     )     Box(
         modifier = Modifier
             .size(100.dp)
             .background(animatedColor)
             .clickable { isRed = !isRed }
     )
 }
 ```

### 4. `AnimatedContent`

-   **Description:** Allows animations between different content layouts depending on the state.
-   **Use Case:** Excellent for dynamic UI areas where content changes dramatically (e.g., tab changes).
    - **Example**
```compose
@Composable
 fun ContentAnimation() {
     var isFirstContent by remember { mutableStateOf(true) }     // Toggle content on click (e.g., from a button)
     // Button(onClick = { isFirstContent = !isFirstContent }) {
     //     Text("Toggle Content")
     // }     AnimatedContent(
         targetState = isFirstContent,
         transitionSpec = {
             if (targetState) {
                 (slideInHorizontally { width -> -width } + fadeIn()).togetherWith(
                     slideOutHorizontally { width -> width } + fadeOut())
             } else {
                 (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                     slideOutHorizontally { width -> -width } + fadeOut())
             }.using(
                 SizeTransform(clip = false)
             )
         }, label = ""
     ) { targetIsFirstContent ->
         if (targetIsFirstContent) {
             Text("First Content")
         } else {
             Text("Second Content")
         }
     }
 }
```
### 5. `Crossfade`

-   **Description:** A simpler way to animate content changes with a crossfade effect.
-   **Use Case:** Best for cases where a simple content swap with visual continuity is needed.
    - **Example**
```compose
@Composable
fun CrossfadeAnimation() {
     var isFirstContent by remember { mutableStateOf(true) }     // Toggle content on click (e.g., from a button)
     //Button(onClick = { isFirstContent = !isFirstContent }) {
     //    Text("Toggle Content")
     //}     Crossfade(targetState = isFirstContent, label = "") { targetIsFirstContent ->
         if (targetIsFirstContent) {
             Text("First Content")
         } else {
             Text("Second Content")
         }
     }
 }
```
## Animation Comparison Table

| Feature             | `AnimatedVisibility` | `animateContentSize` | `Animated*AsState` | `AnimatedContent` | `Crossfade` |
| :------------------ | :------------------- | :------------------- | :----------------- | :---------------- | :---------- |
| **Purpose**         | Show/hide content    | Animate size change  | Animate value      | Animate content change      | Simple content swap with transition |
| **Scope**           | Composable visibility| Composable size      | Single value       | Content layout    | Content |
| **Complexity**      | Low                  | Medium               | Medium             | High           | Low         |
| **Customization**   | Entry/exit effects  | Easing, duration      | Easing, duration    | Complete transition logic            | Duration         |
| **Use Cases**       | Dialogs, menus       | Accordions, lists    | Color, size changes | Tab changes      | Swapping images, text |



