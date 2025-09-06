import com.vanniktech.maven.publish.GradlePublishPlugin

plugins {
    alias(libs.plugins.kotlinJvm)
    `java-gradle-plugin`
    alias(libs.plugins.gradlePluginPublish)
    alias(libs.plugins.mavenPublish)
    alias(libs.plugins.compatPatrouille)
}

compatPatrouille {
    java(17)
    kotlin(libs.versions.kotlin.get())
}

dependencies {
    api(project(":core"))
}

gradlePlugin {
    website = "https://github.com/litrik/"
    vcsUrl = "https://github.com/litrik/gsheet-resources"
    plugins {
        create("gsheetResources") {
            id = "be.norio.gsheet-resources"
            displayName = "Generate string resource files from a Google Sheet"
            description = "Plugin to generate string resource files for Android and KMP apps from a Google Sheet"
            tags = listOf("android", "translations")
            implementationClass = "be.norio.gsheetresources.GsheetResourcesPlugin"
        }
    }
}

mavenPublishing {
    configure(GradlePublishPlugin())
    if (version.toString().endsWith("-SNAPSHOT")) {
        // Publish snapshots to Maven Central
        publishToMavenCentral(automaticRelease = true)
        signAllPublications()
    }
    pom {
        name = "Google Sheet to Resources Gradle plugin"
        description = "Gradle plugin to generate string resource files for Android and KMP apps from a Google Sheet."
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
