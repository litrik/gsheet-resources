package be.norio.gsheetresources

import org.junit.Assert
import org.junit.Test
import java.io.File

class ParserTest {

    val idButton = "button_save"

    private fun parseFile(filename: String) = File("src/test/resources/$filename")
        .readText()
        .let { Parser().parse(it) }

    @Test
    fun `Parse basic CSV`() {
        val translations = parseFile("basic.csv")
        Assert.assertEquals(2, translations.entries.size)
        Assert.assertEquals(2, translations.getLanguages().size)
        Assert.assertEquals(true, translations.getLanguages().contains("en"))
        Assert.assertEquals(true, translations.getLanguages().contains("nl"))

        Assert.assertEquals(2, translations.entries[idButton]!!.size)
        Assert.assertEquals("Save", translations.getOrNull(idButton, "en"))
        Assert.assertEquals("Bewaren", translations.getOrNull(idButton, "nl"))

        Assert.assertEquals(null, translations.entries["unknown"])
    }

    @Test
    fun `Skip empty lines`() {
        val translations = parseFile("empty_line.csv")
        Assert.assertEquals(2, translations.entries.size)
    }

    @Test
    fun `Partially translated`() {
        val translations = parseFile("partial.csv")
        Assert.assertEquals(2, translations.entries.size)

        Assert.assertEquals(1, translations.entries[idButton]!!.size)
        Assert.assertEquals("Save", translations.getOrNull(idButton, "en"))
        Assert.assertEquals(null, translations.getOrNull(idButton, "nl"))
    }

    @Test
    fun `Ignore non-language columns`() {
        val translations = parseFile("extra_columns.csv")
        Assert.assertEquals(2, translations.entries.size)
        Assert.assertEquals(2, translations.entries[idButton]!!.size)
    }

    @Test
    fun `Multiple regions`() {
        val translations = parseFile("regions.csv")
        Assert.assertEquals(1, translations.entries.size)

        val idColor = "color"
        Assert.assertEquals(2, translations.entries[idColor]!!.size)
        Assert.assertEquals("Color", translations.getOrNull(idColor, "en"))
        Assert.assertEquals("Colour", translations.getOrNull(idColor, "en-rGB"))
        Assert.assertEquals(null, translations.getOrNull(idColor, "en-rCA"))
    }

}
