import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    id("kotlin-parcelize")
}

android {
    namespace = "com.jvoye.tasky"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.jvoye.tasky"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        val properties = Properties()
        properties.load(rootProject.file("local.properties").inputStream())
        debug {
            isMinifyEnabled = false
            buildConfigField("String", "BASE_URL", properties.getProperty("BASE_URL"))
            buildConfigField("String", "API_KEY", properties.getProperty("API_KEY"))
        }
        release {
            isMinifyEnabled = false
            buildConfigField("String", "BASE_URL", properties.getProperty("BASE_URL"))
            buildConfigField("String", "API_KEY", properties.getProperty("API_KEY"))
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
        buildConfig = true
        compose = true
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_11
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.material3)
    implementation(libs.androidx.datastore.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    androidTestImplementation(libs.kotlinx.coroutines.test)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Splashscreen
    implementation(libs.androidx.core.splashscreen)

    // Database - Room
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)

    implementation(libs.kotlinx.serialization.json)

    // Logging
    implementation(libs.timber)

    // WindowSizeClasses
    implementation(libs.material3.adaptive)
    implementation(libs.androidx.compose.material3.windowsizeclass)

    // Navigation 3
    implementation(libs.nav3.runtime)
    implementation(libs.nav3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.nav3)

    //Ktor
    implementation(libs.bundles.ktor)

    // Koin DI
    implementation(libs.bundles.koin)

    // Allow use of java.time.Instant below API 26
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    // Extend compose system pieces
    implementation(libs.androidx.compose.foundation)

    // For EncryptedFile
    implementation(libs.androidx.security.crypto.ktx)

    // For Preferences DataStore
    implementation(libs.androidx.datastore.preferences)

    // Kotlin DateTime
    implementation(libs.kotlinx.datetime)

    // Image Loading
    implementation(libs.coil.compose)
}