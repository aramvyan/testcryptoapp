plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.testcryptoapp"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.testcryptoapp"
        minSdk = 29
        targetSdk = 36
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
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))

    // Core SDK (required)
    implementation(files("libs/dynamic-sdk-android.aar"))

    // Solana SDK (optional - remove if not needed)
    implementation(files("libs/solana-web3.aar"))
    implementation(libs.sol4k)  // Required by Dynamic Solana SDK

    // JSON serialization
    implementation(libs.kotlinx.serialization.json)

    // Android WebView
    implementation(libs.androidx.webkit)

    // HTTP client
    //noinspection UseTomlInstead
    implementation("com.squareup.okhttp3:okhttp:5.3.2")

    // Custom Tabs for authentication
    implementation(libs.androidx.browser)

    // Secure storage - DataStore + Tink
    implementation(libs.androidx.datastore.preferences)
    //noinspection UseTomlInstead
    implementation("com.google.crypto.tink:tink-android:1.20.0")

    implementation("androidx.credentials:credentials:1.2.2")
    implementation("androidx.credentials:credentials-play-services-auth:1.2.2")
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.material.icons.extended)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.koin.androidx.compose)

    implementation(libs.androidx.navigation.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}