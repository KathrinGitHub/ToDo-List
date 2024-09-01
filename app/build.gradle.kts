plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android") version "1.9.0"
    id("com.google.gms.google-services")
}

android {
    namespace = "at.fhjoanneum.todoapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "at.fhjoanneum.todoapp"
        minSdk = 28
        targetSdk = 34
        versionCode = 2
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
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
}

dependencies {

    implementation(libs.androidx.junit.ktx)
    implementation(libs.androidx.espresso.core.v361)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)
    androidTestImplementation(libs.androidx.junit.v121)
    androidTestImplementation(libs.androidx.espresso.core.v361)
    implementation(libs.material.v1120)
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core.v500)
    testImplementation(libs.mockito.kotlin)
    testImplementation (libs.mockito.inline.v500)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation (libs.junit)

}