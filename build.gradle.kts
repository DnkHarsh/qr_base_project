plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.google.gms.google-services") version "4.3.15" apply false
    id("com.google.dagger.hilt.android") version "2.45" apply false
    id("com.google.firebase.crashlytics") version "2.9.9" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()

        maven {
            setUrl("https://www.jitpack.io")
        }
        maven {
            setUrl("https://android-sdk.is.com/")
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}