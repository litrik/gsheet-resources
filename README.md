
gsheet-resources

![GitHub License](https://img.shields.io/github/license/litrik/gsheet-resources)

## What's this?

**gsheet-resources** is a tool to export/convert the strings in a Google Sheet into resource files for Android apps.

Storing the translations of an app in a Google Sheet is simple and easy.
 - It allows customers to change the strings of an app without having to learn any "real" translation tools (e.g. to edit PO files)
 - It makes it possible to share the strings with other platforms (like iOS) without having to use a separate shared repository and git submodules.

### Isn't this approach too basic?

Maybe. But it has worked for me, in multiple Android projects over the years.

## How to use it?

**gsheet-resources** is available as a Gradle plugin.

![Gradle Plugin Portal Version](https://img.shields.io/gradle-plugin-portal/v/be.norio.gsheet-resources)

### Installation

Add the following line to your `libs.versions.toml` file in the `[plugins]` section:
```toml
gsheetresources = "be.norio.gsheet-resources:<LATEST_VERSION>"
```
with `<LATEST_VERSION>` being the version you want to use of the plugin (see [releases](https://github.com/litrik/gsheet-resources/releases)).

Then, define the plugin in your root `build.gradle.kts` file:
```kotlin
plugins {
    alias(libs.plugins.gsheetresources) apply (false) 
}
```

Then, apply the plugin to your module's build file, e.g `app/build.gradle.kts`:
```kotlin
plugins {
    alias(libs.plugins.gsheetresources)
}
```

### Make a Google Sheet with translations

The Google Sheet that contains your translation strings **MUST** follow these conventions:
1. Only the first tab is used. All other tabs are ignored.
2. The first row is a header row.
3. The first column must contain the IDs for the resources. This must be a valid Java/Kotlin identifier.
4. All subsequent columns will be treated as a language if the header row contains a [valid language qualifier](https://developer.android.com/guide/topics/resources/providing-resources#AlternativeResources).

Additionally, you must go into the share settings of the Google Sheet and make sure that "_Anyone with the link_" is a "_Viewer_".

**Be aware:** This will make your translations sheet readable by anybody with the link. I do not consider this a (security) problem, because in the end your translations will be embedded in an Android app anyway.

### Configuration

The configuration of the plugin is done via the `gsheetresources` block in your module's build file e.g. `app/build.gradle.kts` file, like so:
```kotlin
gsheetresources {
    sheetId = "<SHEET_ID>"
}
```

with `SHEET_ID` being the unique identifier of your Google Sheet. This is part of the sheet URL. E.g for this [sample Google Sheet](https://docs.google.com/spreadsheets/d/1L6EsUDM9qnsQjrT2Y16eXRzMP3v5Njp2gckwY-oZpcM/edit?gid=0#gid=0)
it is `1L6EsUDM9qnsQjrT2Y16eXRzMP3v5Njp2gckwY-oZpcM`.

### Execution

You can launch the tool by running the following command in your terminal:
```bash
./gradlew generateSheetResources
```

This will automatically generate a string resource file called `strings_generated.xml` for each language found in the sheet.

I recommend to add the generated files into version control. You can rerun the above command to update the generated files after changes were made to the Google Sheet.

## Sample

This is a [sample Google Sheet](https://docs.google.com/spreadsheets/d/1L6EsUDM9qnsQjrT2Y16eXRzMP3v5Njp2gckwY-oZpcM/edit?gid=0#gid=0).

| ID                    | en                | nl                | nl-rBE    |
! --------------------- | ----------------- | ----------------- | --------- |
| hello                 | Hello World!      | Hallo Wereld!     |           |
| button_save           | Save              | Bewaren           |           |
| button_cancel         | Cancel            | Annuleren         |           |
| ellipsis              | More...           | Meer...           |           |
| ampersand             | You & Me          | Jij & ik          |           |
| fries                 | French fries      | Friet             | Fritten   |
| placeholder_string    | Value: %1$s       | Waarde: %1$s      |           |
| placeholder_numbers   | From %1$d to %2$d | Van %1$d tot %2$d |           |

This results in the following resource files:
 - [src/main/res/values/strings_generated.xml](https://github.com/litrik/gsheet-resources/blob/main/test-module/src/main/res/values/strings_generated.xml)
 - [src/main/res/values-nl/strings_generated.xml](https://github.com/litrik/gsheet-resources/blob/main/test-module/src/main/res/values-nl/strings_generated.xml)
 - [src/main/res/values-nl-rBE/strings_generated.xml](https://github.com/litrik/gsheet-resources/blob/main/test-module/src/main/res/values-nl-rBE/strings_generated.xml)
