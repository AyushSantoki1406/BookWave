plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.bookwave"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.bookwave"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
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
}

dependencies {
    //skeleton view
    implementation ("com.facebook.shimmer:shimmer:0.5.0")


    //image slider
    implementation ("com.github.bumptech.glide:glide:4.11.0")

    //firebase
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-analytics")
    implementation(libs.androidx.recyclerview)

    //lottie
    val lottieVersion = "6.0.0"
    implementation ("com.airbnb.android:lottie:$lottieVersion")

    //view pager 2
    implementation ("androidx.viewpager2:viewpager2:1.1.0")
    implementation("com.google.android.material:material:1.9.0")

    //for set image
    implementation ("com.github.bumptech.glide:glide:4.16.0")

    //google auth
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    //blur bg
    implementation ("jp.wasabeef:blurry:4.0.1")

    //circular image
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}