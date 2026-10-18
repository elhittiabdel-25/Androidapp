import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

// Every CI build gets a higher versionCode (Play requires it to grow with each upload).
val buildNumber = (System.getenv("GITHUB_RUN_NUMBER") ?: "1").toInt()

// The upload key never lives in the repository: CI writes it from the repository secrets
// (UPLOAD_KEYSTORE_BASE64 and friends) and passes its path here. Without it, release builds
// fall back to the debug key, which is fine for testing but not accepted by Google Play.
val uploadKeystore: String? = System.getenv("UPLOAD_KEYSTORE_PATH")?.takeIf { it.isNotBlank() && file(it).exists() }

android {
    namespace = "app.usulcards"
    compileSdk = 36

    defaultConfig {
        applicationId = "app.reiman"
        minSdk = 26
        targetSdk = 36
        versionCode = buildNumber
        versionName = "1.0.0"
    }

    signingConfigs {
        if (uploadKeystore != null) {
            create("upload") {
                storeFile = file(uploadKeystore)
                storePassword = System.getenv("UPLOAD_KEYSTORE_PASSWORD")
                keyAlias = System.getenv("UPLOAD_KEY_ALIAS")
                keyPassword = System.getenv("UPLOAD_KEY_PASSWORD")
                storeType = "pkcs12"
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = if (uploadKeystore != null) signingConfigs.getByName("upload") else signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
    androidResources {
        // audio is played straight from the APK (AssetManager.openFd), which needs it stored uncompressed
        noCompress += listOf("mp3", "ogg")
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2024.09.00")
    implementation(composeBom)
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.animation:animation")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-core")
}
