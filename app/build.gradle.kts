plugins {
    alias(libs.plugins.agp.app)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hiddenapi.refine)
}

android {
    namespace = "xyz.mufanc.anpms"
    compileSdk = 34

    defaultConfig {
        applicationId = "xyz.mufanc.anpms"
        minSdk = 33
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles("proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Xposed
    compileOnly(libs.xposed.api)
    implementation(libs.xposed.service)

    // AutoX
    ksp(libs.autox.ksp)
    implementation(libs.autox.annotation)

    // Hidden Api
    compileOnly(libs.hiddenapi.stub)
    implementation(libs.hiddenapi.compat)
    implementation(libs.joor)
}
