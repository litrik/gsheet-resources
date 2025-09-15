package be.norio.gsheetresources

import org.junit.Assert
import org.junit.Test
import java.io.File
import kotlin.io.path.createTempDirectory

class IntegrationTest {

    @Test
    fun `Integration test of demo sheet`() {
        mapOf(
            GoogleSheet.DEFAULT_TAB_ID to listOf(
                "values/strings_generated.xml",
                "values/plurals_generated.xml",
                "values-nl/strings_generated.xml",
                "values-nl-rBE/strings_generated.xml",
            ),
            "147797468" to listOf(
                "values/strings_generated.xml",
            ),
        ).onEach { compareTab(it.key, it.value) }
    }

    private fun compareTab(tabId: String, files: List<String>) {
        val outputPath = createTempDirectory()
        GoogleSheet("1L6EsUDM9qnsQjrT2Y16eXRzMP3v5Njp2gckwY-oZpcM", tabId).readText()
            .let { Parser().parse(it) }
            .let { AndroidWriter(outputPath.toAbsolutePath().toString()).writeAll(it) }

        files.onEach {
            val actualFile = outputPath.resolve(it).toFile()
            val actualText = actualFile.readText()
            val expectedText = File("src/test/resources/expected/$tabId/$it").readText()
            Assert.assertEquals(expectedText, actualText)
        }
        outputPath.toFile().deleteRecursively()
    }

}
