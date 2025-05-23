plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.mlkit"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mlkit"
        minSdk = 34
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }

}

dependencies {
//    implementation(libs.kotlinx.coroutines.android)
//    implementation(libs.kotlinx.coroutines.core)
//    implementation(libs.kotlinx.coroutines.tasks)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.mlkit.barcode.scanning)
    implementation(libs.mlkit.face.detection)
    implementation(libs.mlkit.image.labeling)
    implementation(libs.mlkit.objects.detection)
    implementation(libs.mlkit.selfie.segmentation)
    implementation(libs.mlkit.text.recognition)
    implementation(libs.mlkit.digital.ink.recognition)
    implementation(libs.mlkit.entity.extraction)
    implementation(libs.mlkit.language.id)
    implementation(libs.mlkit.pose.detection)
    implementation(libs.mlkit.pose.detection.accurate)
    implementation(libs.mlkit.translation)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.toolingPreview)
    implementation(libs.compose.material3)
    implementation(libs.coil.compose)
    implementation(libs.glance.appwidget)
    implementation(libs.mlkit.image.labeling)
    implementation(libs.androidx.runtime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
