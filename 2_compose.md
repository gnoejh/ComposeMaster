# Week 2: Introduction to Jetpack Compose and Setup

This week, we'll dive into the world of Jetpack Compose, Android's modern toolkit for building native UI. We'll explore its core concepts, understand how it differs from traditional XML layouts, and get our hands dirty by setting up our development environment and creating our first composable functions.

## Topics

### Overview of Jetpack Compose

**What is Jetpack Compose?**

Jetpack Compose is a modern, declarative UI toolkit for Android. It allows you to build your app's UI using Kotlin code, rather than relying on XML layout files. Compose simplifies and accelerates UI development with less code, powerful tools, and intuitive Kotlin APIs.

**Why Use Jetpack Compose?**

* **Declarative UI:** Compose lets you describe *what* your UI should look like, and the framework takes care of *how* to render it. This leads to more concise and maintainable code.
* **Less Boilerplate:** Say goodbye to verbose XML layouts. Compose reduces the amount of code you need to write to create complex UIs.
* **Intuitive Kotlin APIs:** Compose is built with Kotlin in mind, leveraging its powerful language features for a more enjoyable development experience.
* **Powerful Tools:** Android Studio provides excellent tooling for Compose, including live previews, interactive editing, and more.
* **Interoperability:** Compose can be used alongside existing View-based layouts, allowing you to adopt it incrementally.
* **Material Design 3:** Compose offers an implementation of Material Design 3, the next evolution of Material Design.

**Declarative UI Paradigm**

In a declarative UI paradigm, you describe the desired state of your UI, and the framework handles the process of updating the UI to match that state. This is in contrast to imperative UI, where you manually manipulate UI elements to reflect changes.

### Compose vs. Traditional XML Layouts

**Key Differences**


| Feature          | Compose                            | Traditional XML Layouts                    |
| ---------------- | ---------------------------------- | ------------------------------------------ |
| UI Definition    | Kotlin code                        | XML files                                  |
| UI Paradigm      | Declarative                        | Imperative                                 |
| State Management | Built-in state management features | Manual state management                    |
| Performance      | Optimized for efficient UI updates | Can be less efficient with complex layouts |
| Tooling          | Live previews, interactive editing | Layout Editor, limited live previews       |
| Code Style       | More concise, less boilerplate     | More verbose, more boilerplate             |

**Advantages of Compose**

* **Faster Development:** Compose's declarative nature and concise syntax lead to faster development cycles.
* **Easier Maintenance:** Code is easier to read, understand, and modify.
* **Improved Performance:** Compose's intelligent recomposition system optimizes UI updates.
* **Better Tooling:** Android Studio's Compose tooling is more powerful and intuitive.
* **Modern Design:** Compose offers an implementation of Material Design 3.

### Setting Up the Compose Environment

**Project Creation**

1. Open Android Studio.
2. Click **New Project**.
3. Under the **Phone and Tablet** category, select **Empty Activity**.
4. Click **Next**.
5. Name your app (e.g., "ComposeTutorial").
6. Choose Kotlin as the language.
7. Select a minimum SDK.
8. Click **Finish**.

**Adding Compose Dependencies**

Compose dependencies are usually included by default when you create a new project with the Empty Compose Activity template. If you need to add them manually, open your app-level `build.gradle` file (Module :app) and add the following dependencies within the `dependencies` block:

**Note:** The version numbers may vary. Check the latest version on the official Android developer documentation.

### Your First Composable

**`@Composable` Annotation**

The `@Composable` annotation is the key to building UI with Compose. It marks a function as a composable function, meaning it can be used to define a part of your UI. Composable functions can only be called from other composable functions.

**`Text` Composable**

The `Text` composable is used to display text on the screen.

import androidx.compose.material3.Text 
import androidx.compose.runtime.Composable
@Composable fun Greeting(name: String) { Text(text = "Hello $name!") }

**`Column` and `Row` Composables**

`Column` and `Row` are composables used to arrange other composables vertically and horizontally, respectively.

import androidx.compose.foundation.layout.Column 
import androidx.compose.material3. Text import androidx.compose.runtime. 
Composable

@Composable 
fun MyLayout() { 
    Column { Text("First Item") 
        Text("Second Item") 
        Text("Third Item") 
    } 
}

**Previews**

Compose allows you to preview your composables directly in Android Studio without running the app on a device or emulator. Use the `@Preview` annotation to enable this feature.

import androidx.compose.material3.Text 
import androidx.compose.runtime.Composable 
import androidx.compose.ui.tooling.preview. 
Preview
@Composable 
fun Greeting(name: String) { 
    Text(text = "Hello $name!") 
}

@Preview(showBackground = true) 
@Composable 
fun GreetingPreview() { 
    Greeting("Compose") 
}

### Understanding the Compose Compiler

Jetpack Compose uses a Kotlin compiler plugin to transform composable functions into the app's UI elements. The compiler analyzes your composable functions and generates the necessary code to build and update the UI efficiently.

## Assignments

1.  **Create a New Android Project with Compose:** Follow the steps outlined in the "Setting Up the Compose Environment" section to create a new project.
2.  **Build a Simple "Hello, Compose!" App:**
    *   Create a composable function that displays the text "Hello, Compose!".
    *   Call this composable from your activity's `setContent` block.
    *   Run the app to see the text displayed on the screen.
3.  **Experiment with Basic Composables and Previews:**
    *   Use `Column` and `Row` to arrange multiple `Text` composables.
    *   Create multiple previews to see how your UI looks with different data.
    *   Try adding some basic styling to your `Text` composables (e.g., changing the color or font size).

By the end of this week, you'll have a solid foundation in the basics of Jetpack Compose and be ready to explore more advanced concepts in the following weeks.