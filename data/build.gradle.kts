plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.datat"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {


    implementation(libs.androidx.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.kotlinx.collections.immutable)



    implementation(project(":core"))
    implementation(project(":network"))
    testImplementation(libs.junit.lib)


    //TODO remove maybe
    implementation(libs.serialization.json)

    // Retrofit
    implementation(libs.retrofit.lib)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.okhttp.lib)

    //room
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    //Test
    testImplementation(libs.room.testing)
    androidTestImplementation(libs.junit.lib)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(project(":testUtils"))
    testImplementation(project(":testUtils"))
    testImplementation(libs.kotlinx.coroutines.test)
    //For testing couroutines
    testImplementation(libs.turbine)


    //TODO check if needed
    implementation(libs.material)


}