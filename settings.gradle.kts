import org.gradle.api.internal.FeaturePreviews.Feature

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        google()
        mavenCentral()
    }
}


rootProject.name = "color-picker"
include(":colorpicker")
