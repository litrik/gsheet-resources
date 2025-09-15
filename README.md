
# gsheet-resources

![GitHub License](https://img.shields.io/github/license/litrik/gsheet-resources)

![Logo](resources/logo.png)

## What is this?

**gsheet-resources** is a tool that allows you to export/convert the strings in a Google Sheet into resource files for Android and Kotlin Multiplatform apps.

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

Check the [sample](#sample) for some examples.

Additionally, you must go into the share settings of the Google Sheet and make sure that "_Anyone with the link_" is a "_Viewer_".

**Be aware:** This will make your translations sheet readable by anybody with the link. I do not consider this a (security) problem, because in the end your translations will be embedded in an Android app anyway.

### Configuration

The configuration of the plugin is done via the `gsheetresources` block in your module's build file e.g. `app/build.gradle.kts` file, like so:
```kotlin
gsheetresources {
    // The unique identifier of your Google Sheet. This is part of the sheet URL.
    // Required
    sheetId = "sheet_id"

    // The unique identifier of the tab in your Google Sheet. This is the 'gid' parameter of the sheet URL.
    // Optional. Defaults to "0".
    tabId = "tab_id"

    // Path to the resource directory. Relative to the module.
    // Optional. Defaults to "src/main/res".
    resourceDir = "path/to/resource/dir"

    // Name of the generated string resource files. Should have the extension ".xml".
    // Optional. Defaults to "strings_generated.xml".
    outputFilename = "output.xml"

    // Name of the generated plural resource file. Should have the extension ".xml".
    // Optional. Defaults to "plurals_generated.xml".
    outputFilename = "plurals.xml"
}
```

#### Typical configuration for Android 

```kotlin
gsheetresources {
    sheetId = "sheet_id"
}
```

#### Typical configuration for Kotlin Multiplatform

```kotlin
gsheetresources {
    sheetId = "sheet_id"
    resourceDir = "src/commonMain/composeResources"
}
```

### Plurals

A file with plural resources will be generated automatically if there are any IDs that follow these conventions:
1. The ID ends with an underscore, followed by one the valid quantity strings: `zero`, `one`, `two`, `few`, `many` or `other`.
2. There are multiple IDs with the same beginning, e.g. `items_one` and `items_other`.

The [sample](#sample) contains example plurals.

Check out the [Android documentation about plurals](https://developer.android.com/guide/topics/resources/string-resource#Plurals) 
or the [KMP documentation about plurals](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-multiplatform-resources-usage.html#plurals) 
for more information.

### Execution

You can launch the tool by running the following command in your terminal:
```bash
./gradlew generateSheetResources
```

This will automatically generate a string resource file called `strings_generated.xml` for each language found in the sheet,
and a plural resource file called `plurals_generated.xml` if any [plurals](#plurals) are present. 

I recommend to add the generated files into version control. You can rerun the above command to update the generated files after changes were made to the Google Sheet.

## Sample

This [sample Google Sheet](https://docs.google.com/spreadsheets/d/1L6EsUDM9qnsQjrT2Y16eXRzMP3v5Njp2gckwY-oZpcM/edit?gid=0#gid=0)...

| ID                  | en                | nl                | nl-rBE    |
|---------------------|-------------------|-------------------| --------- |
| hello               | Hello World!      | Hallo Wereld!     |           |
| button_save         | Save              | Bewaren           |           |
| button_cancel       | Cancel            | Annuleren         |           |
| ellipsis            | More...           | Meer...           |           |
| ampersand           | You & Me          | Jij & ik          |           |
| fries               | French fries      | Friet             | Fritten   |
| placeholder_string  | Value: %1$s       | Waarde: %1$s      |           |
| placeholder_numbers | From %1$d to %2$d | Van %1$d tot %2$d |           |
| items_zero          | No items          | Geen items        |           |
| items_one           | 1 item            | 1 item            |           |
| items_other         | %1$d items        | %1$d items        |           |
| items_no_plural     | Not a plural      | Geen veelvoud     |           |

...with this configuration...

```kotlin
gsheetresources {
    sheetId = "1L6EsUDM9qnsQjrT2Y16eXRzMP3v5Njp2gckwY-oZpcM"
}
```

...results in the following string resource files:
 - [src/main/res/values/strings_generated.xml](https://github.com/litrik/gsheet-resources/blob/main/test-module/src/main/res/values/strings_generated.xml)
 - [src/main/res/values-nl/strings_generated.xml](https://github.com/litrik/gsheet-resources/blob/main/test-module/src/main/res/values-nl/strings_generated.xml)
 - [src/main/res/values-nl-rBE/strings_generated.xml](https://github.com/litrik/gsheet-resources/blob/main/test-module/src/main/res/values-nl-rBE/strings_generated.xml)

...and the following plural resource file:
- [src/main/res/values/plurals_generated.xml](https://github.com/litrik/gsheet-resources/blob/main/test-module/src/main/res/values/plurals_generated.xml)
