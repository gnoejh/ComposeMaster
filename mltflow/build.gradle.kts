plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.example.mltflow"
    compileSdk = 35
    
    defaultConfig {
        applicationId = "com.example.mltflow"
        minSdk = 34
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    
    kotlinOptions {
        jvmTarget = "1.8"
    }
    
    buildFeatures {
        compose = true
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    
    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    
    // TensorFlow Lite - 충돌을 해결하기 위해 의존성 정리
    implementation("org.tensorflow:tensorflow-lite:2.17.0") {
        exclude(group = "org.tensorflow", module = "tensorflow-lite-api")
    }
    
    // ML Kit (ML Kit는 자체적으로 TensorFlow Lite 종속성을 포함하고 있음)
    implementation(libs.mlkit.face.detection)
    implementation(libs.mlkit.objects.detection)
    
    // TensorFlow Lite Task Library - 선택적으로 추가
    implementation("org.tensorflow:tensorflow-lite-task-vision:0.4.4") {
        exclude(group = "org.tensorflow", module = "tensorflow-lite-api")
        exclude(group = "org.tensorflow", module = "tensorflow-lite-support-api")

    }

//    implementation(libs.tensorflow.lite.task.vision) // Use latest version
//    implementation(libs.tensorflow.lite.task.vision) // Use latest version
//    implementation(libs.tensorflow.lite) // Use latest version



    // CameraX
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    
    // Coil for image loading
    implementation(libs.coil.compose.v222)
    
    // Coroutine extensions for Tasks
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.litert.support.api)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    
    // Debug
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
