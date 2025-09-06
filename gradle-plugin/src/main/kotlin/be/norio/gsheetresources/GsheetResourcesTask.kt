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

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

open class GsheetResourcesTask : DefaultTask() {
    @get:Input
    val sheetId = project.objects.property(String::class.java)

    @get:Input
    val resourceDir = project.objects.property(String::class.java)

    @get:Input
    val outputFilename = project.objects.property(String::class.java)

    @TaskAction
    fun generateTranslations() {
        val translations = Parser().parse(Util.readGoogleSheet(sheetId.get()))
        AndroidWriter(
            resourceDir = resourceDir.get(),
            outputFilename = outputFilename.get(),
        ).writeAllLanguages(translations)
    }

}
