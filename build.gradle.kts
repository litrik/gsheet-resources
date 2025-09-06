plugins {
    alias(libs.plugins.caupain)
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.compatPatrouille) apply false
}

val currentVersion = "0.2.0"
version = buildString {
    append(currentVersion)
}

subprojects {
    group = "be.norio.gsheetresources"
    version = rootProject.version
}
