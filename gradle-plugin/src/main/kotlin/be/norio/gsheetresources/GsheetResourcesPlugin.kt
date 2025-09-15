/*
 * MIT License
 *
 * Copyright (c) 2025 Norio BV
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package be.norio.gsheetresources

import org.gradle.api.Plugin
import org.gradle.api.Project
import kotlin.jvm.java


@Suppress("unused") // Used by Gradle
open class GsheetResourcesPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val ext = target.extensions.create("gsheetresources", GsheetResourcesPluginExtension::class.java)

        target.tasks.register("generateSheetResources", GsheetResourcesTask::class.java) {
            it.description = "Generate string resource files from a Google Sheet."

            it.sheetId.set(ext.sheetId.get())
            it.tabId.set(ext.tabId.orNull ?: GoogleSheet.DEFAULT_TAB_ID)

            val resourceDir = ext.resourceDir.orNull ?: AndroidWriter.DEFAULT_RESOURCE_DIR
            it.resourceDir.set(target.layout.projectDirectory.dir(resourceDir).asFile.path)

            val outputFilenameStrings = ext.outputFilename.orNull ?: AndroidWriter.DEFAULT_OUTPUT_FILENAME_STRINGS
            it.outputFilenameStrings.set(outputFilenameStrings)

            val outputFilenamePlurals = ext.pluralsOutputFilename.orNull ?: AndroidWriter.DEFAULT_OUTPUT_FILENAME_PLURALS
            it.outputFilenamePlurals.set(outputFilenamePlurals)
        }
    }

}
