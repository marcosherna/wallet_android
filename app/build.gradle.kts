plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.wallet"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.wallet"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        viewBinding = true
    }
    packagingOptions {
        exclude("META-INF/NOTICE.md".toString())
        exclude("META-INF/LICENSE.md".toString())
    }
}

dependencies {

    // Nuevas dependencias
    implementation(libs.circleimageview)
    implementation(libs.firebase.auth)
    implementation(libs.play.services.auth)
    implementation(libs.glide)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.database)
    annotationProcessor(libs.glide.compiler)

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)

    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.legacy.support.v4)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("io.reactivex.rxjava3:rxjava:3.0.0")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.0")

    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation ("com.sun.mail:android-mail:1.6.7")
    implementation ("com.sun.mail:android-activation:1.6.7")

    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
}
