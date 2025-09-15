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

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader

class Parser() {

    val idRegex = "\\S+".toRegex()
    val localeRegex = "^[a-z]{2}(-r[A-Z]{2})?$".toRegex()

    fun parse(csvString: String): Translations {
        val rows = csvReader()
            .readAllWithHeader(csvString)
            .filter { idRegex.matches(it.values.first()) }
            .sortedBy { it.keys.first() }
        return Translations(
            strings = rows
                .map { row ->
                    row.values.first() to (row.keys
                        .drop(1)
                        .filter { localeRegex.matches(it) }
                        .mapNotNull { row[it]?.takeIf { it.isNotBlank() }?.let { value -> it to value } })
                        .toMap()
                }
                .sortedBy { it.first }
                .toMap(),
            plurals = rows
                .map { row -> row.values.first() }
                .groupBy { id -> id.substringBeforeLast('_') }
                .map { (pluralId, stringIds) -> pluralId to stringIds.filter { plurals.contains(it.substringAfterLast('_')) } }
                .filter { (_, stringIds) -> stringIds.size > 1 }
                .associate { (pluralId, stringIds) ->
                    pluralId to stringIds
                        .sortedBy { it }
                        .associateBy { it.substringAfterLast('_') }
                }
        )
    }

}
