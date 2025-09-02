import be.norio.gsheetresources.GsheetResourcesPluginExtension

buildscript {
    repositories {
        mavenLocal()
    }

    dependencies {
    }
}

plugins {
    id("be.norio.gsheet-resources")
}

gsheetresources {
    sheetId = "1L6EsUDM9qnsQjrT2Y16eXRzMP3v5Njp2gckwY-oZpcM"
}
