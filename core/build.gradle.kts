plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.mavenPublish)
    alias(libs.plugins.compatPatrouille)
}

compatPatrouille {
    java(17)
    kotlin(libs.versions.kotlin.get())
}

dependencies {
    implementation(libs.csv)
    testImplementation(libs.junit)
}

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)
    signAllPublications()
    pom {
        name = "Google Sheet to Resources core library"
        description = "Tool to generate string resource files for an Android app from a Google Sheet. This is the core library used by the Gradle plugin."
        inceptionYear = "2025"
        url = "https://github.com/litrik/gsheet-resources"
        licenses {
            license {
                name = "The MIT License"
                url = "https://opensource.org/license/mit"
                distribution = url
            }
        }
        developers {
            developer {
                id = "litrik"
                name = "Litrik De Roy"
                url = "https://github.com/litrik"
            }
        }
        scm {
            url = "https://github.com/litrik/gsheet-resources"
            connection = "scm:git:git://github.com/litrik/gsheet-resources.git"
            developerConnection = "scm:git:ssh://git@github.com:litrik/gsheet-resources.git"
        }
    }
}
