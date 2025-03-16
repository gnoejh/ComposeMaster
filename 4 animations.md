# **Animations in Compose**

This document provides an overview of various animation techniques available within Jetpack Compose, focusing on their characteristics and use cases.

📌 **Reference:** [Android Developer Guide](https://developer.android.com/develop/ui/compose/animation/introduction)

---
## **Animation Types**

Android Jetpack Compose offers multiple ways to create animations, from simple transitions to complex, state-driven sequences. Here’s a breakdown of common animation types:

### **1. `AnimatedVisibility`**

- **Description:** Controls the visibility of composables with smooth enter/exit transitions.
- **Use Case:** Ideal for showing/hiding UI elements like dialogs, menus, or loading indicators.
- **Example:**

```kotlin
AnimatedVisibility(
    visible = isVisible,
    enter = slideInVertically(),
    exit = slideOutVertically()
) {
    Text("Now you see me!")
}
```

---
### **2. `animateContentSize`**

- **Description:** Smoothly animates changes in a composable’s size.
- **Use Case:** Great for expanding/collapsing content areas, accordions, or list item growth.
- **Example:**

```kotlin
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

---
### **3. `Animated*AsState`**

- **Description:** Animates the transition between different states of a property (e.g., color, size, visibility).
- **Use Case:** Versatile for animating numeric values, colors, or other properties based on state changes.
- **Example:**

```kotlin
@Composable
fun ColorAnimation() {
    var isRed by remember { mutableStateOf(false) }
    val animatedColor by animateColorAsState(
        targetValue = if (isRed) Color.Red else Color.Blue,
        label = ""
    )
    Box(
        modifier = Modifier
            .size(100.dp)
            .background(animatedColor)
            .clickable { isRed = !isRed }
    )
}
```

---
### **4. `AnimatedContent`**

- **Description:** Animates transitions between different content layouts based on state changes.
- **Use Case:** Great for dynamic UI areas where content changes significantly (e.g., tab changes, dynamic content loading).
- **Example:**

```kotlin
@Composable
fun ContentAnimation() {
    var isFirstContent by remember { mutableStateOf(true) }
    Button(onClick = { isFirstContent = !isFirstContent }) {
        Text("Toggle Content")
    }
    AnimatedContent(
        targetState = isFirstContent,
        transitionSpec = {
            if (targetState) {
                (slideInHorizontally { -it } + fadeIn()).togetherWith(slideOutHorizontally { it } + fadeOut())
            } else {
                (slideInHorizontally { it } + fadeIn()).togetherWith(slideOutHorizontally { -it } + fadeOut())
            }.using(SizeTransform(clip = false))
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

---
### **5. `Crossfade`**

- **Description:** A simple way to animate content changes using a crossfade effect.
- **Use Case:** Best for cases where a smooth visual transition is needed between different UI states.
- **Example:**

```kotlin
@Composable
fun CrossfadeAnimation() {
    var isFirstContent by remember { mutableStateOf(true) }
    Button(onClick = { isFirstContent = !isFirstContent }) {
        Text("Toggle Content")
    }
    Crossfade(targetState = isFirstContent, label = "") { targetIsFirstContent ->
        if (targetIsFirstContent) {
            Text("First Content")
        } else {
            Text("Second Content")
        }
    }
}
```

---
## **Animation Comparison Table**

| Feature             | `AnimatedVisibility` | `animateContentSize` | `Animated*AsState` | `AnimatedContent` | `Crossfade` |
|---------------------|---------------------|----------------------|-------------------|----------------|------------|
| **Purpose**        | Show/hide content    | Animate size change  | Animate value    | Animate content change | Simple content swap |
| **Scope**          | Composable visibility | Composable size      | Single value      | Content layout | Content transition |
| **Complexity**     | Low                  | Medium               | Medium           | High           | Low        |
| **Customization**  | Entry/exit effects   | Easing, duration     | Easing, duration | Full transition control | Duration-based |
| **Use Cases**      | Dialogs, menus       | Accordions, lists    | Color, size changes | Tab changes | Swapping images, text |

---

🚀 **Mastering animations in Jetpack Compose allows for smooth, fluid, and engaging user experiences!**