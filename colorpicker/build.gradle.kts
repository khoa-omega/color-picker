import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.multiplatform)
    id("maven-publish")
}

kotlin {
    jvmToolchain(21)

    androidTarget()

    iosArm64()
    iosSimulatorArm64()

    jvm()

    js {
        browser()
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName = "colorpicker"
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.foundation)
            implementation(compose.runtime)
        }
    }
}

android {
    namespace = "io.github.komega.colorpicker"
    compileSdk {
        version = release(36)
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "io.github.komega.colorpicker"
            packageVersion = "1.0.9"
            description = "Message Bar KMP"
            copyright = "© 2024 Stevdza-San. All rights reserved."
        }
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}
