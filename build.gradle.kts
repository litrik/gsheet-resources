plugins {
    alias(libs.plugins.caupain)
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.tapmoc) apply false
}

val currentVersion = "0.6.0"
version = buildString {
    append(currentVersion)
}

subprojects {
    group = "be.norio.gsheetresources"
    version = rootProject.version
}
