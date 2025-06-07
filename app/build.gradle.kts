// app/build.gradle.kts

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose) // For Jetpack Compose with modern Kotlin
}

android {
    namespace = "com.example.spellcastingcompanion"
    compileSdk = 35 // Or your preferred SDK version (e.g., 36) - ensure it's high enough for your libraries

    defaultConfig {
        applicationId = "com.example.spellcastingcompanion"
        minSdk = 26 // Android 8.0 Oreo
        targetSdk = 35 // Should generally match compileSdk
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false // Set to true for production release build
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core Android & Kotlin
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    // implementation("androidx.compose.material3:material3")
    // implementation("androidx.compose.material:material-icons-core")
    // implementation("androidx.compose.material:material-icons-extended")
    implementation(libs.androidx.compose.material3) // This usually brings core icons
    implementation(libs.androidx.material.icons.core) // If not covered by material3 for some reason
    implementation(libs.androidx.material.icons.extended) // For more icons like EnergySavingsLeaf

    // Jetpack Compose (BOM - Bill of Materials manages versions for Compose libraries)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)

    // ViewModel (for Compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Kotlinx Serialization (for JSON parsing)
    implementation(libs.kotlinx.serialization.json)

    // Supabase Kotlin client (using BOM)
    implementation(platform(libs.supabase.bom)) // This imports the BOM
    implementation(libs.supabase.auth)          // Now implement the modules without versions
    implementation(libs.supabase.postgrest)
    implementation(libs.supabase.storage)
    implementation(libs.supabase.realtime)

    // Ktor client (ensure versions are compatible if Supabase BOM pulls in a specific Ktor)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)

    // Material Icons (if you added them to libs.versions.toml)
    implementation(libs.androidx.material.icons.core)
    implementation(libs.androidx.material.icons.extended)

    // Kotlinx Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom)) // For Compose testing libraries
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}