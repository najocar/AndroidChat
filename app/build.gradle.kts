plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.safeArgs)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.chatwebsocket"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.chatwebsocket"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", "\"https://3647-217-113-248-140.ngrok-free.app/api/v1/\"")
            buildConfigField("String", "WSS_URL", "\"ws://www.my-api-rails.com/cable\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "BASE_URL", "\"https://bf61-217-113-248-140.ngrok-free.app/api/v1/\"")
            buildConfigField("String", "WSS_URL", "\"ws://www.my-api-rails.com/cable\"")
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
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.splash.screen)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.recyclerview)
    implementation(libs.coroutines.android)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.dagger.hilt.android)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.work.runtime.ktx)
//    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
//    implementation("com.google.firebase:firebase-analytics")
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging.ktx)
    kapt(libs.androidx.room.compiler)
    kapt(libs.dagger.hilt.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

hilt {
    enableAggregatingTask = true
}
kapt {
    correctErrorTypes = true
}
