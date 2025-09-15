package be.norio.gsheetresources

import org.junit.Assert
import org.junit.Test
import java.io.File
import kotlin.collections.flatten

class ParserTest {

    val idButton = "button_save"

    private fun parseFile(filename: String) = File("src/test/resources/$filename")
        .readText()
        .let { Parser().parse(it) }

    @Test
    fun `Parse basic CSV`() {
        val translations = parseFile("basic.csv")
        Assert.assertEquals(2, translations.strings.size)
        Assert.assertEquals(2, translations.getLanguages().size)
        Assert.assertEquals(true, translations.getLanguages().contains("en"))
        Assert.assertEquals(true, translations.getLanguages().contains("nl"))

        Assert.assertEquals(2, translations.strings[idButton]!!.size)
        Assert.assertEquals("Save", translations.getStringOrNull(idButton, "en"))
        Assert.assertEquals("Bewaren", translations.getStringOrNull(idButton, "nl"))

        Assert.assertEquals(null, translations.strings["unknown"])
    }

    @Test
    fun `Skip empty lines`() {
        val translations = parseFile("empty_line.csv")
        Assert.assertEquals(2, translations.strings.size)
    }

    @Test
    fun `Partially translated`() {
        val translations = parseFile("partial.csv")
        Assert.assertEquals(2, translations.strings.size)

        Assert.assertEquals(1, translations.strings[idButton]!!.size)
        Assert.assertEquals("Save", translations.getStringOrNull(idButton, "en"))
        Assert.assertEquals(null, translations.getStringOrNull(idButton, "nl"))
    }

    @Test
    fun `Ignore non-language columns`() {
        val translations = parseFile("extra_columns.csv")
        Assert.assertEquals(2, translations.strings.size)
        Assert.assertEquals(2, translations.strings[idButton]!!.size)
    }

    @Test
    fun `Multiple regions`() {
        val translations = parseFile("regions.csv")
        Assert.assertEquals(1, translations.strings.size)

        val idColor = "color"
        Assert.assertEquals(2, translations.strings[idColor]!!.size)
        Assert.assertEquals("Color", translations.getStringOrNull(idColor, "en"))
        Assert.assertEquals("Colour", translations.getStringOrNull(idColor, "en-rGB"))
        Assert.assertEquals(null, translations.getStringOrNull(idColor, "en-rCA"))
    }

    @Test
    fun `Parse plurals`() {
        val translations = parseFile("plurals.csv")
        Assert.assertEquals(listOf("item_one", "item_other", "item_zero"), translations.plurals["item"]!!.map { it.value })
    }

    @Test
    fun `Strings without underscore are never plurals`() {
        val translations = parseFile("plurals.csv")
        Assert.assertEquals(false, translations.plurals.values.map { it.values }.flatten().contains("other"))
    }

    @Test
    fun `Single strings are not plurals`() {
        val translations = parseFile("plurals.csv")
        Assert.assertEquals(1, translations.plurals.size)
        Assert.assertEquals(null, translations.plurals.getOrDefault("single", null))
    }

}
