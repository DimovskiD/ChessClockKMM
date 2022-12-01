pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

include(":app", ":shared")
rootProject.name = "ChessClock"

enableFeaturePreview("VERSION_CATALOGS")
