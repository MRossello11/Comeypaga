import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://jitpack.io" )
}

kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("org.jetbrains.compose.material3:material3-desktop:1.2.1")
                implementation ("androidx.compose.material3:material3:1.0.1")
                implementation("com.github.leonard-palm:compose-state-events:1.1.0")
                implementation("com.squareup.retrofit2:retrofit:2.9.0")
                implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
                // https://mvnrepository.com/artifact/com.google.code.gson/gson
                implementation("com.google.code.gson:gson:2.10.1")
                runtimeOnly("androidx.compose.foundation:foundation:1.4.0")
                // navigation
                implementation("com.arkivanov.decompose:decompose:1.0.0-beta-01")
                implementation("com.arkivanov.decompose:extensions-compose-jetbrains:1.0.0-beta-01")
                implementation("com.arkivanov.essenty:parcelable:1.0.0")
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Comeypaga"
            packageVersion = "1.0.0"
        }
    }
}
