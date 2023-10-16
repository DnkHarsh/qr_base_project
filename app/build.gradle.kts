import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

val localProperties = gradleLocalProperties(rootDir)

plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.demo.baseproject"

    defaultConfig {
        applicationId = "com.demo.baseproject"

        minSdk = 24
        targetSdk = 34
        compileSdk = 34

        versionCode = 1
        versionName = "1.0.0"

        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_17
        sourceCompatibility = JavaVersion.VERSION_17
    }

    flavorDimensions += "version"
    productFlavors {
        create("signed") {
            dimension = "version"
            resValue("string", "app_name", "BaseProject")

            resValue("string", "ADMOB_APP_ID", "app-id")
            resValue("string", "IRONSOURCE_APP_ID", "app-id")
            resValue("string", "UNITY_APP_ID", "app-id")
            resValue("string", "INMOBI_APP_ID", "app-id")
            resValue("string", "APPLOVIN", "app-id")

            resValue("string", "PRODUCT_ID_IN", "product-id")
            resValue("string", "PRODUCT_ID_ROW", "product-id")
        }
        create("development") {
            dimension = "version"
            resValue("string", "app_name", "BaseProject")

            resValue("string", "ADMOB_APP_ID", "ca-app-pub-3940256099942544~3347511713")
            resValue("string", "IRONSOURCE_APP_ID", "app-id")
            resValue("string", "UNITY_APP_ID", "app-id")
            resValue("string", "INMOBI_APP_ID", "app-id")
            resValue("string", "APPLOVIN", "app-id")

            resValue("string", "PRODUCT_ID_IN", "product-id")
            resValue("string", "PRODUCT_ID_ROW", "product-id")
        }
    }

    signingConfigs {
        create("release") {
//            keyAlias = localProperties.getProperty("keyAlias")
//            keyPassword = localProperties.getProperty("keyPassword")
//            storeFile = file(localProperties.getProperty("storeFile"))
//            storePassword = localProperties.getProperty("storePassword")
        }

        getByName("debug") {
//            keyAlias = localProperties.getProperty("debugKeyAlias")
//            keyPassword = localProperties.getProperty("debugKeyPassword")
//            storeFile = file(localProperties.getProperty("debugStoreFile"))
//            storePassword = localProperties.getProperty("debugStorePassword")
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }

        getByName("debug") {
            isMinifyEnabled = false
            isDebuggable = true
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

android.packaging {
    resources.excludes.add("AndroidManifest.xml")
    resources.excludes.add("META-INF/services/javax.annotation.processing.Processor")
}

dependencies {
    implementation(fileTree("dir" to "libs", "include" to listOf("*.jar")))
    implementation(project(mapOf("path" to ":nativetemplates")))

    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    testImplementation("junit:junit:4.13.2")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.multidex:multidex:2.0.1")

    implementation("androidx.browser:browser:1.6.0")
    implementation("androidx.recyclerview:recyclerview:1.3.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.22")

    // Material
    implementation("com.google.android.material:material:1.10.0")

    // KTX
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.fragment:fragment-ktx:1.6.1")

    // Work manager
    implementation("androidx.work:work-runtime-ktx:2.8.1")

    // In app billing
    implementation("com.android.billingclient:billing-ktx:6.0.1")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:31.3.0"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-config-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")

    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // Splash
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.45")
    kapt("com.google.dagger:hilt-compiler:2.45")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-process:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    // Ads/Mediation
    implementation("com.google.android.gms:play-services-ads:22.4.0")
    implementation("com.google.android.gms:play-services-ads-identifier:18.0.1")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.ironsource.sdk:mediationsdk:7.5.1")
    implementation("com.unity3d.ads:unity-ads:4.8.0")
    implementation("com.inmobi.monetization:inmobi-ads-kotlin:10.5.7")
    implementation("com.inmobi.omsdk:inmobi-omsdk:1.3.17.1")

    // In app review
    implementation("com.google.android.play:review-ktx:2.0.1")

    // In app update
    implementation("com.google.android.play:app-update-ktx:2.1.0")

    // Image/Animation loading
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.squareup.picasso:picasso:2.8")
    implementation("com.airbnb.android:lottie:5.2.0")
}
