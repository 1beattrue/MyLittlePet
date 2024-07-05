plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
    kotlin("plugin.serialization") version "1.9.0"
}

android {
    namespace = "edu.mirea.onebeattrue.mylittlepet"
    compileSdk = 34

    defaultConfig {
        applicationId = "edu.mirea.onebeattrue.mylittlepet"
        minSdk = 29
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
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

    val retrofitVersion = "2.11.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    val okHttpVersion = "4.11.0"
    implementation("com.squareup.okhttp3:logging-interceptor:$okHttpVersion")

    val cameraxVersion = "1.4.0-beta02"
    implementation("androidx.camera:camera-core:$cameraxVersion")
    implementation("androidx.camera:camera-camera2:$cameraxVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraxVersion")
    implementation("androidx.camera:camera-view:$cameraxVersion")

    val qrCodeVersion = "4.3.0"
    implementation("com.journeyapps:zxing-android-embedded:$qrCodeVersion")
    val qrCodeCoreVersion = "3.4.1"
    implementation("com.google.zxing:core:$qrCodeCoreVersion")

    val dataStoreVersion = "1.1.1"
    implementation("androidx.datastore:datastore-preferences:$dataStoreVersion")

    val accompanistVersion = "0.35.1-alpha"
    implementation("com.google.accompanist:accompanist-permissions:$accompanistVersion")

    val gsonVersion = "2.10.1"
    implementation("com.google.code.gson:gson:$gsonVersion")

    val coilVersion = "2.6.0"
    implementation("io.coil-kt:coil-compose:$coilVersion")

    val constraintLayoutVersion = "1.1.0-alpha13"
    implementation("androidx.constraintlayout:constraintlayout-compose:$constraintLayoutVersion")

    val glideVersion = "1.0.0-beta01"
    implementation("com.github.bumptech.glide:compose:$glideVersion")

    val serializationVersion = "1.6.3"
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")

    val mviVersion = "3.2.1"
    implementation("com.arkivanov.mvikotlin:mvikotlin-extensions-coroutines:$mviVersion")
    implementation("com.arkivanov.mvikotlin:mvikotlin-main:$mviVersion")
    implementation("com.arkivanov.mvikotlin:mvikotlin-logging:$mviVersion")
    implementation("com.arkivanov.mvikotlin:mvikotlin:$mviVersion")

    val decomposeVersion = "3.0.0"
    implementation("com.arkivanov.decompose:extensions-compose:$decomposeVersion")
    implementation("com.arkivanov.decompose:decompose:$decomposeVersion")

    val roomVersion = "2.6.1"
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    val daggerVersion = "2.51.1"
    implementation("com.google.dagger:dagger:$daggerVersion")
    ksp("com.google.dagger:dagger-compiler:$daggerVersion")

    val firebaseVersion = "33.1.1"
    implementation(platform("com.google.firebase:firebase-bom:$firebaseVersion"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.android.play:integrity:1.3.0")

    val material3Version = "1.2.1"
    implementation("androidx.compose.material3:material3:$material3Version")
    implementation("androidx.compose.material:material-icons-extended")

    implementation("androidx.core:core-ktx:1.13.1")

    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.06.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}