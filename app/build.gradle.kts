plugins {
    alias(libs.plugins.agp.app)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hiddenapi.refine)
}

val androidMinSdkVersion: Int by rootProject.extra
val androidTargetSdkVersion: Int by rootProject.extra
val androidCompileSdkVersion: Int by rootProject.extra

val androidSourceCompatibility: JavaVersion by rootProject.extra
val androidTargetCompatibility: JavaVersion by rootProject.extra
val androidKotlinJvmTarget: String by rootProject.extra

android {
    namespace = "xyz.mufanc.anpms"
    compileSdk = androidCompileSdkVersion

    defaultConfig {
        applicationId = "xyz.mufanc.anpms"
        minSdk = androidMinSdkVersion
        targetSdk = androidTargetSdkVersion
        versionCode = 1
        versionName = "1.1"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles("proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = androidSourceCompatibility
        targetCompatibility = androidTargetCompatibility
    }

    kotlinOptions {
        jvmTarget = androidKotlinJvmTarget
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
    compileOnly(project(":api-stub"))
    compileOnly(libs.hiddenapi.stub)
    implementation(libs.hiddenapi.compat)
    implementation(libs.joor)
}
