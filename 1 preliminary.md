# Preliminary Setup for Android Development**

Welcome to your Android development journey! In this section, we will set up the development environment and explore the fundamental tools required to build Android applications using **Jetpack Compose** and **Kotlin**.

---

## **1. Development Environment Setup**

To develop Android apps efficiently, you need to install the necessary tools and configure your development environment.

### **1.1 Installing Android Studio**

Android Studio is the official **Integrated Development Environment (IDE)** for Android app development. It provides tools for coding, debugging, testing, and building Android applications.

#### **Installation Steps**


| Step                 | Description                                                                                                                                 |
| -------------------- | ------------------------------------------------------------------------------------------------------------------------------------------- |
| **1. Download**      | Visit the[Android Studio website](https://developer.android.com/studio/install.html)and download the latest stable version for your OS.     |
| **2. Install**       | Run the installer and follow the setup instructions. The process is guided and straightforward.                                             |
| **3. Initial Setup** | When you launch Android Studio for the first time, configure settings as prompted. The standard installation is recommended for most users. |

---

### **1.2 Android SDK Configuration**

The **Android Software Development Kit (SDK)** provides essential tools and libraries required for Android app development.

#### **Configuring the SDK**


| Step                            | Description                                                                                                   |
| ------------------------------- | ------------------------------------------------------------------------------------------------------------- |
| **1. Open SDK Manager**         | Access it via**Tools > SDK Manager**in Android Studio.                                                        |
| **2. Select Android Platform**  | Choose the Android versions you want to target. Installing at least one recent stable version is recommended. |
| **3. Install SDK Tools**        | In the**SDK Tools**tab, ensure the following are installed:                                                   |
| -**Android SDK Build-Tools**    | Required for building and compiling your app.                                                                 |
| -**Android Emulator**           | Allows you to test apps on virtual devices.                                                                   |
| -**Android SDK Platform-Tools** | Includes ADB (Android Debug Bridge) for debugging.                                                            |
| -**Google Play Services**       | Needed if your app requires Google APIs like Maps.                                                            |

---

### **1.3 Configuring the Android Emulator**

The **Android Emulator** lets you test applications on virtual devices with different Android versions and hardware configurations.

#### **Creating a Virtual Device**


| Step                               | Description                                                                    |
| ---------------------------------- | ------------------------------------------------------------------------------ |
| **1. Open AVD Manager**            | Go to**Tools > AVD Manager**.                                                  |
| **2. Create Virtual Device**       | Click**+ Create Virtual Device**to set up a new emulator.                      |
| **3. Select Hardware Profile**     | Choose a device model (e.g., Pixel, Nexus).                                    |
| **4. Choose System Image**         | Pick an Android version for the emulator. If not available, download it first. |
| **5. Configure Emulator Settings** | Adjust RAM, storage, and graphics settings as needed.                          |
| **6. Start Emulator**              | Launch the emulator from the**AVD Manager**and test your app.                  |

---

## **2. Kotlin Fundamentals**

Kotlin is the **official language** for Android development. It is modern, concise, and designed for improved safety and productivity.

📌 **Explore Kotlin Basics**: [Kotlin Official Guide](https://kotlinlang.org/docs/kotlin-tour-welcome.html)

---

## **3. Understanding Android Project Structure**

A well-structured Android project ensures maintainability and better collaboration.

### **3.1 Project Directory Layout**


| Folder              | Description                                                             |
| ------------------- | ----------------------------------------------------------------------- |
| **manifests/**      | Contains`AndroidManifest.xml`, which defines essential app information. |
| **java/**           | Houses Kotlin source code files.                                        |
| **res/**            | Stores app resources like UI layouts, images, and strings.              |
| **res/layout/**     | XML files defining UI components.                                       |
| **res/drawable/**   | Image assets for UI design.                                             |
| **res/values/**     | Stores app-wide resources (strings, colors, themes, etc.).              |
| **Gradle Scripts/** | Contains build configuration files.                                     |

---

### **3.2 Understanding `build.gradle.kts` Files**

Gradle is the **build automation system** used for managing dependencies and configurations.


| File                                | Description                                                                                                       |
| ----------------------------------- | ----------------------------------------------------------------------------------------------------------------- |
| **Project-level`build.gradle.kts`** | Located in the root directory. Configures global build settings.                                                  |
| **Module-level`build.gradle.kts`**  | Located inside each module (e.g.,`app/build.gradle.kts`). Defines dependencies, compile options, and build types. |

---

### **3.3 `AndroidManifest.xml` – The Core of an Android App**

The **AndroidManifest.xml** file defines essential properties of an Android app.


| Component          | Description                                                                |
| ------------------ | -------------------------------------------------------------------------- |
| **App Components** | Declares activities, services, broadcast receivers, and content providers. |
| **Permissions**    | Lists permissions the app requires (e.g., internet access, camera).        |
| **Metadata**       | Defines app-level settings like app name, icon, and theme.                 |

---

## **4. Summary**

### **What We Covered**

✅ Setting up **Android Studio** and configuring the **SDK**
✅ Creating and configuring an **Android Emulator**
✅ Learning **Kotlin Basics** for Android development
✅ Understanding **Android project structure** and **Gradle configuration**

With this setup, you're now ready to start developing Android apps using **Jetpack Compose and Kotlin**! 🚀
