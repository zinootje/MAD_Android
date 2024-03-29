plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.21"
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.mvp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mvp"
        minSdk = 30
        targetSdk = 34
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
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {

    //modularization
    implementation(project(":core"))
    implementation(project(":data"))
    implementation(project(":network"))
    androidTestImplementation(project(":data"))
    androidTestImplementation(project(":testUtils"))
    testImplementation(project(":testUtils"))

    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.material)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.navigation)
    testImplementation(libs.androidx.navigation.testing)
    implementation(libs.androidx.navigation.testing)
    androidTestImplementation(libs.junit.lib)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test)
    androidTestImplementation(libs.androidx.test.espresso.intents)
    androidTestImplementation(platform(libs.androidx.compose.bom))

    //androidTestImplementation(libs.androidx.compose.ui.lib)
    androidTestImplementation(libs.androidx.test.junit)


    debugImplementation(libs.bundles.composeDebug)
    //Testing
    testImplementation(libs.junit.lib)
    testImplementation(libs.kotlinx.coroutines.test)
    //For testing couroutines
    testImplementation(libs.turbine)


    //Layout
    implementation(libs.androidx.compose.material3.windowSizeClass)


    //rebugger
    //TODO remove in production
    implementation(libs.rebugger.lib)
    implementation(libs.androidx.runtime.tracing)



}