# Week 1: Preliminary Setup

Welcome to the first week of your Android development journey! This week, we'll lay the groundwork by setting up our development environment and getting acquainted with the fundamental tools and concepts we'll be using.

## Topics

### Setting up Android Studio

Android Studio is the official Integrated Development Environment (IDE) for Android app development. It provides a comprehensive suite of tools for coding, debugging, testing, and building Android applications.

#### Installation

1.  **Download:** Head over to the official Android Studio website ([https://developer.android.com/studio/install.html](https://developer.android.com/studio/install.html)) and download the latest stable version for your operating system.
2.  **Installation:** Follow the installation instructions provided on the website. The process is generally straightforward, with the installer guiding you through each step.
3.  **First Launch:** Upon launching Android Studio for the first time, you'll be prompted to configure some initial settings. You can typically go with the standard installation options.

#### SDK Setup

The Android Software Development Kit (SDK) is a collection of tools, libraries, and documentation that are essential for building Android apps.

1.  **SDK Manager:** Android Studio comes with an SDK Manager that allows you to download and manage different SDK components. You can access it through **Tools > SDK Manager**.
2.  **Platform Selection:** In the SDK Manager, you'll find a list of Android platform versions. Select the platforms you want to target. It's recommended to install at least one of the latest stable versions.
3.  **SDK Tools:** In the "SDK Tools" tab, make sure to install the following:
    *   **Android SDK Build-Tools:** These are essential for building your app.
    *   **Android Emulator:** This allows you to run and test your app on virtual devices.
    *   **Android SDK Platform-Tools:** These provide tools like ADB (Android Debug Bridge) for interacting with devices.
4. **Google Play services:** If you plan to use Google services in your app, like maps, you will need to install this.

#### Emulator Configuration

The Android Emulator lets you run your app on a virtual device, simulating various hardware configurations and Android versions.

1.  **AVD Manager:** Access the Android Virtual Device (AVD) Manager through **Tools > AVD Manager**.
2.  **Create Virtual Device:** Click on "+ Create Virtual Device" to start creating a new virtual device.
3.  **Hardware Profile:** Choose a hardware profile that matches the type of device you want to emulate (e.g., Pixel, Nexus).
4.  **System Image:** Select a system image (Android version) for your virtual device. You might need to download the system image first.
5.  **Configuration:** Configure other settings like RAM, storage, and graphics.
6.  **Start the Emulator:** Once created, you can start the emulator from the AVD Manager.

### Kotlin Fundamentals

Kotlin is the official language for Android development. It's a modern, concise, and safe language that enhances developer productivity.

#### Variables

Variables are used to store data. In Kotlin, you can declare variables using `val` (for read-only variables) or `var` (for mutable variables).

#### Data Types

Kotlin has several built-in data types, including:

*   **Numbers:** `Int`, `Long`, `Float`, `Double`
*   **Characters:** `Char`
*   **Boolean:** `Boolean` (true or false)
*   **Strings:** `String`

#### Control Flow

Control flow statements allow you to control the execution of your code. Common control flow structures include:

*   **if-else:** For conditional execution.
*   **when:** For multi-branch conditional execution (similar to switch in other languages).
*   **for:** For looping over a range or collection.
*   **while:** For looping while a condition is true.

#### Functions

Functions are blocks of code that perform specific tasks. They help organize your code and make it reusable.

#### Classes

Classes are blueprints for creating objects. They encapsulate data (properties) and behavior (methods).

### Basic Android Project Structure

Understanding the structure of an Android project is crucial for navigating and managing your code and resources.

#### Project Layout

*   **manifests:** Contains the `AndroidManifest.xml` file, which describes the essential information about your app to the Android system.
*   **java:** Contains your Kotlin source code files.
*   **res:** Contains your app's resources, such as layouts, images, and strings.
    *   **layout:** XML files that define the user interface.
    *   **drawable:** Image assets.
    *   **values:** Resource files for strings, colors, styles, etc.
* **Gradle Scripts:** Contains the build files.

#### `build.gradle` Files

There are two main `build.gradle` files:

1.  **Project-level `build.gradle`:** Located in the root directory of your project. It defines build configurations that apply to all modules in your project.
2.  **Module-level `build.gradle`:** Located within each module (e.g., `app/build.gradle`). It defines build configurations specific to that module, such as dependencies and build types.

#### `AndroidManifest.xml`

This file is the heart of your Android app. It declares:

*   **App Components:** Activities, services, broadcast receivers, and content providers.
*   **Permissions:** What permissions your app requires.
*   **App Metadata:** App name, icon, theme, etc.

## Assignments

1.  **Install Android Studio and set up the Android SDK.**
    *   Download and install Android Studio.
    *   Use the SDK Manager to install the necessary SDK platforms and tools.
    *   Create and configure an Android Virtual Device (AVD).
2.  **Create a basic Kotlin project and write simple Kotlin programs.**
    *   Create a new "Empty Activity" project in Android Studio.
    *   Write simple Kotlin programs to practice variables, data types, control flow, functions, and classes. You can use the `main` function to run your code.
3.  **Explore the structure of a basic Android project.**
    *   Examine the project layout in Android Studio.
    *   Open and inspect the `build.gradle` files and the `AndroidManifest.xml` file.
    *   Identify the different directories and their contents.

By completing these assignments, you'll have a solid foundation to build