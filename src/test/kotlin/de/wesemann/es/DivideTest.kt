package de.wesemann.es

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
internal class DivideTest {


    init {
        val tmpFiles = File(System.getProperty("java.io.tmpdir")).list({ _, n -> n.startsWith("run") || n == "chunktest" })
        for (f in tmpFiles) {
            File("${System.getProperty("java.io.tmpdir")}/$f").delete()
        }
    }

    @Test
    fun `page 1`() {
        val first = Divide(4)
        first.run("page1")
        assertEquals(4, first.fileNames.size)
        assertEquals("2,3,4,6", readFile("run0Chunk1"))
        assertEquals("4,7,8,9", readFile("run0Chunk2"))
        assertEquals("1,3,5,6", readFile("run0Chunk3"))
        assertEquals("1,4,6,7", readFile("run0Chunk4"))
    }

    @Test
    fun `Three Lines with two words each`() {
        val first = Divide(4)
        first.run("ThreeLinesTwoWords")
        assertEquals(2, first.fileNames.size)
        assertEquals("2,3,4,6", readFile("run0Chunk1"))
        assertEquals("4,9", readFile("run0Chunk2"))
    }

    @Test
    fun `One Line with Two Words`() {
        val first = Divide(4)
        first.run("oneLineTwoWords")
        assertEquals(1, first.fileNames.size)
        assertEquals("3,4", readFile("run0Chunk1"))
    }

    @Test
    fun `real Words`() {
        val first = Divide(4)
        first.run("Words")
        assertEquals(2, first.fileNames.size)
        assertEquals("ABC,DEF,TEST,dEF", readFile("run0Chunk1"))
        assertEquals("TAG,WORD,WORDS", readFile("run0Chunk2"))
    }

    @Test
    fun `Run one test more`() {
        val first = Divide(6)
        first.run("page1")
        assertEquals(3, first.fileNames.size)
        assertEquals("2,3,4,4,6,9", readFile("run0Chunk1"))
        assertEquals("1,3,5,6,7,8", readFile("run0Chunk2"))
        assertEquals("1,4,6,7", readFile("run0Chunk3"))
    }
}

fun readFile(fileName: String) = File("${System.getProperty("java.io.tmpdir")}/$fileName").readText()