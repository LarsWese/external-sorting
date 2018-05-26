package de.wesemann.es

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class ConquerTest {
    init {
        File("${System.getProperty("java.io.tmpdir")}/sortedFile").delete()
    }

    @Test
    fun getCurrentWordsTest() {
        start(4, "Words", "sortedFile")
        val content = readFileLines("sortedFile")
        assertEquals(2, content.size)
        assertEquals("ABC,DEF,TAG,TEST", content[0])
        assertEquals("WORD,WORDS,dEF", content[1])

    }

    @Test
    fun getCurrentWordsTest2() {
        start(3, "Words", "sortedFile")
        val content = readFileLines("sortedFile")
        assertEquals(3, content.size)
        assertEquals("ABC,DEF,TAG", content[0])
        assertEquals("TEST,WORD,WORDS", content[1])
        assertEquals("dEF", content[2])

    }

    @Test
    fun getCurrentWordsTest3() {
        start(4, "ThreeLinesTwoWords", "sortedFile")
        val content = readFileLines("sortedFile")
        assertEquals(2, content.size)
        assertEquals("2,3,4,4", content[0])
        assertEquals("6,9", content[1])
    }

    @Test
    fun getCurrentWordsTest4() {
        start(4, "oneLineTwoWords", "sortedFile")
        val content = readFileLines("sortedFile")
        assertEquals(1, content.size)
        assertEquals("3,4", content[0])
    }
}

fun readFileLines(fileName: String) = File("${System.getProperty("java.io.tmpdir")}/$fileName").readLines()