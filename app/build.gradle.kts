import java.io.FileInputStream
import java.util.Properties

var localProperties = Properties();
var localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.uit_mobileapp_lab03"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.uit_mobileapp_lab03"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField("String", "NEWS_API_KEY", "\"${localProperties.getProperty("NEWS_API_KEY")}\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // 1. Networking: Retrofit & GSON
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.8.7")
    // 2. Paging Library (Xử lý phân trang dữ liệu lớn)
    // Mặc dù lab dùng -ktx cho Kotlin, nhưng Java vẫn sử dụng tốt bản này
    implementation("androidx.paging:paging-runtime:3.2.1")
    // 3. AI: TensorFlow Lite cho Sentiment Analysis
    implementation("org.tensorflow:tensorflow-lite-task-text:0.4.4")
    // 4. Image Loading: Coil (Hỗ trợ load hình ảnh bài báo)
    implementation("io.coil-kt:coil:2.5.0")
    // 5. Glide for handling asynchronous loading and memory management
    implementation("com.github.bumptech.glide:glide:4.12.0")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.paging.rxjava3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}