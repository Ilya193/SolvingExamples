plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "ru.kraz.solvingexamples"
    compileSdk = 34

    defaultConfig {
        applicationId = "ru.kraz.solvingexamples"
        minSdk = 21
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

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(project(":feature-menu"))
    implementation(project(":common"))
    implementation(project(":feature-game"))
    implementation(project(":feature-game-result"))
    implementation(project(":feature-setting-timer"))
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation(ru.kraz.buildsrc.Libs.koin)
    implementation(ru.kraz.buildsrc.Libs.fragmentKtx)

    implementation(ru.kraz.buildsrc.Libs.roomRuntime)
    annotationProcessor(ru.kraz.buildsrc.Libs.roomCompiler)
    implementation(ru.kraz.buildsrc.Libs.roomKtx)
    ksp(ru.kraz.buildsrc.Libs.roomCompiler)
}