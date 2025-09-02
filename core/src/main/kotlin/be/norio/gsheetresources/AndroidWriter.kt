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

import java.io.File

class AndroidWriter(
    private val resourceDir: String = DEFAULT_RESOURCE_DIR,
    private val outputFilename: String = DEFAULT_OUTPUT_FILENAME,
) {

    fun writeAllLanguages(translations: Translations) {
        translations.getLanguages().forEachIndexed { index, language ->
            val outDir = File(resourceDir, if (index == 0) "values" else "values-${language}")
            outDir.mkdirs()
            val outFile = File(outDir, outputFilename)
            print("Generating $language string resources in ${outFile}...")
            val entriesWritten = writeLanguage(
                outFile = outFile,
                entries = translations.entries.keys.mapNotNull { translationId -> translations.getOrNull(translationId, language)?.let { translationId to it } }.toMap()
            )
            println(entriesWritten)
        }
    }

    private fun writeLanguage(outFile: File, entries: Map<ResourceId, String>): Int {
        outFile.writeText("<resources>\n")
        entries.onEach { outFile.appendText("<string name=\"${it.key}\">${escapeValue(it.value)}</string>\n") }
        outFile.appendText("</resources>\n")
        return entries.size
    }

    private fun escapeValue(value: String) = value.trim()
        .replace("'", "\\'")
        .replace("&", "&amp;")
        .replace("...", "…")
        .run {
            if (contains('<')) {
                "<![CDATA[$this]]>"
            } else {
                this
            }
        }

    companion object {
        const val DEFAULT_RESOURCE_DIR = "src/main/res"
        const val DEFAULT_OUTPUT_FILENAME = "strings_generated.xml"
    }

}